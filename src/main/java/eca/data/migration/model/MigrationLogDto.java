package eca.data.migration.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import eca.data.migration.model.entity.MigrationLogSource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Migration log dto model.
 *
 * @author Roman Batygin
 */
@Data
public class MigrationLogDto {

    /**
     * Data file name
     */
    @ApiModelProperty(notes = "Source file name", required = true)
    private String sourceFileName;

    /**
     * Table name in database
     */
    @ApiModelProperty(notes = "Table name in database", required = true)
    private String tableName;

    /**
     * Migration end date
     */
    @ApiModelProperty(notes = "Migrated date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime finishDate;

    /**
     * Migration log source
     */
    private MigrationLogSource migrationLogSource;
}
