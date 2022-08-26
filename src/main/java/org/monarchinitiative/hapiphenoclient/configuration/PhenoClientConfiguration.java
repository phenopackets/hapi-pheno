package org.monarchinitiative.hapiphenoclient.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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

//    @Bean("igPath")
//    public Path igPath() {
//        return Path.of(applicationProperties.igPath());
//    }



}
