
alter table ab_apartments_tbl
  add column apartment_type varchar(255) not null comment 'Class hierarchy descriminator, all apartments should have the same value',
  comment='Apartments';

update ab_apartments_tbl set apartment_type='ab';

alter table ab_buildings_tbl
  add column status integer not null comment 'Enabled/Disabled status';

update ab_buildings_tbl set status=0;

alter table ab_apartments_tbl
  drop index FKBEC651DEF71F858D,
  drop foreign key FKBEC651DEF71F858D;

alter table ab_apartments_tbl
    modify column status integer not null comment 'Enabled/Disabled status',
    modify column building_id bigint not null comment 'Building reference',
    add index ab_apartments_tbl_building_id (building_id),
    add constraint ab_apartments_tbl_building_id
    foreign key (building_id) 
    references ab_buildings_tbl (id);

update common_version_tbl set last_modified_date='2009-05-20', date_version=0;
