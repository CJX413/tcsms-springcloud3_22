package com.tcsms.business.Dao;

import com.tcsms.business.Entity.BuildingRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface BuildingRegistryDao extends JpaRepository<BuildingRegistry, String>, JpaSpecificationExecutor<BuildingRegistry>, Serializable {
}
