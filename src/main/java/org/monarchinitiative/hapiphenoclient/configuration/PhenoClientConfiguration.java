package org.monarchinitiative.hapiphenoclient.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
public class PhenoClientConfiguration {

    @Autowired
    ApplicationProperties applicationProperties;




    @Bean("version")
    public String getVersion() {
        return applicationProperties.getApplicationVersion();
    }

    @Bean("hapiUrl")
    public String getHapiUrl() {
        return applicationProperties.getHapiServerUrl();
    }


}
