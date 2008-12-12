
alter table JBPM_ACTION modify column EXPRESSION_ longtext;
alter table JBPM_COMMENT modify column MESSAGE_ longtext;
alter table JBPM_DELEGATION modify column CLASSNAME_ longtext, modify column CONFIGURATION_ longtext;
alter table JBPM_EXCEPTIONHANDLER modify column EXCEPTIONCLASSNAME_ longtext;
alter table JBPM_JOB modify column EXCEPTION_ longtext;
alter table JBPM_LOG modify column MESSAGE_ longtext, modify column EXCEPTION_ longtext, modify column OLDSTRINGVALUE_ longtext, modify column NEWSTRINGVALUE_ longtext;
alter table JBPM_NODE modify column DESCRIPTION_ longtext;
alter table JBPM_PROCESSDEFINITION modify column DESCRIPTION_ longtext;
alter table JBPM_TASK modify column DESCRIPTION_ longtext;
alter table JBPM_TASKINSTANCE modify column DESCRIPTION_ longtext;
alter table JBPM_TRANSITION modify column DESCRIPTION_ longtext;

update common_version_tbl set last_modified_date='2008-12-10', date_version=0;
