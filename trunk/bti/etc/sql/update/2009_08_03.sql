alter table bti_apartment_attributes_tbl
	add column date_value datetime comment 'Optional date value',
    add column int_value integer comment 'Optional int value',
    add column bool_value bit comment 'Optional boolean value',
    add column long_value bigint comment 'Optional long value',
    add column string_value varchar(255) comment 'Optional string value',
    add column double_value double precision comment 'Optional double value',
    add column decimal_value decimal(19,5) comment 'Optional double value',
    add column value_type integer not null comment 'Value type discriminator',
    add column begin_date date comment 'Attribute value begin date',
    add column end_date date comment 'Attribute value end date',
    add column temporal_flag integer not null comment 'Temporal attribute flag';

-- migrate normal attributes (value_type=4 is a string)
update bti_apartment_attributes_tbl set
		value_type=4, string_value=normal_attribute_value,
		begin_date='1900-01-01', end_date='2100-12-31',
		temporal_flag=0
where discriminator='normal';

-- migrate tmp attributes (value_type=4 is a string)
insert into bti_apartment_attributes_tbl (apartment_id, attribute_type_id, value_type, string_value, begin_date, end_date, temporal_flag, discriminator)
	(select a.apartment_id, a.attribute_type_id, 4, v.attribute_value, v.begin_date, v.end_date, 1, 'hz'
	from bti_apartment_attributes_tbl a
			left outer join bti_apartment_attribute_temp_values_tbl v on v.attribute_id=a.id
	where a.discriminator='tmp');

delete from bti_apartment_attributes_tbl where discriminator='tmp';

alter table bti_apartment_attributes_tbl
	drop column discriminator,
	drop column normal_attribute_value;

drop table bti_apartment_attribute_temp_values_tbl;

update common_version_tbl set last_modified_date='2009-08-03', date_version=0;
