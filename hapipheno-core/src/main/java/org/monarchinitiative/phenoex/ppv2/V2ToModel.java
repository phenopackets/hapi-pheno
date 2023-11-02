package org.monarchinitiative.phenoex.ppv2;

import com.essaid.model.EntityManager;

import java.util.HashMap;
import java.util.Map;

public class V2ToModel implements V2Visitor {


    private final EntityManager entityManager;
    private final Map<String, String> prefixMap = new HashMap<>();

    public V2ToModel(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Map<String, String> getPrefixMap() {
        return prefixMap;
    }
}
