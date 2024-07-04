package com.project.psdcompensationdeviceservice.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "full-information-service")
public interface FullInformationServiceClient {

    @RequestMapping(value = "fullInformation/{fullInformationId}", method = RequestMethod.GET)
    FullInformationResponseDTO getFullInformationById(@PathVariable("fullInformationId") short fullInformationId);

}
