package com.akigo.dao.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSetting {

    private List<String> jndiNames = new ArrayList<>();

    private List<JndiRef> jndiRef = new ArrayList<>();

    public List<String> getJndiNames() {
        return jndiNames;
    }

    @JsonProperty
    public void setJndiNames(List<String> jndiNames) {
        this.jndiNames = jndiNames;
    }

    public List<JndiRef> getJndiRef() {
        return jndiRef;
    }

    @JsonProperty
    public void setJndiRef(List<JndiRef> jndiRef) {
        this.jndiRef = jndiRef;
    }
}
