package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcquisitionRepository extends JpaRepository<Acquisition, Long> {
}
