package eca.data.migration.mapping;

import eca.data.migration.TestHelperUtils;
import eca.data.migration.model.MigrationLogDto;
import eca.data.migration.model.entity.MigrationLog;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link MigrationLogMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MigrationLogMapperTest {

    @Inject
    private MigrationLogMapper migrationLogMapper;

    @Test
    public void testMapMigrationLog() {
        MigrationLog migrationLog = TestHelperUtils.createMigrationLog();
        MigrationLogDto migrationLogDto = migrationLogMapper.map(migrationLog);
        Assertions.assertThat(migrationLogDto.getTableName()).isEqualTo(migrationLog.getTableName());
        Assertions.assertThat(migrationLogDto.getNumInstances()).isEqualTo(migrationLog.getNumInstances());
        Assertions.assertThat(migrationLogDto.getNumAttributes()).isEqualTo(migrationLog.getNumAttributes());
        Assertions.assertThat(migrationLogDto.getSourceFileName()).isEqualTo(migrationLog.getSourceFileName());
        Assertions.assertThat(migrationLogDto.getFinishDate()).isEqualTo(migrationLog.getFinishDate());
        Assertions.assertThat(migrationLogDto.getMigrationLogSource()).isEqualTo(migrationLog.getMigrationLogSource());
    }
}
