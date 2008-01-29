drop database if exists flexpay_db;
create database flexpay_db DEFAULT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
create user flexpay_user identified by 'flexpay';
use flexpay_db;
grant all privileges on flexpay_db.* to flexpay_user;
flush privileges;

drop table if exists SEMAPHORE;
create table SEMAPHORE(semaphoreID varchar(255) , primary key(semaphoreID));
