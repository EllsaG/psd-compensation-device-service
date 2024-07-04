package com.project.psdcompensationdeviceservice.controller.dto;


import com.project.psdcompensationdeviceservice.entity.CompensationDevice;
import com.project.psdcompensationdeviceservice.entity.CompensationDeviceSelection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompensationDeviceResponseDTO {
    List<CompensationDevice> compensationDeviceList;
    List<CompensationDeviceSelection> compensationDeviceSelectionList;



}
