package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(String name);
}
