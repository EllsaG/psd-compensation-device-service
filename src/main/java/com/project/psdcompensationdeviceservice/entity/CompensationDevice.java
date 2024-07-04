package com.project.psdcompensationdeviceservice.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "compensation_device")
public class CompensationDevice {
    @Id
    @Column(name = "compensation_device_id")
    private short compensationDeviceId;
    @Column(name = "model_of_compensation_device")
    private String modelOfCompensationDevice;
    @Column(name = "reactive_power_of_compensation_device")
    private float reactivePowerOfCompensationDevice;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompensationDevice that = (CompensationDevice) o;
        return compensationDeviceId == that.compensationDeviceId && Float.compare(reactivePowerOfCompensationDevice, that.reactivePowerOfCompensationDevice) == 0 && Objects.equals(modelOfCompensationDevice, that.modelOfCompensationDevice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compensationDeviceId, modelOfCompensationDevice, reactivePowerOfCompensationDevice);
    }
}