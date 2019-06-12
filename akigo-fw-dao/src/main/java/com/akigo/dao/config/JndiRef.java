package com.akigo.dao.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JndiRef {
    @JsonProperty
    private String key = "";

    @JsonProperty
    private String jndiName = "";
}
