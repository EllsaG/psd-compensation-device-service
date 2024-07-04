package com.project.psdcompensationdeviceservice.controller;


import com.project.psdcompensationdeviceservice.controller.dto.CompensationDeviceRequestDTO;
import com.project.psdcompensationdeviceservice.controller.dto.CompensationDeviceResponseDTO;
import com.project.psdcompensationdeviceservice.controller.dto.CompensationDeviceSelectionInformationRequestDTO;
import com.project.psdcompensationdeviceservice.service.CompensationDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationDeviceController {

    private final CompensationDeviceService compensationDeviceService;

    @Autowired
    public CompensationDeviceController(CompensationDeviceService compensationDeviceService) {
        this.compensationDeviceService = compensationDeviceService;
    }



    @GetMapping("/getAllInformation")
    public CompensationDeviceResponseDTO getAllInformation(){
        return new CompensationDeviceResponseDTO(compensationDeviceService.getAllCompensationDevices(),
                compensationDeviceService.getAllCompensationDevicesSelectionInformation());
    }
    @PutMapping("/create/compensationDevice")
    public CompensationDeviceResponseDTO createCompensationDevice(@RequestBody CompensationDeviceRequestDTO compensationDeviceRequestDTO) {
        return compensationDeviceService.saveCompensationDevice(compensationDeviceRequestDTO.getCompensationDeviceId(),
                compensationDeviceRequestDTO.getNameOfCompensationDevice(),
                compensationDeviceRequestDTO.getReactivePowerOfCompensationDevice());
    }

    @PutMapping("/create/selectionInformation")
    public void createCompensationDeviceSelectionInformation(@RequestBody CompensationDeviceSelectionInformationRequestDTO compensationDeviceSelectionInformationRequestDTO) {
         compensationDeviceService.saveCompensationDeviceSelectionInformation(
                compensationDeviceSelectionInformationRequestDTO.getCompensationDeviceSelectionId(),
                compensationDeviceSelectionInformationRequestDTO.getAvgDailyActivePower(),
                compensationDeviceSelectionInformationRequestDTO.getTgfBeforeCompensation());
    }


    @PutMapping("/update/compensationDevice")
    public CompensationDeviceResponseDTO updateCompensationDevice(@RequestBody CompensationDeviceRequestDTO compensationDeviceRequestDTO){
        return compensationDeviceService.updateCompensationDevice(compensationDeviceRequestDTO.getCompensationDeviceId(),
                compensationDeviceRequestDTO.getNameOfCompensationDevice(),
                compensationDeviceRequestDTO.getReactivePowerOfCompensationDevice());
    }

    @DeleteMapping("/delete/{compensationDeviceId}")
    public CompensationDeviceResponseDTO deleteCompensationDevice(@PathVariable short compensationDeviceId){
        return compensationDeviceService.deleteCompensationDeviceById(compensationDeviceId);
    }

    @DeleteMapping("/delete/selectionInformation/{compensationDeviceId}")
    public void deleteCompensationDeviceSelectionInformation(@PathVariable short compensationDeviceId){
        compensationDeviceService.deleteCompensationDeviceSelectionInformationById(compensationDeviceId);
    }


    @GetMapping("/check/{compensationDeviceId}")
    public Boolean checkAvailability(@PathVariable short compensationDeviceId){
        return compensationDeviceService.isAvailable(compensationDeviceId);
    }








}
