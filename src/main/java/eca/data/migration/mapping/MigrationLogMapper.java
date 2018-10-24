package eca.data.migration.mapping;

import eca.data.migration.model.MigrationLogDto;
import eca.data.migration.model.entity.MigrationLog;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Implements mapping migration log entity to its dto model.
 *
 * @author Roman Batygin
 */
@Mapper
public interface MigrationLogMapper {

    /**
     * Maps migration log entity to its dto model.
     *
     * @param migrationLog - migration log entity
     * @return migration log dto model
     */
    MigrationLogDto map(MigrationLog migrationLog);

    /**
     * Maps migration log entities to its dto models.
     *
     * @param migrationLogList - migration log entities
     * @return migration log dto models list
     */
    List<MigrationLogDto> map(List<MigrationLog> migrationLogList);
}
