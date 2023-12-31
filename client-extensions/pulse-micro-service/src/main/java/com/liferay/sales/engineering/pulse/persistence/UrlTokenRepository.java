package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.UrlToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlTokenRepository extends JpaRepository<UrlToken, String> {
    void deleteByExternalReferenceCode(String externalReferenceCode);

    boolean existsByExternalReferenceCode(String externalReferenceCode);

    UrlToken findByExternalReferenceCode(String externalReferenceCode);
}