ALTER TABLE street_types_temporal_tbl MODIFY COLUMN street_type_id BIGINT(20);

ALTER TABLE street_types_temporal_tbl DROP FOREIGN KEY FK9EECCCE33E877574,
 ADD CONSTRAINT FK_street_type FOREIGN KEY FK_street_type (street_type_id)
    REFERENCES street_types_tbl (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE street_types_temporal_tbl DROP FOREIGN KEY FK9EECCCE3311847ED,
 ADD CONSTRAINT FK_street FOREIGN KEY FK_street (street_id)
    REFERENCES streets_tbl (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

create index data_index on person_identities_tbl (first_name, middle_name, last_name);

