package org.monarchinitiative.hapiphenoclient.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    private final String hapiServerUrl;
    private final String applicationVersion;

    @Autowired
    public ApplicationProperties(@Value("${hapi.server}") String serverUrl,
                                 @Value("${application.version}") String version) {
        this.hapiServerUrl = serverUrl;
        this.applicationVersion = version;
    }

    public String getHapiServerUrl(){
        return hapiServerUrl;
    }
    public String getApplicationVersion() {
        return applicationVersion;
    }


}
