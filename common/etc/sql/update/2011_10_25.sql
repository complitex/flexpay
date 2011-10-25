alter table Task
		add column processId varchar(255),
		add column processSessionId integer not null;
alter table VariableInstanceLog modify `value` varchar(4000);

update common_version_tbl set last_modified_date='2011-10-25', date_version=0;
