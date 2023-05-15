package com.w3wcodingtest.apirest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w3wcodingtest.apirest.dto.EmergencyReport;
import com.w3wcodingtest.apirest.dto.ThreeWordAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmergencyReportAppTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validEmergencyReportWithFieldsFilled_validateReport_ResultWithoutError() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(51.508341);
        emergencyReport.setLongitude(-0.125499);
        emergencyReport.setThreeWordAddress("daring.lion.race");
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void validEmergencyReportWithThreeWordFieldNull_validateReport_ResultWithoutErrorAndThreeWordFieldFilled() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(51.508341);
        emergencyReport.setLongitude(-0.125499);
        emergencyReport.setThreeWordAddress(null);
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        MvcResult result = mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        EmergencyReport emergencyReportResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmergencyReport.class);
        assertEquals("daring.lion.race", emergencyReportResponse.getThreeWordAddress());
    }

    @Test
    public void validEmergencyReportWithCoordinatesFieldsNull_validateReport_ResultWithoutErrorAndCoordinatesFieldsFilled() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(null);
        emergencyReport.setLongitude(null);
        emergencyReport.setThreeWordAddress("daring.lion.race");
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        MvcResult result = mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        EmergencyReport emergencyReportResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmergencyReport.class);
        assertEquals(51.508341, emergencyReportResponse.getLatitude());
        assertEquals(-0.125499, emergencyReportResponse.getLongitude());
    }

    @Test
    public void invalidEmergencyReportWithCoordinatesAndThreeWordFieldsNull_validateReport_ResultWithError() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(null);
        emergencyReport.setLongitude(null);
        emergencyReport.setThreeWordAddress(null);
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You should provide at least a three-word address or a pair of latitude and longitude address"));
    }

    @Test
    public void invalidEmergencyReportWithThreeWordFieldOutOfTheUK_validateReport_ResultWithError() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(null);
        emergencyReport.setLongitude(null);
        emergencyReport.setThreeWordAddress("daring.lion.races");
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa address supplied is not within the bounds of the UK"));
    }

    @Test
    public void invalidEmergencyReportWithInvalidThreeWordField_validateReport_ResultWithError() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(null);
        emergencyReport.setLongitude(null);
        emergencyReport.setThreeWordAddress("invalidThreeWordAddress");
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa not recognised: invalidThreeWordAddress"));
    }

    @Test
    public void invalidEmergencyReportWithIncompleteThreeWordField_validateReport_ResultWithErrorAndSuggestions() throws Exception {
        EmergencyReport emergencyReport = new EmergencyReport();
        emergencyReport.setMessage("A hiker has got lost");
        emergencyReport.setLatitude(null);
        emergencyReport.setLongitude(null);
        emergencyReport.setThreeWordAddress("daring.lion.ra");
        emergencyReport.setReportingOfficerName("Joe Bloggs");
        String emergencyReportJsonified = new ObjectMapper().writeValueAsString(emergencyReport);

        mockMvc.perform(post("/emergencyapi/reports").contentType(MediaType.APPLICATION_JSON).content(emergencyReportJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa not recognised: daring.lion.ra"))
                .andExpect(jsonPath("$.suggestions[0].words").value("daring.lion.race"));
    }

    @Test
    public void validWelshThreeWordAddress_englishConvert_EnglishThreeWordAddressEquivalentResult() throws Exception {
        ThreeWordAddress threeWordAddress = new ThreeWordAddress();
        threeWordAddress.setThreeWordAddress("sychach.parciau.lwmpyn");
        String threeWordAddressJsonified = new ObjectMapper().writeValueAsString(threeWordAddress);

        MvcResult result = mockMvc.perform(post("/emergencyapi/welsh-3wa").contentType(MediaType.APPLICATION_JSON).content(threeWordAddressJsonified).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        ThreeWordAddress threeWordAddressResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ThreeWordAddress.class);
        assertEquals("daring.lion.race", threeWordAddressResponse.getThreeWordAddress());
    }

    @Test
    public void validEnglishThreeWordAddress_welshConvert_WelshThreeWordAddressEquivalentResult() throws Exception {
        ThreeWordAddress threeWordAddress = new ThreeWordAddress();
        threeWordAddress.setThreeWordAddress("daring.lion.race");
        String threeWordAddressJsonified = new ObjectMapper().writeValueAsString(threeWordAddress);

        MvcResult result = mockMvc.perform(post("/emergencyapi/welsh-convert").contentType(MediaType.APPLICATION_JSON).content(threeWordAddressJsonified).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        ThreeWordAddress threeWordAddressResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ThreeWordAddress.class);
        assertEquals("sychach.parciau.lwmpyn", threeWordAddressResponse.getThreeWordAddress());
    }

    @Test
    public void missingEnglishThreeWordAddress_welshConvert_ResultWithError() throws Exception {
        ThreeWordAddress threeWordAddress = new ThreeWordAddress();
        threeWordAddress.setThreeWordAddress(null);
        String threeWordAddressJsonified = new ObjectMapper().writeValueAsString(threeWordAddress);

        mockMvc.perform(post("/emergencyapi/welsh-convert").contentType(MediaType.APPLICATION_JSON).content(threeWordAddressJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa address must be supplied"));
    }

    @Test
    public void incompleteEnglishThreeWordAddress_welshConvert_ResultWithError() throws Exception {
        ThreeWordAddress threeWordAddress = new ThreeWordAddress();
        threeWordAddress.setThreeWordAddress("daring.lion.ra");
        String threeWordAddressJsonified = new ObjectMapper().writeValueAsString(threeWordAddress);

        mockMvc.perform(post("/emergencyapi/welsh-convert").contentType(MediaType.APPLICATION_JSON).content(threeWordAddressJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa not recognised: daring.lion.ra"))
                .andExpect(jsonPath("$.suggestions[0].words").value("daring.lion.race"));;
    }

    @Test
    public void outboundEnglishThreeWordAddress_welshConvert_ResultWithError() throws Exception {
        ThreeWordAddress threeWordAddress = new ThreeWordAddress();
        threeWordAddress.setThreeWordAddress("daring.lion.races");
        String threeWordAddressJsonified = new ObjectMapper().writeValueAsString(threeWordAddress);

        mockMvc.perform(post("/emergencyapi/welsh-convert").contentType(MediaType.APPLICATION_JSON).content(threeWordAddressJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa address supplied is not within the bounds of the UK"));
    }

    @Test
    public void invalidEnglishThreeWordAddress_welshConvert_ResultWithError() throws Exception {
        ThreeWordAddress threeWordAddress = new ThreeWordAddress();
        threeWordAddress.setThreeWordAddress("invalidThreeWordAddress");
        String threeWordAddressJsonified = new ObjectMapper().writeValueAsString(threeWordAddress);

        mockMvc.perform(post("/emergencyapi/welsh-convert").contentType(MediaType.APPLICATION_JSON).content(threeWordAddressJsonified).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("3wa not recognised: invalidThreeWordAddress"));
    }
}
