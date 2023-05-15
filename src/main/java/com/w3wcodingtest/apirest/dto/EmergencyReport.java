package com.w3wcodingtest.apirest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmergencyReport {
    @JsonProperty("message")
    private String message;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lng")
    private Double longitude;

    @JsonProperty("3wa")
    private String threeWordAddress;

    @JsonProperty("reportingOfficerName")
    private String reportingOfficerName;
}
