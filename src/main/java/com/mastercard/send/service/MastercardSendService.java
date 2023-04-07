package com.mastercard.send.service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import com.mastercard.send.exception.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.developer.encryption.JweConfig;
import com.mastercard.developer.encryption.JweConfigBuilder;
import com.mastercard.developer.interceptors.OkHttpEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import com.mastercard.developer.utils.EncryptionUtils;

import okhttp3.OkHttpClient.Builder;

@Service
public class MastercardSendService {

    private static final String ENVIRONMENT_SANDBOX = "sandbox";
    private static final String ENVIRONMENT_MTF = "mtf";
    private static final String ENVIRONMENT_PRODUCTION = "production";

    @Value("${environment}")
    private String environment;

    @Value("${signing.consumerKey}")
    private String consumerKey;

    @Value("${signing.pkcs12KeyFile}")
    private String pkcs12KeyFile;

    @Value("${signing.keyAlias}")
    private String signingKeyAlias;

    @Value("${signing.keyPassword}")
    private String signingKeyPassword;

    @Value("${client.encryption.enabled}")
    private boolean xEncrypted;

    @Value("${client.encryption.certificate}")
    private String clientEncryptionCertificate;

    @Value("${mastercard.encryption.pkcs12KeyFile}")
    private String mastercardEncryptionPkcs12KeyFile;

    @Value("${mastercard.encryption.keyAlias}")
    private String mastercardEncryptionKeyAlias;

    @Value("${mastercard.encryption.keyPassword}")
    private String mastercardEncryptionKeyPassword;

    public ApiClient getApiClient() throws EncryptionException, GeneralSecurityException, IOException {
        ApiClient apiClient = new ApiClient();

        // Set API Base Path depending on the environment set in properties.
        if (getEnvironment().equals(ENVIRONMENT_SANDBOX)) {
            apiClient.setBasePath("https://sandbox.api.mastercard.com/send/partners");
        } else if (getEnvironment().equals(ENVIRONMENT_MTF)) {
            apiClient.setBasePath("https://mtf.api.mastercard.com/send/partners");
        } else if (getEnvironment().equals(ENVIRONMENT_PRODUCTION)) {
            apiClient.setBasePath("https://api.mastercard.com/send/partners");
        }

        apiClient.setDebugging(true);

        Builder okHttpClient = apiClient.getHttpClient()
                .newBuilder();

        if (xEncrypted) {
            okHttpClient.addInterceptor(OkHttpEncryptionInterceptor.from(getEncryptionConfig()));
        }

        if (StringUtils.isEmpty(consumerKey) ||
                StringUtils.isEmpty(pkcs12KeyFile) ||
                StringUtils.isEmpty(signingKeyAlias) ||
                StringUtils.isEmpty(signingKeyPassword)) {
            throw new ConfigurationException("Authentication section of the application.properties file is not updated");
        }

        okHttpClient.addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, getSigningKey()));
        
        apiClient.setHttpClient(okHttpClient.build());

        return apiClient;
    }

    /**
     * Load Signing key as a Private Key
     * by providing the p12 file location with key alias and key password
     * using Mastercard/oauth1-signer-java library
     * 
     * @return PrivateKey the signing key
     * 
     * @throws UnrecoverableKeyException This exception is thrown if a key in the keystore cannot be recovered
     * @throws KeyStoreException This exception is thrown when there is an issue with the keystore
     * @throws CertificateException This exception indicates one of a variety of certificate problems
     * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic algorithm is requested
     * but is not available in the environment
     * @throws IOException This exception is the base class for exceptions while accessing information using streams,
     * files and directories
     */
    private PrivateKey getSigningKey() throws UnrecoverableKeyException, KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        File signingKey = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + pkcs12KeyFile);

        return AuthenticationUtils.loadSigningKey(
                signingKey.getPath(),
                signingKeyAlias,
                signingKeyPassword);
    }

    /**
     * Generate encryption key config using Mastercard/client-encryption-java
     * library
     * 
     * @return JweConfig the encryption config
     * 
     * @throws EncryptionException This exception is thrown for any problems related to encryption. hashing, or digital signatures
     * @throws GeneralSecurityException This is a generic security exception
     * @throws IOException This exception is the base class for exceptions while accessing information using streams,
     *      * files and directories
     */
    public JweConfig getEncryptionConfig() throws EncryptionException, GeneralSecurityException, IOException {
        File clientEncryption = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + clientEncryptionCertificate);
        Certificate encryptionCertificate = EncryptionUtils.loadEncryptionCertificate(clientEncryption.getPath());

        File mastercardEncryption = ResourceUtils
                .getFile(ResourceUtils.CLASSPATH_URL_PREFIX + mastercardEncryptionPkcs12KeyFile);
        PrivateKey decryptionKey = EncryptionUtils.loadDecryptionKey(
                mastercardEncryption.getPath(),
                mastercardEncryptionKeyAlias,
                mastercardEncryptionKeyPassword);

        return JweConfigBuilder.aJweEncryptionConfig()
                .withEncryptionCertificate(encryptionCertificate)
                .withDecryptionKey(decryptionKey)
                .withEncryptionPath("$", "$.encrypted_payload")
                .withDecryptionPath("$.encrypted_payload.data", "$")
                .withEncryptedValueFieldName("data")
                .build();
    }

    private String getEnvironment() {
        if (environment.equalsIgnoreCase(ENVIRONMENT_SANDBOX)) {
            return ENVIRONMENT_SANDBOX;
        } else if (environment.equalsIgnoreCase(ENVIRONMENT_MTF)) {
            return ENVIRONMENT_MTF;
        } else if (environment.equalsIgnoreCase(ENVIRONMENT_PRODUCTION)) {
            return ENVIRONMENT_PRODUCTION;
        } else {
            return ENVIRONMENT_SANDBOX;
        }
    }
}
