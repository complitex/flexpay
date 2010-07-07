alter table eirc_eirc_accounts_tbl
		add unique index I_eirc_eirc_accounts_account_number (account_number);

alter table eirc_consumer_attributes_tbl
		add unique index I_stringvalue_begindate_enddate_consumerid_typeid (string_value, begin_date, end_date, consumer_id, type_id);

update common_version_tbl set last_modified_date='2010-07-06', date_version=0;
