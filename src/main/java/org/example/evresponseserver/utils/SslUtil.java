package org.example.evresponseserver.utils;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;

@Slf4j
public class SslUtil {
    String caPath;
    String certPath;
    String keyPath;
    String password = "dskys";
    File ca;
    File cert; // = new File(certPath); // 인증서 파일
    File key; // = new File(keyPath);

    public SslUtil() {

    }

    public SslContext getCertificate() {
        if (cert == null)
            cert = new File(certPath); // 인증서 파일

        if (key == null)
            key = new File(keyPath);

//
//        if (ca == null)
//            ca = new File(caPath);
////
//        Security.addProvider(new BouncyCastleProvider());
//
//        // load CA certificate
//        X509Certificate caCert = EC.loadX509Crt(caPath);
//
//        // load client certificate
//        X509Certificate cert = EC.loadX509Crt(certPath);
//
//        // load client private key
//        KeyPair key = EC.loadKey(keyPath);

        try {
            if (certPath != null && keyPath != null) {
//                File cert = new File(certPath); // 인증서 파일
//                File key = new File(keyPath); // 개인키 파일
//
//
//                // load CA certificate
//                X509Certificate caCert = EC.loadX509Crt(caPath);
//
//                // load client certificate
//                X509Certificate cert = EC.loadX509Crt(certPath);
//
//                // load client private key
//                KeyPair key = EC.loadKey(keyPath);

                // CA certificate is used to authenticate server
//                KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
//                caKs.load(null, null);
//                caKs.setCertificateEntry("ca-certificate", caCert);
//                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                tmf.init(caKs);
//
//                // client key and certificates are sent to server so it can authenticate us
//                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//                ks.load(null, null);
//                ks.setCertificateEntry("certificate", cert);
//                ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
//                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//                kmf.init(ks, password.toCharArray());

                // finally, create SSL socket factory
//                SSLContext context = SSLContext.getInstance("TLSv1.2");
//                context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                return SslContextBuilder.forServer(cert,key)
//                        .sslContextProvider(new BouncyCastleProvider())
//                        .trustManager(tmf)
//                        .keyManager(kmf)
                        .build();
            } else {
                log.error("Certification or Key path is null");
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
//
//    public SSLContext getSslContext() throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
//
//        // load CA certificate
//        X509Certificate caCert = EC.loadX509Crt(caPath);
//
//        // load client certificate
//        X509Certificate cert = EC.loadX509Crt(certPath);
//
//        // load client private key
//        KeyPair key = EC.loadKey(keyPath);
//
//        // CA certificate is used to authenticate server
//        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
//        caKs.load(null, null);
//        caKs.setCertificateEntry("ca-certificate", caCert);
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        tmf.init(caKs);
//
//        // client key and certificates are sent to server so it can authenticate us
//        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//        ks.load(null, null);
//        ks.setCertificateEntry("certificate", cert);
//        ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
//        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        kmf.init(ks, password.toCharArray());
//
//        // finally, create SSL socket factory
//        SSLContext context = SSLContext.getInstance("TLSv1.2");
//        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//        return context;
//    }

    public void setCaPath(String caPath) {
        this.caPath = caPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public void setGetKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }
}
