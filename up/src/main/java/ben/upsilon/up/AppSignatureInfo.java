package ben.upsilon.up;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * 获取app签名信息.
 * Created by ben on 16/7/24.
 */

public class AppSignatureInfo {
    private static final String TAG = "AppSignatureInfo";

    void show(Context context) {
        Signature[] sigs = new Signature[0];
        try {
            sigs = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature sig : sigs) {
            Log.d(TAG, "onCreate: " + "Signature hashcode : " + sig.hashCode());
            final byte[] rawCert = sig.toByteArray();
            InputStream certStream = new ByteArrayInputStream(rawCert);
            final CertificateFactory certFactory;
            final X509Certificate x509Cert;
            try {
                certFactory = CertificateFactory.getInstance("X509");
                x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);

                Log.d(TAG, "onCreate: Certificate subject: " + x509Cert.getSubjectDN());
                Log.d(TAG, "onCreate: Certificate issuer: " + x509Cert.getIssuerDN());
                Log.d(TAG, "onCreate: Certificate serial number: " + x509Cert.getSerialNumber());
            } catch (CertificateException e) {
                // e.printStackTrace();
            }
            byte[] hexBytes = sig.toByteArray();
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] md5digest = new byte[0];
            if (digest != null) {
                md5digest = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < md5digest.length; ++i) {
                    sb.append((Integer.toHexString((md5digest[i] & 0xFF) | 0x100)).substring(1, 3));
                }
                String fingerprintMD5 = sb.toString();
                Log.d(TAG, "onCreate: fingerprintMD5 > " + fingerprintMD5);
            }
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(sig.toByteArray());
                byte[] b = md.digest(sig.toByteArray());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < b.length; ++i) {
                    sb.append((Integer.toHexString((b[i] & 0xFF) | 0x100)).substring(1, 3));
                }
                Log.d(TAG, "onCreate: fingerprint sha1 >" + sb);
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, "onCreate: fingerprint sha1 >" + something);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


        }
    }
}
