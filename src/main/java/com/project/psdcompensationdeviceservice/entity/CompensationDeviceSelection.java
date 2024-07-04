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
@Table(name = "compensation_device_selection")
public class CompensationDeviceSelection {
    @Id
    @Column(name ="compensation_device_selection_id")
    private short compensationDeviceSelectionId;
    @Column(name = "min_power_of_compensation_device")
    private float minPowerOfCompensatingDevice;
    @Column(name = "max_power_of_compensation_device")
    private float maxPowerOfCompensatingDevice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompensationDeviceSelection that = (CompensationDeviceSelection) o;
        return compensationDeviceSelectionId == that.compensationDeviceSelectionId && Float.compare(minPowerOfCompensatingDevice, that.minPowerOfCompensatingDevice) == 0 && Float.compare(maxPowerOfCompensatingDevice, that.maxPowerOfCompensatingDevice) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compensationDeviceSelectionId, minPowerOfCompensatingDevice, maxPowerOfCompensatingDevice);
    }
}