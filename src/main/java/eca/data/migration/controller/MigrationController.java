package eca.data.migration.controller;

import eca.data.migration.mapping.MigrationLogMapper;
import eca.data.migration.model.MigrationData;
import eca.data.migration.model.MigrationLogDto;
import eca.data.migration.model.MultipartFileResource;
import eca.data.migration.model.entity.MigrationLog;
import eca.data.migration.model.entity.MigrationLogSource;
import eca.data.migration.model.entity.MigrationStatus;
import eca.data.migration.repository.MigrationLogRepository;
import eca.data.migration.service.MigrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 * Migration controller.
 *
 * @author Roman Batygin
 */
@Api(tags = "Migration tool operations")
@Slf4j
@RestController
@RequestMapping("/migration-tool")
@RequiredArgsConstructor
public class MigrationController {

    private final MigrationLogRepository migrationLogRepository;
    private final MigrationService migrationService;
    private final MigrationLogMapper migrationLogMapper;

    /**
     * Migrates training data into database.
     *
     * @param dataFile - multipart data file
     * @return response entity
     */
    @ApiOperation(
            value = "Migrate data file into database",
            notes = "Migrate data file into database"
    )
    @PostMapping(value = "/migrate")
    public ResponseEntity migrate(
            @ApiParam(value = "Training data file", required = true) @RequestParam MultipartFile dataFile,
            @ApiParam(value = "Table name") @RequestParam(required = false) String tableName) {
        try {
            MigrationData migrationData = new MigrationData();
            migrationData.setDataResource(new MultipartFileResource(dataFile));
            migrationData.setMigrationLogSource(MigrationLogSource.MANUAL);
            migrationData.setTableName(tableName);
            migrationService.migrateData(migrationData);
        } catch (Exception ex) {
            log.error("There was an error while migration file '{}': {}", dataFile.getOriginalFilename(),
                    ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Gets successfully migrated tables info.
     *
     * @return migration logs dto list
     */
    @ApiOperation(
            value = "Gets successfully migrated tables info",
            notes = "Gets successfully migrated tables info"
    )
    @GetMapping(value = "/tables")
    public List<MigrationLogDto> getTables() {
        List<MigrationLog> migrationLogList =
                migrationLogRepository.findByMigrationStatusIn(Collections.singletonList(MigrationStatus.SUCCESS));
        return migrationLogMapper.map(migrationLogList);
    }
}
