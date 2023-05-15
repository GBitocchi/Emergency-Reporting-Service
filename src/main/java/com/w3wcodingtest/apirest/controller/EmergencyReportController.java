package com.w3wcodingtest.apirest.controller;

import com.w3wcodingtest.apirest.dto.ThreeWordAddress;
import com.w3wcodingtest.apirest.service.EmergencyReportService;
import com.w3wcodingtest.apirest.dto.EmergencyReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergencyapi")
@CrossOrigin(origins = "*", methods= {RequestMethod.POST})
@Api(tags = {"Reports"})
@Log4j2
public class EmergencyReportController {

    private final EmergencyReportService emergencyReportService;

    public EmergencyReportController(EmergencyReportService emergencyReportService) {
        this.emergencyReportService = emergencyReportService;
    }

    @PostMapping("/reports")
    @ApiOperation(value = "Takes the report information, validates it and fills in the missing information.")
    public EmergencyReport createValidatedReport(@Validated @RequestBody EmergencyReport emergencyReport) {
        log.info("Report validation request made.");
        return this.emergencyReportService.createValidatedReport(emergencyReport);
    }

    @PostMapping("/welsh-convert")
    @ApiOperation(value = "Returns the equivalent Welsh 3wa to a location when submitted to in English.")
    public ThreeWordAddress convertEquivalentWelshThreeWordLocation(@Validated @RequestBody ThreeWordAddress threeWordAddress) {
        log.info("English 3wa to Welsh 3wa equivalent request made.");
        return this.emergencyReportService.convertEquivalentThreeWordLocation(threeWordAddress, "en", "cy");
    }

    @PostMapping("/welsh-3wa")
    @ApiOperation(value = "Returns the equivalent English 3wa to a location when submitted to in Welsh.")
    public ThreeWordAddress convertEquivalentEnglishThreeWordLocation(@Validated @RequestBody ThreeWordAddress threeWordAddress) {
        log.info("Welsh 3wa to English 3wa equivalent request made.");
        return this.emergencyReportService.convertEquivalentThreeWordLocation(threeWordAddress, "cy", "en");
    }
}
