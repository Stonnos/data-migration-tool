package eca.data.migration.model;

import eca.data.file.resource.DataResource;
import eca.data.migration.model.entity.MigrationLogSource;
import lombok.Data;

/**
 * Migration data model.
 *
 * @author Roman Batygin
 */
@Data
public class MigrationData {

    /**
     * Data resource
     */
    private DataResource dataResource;

    /**
     * Specified table name in database
     */
    private String tableName;

    /**
     * Migration log source
     */
    private MigrationLogSource migrationLogSource;
}
