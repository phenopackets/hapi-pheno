package org.monarchinitiative.hapiphenocore.except;

public class PhenoClientRuntimeException extends RuntimeException {

    public PhenoClientRuntimeException(){
       super();
    }

    public PhenoClientRuntimeException(String msg) {
        super(msg);
    }
}
