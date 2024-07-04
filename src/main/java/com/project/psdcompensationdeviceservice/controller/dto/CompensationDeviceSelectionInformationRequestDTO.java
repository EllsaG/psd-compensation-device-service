package com.project.psdcompensationdeviceservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompensationDeviceSelectionInformationRequestDTO {
    @JsonProperty("compensationDeviceSelectionId")
    private short compensationDeviceSelectionId;
    @JsonProperty("avgDailyActivePower")
    private float avgDailyActivePower;
    @JsonProperty("tgfBeforeCompensation")
    private float tgfBeforeCompensation;
}
