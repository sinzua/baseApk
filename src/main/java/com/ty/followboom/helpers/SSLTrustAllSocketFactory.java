package com.ty.followboom.helpers;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class SSLTrustAllSocketFactory extends SSLSocketFactory {
    private static final String TAG = "SSLTrustAllSocketFactory";
    private SSLContext mCtx;

    public class SSLTrustAllManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public SSLTrustAllSocketFactory(KeyStore truststore) throws Throwable {
        super(truststore);
        try {
            this.mCtx = SSLContext.getInstance("TLS");
            this.mCtx.init(null, new TrustManager[]{new SSLTrustAllManager()}, null);
            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
        }
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return this.mCtx.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket() throws IOException {
        return this.mCtx.getSocketFactory().createSocket();
    }

    public static SSLSocketFactory getSocketFactory() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            return new SSLTrustAllSocketFactory(trustStore);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
