package com.w3wcodingtest.apirest.exception;

import com.w3wcodingtest.apirest.dto.ThreeWordsSuggestion;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class EmergencyReportException extends RuntimeException {
    private String message;
    private List<ThreeWordsSuggestion> suggestions;
    private HttpStatus httpStatus;

    public EmergencyReportException(String message, List<ThreeWordsSuggestion> suggestions, HttpStatus httpStatus) {
        this.message = message;
        this.suggestions = suggestions;
        this.httpStatus = httpStatus;
    }
}
