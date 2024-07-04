package com.project.psdcompensationdeviceservice.repository;

import com.project.psdcompensationdeviceservice.entity.CompensationDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompensationDeviceRepository
        extends JpaRepository<CompensationDevice,Short> {
}
