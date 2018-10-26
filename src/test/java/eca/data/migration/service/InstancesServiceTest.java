package eca.data.migration.service;

import eca.data.db.SqlQueryHelper;
import eca.data.migration.TestHelperUtils;
import eca.data.migration.config.MigrationConfig;
import eca.data.migration.model.entity.MigrationLog;
import eca.data.migration.repository.MigrationLogRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import weka.core.Instances;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link InstancesService} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@AutoConfigureDataJpa
@EnableJpaRepositories(basePackageClasses = MigrationLogRepository.class)
@EntityScan(basePackageClasses = MigrationLog.class)
@EnableConfigurationProperties
@TestPropertySource("classpath:application.properties")
@Import({InstancesService.class, TransactionalService.class, SqlQueryHelper.class, MigrationConfig.class})
public class InstancesServiceTest {

    private static final String TABLE_NAME = "test_table";
    private static final String SELECT_COUNT_FORMAT = "SELECT count(*) FROM %s";

    @Inject
    private InstancesService instancesService;
    @Inject
    private JdbcTemplate jdbcTemplate;

    private Instances instances;

    @Before
    public void init() throws Exception {
        instances = TestHelperUtils.loadInstances();
    }

    @Test
    public void testMigrateInstances() {
        instancesService.migrateInstances(TABLE_NAME, instances);
        Integer result = jdbcTemplate.queryForObject(String.format(SELECT_COUNT_FORMAT, TABLE_NAME), Integer.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(instances.numInstances());
    }
}
