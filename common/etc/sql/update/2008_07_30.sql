create table common_dual_tbl (
	id bigint not null auto_increment,
	primary key (id)
);

update common_version_tbl set last_modified_date='2008-07-30', date_version=0;

INSERT INTO common_dual_tbl (id) VALUES (1);
