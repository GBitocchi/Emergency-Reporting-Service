package com.w3wcodingtest.apirest.service;

import com.w3wcodingtest.apirest.dto.EmergencyReport;
import com.w3wcodingtest.apirest.dto.ThreeWordAddress;

public interface EmergencyReportService {

    /**
     * Takes the report information, validates it and fills in the missing information.
     *
     * @param emergencyReport a report to validate
     * @return the validated report
     */
    EmergencyReport createValidatedReport(EmergencyReport emergencyReport);

    /**
     * Takes a three-word address with a determined language and converts it to another language.
     *
     * @param threeWordAddress a three-word address
     * @param fromLanguage the current language of the three-word address
     * @param toLanguage the language that the three-word address will be converted
     *
     * @return the validated report
     */
    ThreeWordAddress convertEquivalentThreeWordLocation(ThreeWordAddress threeWordAddress, String fromLanguage, String toLanguage);
}
