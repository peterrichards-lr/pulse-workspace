package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    boolean existsByName(String name);
}
