package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    void deleteByExternalReferenceCode(String externalReferenceCode);

    boolean existsByExternalReferenceCode(String externalReferenceCode);

    boolean existsByName(String name);

    Campaign findByExternalReferenceCode(String externalReferenceCode);

    @Query("SELECT c FROM Campaign c WHERE c.end <= CURRENT_TIME AND c.status.name = 'Active'")
    List<Campaign> findExpiredCampaigns();

    @Query("SELECT c FROM Campaign c WHERE c.begin <= CURRENT_TIME AND (c.end IS NULL OR c.end >= CURRENT_TIME) AND c.status.name = 'Inactive'")
    List<Campaign> findPendingCampaigns();
}
