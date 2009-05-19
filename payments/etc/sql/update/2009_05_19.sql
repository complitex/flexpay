create table payments_service_provider_attribute_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        name varchar(50) not null comment 'Attribute name',
        service_provider_id bigint not null comment 'Service provider reference',
        value varchar(255) comment 'Attribute value',
        primary key (id),
        unique (name, service_provider_id)
    ) comment='ServiceProviderAttribute';

alter table payments_service_provider_attribute_tbl
    add index FK_payments_service_provider_attribute_tbl_service_provider_id (service_provider_id),
    add constraint FK_payments_service_provider_attribute_tbl_service_provider_id
    foreign key (service_provider_id)
    references orgs_service_providers_tbl (id);

update common_version_tbl set last_modified_date='2009-05-19', date_version=0;
