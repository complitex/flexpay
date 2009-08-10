update common_users_tbl set ab_country_filter='1', ab_region_filter='1000', ab_town_filter='2';

alter table common_users_tbl
    modify column ab_country_filter bigint comment 'Country filter',
    modify column ab_region_filter bigint comment 'Region filter',
    modify column ab_town_filter bigint comment 'Town filter';

update common_version_tbl set last_modified_date='2009-08-07', date_version=1;
