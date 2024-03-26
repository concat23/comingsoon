package com.dev.comingsoon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
public class RSAKeyRecord {
    private final RSAPublicKey rsaPublicKey;
    private final RSAPrivateKey rsaPrivateKey;

    public RSAKeyRecord(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        this.rsaPublicKey = rsaPublicKey;
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public RSAPublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

    public RSAPrivateKey getRsaPrivateKey() {
        return rsaPrivateKey;
    }
}