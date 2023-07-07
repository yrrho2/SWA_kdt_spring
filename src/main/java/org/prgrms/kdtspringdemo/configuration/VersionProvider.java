package org.prgrms.kdtspringdemo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("version.properties")
public class VersionProvider {
    private String version;


    public String getVersion() {
        return version;
    }

    public VersionProvider(@Value("${version:v0.0.0")String version) {
        this.version = version;
    }
}
