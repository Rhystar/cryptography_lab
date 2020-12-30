package com.dao;

import com.bean.Certificate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import static com.util.labDatabase.*;

public class CertificateData {
    public boolean register(Certificate ca) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection DBConnection = getLabConnection();
        String sql = "INSERT INTO certificate (VersionNumber, SerialNumber, SignatureAlgorithmID, IssuerName, " +
                "NotBefore, NotAfter, SubjectName, PublicKeyAlgorithm, PublicKey, CertificateSignatureAlgorithm, " +
                "CertificateSignature) VALUES (" +
                "'" + ca.getVersionNumber() + "', " +
                "'" + ca.getSerialNumber() + "', " +
                "'" + ca.getSignatureAlgorithmID() + "', " +
                "'" + ca.getIssuerName() + "', " +
                "'" + sdf.format(ca.getNotBefore()) + "', " +
                "'" + sdf.format(ca.getNotAfter()) + "', " +
                "'" + ca.getSubjectName() + "', " +
                "'" + ca.getPublicKeyAlgorithm() + "', " +
                "'" + ca.getPublicKey() + "', " +
                "'" + ca.getCertificateSignatureAlgorithm() + "', " +
                "'" + ca.getCertificateSignature() + "');";
        int flag = operateDatabase(DBConnection, sql);
        closeConnection(DBConnection);
        return flag == 1;
    }

    public boolean revoke(String SerialNumber) {
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM certificate WHERE SerialNumber='" + SerialNumber + "';";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            if (Result.next()) {
                String insert = "INSERT INTO crl (VersionNumber, SerialNumber, SignatureAlgorithmID, IssuerName, " +
                        "NotBefore, NotAfter, SubjectName, PublicKeyAlgorithm, PublicKey, CertificateSignatureAlgorithm, " +
                        "CertificateSignature) VALUES (" +
                        "'" + Result.getString("VersionNumber") + "', " +
                        "'" + SerialNumber + "', " +
                        "'" + Result.getString("SignatureAlgorithmID") + "', " +
                        "'" + Result.getString("IssuerName") + "', " +
                        "'" + Result.getString("NotBefore") + "', " +
                        "'" + Result.getString("NotAfter") + "', " +
                        "'" + Result.getString("SubjectName") + "', " +
                        "'" + Result.getString("PublicKeyAlgorithm") + "', " +
                        "'" + Result.getString("PublicKey") + "', " +
                        "'" + Result.getString("CertificateSignatureAlgorithm") + "', " +
                        "'" + Result.getString("CertificateSignature") + "');";
                int flag = operateDatabase(DBConnection, insert);
                return flag == 1;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(DBConnection);
            closeResultSet(Result);
        }
    }
}
