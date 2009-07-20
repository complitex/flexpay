alter table common_users_tbl
	add column ab_country_filter varchar(255) comment 'Country filter',
	add column ab_region_filter varchar(255) comment 'Region filter',
	add column ab_town_filter varchar(255) comment 'Town filter';

update common_version_tbl set last_modified_date='2009-07-17', date_version=1;
