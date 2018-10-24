package eca.data.migration;

import eca.data.file.resource.FileResource;
import eca.data.file.xls.XLSLoader;
import eca.data.migration.model.MigrationData;
import eca.data.migration.model.entity.MigrationLog;
import eca.data.migration.model.entity.MigrationLogSource;
import eca.data.migration.model.entity.MigrationStatus;
import weka.core.Instances;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Test helper utility class.
 *
 * @author Roman Batygin
 */
public class TestHelperUtils {

    private static final String DATA_PATH = "german_credit.xls";
    private static final String TABLE_NAME = "table";
    private static final String FILE_NAME = "table.xls";

    /**
     * Loads test data set.
     *
     * @return created training data
     */
    public static Instances loadInstances() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        XLSLoader dataLoader = new XLSLoader();
        dataLoader.setSource(new FileResource(new File(classLoader.getResource(DATA_PATH).getFile())));
        return dataLoader.loadInstances();
    }

    /**
     * Creates migration data.
     *
     * @return migration data
     */
    public static MigrationData createMigrationData() {
        MigrationData migrationData = new MigrationData();
        migrationData.setDataResource(new FileResource(new File(DATA_PATH)));
        migrationData.setMigrationLogSource(MigrationLogSource.JOB);
        return migrationData;
    }

    /**
     * Creates migration log.
     * @return migration log
     */
    public static MigrationLog createMigrationLog() {
        MigrationLog migrationLog = new MigrationLog();
        migrationLog.setTableName(TABLE_NAME);
        migrationLog.setStartDate(LocalDateTime.now().minusSeconds(1L));
        migrationLog.setFinishDate(LocalDateTime.now().plusSeconds(1L));
        migrationLog.setSourceFileName(FILE_NAME);
        migrationLog.setMigrationStatus(MigrationStatus.SUCCESS);
        migrationLog.setMigrationLogSource(MigrationLogSource.MANUAL);
        return migrationLog;
    }
}
