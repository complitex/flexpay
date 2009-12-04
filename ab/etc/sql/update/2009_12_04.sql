alter table ab_streets_districts_tbl
    add column id bigint(20) not null auto_increment first,
    drop primary key,
    add primary key using btree(id),
    add unique (street_id, district_id);

alter table ab_streets_districts_tbl
    drop index FK93093857311847ED,
    add index FK_ab_streets_districts_tbl_street_id using btree(street_id),
    drop foreign key FK93093857311847ED,
    add constraint FK_ab_streets_districts_tbl_street_id foreign key FK_ab_streets_districts_tbl_street_id (street_id)
        references ab_streets_tbl (id)
        on delete restrict
        on update restrict;

alter table ab_streets_districts_tbl
    drop index FK930938571AE9F4D,
    add index FK_ab_streets_districts_tbl_district_id using btree(district_id),
    drop foreign key FK930938571AE9F4D,
    add constraint FK_ab_streets_districts_tbl_district_id foreign key FK_ab_streets_districts_tbl_district_id (district_id)
        references ab_districts_tbl (id)
        on delete restrict
        on update restrict;

update common_version_tbl set last_modified_date='2009-12-04', date_version=0;
