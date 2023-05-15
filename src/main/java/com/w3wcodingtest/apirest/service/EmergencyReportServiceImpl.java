package com.w3wcodingtest.apirest.service;

import com.w3wcodingtest.apirest.dto.EmergencyReport;
import com.w3wcodingtest.apirest.dto.ThreeWordAddress;
import com.w3wcodingtest.apirest.dto.ThreeWordsSuggestion;
import com.w3wcodingtest.apirest.exception.EmergencyReportException;
import com.w3wcodingtest.apirest.util.WhatThreeWordsApi;
import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import com.what3words.javawrapper.response.ConvertTo3WA;
import com.what3words.javawrapper.response.ConvertToCoordinates;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class EmergencyReportServiceImpl implements EmergencyReportService {

    private static final What3WordsV3 whatThreeWordsWrapper = WhatThreeWordsApi.getWrapper();

    private ConvertToCoordinates getThreeWordCoordinates(String threeWordAddress, String language){
        ConvertToCoordinates threeWordCoordinatesConverted = whatThreeWordsWrapper.convertToCoordinates(threeWordAddress).execute();

        if(!threeWordCoordinatesConverted.isSuccessful()){

            if(threeWordCoordinatesConverted.getError().getKey().equals("BadWords")){
                autoSuggestThreeWordAddresses(threeWordAddress, language);
            }

            log.error("Error while performing convertToCoordinates request. Details: " + threeWordCoordinatesConverted.getError().getMessage());
            throw new EmergencyReportException(threeWordCoordinatesConverted.getError().getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return threeWordCoordinatesConverted;
    }

    private ConvertTo3WA getThreeWordAddressFromCoordinates(Double latitude, Double longitude, String language){
        ConvertTo3WA coordinatesThreeWordConverted = whatThreeWordsWrapper.convertTo3wa(new Coordinates(latitude, longitude)).language(language).execute();

        if(!coordinatesThreeWordConverted.isSuccessful()){
            log.error("Error while performing convertTo3wa request. Details: " + coordinatesThreeWordConverted.getError().getMessage());
            throw new EmergencyReportException(coordinatesThreeWordConverted.getError().getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return coordinatesThreeWordConverted;
    }

    private boolean isValidUKAddress(Double latitude, Double longitude) {
        return getThreeWordAddressFromCoordinates(latitude, longitude, "en").getCountry().equals("GB");
    }

    private boolean isValidUKAddress(String threeWordAddress, String language) {
        return getThreeWordCoordinates(threeWordAddress, language).getCountry().equals("GB");
    }

    private void autoSuggestThreeWordAddresses(String threeWordAddress, String language){
        Autosuggest autoSuggestThreeWordAddresses = whatThreeWordsWrapper.autosuggest(threeWordAddress).clipToCountry("GB").language(language).execute();

        if(!autoSuggestThreeWordAddresses.isSuccessful()){
            log.error("Error while performing autosuggest request. Details: " + autoSuggestThreeWordAddresses.getError().getMessage());
            throw new EmergencyReportException(autoSuggestThreeWordAddresses.getError().getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<ThreeWordsSuggestion> threeWordSuggestions = autoSuggestThreeWordAddresses.getSuggestions().stream().map(suggestion -> new ThreeWordsSuggestion(suggestion.getCountry(), suggestion.getNearestPlace(), suggestion.getWords())).toList();

        throw new EmergencyReportException("3wa not recognised: " + threeWordAddress, threeWordSuggestions, HttpStatus.BAD_REQUEST);
    }

    @Override
    public EmergencyReport createValidatedReport(EmergencyReport emergencyReport) {
        if((emergencyReport.getThreeWordAddress() == null) && (emergencyReport.getLatitude() == null || emergencyReport.getLongitude() == null)){
            throw new EmergencyReportException("You should provide at least a three-word address or a pair of latitude and longitude address", null, HttpStatus.BAD_REQUEST);
        }

        if(emergencyReport.getThreeWordAddress() == null){
            ConvertTo3WA threeWordAddress = getThreeWordAddressFromCoordinates(emergencyReport.getLatitude(), emergencyReport.getLongitude(), "en");
            emergencyReport.setThreeWordAddress(threeWordAddress.getWords());
        }

        if(!WhatThreeWordsApi.isValidThreeWordAddress(emergencyReport.getThreeWordAddress())){
            autoSuggestThreeWordAddresses(emergencyReport.getThreeWordAddress(), "en");
        }

        if(!isValidUKAddress(emergencyReport.getThreeWordAddress(), "en")){
            throw new EmergencyReportException("3wa address supplied is not within the bounds of the UK", null, HttpStatus.BAD_REQUEST);
        }

        if(emergencyReport.getLatitude() == null || emergencyReport.getLongitude() == null){
            ConvertToCoordinates coordinates = getThreeWordCoordinates(emergencyReport.getThreeWordAddress(), "en");
            emergencyReport.setLatitude(coordinates.getCoordinates().getLat());
            emergencyReport.setLongitude(coordinates.getCoordinates().getLng());
        }

        if(!isValidUKAddress(emergencyReport.getLatitude(), emergencyReport.getLongitude())){
            throw new EmergencyReportException("Latitude and Longitude supplied are not within the bounds of the UK", null, HttpStatus.BAD_REQUEST);
        }

        //TODO: Should we check if the three-word address and the coordinates are equivalent?

        return emergencyReport;
    }

    @Override
    public ThreeWordAddress convertEquivalentThreeWordLocation(ThreeWordAddress threeWordAddress, String fromLanguage, String toLanguage) {
        if(threeWordAddress.getThreeWordAddress() == null){
            throw new EmergencyReportException("3wa address must be supplied", null, HttpStatus.BAD_REQUEST);
        }

        if(!WhatThreeWordsApi.isValidThreeWordAddress(threeWordAddress.getThreeWordAddress())){
            autoSuggestThreeWordAddresses(threeWordAddress.getThreeWordAddress(), fromLanguage);
        }

        if(!isValidUKAddress(threeWordAddress.getThreeWordAddress(), fromLanguage)){
            throw new EmergencyReportException("3wa address supplied is not within the bounds of the UK", null, HttpStatus.BAD_REQUEST);
        }

        // We get first the coordinates from the current three-word address in its original language, then we retrieve its three-word address in the required language.
        ConvertToCoordinates coordinates = getThreeWordCoordinates(threeWordAddress.getThreeWordAddress(), fromLanguage);
        ConvertTo3WA threeWordAddressConverted = getThreeWordAddressFromCoordinates(coordinates.getCoordinates().getLat(), coordinates.getCoordinates().getLng(), toLanguage);

        threeWordAddress.setThreeWordAddress(threeWordAddressConverted.getWords());

        return threeWordAddress;
    }
}
