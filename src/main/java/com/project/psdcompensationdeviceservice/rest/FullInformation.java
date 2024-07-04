package com.project.psdcompensationdeviceservice.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullInformation {
    private short fullInformationId;
    private String busbarName;
    private short amount;
    private float avgDailyActivePower;
    private float avgDailyReactivePower;
    private short effectiveAmountOfEquipment;
    private float coefficientMax;
    private float maxActivePower;
    private float maxReactivePower;
    private float maxFullPower;
    private float maxElectricCurrent;
    private float activePower;
    private float cosF;
    private float tgF;
    private float ki;
    private float module;
}
