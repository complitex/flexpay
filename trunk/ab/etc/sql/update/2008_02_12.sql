
-- Create street type temporals table
    create table street_types_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        street_id bigint not null,
        street_type_id bigint,
        primary key (id)
    );

    alter table street_types_temporal_tbl
        add index FK9EECCCE3311847ED (street_id),
        add constraint FK9EECCCE3311847ED
        foreign key (street_id)
        references streets_tbl (id);

    alter table street_types_temporal_tbl
        add index FK9EECCCE33E877574 (street_type_id),
        add constraint FK9EECCCE33E877574
        foreign key (street_type_id)
        references street_types_tbl (id);

SELECT @ru_id:=id FROM languages_tbl WHERE lang_iso_code='ru';
SELECT @en_id:=id FROM languages_tbl WHERE lang_iso_code='en';

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Улица', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Street', @en_id, @street_type_id);

INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'ул', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Проспект', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Avenue', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'просп', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Виадук', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Viaduct', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'в-д', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Переулок', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Lane', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пер', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Набережная', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Embankment', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'наб', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Проезд', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Passage', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пр-д', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Площадь', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Square', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пл', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Шоссе', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Highway', @en_id, @street_type_id);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Бульвар', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Boulevard', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'б-р', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Спуск', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Slope', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'сп', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Поселок', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, language_id, street_type_id)
	VALUES ('Settlement', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пос', NULL);

