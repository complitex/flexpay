ALTER TABLE person_identities_tbl MODIFY COLUMN serial_number VARCHAR(10)  NOT NULL DEFAULT '',
 MODIFY COLUMN document_number VARCHAR(20)  NOT NULL DEFAULT '';

ALTER TABLE identity_types_tbl ADD COLUMN type_enum INTEGER NOT NULL DEFAULT 0 AFTER status;
UPDATE identity_types_tbl SET type_enum=1 WHERE id=1;
UPDATE identity_types_tbl SET type_enum=2 WHERE id=2;