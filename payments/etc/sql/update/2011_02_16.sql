
alter table payments_operations_tbl
  add column can_return bit default true not null comment 'Can this operation return or not';

alter table payments_documents_tbl
  add column can_return bit default true not null comment 'Can this document service return or not';

alter table payments_services_tbl
  add column can_return bit default true not null comment 'Can this service operations return or not';

update common_version_tbl set last_modified_date='2011-02-16', date_version=0;
