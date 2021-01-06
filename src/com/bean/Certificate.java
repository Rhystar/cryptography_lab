package com.bean;

import java.util.*;

/*
public class Certificate {
    private String VersionNumber;
    private String SerialNumber;
    private String SignatureAlgorithmID;
    private String IssuerName;
    private ValidityPeriod Validity;
    private String SubjectName;
    private SubjectPublicKeyInfo SubjectPublicKey;
    private String CertificateSignatureAlgorithm;
    private String CertificateSignature;

    public Certificate(String VersionNumber, String SerialNumber, String SignatureAlgorithmID, String IssuerName,
                       Date NotBefore, Date NotAfter, String SubjectName, String PublicKeyAlgorithm,
                       String PublicKey, String CertificateSignatureAlgorithm, String CertificateSignature) {
        this.VersionNumber = VersionNumber;
        this.SerialNumber = SerialNumber;
        this.SignatureAlgorithmID = SignatureAlgorithmID;
        this.IssuerName = IssuerName;
        this.Validity = new ValidityPeriod(NotBefore, NotAfter);
        this.SubjectName = SubjectName;
        this.SubjectPublicKey = new SubjectPublicKeyInfo(PublicKeyAlgorithm, PublicKey);
        this.CertificateSignatureAlgorithm = CertificateSignatureAlgorithm;
        this.CertificateSignature = CertificateSignature;
    }

    public void setVersionNumber(String versionNumber) {
        VersionNumber = versionNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public void setSignatureAlgorithmID(String signatureAlgorithmID) {
        SignatureAlgorithmID = signatureAlgorithmID;
    }

    public void setIssuerName(String issuerName) {
        IssuerName = issuerName;
    }

    public void setValidity(ValidityPeriod validity) {
        Validity = validity;
    }

    public void setValidity(Date notBefore, Date notAfter) {
        Validity = new ValidityPeriod(notBefore, notAfter);
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public void setSubjectPublicKey(SubjectPublicKeyInfo subjectPublicKey) {
        SubjectPublicKey = subjectPublicKey;
    }

    public void setSubjectPublicKey(String publicKeyAlgorithm, String subjectPublicKey) {
        SubjectPublicKey = new SubjectPublicKeyInfo(publicKeyAlgorithm, subjectPublicKey);
    }

    public void setCertificateSignatureAlgorithm(String certificateSignatureAlgorithm) {
        CertificateSignatureAlgorithm = certificateSignatureAlgorithm;
    }

    public void setCertificateSignature(String certificateSignature) {
        CertificateSignature = certificateSignature;
    }

    public String getVersionNumber() {
        return VersionNumber;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public String getSignatureAlgorithmID() {
        return SignatureAlgorithmID;
    }

    public String getIssuerName() {
        return IssuerName;
    }

    public ValidityPeriod getValidity() {
        return Validity;
    }

    public Date getNotBefore() {
        return Validity.getNotBefore();
    }

    public Date getNotAfter() {
        return Validity.getNotAfter();
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public SubjectPublicKeyInfo getSubjectPublicKey() {
        return SubjectPublicKey;
    }

    public String getPublicKeyAlgorithm() {
        return SubjectPublicKey.getPublicKeyAlgorithm();
    }

    public String getPublicKey() {
        return SubjectPublicKey.getPublicKey();
    }

    public String getCertificateSignatureAlgorithm() {
        return CertificateSignatureAlgorithm;
    }

    public String getCertificateSignature() {
        return CertificateSignature;
    }
}

class ValidityPeriod {
    private Date NotBefore;
    private Date NotAfter;

    public ValidityPeriod(Date NotBefore, Date NotAfter) {
        this.NotBefore = NotBefore;
        this.NotAfter = NotAfter;
    }

    public void setNotAfter(Date notAfter) {
        NotAfter = notAfter;
    }

    public void setNotBefore(Date notBefore) {
        NotBefore = notBefore;
    }

    public Date getNotAfter() {
        return NotAfter;
    }

    public Date getNotBefore() {
        return NotBefore;
    }
}


class SubjectPublicKeyInfo {
    private String PublicKeyAlgorithm;
    private String PublicKey;

    public SubjectPublicKeyInfo(String PublicKeyAlgorithm, String PublicKey) {
        this.PublicKeyAlgorithm = PublicKeyAlgorithm;
        this.PublicKey = PublicKey;
    }

    public void setPublicKeyAlgorithm(String publicKeyAlgorithm) {
        PublicKeyAlgorithm = publicKeyAlgorithm;
    }

    public void setPublicKey(String publicKey) {
        PublicKey = publicKey;
    }

    public String getPublicKeyAlgorithm() {
        return PublicKeyAlgorithm;
    }

    public String getPublicKey() {
        return PublicKey;
    }
}
*/

public class Certificate {
    private String SerialNumber;
    private String Path;
    private Date NotBefore;
    private Date NotAfter;
    private String Username;

    public Certificate(String SerialNumber, String Path, Date NotBefore, Date NotAfter, String Username) {
        this.SerialNumber = SerialNumber;
        this.Path = Path;
        this.NotBefore = NotBefore;
        this.NotAfter = NotAfter;
        this.Username = Username;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public void setPath(String path) {
        Path = path;
    }

    public void setNotBefore(Date notBefore) {
        NotBefore = notBefore;
    }

    public void setNotAfter(Date notAfter) {
        NotAfter = notAfter;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public String getPath() {
        return Path;
    }

    public Date getNotBefore() {
        return NotBefore;
    }

    public Date getNotAfter() {
        return NotAfter;
    }

    public String getUsername() {
        return Username;
    }
}
