package com.eseict.reapi.hams.client.config;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HamsPropertiesTest {
    @Test
    void defaultsDoNotContainCredentialsOrEnableDeviceConnections() {
        HamsProperties properties = new HamsProperties();
        assertThat(properties.getId()).isEmpty();
        assertThat(properties.getPw()).isEmpty();
        assertThat(properties.getCryptKey()).isEmpty();
        assertThat(properties.getTcp().isEnabled()).isFalse();
    }
}
