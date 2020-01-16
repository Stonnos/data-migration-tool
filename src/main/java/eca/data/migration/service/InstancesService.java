package eca.data.migration.service;

import eca.data.db.SqlQueryHelper;
import eca.data.migration.config.MigrationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weka.core.Instances;

/**
 * Instances migration service.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InstancesService {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionalService transactionalMigrationService;
    private final MigrationConfig migrationConfig;
    private final SqlQueryHelper sqlQueryHelper;

    /**
     * Saves training data into database.
     *
     * @param tableName - training data table name
     * @param instances - training data
     */
    @Transactional
    public void migrateInstances(String tableName, Instances instances) {
        log.info("Starting to migrate instances '{}' into table '{}'.", instances.relationName(), tableName);
        log.info("Starting to create table '{}'.", tableName);
        String createTableQuery = sqlQueryHelper.buildCreateTableQuery(tableName, instances);
        log.trace("create table query: {}", createTableQuery);
        jdbcTemplate.execute(createTableQuery);
        log.info("Table '{}' has been successfully created.", tableName);

        log.info("Starting to migrate data into table '{}'.", tableName);
        int batchSize = migrationConfig.getBatchSize();
        for (int offset = 0; offset < instances.numInstances(); offset += batchSize) {
            log.trace("Starting to migrate batch with limit = {}, offset = {} into table '{}'.", batchSize, offset,
                    tableName);
            transactionalMigrationService.migrateBatch(tableName, instances, batchSize, offset);
            log.trace("{} rows has been migrated into table '{}'.", offset + batchSize, tableName);
        }
        log.info("Data has been migrated into table '{}'.", tableName);
        log.info("Migration has been successfully completed. Instances '{}' has been migrated into table '{}.",
                instances.relationName(), tableName);
    }
}
