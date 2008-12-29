-- delete constraint for foreign key: (service_type_id) references eirc_service_types_tbl (id)
alter table eirc_ticket_service_amounts_tbl 
        drop 
        foreign key FK4C2595072C648686;

ALTER TABLE eirc_ticket_service_amounts_tbl
 DROP COLUMN service_type_id,
 ADD COLUMN consumer_id bigint not null;

alter table eirc_ticket_service_amounts_tbl 
   add index FK4C25950791349F59 (consumer_id), 
   add constraint FK4C25950791349F59 
   foreign key (consumer_id) 
   references eirc_consumers_tbl (id);

ALTER TABLE eirc_account_records_tbl MODIFY COLUMN record_type_id BIGINT(20);