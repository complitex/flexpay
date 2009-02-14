update `bti_building_attribute_types_tbl` set group_id = 11 where unique_code = 'ATTR_TOILETS_WITH_CENTRAL_SEWER_TOTAL_SQUARE';

select @ru_id :=1;
select @en_id :=2;
select @attribute_group_2 := 2;

INSERT INTO `bti_building_attribute_types_tbl` (id, discriminator, group_id, unique_code, is_temporal)
VALUES (65,'simple',@attribute_group_2,'ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER', 0),
(66,'simple',@attribute_group_2,'ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER_2', 0);


INSERT INTO `bti_building_attribute_type_names_tbl` (id, name, language_id, attribute_type_id) VALUES
        (69,'Количество квартир со скрытой электропроводкой ',@ru_id,65),
        (69,'Количество квартир с открытой электропроводкой ',@ru_id,66);
