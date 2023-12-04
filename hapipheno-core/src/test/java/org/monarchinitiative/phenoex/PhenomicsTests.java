package org.monarchinitiative.phenoex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.essaid.model.Configs;
import com.essaid.model.EntityManager;
import com.google.protobuf.util.JsonFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.monarchinitiative.phenoex.fhirr4.ToR4Bundle;
import org.monarchinitiative.phenoex.model.PhenomicsExchangePacket;
import org.monarchinitiative.phenoex.model.Phenotype;
import org.monarchinitiative.phenoex.ppv2.V2ToModel;
import org.phenopackets.schema.v2.Phenopacket;

@TestInstance(Lifecycle.PER_CLASS)
public class PhenomicsTests {

  private static FhirContext context;
  private static IParser parser;

  @BeforeAll
  static void setup() {
    context = FhirContext.forR4();
    parser = context.newJsonParser().setPrettyPrint(true);
  }


  @Test
  void convertV2PhenopacketFileToR4Bundle() throws IOException {

    Phenopacket phenopacket = null;
    try (InputStream is = getClass().getClassLoader()
        .getResourceAsStream(
            "org/monarchinitiative/phenoex/examples" + "/v2" + "/retinoblastoma.json")) {
      Phenopacket.Builder builder = Phenopacket.newBuilder();
      assert is != null;
      JsonFormat.parser().ignoringUnknownFields().merge(new InputStreamReader(is), builder);
      phenopacket = builder.build();
    }

    // Set up the interface model and the converter (visitor)
    EntityManager entityManager = Configs.createDefaultMapEntityManager();
    V2ToModel v2ToModel = new V2ToModel(entityManager);
    v2ToModel.getPrefixMap().put("HP", "http://HP");
    PhenomicsExchangePacket phenomicsPacket = v2ToModel.visit(phenopacket);

    // the R4 conversion
    ToR4Bundle toR4Bundle = ToR4Bundle.builder().type(BundleType.TRANSACTION).build();
    Bundle bundle = toR4Bundle.visit(phenomicsPacket);

    parser.encodeResourceToWriter(bundle,
        new FileWriter(Path.of("target/bundle.json").toFile()));
  }


  @Test
  void createPhenotype() throws IOException {

    // Set up the interface model and the converter (visitor)
    EntityManager entityManager = Configs.createDefaultMapEntityManager();
//    V2ToModel v2ToModel = new V2ToModel(entityManager);
//    v2ToModel.getPrefixMap().put("HP", "http://HP");

    Phenotype phenotype = entityManager.create(Phenotype.class);
    phenotype.addCodings().csetCode("SomeCode").csetDisplay("Some display")
        .csetSystem("http://example.com");

    ToR4Bundle toR4Bundle = ToR4Bundle.builder().build();
    Observation o = toR4Bundle.visit(phenotype);

    parser.encodeResourceToWriter(o,
        new FileWriter(Path.of("target/phenotype.json").toFile()));

  }

}
