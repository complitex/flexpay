drop database if exists FLEXPAY_DB_PLACEHOLDER;
create database FLEXPAY_DB_PLACEHOLDER DEFAULT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
use FLEXPAY_DB_PLACEHOLDER;
grant all privileges on FLEXPAY_DB_PLACEHOLDER.* to flexpay_user identified by 'flexpay';
flush privileges;

drop table if exists common_semaphores_tbl;
create table common_semaphores_tbl(semaphoreID varchar(255) , primary key(semaphoreID));

drop table if exists common_version_tbl;
CREATE TABLE common_version_tbl (
	last_modified_date date NOT NULL,
	date_version int NOT NULL
);

INSERT INTO common_version_tbl (last_modified_date, date_version) VALUES ('2011-02-16', 0);
