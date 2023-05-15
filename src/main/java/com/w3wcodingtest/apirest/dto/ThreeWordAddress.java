package com.w3wcodingtest.apirest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ThreeWordAddress {
    @JsonProperty("3wa")
    private String threeWordAddress;
}
