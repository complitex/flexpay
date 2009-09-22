drop database if exists flexpay_test_db;
create database flexpay_test_db DEFAULT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
use flexpay_test_db;
grant all privileges on flexpay_test_db.* to flexpay_user identified by 'flexpay';
flush privileges;

drop table if exists common_semaphores_tbl;
create table common_semaphores_tbl(semaphoreID varchar(255) , primary key(semaphoreID));

drop table if exists common_version_tbl;
CREATE TABLE common_version_tbl (
	last_modified_date date NOT NULL,
	date_version int NOT NULL
);

INSERT INTO common_version_tbl (last_modified_date, date_version) VALUES ('2009-09-22', 0);
