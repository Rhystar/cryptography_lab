package com.dao;

import com.bean.Certificate;

import java.sql.Connection;
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
}
