package eca.data.migration.service;

import eca.data.db.SqlQueryHelper;
import eca.data.file.FileDataLoader;
import eca.data.migration.config.MigrationConfig;
import eca.data.migration.exception.MigrationException;
import eca.data.migration.model.MigrationData;
import eca.data.migration.model.entity.MigrationLog;
import eca.data.migration.model.entity.MigrationStatus;
import eca.data.migration.repository.MigrationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import weka.core.Instances;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Service for migration data file into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MigrationService {

    private static final String TABLE_NAME_FORMAT = "%s_%d";
    private static final String LIKE_PATTERN = "%{0}%";

    private final MigrationConfig config;
    private final InstancesService instancesService;
    private final MigrationLogRepository migrationLogRepository;

    /**
     * Migrates training data file into database.
     *
     * @param migrationData - migration data
     */
    public void migrateData(MigrationData migrationData) {
        FileDataLoader dataLoader = new FileDataLoader();
        dataLoader.setDateFormat(config.getDateFormat());
        dataLoader.setSource(migrationData.getDataResource());
        log.info("Starting to migrate file '{}'.", migrationData.getDataResource().getFile());
        MigrationLog migrationLog = createAndSaveMigrationLog(migrationData);
        try {
            Instances instances = dataLoader.loadInstances();
            log.info("Data has been loaded from file '{}'", migrationData.getDataResource().getFile());
            instancesService.migrateInstances(migrationLog.getTableName(), instances);
            migrationLog.setNumInstances(instances.numInstances());
            migrationLog.setNumAttributes(instances.numAttributes());
            migrationLog.setMigrationStatus(MigrationStatus.SUCCESS);
        } catch (Exception ex) {
            migrationLog.setMigrationStatus(MigrationStatus.ERROR);
            migrationLog.setDetails(ex.getMessage());
            throw new MigrationException(ex.getMessage());
        } finally {
            migrationLog.setFinishDate(LocalDateTime.now());
            migrationLogRepository.save(migrationLog);
        }
    }

    private synchronized MigrationLog createAndSaveMigrationLog(MigrationData migrationData) {
        MigrationLog migrationLog = new MigrationLog();
        migrationLog.setSourceFileName(migrationData.getDataResource().getFile());
        migrationLog.setTableName(getTableName(migrationData));
        migrationLog.setMigrationStatus(MigrationStatus.IN_PROGRESS);
        migrationLog.setMigrationLogSource(migrationData.getMigrationLogSource());
        migrationLog.setStartDate(LocalDateTime.now());
        return migrationLogRepository.save(migrationLog);
    }

    /**
     * Generates table name in case if it isn't specified. A new unique table postfix is generated.
     *
     * @param migrationData - migration data
     * @return generated table name
     */
    private String getTableName(MigrationData migrationData) {
        if (StringUtils.isEmpty(migrationData.getTableName())) {
            long maxId = migrationLogRepository.findMaxId();
            String normalizedTableName = SqlQueryHelper.normalizeName(migrationData.getDataResource().getFile());
            Set<String> tableNames =
                    migrationLogRepository.findTableNamesLike(MessageFormat.format(LIKE_PATTERN, normalizedTableName));
            String tableName = String.format(TABLE_NAME_FORMAT, normalizedTableName, ++maxId);
            while (tableNames.contains(tableName)) {
                tableName = String.format(TABLE_NAME_FORMAT, normalizedTableName, maxId << 2);
            }
            return tableName;
        } else {
            return SqlQueryHelper.normalizeName(migrationData.getTableName());
        }
    }
}
