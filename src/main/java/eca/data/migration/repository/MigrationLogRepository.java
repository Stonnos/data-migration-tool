package eca.data.migration.repository;

import eca.data.migration.model.entity.MigrationLog;
import eca.data.migration.model.entity.MigrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Repository to manage with {@link MigrationLog} persistence entity.
 *
 * @author Roman Batygin
 */
public interface MigrationLogRepository extends JpaRepository<MigrationLog, Long> {

    /**
     * Finds last id.
     *
     * @return last id
     */
    @Query("select coalesce(max(ml.id), 0) from MigrationLog ml")
    Long findMaxId();

    /**
     * Gets table names contains specified name.
     *
     * @param tableName - table name like pattern
     * @return table names
     */
    @Query("select ml.tableName from MigrationLog ml where ml.tableName like :tableName")
    Set<String> findTableNamesLike(@Param("tableName") String tableName);

    /**
     * Gets migration logs with specified statuses.
     *
     * @param migrationStatuses - migration statuses
     * @return migration log list
     */
    List<MigrationLog> findByMigrationStatusIn(Collection<MigrationStatus> migrationStatuses);
}
