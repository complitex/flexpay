
ALTER TABLE `flexpay_db`.`ab_street_types_temporal_tbl`
    MODIFY COLUMN `street_id` BIGINT(20) NOT NULL COMMENT 'Street reference',
    DROP INDEX `ab_street_names_temporal_tbl_street_id`,
    DROP INDEX `FK_street_type`,
    ADD INDEX `FK_ab_street_types_temporal_tbl_street_id` USING BTREE(`street_id`),
    ADD INDEX `FK_ab_street_types_temporal_tbl_street_type_id` USING BTREE(`street_type_id`),
    DROP FOREIGN KEY `FK_street_type`,
    DROP FOREIGN KEY `ab_street_names_temporal_tbl_street_id`,
    ADD CONSTRAINT `FK_ab_street_types_temporal_tbl_street_type_id`
        FOREIGN KEY `FK_ab_street_types_temporal_tbl_street_type_id` (`street_type_id`)
        REFERENCES `ab_street_types_tbl` (`id`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    ADD CONSTRAINT `FK_ab_street_types_temporal_tbl_street_id`
        FOREIGN KEY `FK_ab_street_types_temporal_tbl_street_id` (`street_id`)
        REFERENCES `ab_streets_tbl` (`id`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    COMMENT = 'Street type temporals';

INSERT INTO common_flexpay_modules_tbl (name) VALUES ('ab');
SELECT @module_ab:=last_insert_id();
