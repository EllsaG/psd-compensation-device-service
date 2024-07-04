package com.project.psdcompensationdeviceservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.psdcompensationdeviceservice.PsdCompensationDeviceServiceApplication;
import com.project.psdcompensationdeviceservice.config.SpringH2DatabaseConfig;
import com.project.psdcompensationdeviceservice.controller.dto.CompensationDeviceRequestDTO;
import com.project.psdcompensationdeviceservice.controller.dto.CompensationDeviceResponseDTO;
import com.project.psdcompensationdeviceservice.controller.dto.CompensationDeviceSelectionInformationRequestDTO;
import com.project.psdcompensationdeviceservice.entity.CompensationDevice;
import com.project.psdcompensationdeviceservice.entity.CompensationDeviceSelection;
import com.project.psdcompensationdeviceservice.repository.CompensationDeviceRepository;
import com.project.psdcompensationdeviceservice.repository.CompensationDeviceSelectionRepository;
import com.project.psdcompensationdeviceservice.rest.FullInformation;
import com.project.psdcompensationdeviceservice.rest.FullInformationResponseDTO;
import com.project.psdcompensationdeviceservice.rest.FullInformationServiceClient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {PsdCompensationDeviceServiceApplication.class, SpringH2DatabaseConfig.class})
public class CompensationDeviceControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CompensationDeviceRepository compensationDeviceRepository;

    @Autowired
    private CompensationDeviceSelectionRepository compensationDeviceSelectionRepository;

    @MockBean
    private FullInformationServiceClient fullInformationServiceClient;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceSelectionInformationDB.sql"})
    public void createCompensationDeviceSelectionInformation() throws Exception {

        String REQUEST = createCompensationDeviceSelectionInformationRequestAsString();

        mockMvc.perform(put("/create/selectionInformation")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        CompensationDeviceSelection compensationDeviceSelection = compensationDeviceSelectionRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(0.64F, compensationDeviceSelection.getMinPowerOfCompensatingDevice());
        Assertions.assertEquals(0.7F, compensationDeviceSelection.getMaxPowerOfCompensatingDevice());
    }

    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceSelectionInformationDB.sql",
            "/sql/addCompensationDeviceSelectionInformation.sql"})
    public void createCompensationDevice() throws Exception {

        Mockito.when(fullInformationServiceClient.getFullInformationById(ArgumentMatchers.anyShort()))
                .thenReturn(createFullInformationResponseDTO());

        String REQUEST = createCompensationDeviceRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/create/compensationDevice")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        CompensationDevice compensationDeviceById = compensationDeviceRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        ObjectMapper objectMapper = new ObjectMapper();
        CompensationDeviceResponseDTO compensationDeviceResponseDTO = objectMapper.readValue(body, CompensationDeviceResponseDTO.class);
        CompensationDevice compensationDevice = compensationDeviceResponseDTO.getCompensationDeviceList().get(2);


        Assertions.assertEquals(compensationDeviceById.getModelOfCompensationDevice(), compensationDevice.getModelOfCompensationDevice());
        Assertions.assertEquals(compensationDeviceById.getReactivePowerOfCompensationDevice(), compensationDevice.getReactivePowerOfCompensationDevice());

    }


    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceDB.sql", "/sql/clearCompensationDeviceSelectionInformationDB.sql",
            "/sql/addCompensationDeviceInformation.sql", "/sql/addCompensationDeviceSelectionInformation.sql"})
    public void getAllInformation() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/getAllInformation"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        List<CompensationDeviceSelection> compensationDeviceSelectionRepositoryList = compensationDeviceSelectionRepository.findAll();

        List<CompensationDevice> compensationDeviceRepositoryList = compensationDeviceRepository.findAll();

        ObjectMapper objectMapper = new ObjectMapper();
        CompensationDeviceResponseDTO compensationDeviceResponseDTO = objectMapper.readValue(body, CompensationDeviceResponseDTO.class);

        List<CompensationDevice> compensationDeviceResponseList = compensationDeviceResponseDTO.getCompensationDeviceList();
        List<CompensationDeviceSelection> compensationDeviceSelectionResponseList = compensationDeviceResponseDTO.getCompensationDeviceSelectionList();

        Assertions.assertEquals(compensationDeviceSelectionResponseList.get(0).getCompensationDeviceSelectionId(),
                compensationDeviceSelectionRepositoryList.get(0).getCompensationDeviceSelectionId());

        Assertions.assertEquals(compensationDeviceRepositoryList.get(0).getCompensationDeviceId(),
                compensationDeviceResponseList.get(0).getCompensationDeviceId());
        Assertions.assertEquals(compensationDeviceRepositoryList.get(1).getCompensationDeviceId(),
                compensationDeviceResponseList.get(1).getCompensationDeviceId());
        Assertions.assertEquals(compensationDeviceRepositoryList.get(2).getCompensationDeviceId(),
                compensationDeviceResponseList.get(2).getCompensationDeviceId());

    }


    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceSelectionInformationDB.sql", "/sql/clearCompensationDeviceDB.sql",
            "/sql/addCompensationDeviceInformation.sql", "/sql/addCompensationDeviceSelectionInformation.sql"})
    public void updateCompensationDevice() throws Exception {

        Mockito.when(fullInformationServiceClient.getFullInformationById(ArgumentMatchers.anyShort()))
                .thenReturn(createFullInformationResponseDTO());

        String REQUEST = createCompensationDeviceRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/update/compensationDevice")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        CompensationDevice compensationDeviceById = compensationDeviceRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        ObjectMapper objectMapper = new ObjectMapper();
        CompensationDeviceResponseDTO compensationDeviceResponseDTO = objectMapper.readValue(body, CompensationDeviceResponseDTO.class);
        CompensationDevice compensationDevice = compensationDeviceResponseDTO.getCompensationDeviceList().get(2);

        Assertions.assertEquals(compensationDeviceById.getModelOfCompensationDevice(), compensationDevice.getModelOfCompensationDevice());
        Assertions.assertEquals(compensationDeviceById.getReactivePowerOfCompensationDevice(), compensationDevice.getReactivePowerOfCompensationDevice());

    }


    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceDB.sql", "/sql/addCompensationDeviceInformation.sql"})
    public void deleteCompensationDevice() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/delete/3"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        List<CompensationDevice> compensationDeviceRepositoryList = compensationDeviceRepository.findAll();

        ObjectMapper objectMapper = new ObjectMapper();
        CompensationDeviceResponseDTO compensationDeviceResponseDTO = objectMapper.readValue(body, CompensationDeviceResponseDTO.class);
        List<CompensationDevice> compensationDeviceResponseList = compensationDeviceResponseDTO.getCompensationDeviceList();

        Assertions.assertEquals(compensationDeviceRepositoryList.get(0).getCompensationDeviceId(),
                compensationDeviceResponseList.get(0).getCompensationDeviceId(), (short) 1);
        Assertions.assertEquals(compensationDeviceRepositoryList.get(1).getCompensationDeviceId(),
                compensationDeviceResponseList.get(1).getCompensationDeviceId(), (short) 2);

    }


    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceSelectionInformationDB.sql",
            "/sql/addCompensationDeviceSelectionInformation.sql"})
    public void deleteCompensationDeviceSelectionInformation() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/delete/selectionInformation/3"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        List<CompensationDeviceSelection> compensationDeviceSelectionsRepositoryList = compensationDeviceSelectionRepository.findAll();


        Assertions.assertEquals(body.length(),
                compensationDeviceSelectionsRepositoryList.size(), 0);

    }

    @Test
    @Sql(scripts = {"/sql/clearCompensationDeviceSelectionInformationDB.sql",
            "/sql/addCompensationDeviceSelectionInformation.sql"})
    public void checkAvailability() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/check/3"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        boolean fromRepository = compensationDeviceSelectionRepository.existsById((short) 3);
        String body = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        boolean fromResponse = objectMapper.readValue(body, Boolean.class);

        Assertions.assertEquals(fromRepository, fromResponse);
    }

    private String createCompensationDeviceRequestAsString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(new CompensationDeviceRequestDTO((short) 3, "KRM",
                0.65F));
    }

    private String createCompensationDeviceSelectionInformationRequestAsString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(new CompensationDeviceSelectionInformationRequestDTO(
                (short) 3, 0.94F, 1.16F));
    }

    private FullInformationResponseDTO createFullInformationResponseDTO() {
        return new FullInformationResponseDTO(new FullInformation((short) 3, "ШМА-1", (short) 3, 0.94F,
                1.09F, (short) 3, 3.23F, 3.04F, 1.09F, 3.23F,
                4.91F, 16.5F, 0.65F, 1.16F, 0.06F, 1));
    }


}
