package com.w3wcodingtest.apirest.dto;

import lombok.Data;

@Data
public class ThreeWordsSuggestion {

    private String country;

    private String nearestPlace;

    private String words;

    public ThreeWordsSuggestion(String country, String nearestPlace, String words) {
        this.country = country;
        this.nearestPlace = nearestPlace;
        this.words = words;
    }
}
