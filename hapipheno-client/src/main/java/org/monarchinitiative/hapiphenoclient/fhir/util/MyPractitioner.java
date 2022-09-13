package org.monarchinitiative.hapiphenoclient.fhir.util;

/**
 * Convenience class to provide data about a practitioner needed for the FHIR API
 */
public class MyPractitioner {
    private final String id;
    private final String familyName;
    private final String givenName;




    private final static MyPractitioner HARVEY = new MyPractitioner("William", "Harvey", "f1002");
    private final static MyPractitioner APGAR = new MyPractitioner("Virginia", "Apgar", "f1001");

    public MyPractitioner(String name, String familyName, String id){
       this.givenName = name;
       this.familyName = familyName;
       this.id = id;
    }


    public static MyPractitioner harvey() {
        return HARVEY;
    }

    public static MyPractitioner apgar() {
        return APGAR;
    }

    public String getDisplayName() {
        return givenName + " " + familyName;
    }

    public String getReference() {
        return "Practitioner/"  + id;
    }

    public String getId() {
        return id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String suffix() {
        return "MD";
    }
}
