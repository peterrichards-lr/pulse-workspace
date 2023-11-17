package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private static final Log _log = LogFactory.getLog(
            DatabaseLoader.class);
    private final StatusRepository statusRepository;

    @Autowired
    public DatabaseLoader(
            StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    private void populateStatus() {
        Arrays.asList("Draft", "Active", "Complete", "Inactive", "Expired").forEach((name) -> statusRepository.save(new Status(name)));
    }

    @Override
    public void run(String... params) {
        populateStatus();
        _log.info("Populated statuses");
    }
}