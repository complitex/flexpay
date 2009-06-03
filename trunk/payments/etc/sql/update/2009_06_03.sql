create table payments_cashbox_name_translations_tbl (
    id bigint not null auto_increment comment 'Primary key',
    version integer not null comment 'Optimistic lock version',
    name varchar(255) not null comment 'Name',
    language_id bigint not null comment 'Language reference',
    cashbox_id bigint not null comment 'Cashbox reference',
    primary key (id),
    unique (language_id, cashbox_id)
);

create table payments_cashboxes_tbl (
    id bigint not null auto_increment comment 'Primary key',
    version integer not null comment 'Optimistic lock version',
    status integer not null comment 'Cashbox status',
    payment_point_id bigint not null comment 'Payment point reference',
    primary key (id)
) comment='Cashboxes table';

alter table payments_cashbox_name_translations_tbl
    add index FK_payments_cashbox_name_translation_cashbox (cashbox_id),
    add constraint FK_payments_cashbox_name_translation_cashbox
    foreign key (cashbox_id)
    references payments_cashboxes_tbl (id);

alter table payments_cashbox_name_translations_tbl
    add index FK_payments_cashbox_name_translation_language (language_id),
    add constraint FK_payments_cashbox_name_translation_language
    foreign key (language_id)
    references common_languages_tbl (id);

alter table payments_cashboxes_tbl
    add index FK_payments_cashboxes_tbl_payment_point_id (payment_point_id),
    add constraint FK_payments_cashboxes_tbl_payment_point_id
    foreign key (payment_point_id)
    references orgs_payment_points_tbl (id);

update common_version_tbl set last_modified_date='2009-06-03', date_version=0;
