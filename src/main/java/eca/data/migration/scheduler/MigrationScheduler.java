package eca.data.migration.scheduler;

import eca.data.DataFileExtension;
import eca.data.file.resource.FileResource;
import eca.data.migration.config.MigrationConfig;
import eca.data.migration.model.MigrationData;
import eca.data.migration.model.entity.MigrationLogSource;
import eca.data.migration.service.MigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

/**
 * Service for migration data from specified directory.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MigrationScheduler {

    private final MigrationService migrationService;
    private final MigrationConfig migrationConfig;

    /**
     * Reads training data files from specified directory on disk and saves them into database.
     */
    @Scheduled(fixedDelayString = "${migration.durationInSeconds}000")
    public void migrate() {
        if (!Boolean.TRUE.equals(migrationConfig.getJobEnabled())) {
            log.trace("Migration job is disabled.");
        } else {
            log.trace("Starting to migrate files.");
            Collection<File> listFiles =
                    FileUtils.listFiles(new File(migrationConfig.getDataStoragePath()),
                            DataFileExtension.getExtensions(),
                            true);
            log.trace("Fetching {} new data files.", listFiles.size());
            for (File file : listFiles) {
                try {
                    MigrationData migrationData = new MigrationData();
                    migrationData.setDataResource(new FileResource(file));
                    migrationData.setMigrationLogSource(MigrationLogSource.JOB);
                    migrationService.migrateData(migrationData);
                    FileUtils.forceDelete(file);
                } catch (Exception ex) {
                    log.error("There was an error while migration file '{}': {}", file.getAbsolutePath(),
                            ex.getMessage());
                }
            }
            log.trace("Files migration has been completed.");
        }
    }
}
