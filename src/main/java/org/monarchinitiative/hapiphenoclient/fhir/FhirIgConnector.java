package org.monarchinitiative.hapiphenoclient.fhir;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.apache.ApacheHttpRequest;
import ca.uhn.fhir.rest.client.apache.ApacheHttpResponse;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.monarchinitiative.hapiphenoclient.except.PhenoClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FhirIgConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(FhirIgConnector.class);
    public static final String ID_TAG_IRI = "http://github.com/phenopackets/core-ig/ig-tools/id-tag";
    public static final String TAG_URI = "http://github.com/phenopackets/core-ig/ig-tools/tag";
    public static final String TAG_GENERATED = "generated";
    public static final String VALIDATION_EXT_URI = "http://github.com/phenopackets/core-ig/ig-tools/validation-error-count";

    private final IParser xmlParser;

    private final IParser jsonParser;

    private IGenericClient client;

    private final ObjectMapper objectMapper =  new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private final CapturingInterceptor capturing;

    private final String hapiFhirUrl;

    private final ImplementationGuide implementationGuide;

    private final Path igPath;

    private final Map<String, Set<Reference>> refTypeMap = new HashMap<>();

    private final List<ImplementationGuide.ImplementationGuideDefinitionResourceComponent> examples = new ArrayList<>();

    public FhirIgConnector(String hapiUrl, Path igOutPath) {
        FhirContext ctx = FhirContext.forR5();
        xmlParser = ctx.newXmlParser();
        jsonParser = ctx.newJsonParser().setPrettyPrint(true);
        this.hapiFhirUrl = hapiUrl;
        Objects.requireNonNull(this.hapiFhirUrl);
        client = ctx.newRestfulGenericClient(hapiFhirUrl);
        this.capturing = new CapturingInterceptor();
        client.registerInterceptor(capturing);
        this.igPath = igOutPath;
        implementationGuide = initializeIg();
        initializeResources();
        loadExamples();
    }





    private ValueSet expandValueSet(ValueSet vs) {
        Parameters parameters = this.client.operation().onInstance("ValueSet/" + vs.getIdElement().getIdPart())
                .named("expand").withNoParameters(Parameters.class).execute();
        ValueSet expanded = (ValueSet) parameters.getParameterFirstRep().getResource();
        ValueSet.ValueSetExpansionComponent expansion = expanded.getExpansion();
        vs.setExpansion(expansion);
        MethodOutcome mo = this.client.update().resource(vs).execute();
        vs = (ValueSet) mo.getResource();
        return vs;
    }

    private void initializeResources() {
        List<ImplementationGuide.ImplementationGuideDefinitionResourceComponent> toRemove = new ArrayList<>();
        for (ImplementationGuide.ImplementationGuideDefinitionResourceComponent rc : implementationGuide.getDefinition().getResource()) {
            if (rc.hasExample()) {
                examples.add(rc);
                continue;
            }
            Reference ref = rc.getReference();
            String[] refParts = ref.getReference().split("/");

            if (refParts.length == 2) {
                Set<Reference> refTypeSet = refTypeMap.computeIfAbsent(refParts[0], k -> new HashSet<>());
                switch (refParts[0]) {
                    case "CodeSystem":
                    case "ValueSet":
                    case "StructureDefinition":
                    case "SearchParameter":
                        refTypeSet.add(ref);
                        break;
                    default:
                        LOGGER.info("Ignoring reference: " + rc.getReference().getReference());
                        toRemove.add(rc);
                }
            }
            if (toRemove.size() > 0) {
                LOGGER.info("Not loading the following resources...");
                for (ImplementationGuide.ImplementationGuideDefinitionResourceComponent resource : toRemove) {
                    LOGGER.info("Failed to load {}", resource.getId());
                }
            }
            implementationGuide.getDefinition().getResource().removeAll(toRemove);

            try {
                loadResourceSet(refTypeMap.get("CodeSystem"));
                Thread.sleep(5000);
                loadResourceSet(refTypeMap.get("ValueSet"));
                Thread.sleep(5000);
                loadResourceSet(refTypeMap.get("StructureDefinition"));
                Thread.sleep(5000);
                loadResourceSet(refTypeMap.get("SearchParameter"));
                Thread.sleep(5000);
                //loadResrouces();
                //loadUpdate(ig, true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     *
     */
    private void loadResourceSet(Set<Reference> references) {
        for (Reference reference : references) {
            String[] parts = reference.getReference().split("/");
            MetadataResource updatedMetadata = loadUpdate(loadMetadata(parts[0] + "-" + parts[1] + ".xml"), false);

            if (ValueSet.class.isInstance(updatedMetadata)) {
                // we need to expand it on the server
//                ValueSet vs = (ValueSet) updatedMetadata;
//                updatedMetadata = expandValueSet(vs);
            }
            if (updatedMetadata != null) {
                String id = updatedMetadata.getIdElement().asStringValue();
                LOGGER.info("Updating IG reference from:" + reference.getReference() + " to:" + id);
                reference.setReferenceElement(new StringType(id));
            }
        }
    }

    private MetadataResource loadUpdate(MetadataResource resource, boolean deleteExisting) {
        LOGGER.info("Loading: " + resource.getId() + ", url:" + resource.getUrl());
        MetadataResource resourceCopy = resource.copy();
        tagResourceId(resourceCopy, resource);
        String url = resource.getUrl();
        Bundle bundle = (Bundle) this.client.search().forResource(resource.getClass())
                .and(new StringClientParam("url").matchesExactly().value(url)).execute();

        List<Bundle.BundleEntryComponent> entries = bundle.getEntry();
        MetadataResource existingResource = null;

        if (entries.size() > 1) {
            String json = this.jsonParser.encodeResourceToString(bundle);
            LOGGER.warn("Search returned: multiple bundle entries. Skippping. JSON:");
            LOGGER.debug(json);
            return null;

        } else if (entries.size() == 1) {
            existingResource = (MetadataResource) entries.get(0).getResource();
            LOGGER.info("Search returned: " + existingResource.getId());
        }

        if (deleteExisting && existingResource != null) {
            LOGGER.info("Deleting resource: " + existingResource.getId());
            this.client.delete().resource(existingResource).execute();
            existingResource = null;
        }

        logRequest();
        logResponse();

        MethodOutcome mo;

        if (existingResource == null) {
            LOGGER.info("Creating resource...");
            mo = this.client.create().resource(resourceCopy).execute();
        } else {
            resourceCopy.setId(existingResource.getId());
            LOGGER.info("Updating resource: " + resourceCopy.getId());
            mo = this.client.update().resource(resourceCopy).execute();
        }

        checkOutcome( (OperationOutcome) mo.getOperationOutcome());

        MetadataResource mr = (MetadataResource) mo.getResource();

        validate( mr, null);

        return (MetadataResource) mo.getResource();
    }

    @SuppressWarnings("unchecked")
    public <T extends MetadataResource> T loadMetadata(String fileName) {
        try {
            File f = igPath.resolve(fileName).toFile();
            LOGGER.info("loadMetadata: {}", f.getAbsoluteFile());
            return (T) xmlParser.parseResource(new FileInputStream(f));
        } catch (ConfigurationException | DataFormatException | FileNotFoundException e) {
            throw new PhenoClientRuntimeException(e.getMessage());
        }
    }

    private int checkOutcome(OperationOutcome outcome) {
        Map<OperationOutcome.IssueSeverity, List<String>> issues = new HashMap<>();
        issues.put(OperationOutcome.IssueSeverity.FATAL, new ArrayList<>());
        issues.put(OperationOutcome.IssueSeverity.ERROR, new ArrayList<>());
        issues.put(OperationOutcome.IssueSeverity.WARNING, new ArrayList<>());
        issues.put(OperationOutcome.IssueSeverity.INFORMATION, new ArrayList<>());
        issues.put(null, new ArrayList<>());
        if (outcome != null) {
            for (OperationOutcome.OperationOutcomeIssueComponent issue : outcome.getIssue()) {
                StringBuilder sb = new StringBuilder();
                OperationOutcome.IssueSeverity severity = issue.getSeverity();
                sb.append("Severity:").append(severity);
                sb.append(", Code:").append(issue.getCode());
                sb.append(", Details:").append(issue.getDetails().getText());
                sb.append(", Location:").append(issue.getLocation());
                sb.append(", Diagnositc:").append(issue.getDiagnostics());
                issues.get(severity).add(sb.toString());
            }
        }
        // a one line output summary
        String sb = "Outcome summary: " + issues.get(OperationOutcome.IssueSeverity.FATAL).size() + " fatals," +
                issues.get(OperationOutcome.IssueSeverity.ERROR).size() + " errors," +
                issues.get(OperationOutcome.IssueSeverity.WARNING).size() + " warnings," +
                issues.get(OperationOutcome.IssueSeverity.INFORMATION).size() + " infos";
        LOGGER.info(sb);

        // for each severity
        for (String severity : issues.get(OperationOutcome.IssueSeverity.FATAL)) {
            LOGGER.error("Outcome: " + severity);
        }
        for (String severity : issues.get(OperationOutcome.IssueSeverity.ERROR)) {
            LOGGER.error("Outcome: " + severity);
        }
        for (String severity : issues.get(OperationOutcome.IssueSeverity.WARNING)) {
            LOGGER.warn("Outcome: " + severity);
        }
        for (String severity : issues.get(OperationOutcome.IssueSeverity.INFORMATION)) {
            LOGGER.info("Outcome: " + severity);
        }

        return issues.get(OperationOutcome.IssueSeverity.FATAL).size() + issues.get(OperationOutcome.IssueSeverity.ERROR).size();
    }


    public void tagResourceId(IBaseResource target, IBaseResource source) {
        String id = source.getIdElement().getValue();

        for (IBaseCoding coding : target.getMeta().getTag()) {
            if (coding.getSystem().equals(ID_TAG_IRI)) {
                coding.setCode(id);
                return;
            }
        }
        IBaseCoding tag = target.getMeta().addTag();
        tag.setSystem(ID_TAG_IRI);
        tag.setCode(id);
    }


    /**
     * This method expects the FHIR Phenopacket IG XML file
     * @return ImplementationGuide as initialized from the corresponding XML file
     */
    private ImplementationGuide initializeIg() {
        LOGGER.info("Searching for implementation guide at {}", igPath);
        if (this.xmlParser == null) {
            throw new PhenoClientRuntimeException("Xml Parser is null. Did you call setupFhirClient?");
        }
        List<Path> paths;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(igPath, "ImplementationGuide-*.xml")) {
            paths =  StreamSupport.stream(stream.spliterator(), false).collect(Collectors.toList());
        } catch (IOException e) {
            throw new PhenoClientRuntimeException(e.getMessage());
        }
        if (paths.size() != 1) {
            throw new IllegalArgumentException("Could not find one ImplementationGuide-*.xml file at: " + igPath);
        }

        Path path0 = paths.get(0);
        File igFile = path0.toFile();
        LOGGER.info("Found path at {}", igFile.getAbsoluteFile());
        LOGGER.info("Path exists?: " + igFile.isFile());

        try {
            return xmlParser.parseResource(ImplementationGuide.class, new FileInputStream(igFile));
        } catch (DataFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param fileName is relative to the IG output directory.
     * @return the FHIR resource loaded from the file
     */
    public Resource loadResource(String fileName) {
        try {
            return (Resource) xmlParser.parseResource(new FileInputStream(this.igPath.resolve(fileName).toFile()));
        } catch (ConfigurationException | DataFormatException | FileNotFoundException e) {
            String msg = String.format("Could not read %s: %s", fileName, e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        }
    }

    public void validate(DomainResource resource, String profile) {
        int maxErrors = 0;
        Extension validationExt = resource.getExtensionByUrl(VALIDATION_EXT_URI);
        if (validationExt != null) {
            maxErrors = Integer.parseInt(validationExt.getValue().primitiveValue());
        }
        LOGGER.info("Validating with profile: " + profile + ", and max errors: " + maxErrors);
        Parameters params = new Parameters();
        if (profile != null) {
            params.addParameter("profile", profile);
        }
        MethodOutcome methodOutcome;
        OperationOutcome operationOutcome;
        try {
            methodOutcome = this.client.operation().onInstance(resource.getId()).named("$validate")
                    .withParameters(params).returnMethodOutcome().execute();
            if (methodOutcome.getResource() instanceof OperationOutcome) {
                operationOutcome = (OperationOutcome) methodOutcome.getResource();
            } else {
                operationOutcome = (OperationOutcome) methodOutcome.getOperationOutcome();
            }
        } catch (PreconditionFailedException e) {
            operationOutcome = (OperationOutcome) e.getOperationOutcome();
        }

        int actualErrors = checkOutcome(operationOutcome);

        if (actualErrors > maxErrors) {
            LOGGER.error("VALIDATION ERROR COUNT OF: " + actualErrors + " exceeded max allowed validation error count of:"
                    + maxErrors);
        }
        if (actualErrors < maxErrors) {
            LOGGER.warn("VALIDATION ERROR COUNT OF: " + actualErrors + " less than max allowed validation error count of:"
                    + maxErrors);
        }
    }

    private void loadExamples() {
        List<ImplementationGuide.ImplementationGuideDefinitionResourceComponent> addedComponents = new ArrayList<>();
        for (ImplementationGuide.ImplementationGuideDefinitionResourceComponent rc : examples) {
            try {
                addOneExample(rc);
            } catch (ResourceNotFoundException rnfe) {
                LOGGER.error("Could not find resource for {}: {}", rc.getName(), rnfe.getMessage());
            } catch (PhenoClientRuntimeException e) {
                LOGGER.error("Other exception: {}", e.getMessage());
            }

            // //
            // // now if we have a profile for the example, update or create
            // if (refProfile != null) {
            // exampleCopy = example.copy();
            // Utils.tagResourceId(exampleCopy, example);
            // if (!exampleCopy.getMeta().hasProfile(refProfile)) {
            // exampleCopy.getMeta().addProfile(refProfile);
            // }
            // if (existingGenerated != null) {
            // exampleCopy.setId(existingGenerated.getId());
            // methodOutcome = main.getClient().update().resource(exampleCopy).execute();
            // } else {
            // methodOutcome = main.getClient().create().resource(exampleCopy).execute();
            // }
            // updatedGenerated = (DomainResource) methodOutcome.getResource();
            // }
            //
            // // TODO: validation and reporting
        }

        implementationGuide.getDefinition().getResource().addAll(addedComponents);
    }


    private void addOneExample(ImplementationGuide.ImplementationGuideDefinitionResourceComponent rc)
            throws ResourceNotFoundException {
        String ref = rc.getReference().getReference();
        String refProfile = rc.getName();//
        //  rc.getExampleCanonicalType().asStringValue();

        String[] refParts = ref.split("/");
        DomainResource example = (DomainResource) loadResource(refParts[0] + "-" + refParts[1] + ".xml");
        LOGGER.info("Loading example: {}", example.getId());

        // search if already loaded
        DomainResource existing = null;
        DomainResource existingGenerated = null;

        TokenClientParam tokenParam = new TokenClientParam("_tag");
        ICriterion<TokenClientParam> tokenCriterion = tokenParam.exactly().systemAndCode(ID_TAG_IRI, ref);

        Bundle bundle = (Bundle) this.client.search().forResource(example.getClass()).and(tokenCriterion).execute();

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            DomainResource resource = (DomainResource) entry.getResource();
            if (resource.getMeta().getTag(TAG_URI, TAG_GENERATED) != null) {
                existingGenerated = resource;
                LOGGER.info("Found existing generated: {}", existingGenerated.getId());
            } else {
                existing = resource;
                LOGGER.info("Found existing: {}", existing.getId());
            }
        }


        // create or update original
        DomainResource updated;

        // without profile first
        DomainResource exampleCopy = example.copy();
        tagResourceId(exampleCopy, example);
        MethodOutcome methodOutcome;
        boolean created = false;
        if (existing != null) {
            exampleCopy.setId(existing.getId());
            methodOutcome = this.client.update().resource(exampleCopy).execute();
        } else {
            methodOutcome = this.client.create().resource(exampleCopy).execute();
            created = true;
        }
        updated = (DomainResource) methodOutcome.getResource();
        if (created) {
            LOGGER.info("Created: " + updated.getId());
        } else {
            LOGGER.info("Updated: " + updated.getId());
        }
        checkOutcome((OperationOutcome) methodOutcome.getOperationOutcome());
        rc.getReference().setReference(updated.getIdElement().asStringValue());

        validate(updated, refProfile);

        // if the example doesn't already have the profile, and we have a profile, we'll
        // add another example instance with the profile
        DomainResource updatedGenerated = null;
        if (refProfile != null && !updated.getMeta().hasProfile(refProfile)) {
            // we'll add the profile to .meta.profile and create/update
            DomainResource generatedCopy = example.copy();
            generatedCopy.getMeta().addProfile(refProfile);
            generatedCopy.getMeta().addTag(TAG_URI, "generated", "generated");
            tagResourceId(generatedCopy, example);
            MethodOutcome mo;
            if (existingGenerated != null) {
                generatedCopy.setId(existingGenerated.getId());
                mo = this.client.update().resource(generatedCopy).execute();
                LOGGER.info("Updated generated example: " + ((Resource) mo.getResource()).getId());
            } else {
                mo = this.client.create().resource(generatedCopy).execute();
                LOGGER.info("Created generated example: " + ((Resource) mo.getResource()).getId());
            }
            checkOutcome((OperationOutcome) mo.getOperationOutcome());
            generatedCopy = (DomainResource) mo.getResource();
            // Reference r = new Reference(generatedCopy);
            ImplementationGuide.ImplementationGuideDefinitionResourceComponent rcGenerated =
                    new ImplementationGuide.ImplementationGuideDefinitionResourceComponent();
            rcGenerated.getReference().setReference(generatedCopy.getId());
            rcGenerated.addExtension(TAG_URI, new StringType("generated"));
        } else {
            // skip but delete any existing generated instance from the past.
            if (existingGenerated != null) {
                LOGGER.info("Deleting no longer needed existing generated example: " + existingGenerated.getId());
                MethodOutcome execute = this.client.delete().resource(existingGenerated).execute();
                checkOutcome((OperationOutcome) execute.getOperationOutcome());
            }
        }
    }




    public String logRequest() {
        String request = getRequest();
        LOGGER.debug(request);
        return request;
    }

    public  String getRequest() {
        return getRequestMethod() + "\n" +
                getRequestHeaders() + "\n" +
                getRequestBody() + "\n";
    }

    public String getRequestMethod() {
        ApacheHttpRequest request = (ApacheHttpRequest) getCapturing().getLastRequest();
        StringBuilder sb = new StringBuilder();
        sb.append(request.getHttpVerbName()).append(" ");
        try {
            sb.append(URLDecoder.decode(request.getUri(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            throw new PhenoClientRuntimeException(e.getMessage());
        }
        sb.append(" ").append(request.getApacheRequest().getProtocolVersion());
        return sb.toString();
    }

    public CapturingInterceptor getCapturing() {
        return capturing;
    }

    public String getRequestHeaders() {
        ApacheHttpRequest request = (ApacheHttpRequest) getCapturing().getLastRequest();
        StringBuilder sb = new StringBuilder();
        for (String header : request.getAllHeaders().keySet()) {
            sb.append(header).append(": ").append(request.getAllHeaders().get(header)).append("\n");
        }
        return sb.toString();
    }

    public String getRequestBody() {
        ApacheHttpRequest request = (ApacheHttpRequest) getCapturing().getLastRequest();
        StringBuilder sb = new StringBuilder();
        String bodyFromStream;
        try {
            bodyFromStream = request.getRequestBodyFromStream();
            if (bodyFromStream == null) {
                sb.append("NO BODY AVAILABLE");
            } else {
                Object json = objectMapper.readValue(bodyFromStream, Object.class);
                bodyFromStream = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                sb.append(bodyFromStream);
            }
        } catch (IOException e) {
            throw new PhenoClientRuntimeException(e.getMessage());
        }
        sb.append("\n");
        return sb.toString();
    }

    public String logResponse() {
        String request = getResponse();
        LOGGER.debug(request);
        return request;
    }

    public String getResponse() {
        return getResponseStatus() + "\n" +
                getResponseHeaders() + "\n" +
                getResponseBody() + "\n";
    }

    public String getResponseStatus( ) {
        ApacheHttpResponse response = (ApacheHttpResponse) getCapturing().getLastResponse();
        return response.getResponse().getStatusLine().toString();
    }

    public String getResponseHeaders() {
        ApacheHttpResponse response = (ApacheHttpResponse) getCapturing().getLastResponse();
        StringBuilder sb = new StringBuilder();
        for (String header : response.getAllHeaders().keySet()) {
            sb.append(header).append(": ").append(response.getAllHeaders().get(header)).append("\n");
        }
        return sb.toString();
    }

    public String getResponseBody() {
        ApacheHttpResponse response = (ApacheHttpResponse) getCapturing().getLastResponse();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(readReader(response.createReader()));
        } catch (IOException e) {
            throw new PhenoClientRuntimeException(e.getMessage());
        }
        return sb.toString();
    }

    private  String readReader(Reader reader) {

        char[] buffer = new char[4096];
        StringBuilder builder = new StringBuilder();
        int numChars;
        try {
            while ((numChars = reader.read(buffer)) >= 0) {
                builder.append(buffer, 0, numChars);
            }
        } catch (IOException e) {
            throw new PhenoClientRuntimeException(e.getMessage());
        }
        return builder.toString();
    }

    /**
     * Print out some data about this IG
     * @param writer file handle
     * @throws IOException if we cannot write to the file handle
     */
    public void printStatus(Writer writer) throws IOException {
        writer.write("[INFO] HAPI FHIR URL: " + this.hapiFhirUrl + "\n");
        writer.write("[INFO] Implementation guide: " + implementationGuide.getName() + "\n");
        writer.write("[INFO] " + examples.size() + " examples.\n");
        for (var ex : examples) {
            writer.write("[INFO]\t\t" + ex.getName() + "\n");
        }
        writer.write("[INFO] " + implementationGuide.getDescription() + "\n");
        writer.write("[INFO] version: " + implementationGuide.getVersion() + "\n");

    }

}
