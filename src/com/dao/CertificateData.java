package com.dao;

import com.bean.Certificate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.util.labDatabase.*;

public class CertificateData {
    /*
    public boolean register(Certificate ca, String path) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection DBConnection = getLabConnection();
        String sql1 = "INSERT INTO certificate (VersionNumber, SerialNumber, SignatureAlgorithmID, IssuerName, " +
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
        String sql2 = "INSERT INTO capath (SerialNumber, path) VALUES (" +
                "'" + ca.getSerialNumber() + "', " +
                "'" + path + "'); ";
        int flag1 = operateDatabase(DBConnection, sql1);
        int flag2 = operateDatabase(DBConnection, sql2);
        closeConnection(DBConnection);
        return flag1 == 1 && flag2 == 1;
    }
    */

    public boolean register(Certificate ca) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection DBConnection = getLabConnection();
        String sql = "INSERT INTO certificate (SerialNumber, Path, NotBefore, NotAfter, User) VALUES (" +
                "'" + ca.getSerialNumber() + "', " +
                "'" + ca.getPath().replaceAll("\\\\","\\\\\\\\") + "', " +
                "'" + sdf.format(ca.getNotBefore()) + "', " +
                "'" + sdf.format(ca.getNotAfter()) + "', " +
                "'" + ca.getUsername() + "');";
        int flag = operateDatabase(DBConnection, sql);
        closeConnection(DBConnection);
        return flag == 1;
    }

    /*
    public boolean revoke(String SerialNumber) {
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM certificate WHERE SerialNumber='" + SerialNumber + "';";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            if (Result.next()) {
                String insert = "INSERT INTO crl (VersionNumber, SerialNumber, SignatureAlgorithmID, IssuerName, " +
                        "NotBefore, NotAfter, SubjectName, PublicKeyAlgorithm, PublicKey, " +
                        "CertificateSignatureAlgorithm, CertificateSignature) VALUES (" +
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
     */

    public boolean revoke(String SerialNumber) {
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM certificate WHERE SerialNumber='" + SerialNumber + "';";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            if (Result.next()) {
                String insert = "INSERT INTO crl (SerialNumber, Path, NotBefore, NotAfter, User) VALUES (" +
                        "'" + SerialNumber + "', " +
                        "'" + Result.getString("Path").replaceAll("\\\\","\\\\\\\\") + "', " +
                        "'" + Result.getString("NotBefore") + "', " +
                        "'" + Result.getString("NotAfter") + "', " +
                        "'" + Result.getString("User") + "');";
                int flag = operateDatabase(DBConnection, insert);
                return flag == 1;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCAPath(String SerialNumber) {
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM certificate WHERE SerialNumber='" + SerialNumber + "';";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            if (Result.next()) {
                return Result.getString("Path");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection(DBConnection);
            closeResultSet(Result);
        }
    }

    private List<Certificate> getCertificates(String table) {
        List<Certificate> Crl = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM " + table + ";";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            while (Result.next()) {
                Certificate ca = new Certificate(
                        Result.getString("SerialNumber"),
                        Result.getString("Path"),
                        sdf.parse(Result.getString("NotBefore")),
                        sdf.parse(Result.getString("NotAfter")),
                        Result.getString("User")
                );
                Crl.add(ca);
            }
            return Crl;
        } catch (Exception e) {
            e.printStackTrace();
            return Crl;
        } finally {
            closeConnection(DBConnection);
            closeResultSet(Result);
        }
    }

    public List<Certificate> getCrl() {
        return getCertificates("crl");
    }

    public List<Certificate> getAllCa() {
        return getCertificates("certificate");
    }

    public Certificate getCa(String SerialNumber) {
        Connection DBConnection = getLabConnection();
        String sql = "SELECT * FROM certificate WHERE SerialNumber='" + SerialNumber + "';";
        ResultSet Result = getResultSet(DBConnection, sql);
        try {
            if (Result.next()) {
                var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new Certificate(SerialNumber, Result.getString("Path"), sdf.parse(Result.getString("NotBefore")), sdf.parse(Result.getString("NotAfter")), Result.getString("User"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
