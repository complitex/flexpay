alter table JBPM_EXCEPTIONHANDLER modify column EXCEPTIONCLASSNAME_ text;
alter table JBPM_LOG modify column MESSAGE_ text, modify column EXCEPTION_ text;
alter table JBPM_LOG modify column OLDSTRINGVALUE_ text, modify column NEWSTRINGVALUE_ text;
alter table JBPM_NODE add column ISASYNCEXCL_ bit;
alter table JBPM_PROCESSDEFINITION add column DESCRIPTION_ text;

update common_version_tbl set last_modified_date='2008-11-14', date_version=1;
