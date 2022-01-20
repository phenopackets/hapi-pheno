package org.monarchinitiative.hapiphenoclient.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {


    private final String hapiServerUrl;
    private final String applicationVersion;
    private final String igPath;

    @Autowired
    public ApplicationProperties(@Value("${hapi.server}") String serverUrl,
                                 @Value("${phenoclient.version}") String version,
                                @Value("${ig.path}") String fhirIgPath) {
        this.hapiServerUrl = serverUrl;
        this.applicationVersion = version;
        this.igPath = fhirIgPath;
    }

    public String getHapiServerUrl(){
        return hapiServerUrl;
    }
    public String getApplicationVersion() {
        return applicationVersion;
    }
    public String igPath() {
        return igPath;
    }

}
