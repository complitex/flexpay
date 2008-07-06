ALTER TABLE countries_tbl RENAME TO ab_countries_tbl;
ALTER TABLE country_name_translations_tbl RENAME TO ab_country_name_translations_tbl;

ALTER TABLE regions_tbl RENAME TO ab_regions_tbl;
ALTER TABLE region_names_temporal_tbl RENAME TO ab_region_names_temporal_tbl;
ALTER TABLE region_names_tbl RENAME TO ab_region_names_tbl;
ALTER TABLE region_name_translations_tbl RENAME TO ab_region_name_translations_tbl;

ALTER TABLE towns_tbl RENAME TO ab_towns_tbl;
ALTER TABLE town_names_temporal_tbl RENAME TO ab_town_names_temporal_tbl;
ALTER TABLE town_names_tbl RENAME TO ab_town_names_tbl;
ALTER TABLE town_name_translations_tbl RENAME TO ab_town_name_translations_tbl;
ALTER TABLE town_types_tbl RENAME TO ab_town_types_tbl;
ALTER TABLE town_type_translations_tbl RENAME TO ab_town_type_translations_tbl;
ALTER TABLE town_types_temporal_tbl RENAME TO ab_town_types_temporal_tbl;

ALTER TABLE districts_tbl RENAME TO ab_districts_tbl;
ALTER TABLE district_names_temporal_tbl RENAME TO ab_district_names_temporal_tbl;
ALTER TABLE district_names_tbl RENAME TO ab_district_names_tbl;
ALTER TABLE district_name_translations_tbl RENAME TO ab_district_name_translations_tbl;

ALTER TABLE streets_tbl RENAME TO ab_streets_tbl;
ALTER TABLE street_types_temporal_tbl RENAME TO ab_street_types_temporal_tbl;
ALTER TABLE street_names_temporal_tbl RENAME TO ab_street_names_temporal_tbl;
ALTER TABLE street_names_tbl RENAME TO ab_street_names_tbl;
ALTER TABLE street_name_translations_tbl RENAME TO ab_street_name_translations_tbl;
ALTER TABLE street_types_tbl RENAME TO ab_street_types_tbl;
ALTER TABLE street_type_translations_tbl RENAME TO ab_street_type_translations_tbl;

ALTER TABLE streets_districts_tbl RENAME TO ab_streets_districts_tbl;


update common_version_tbl set last_modified_date='2008-07-06', date_version=0;
