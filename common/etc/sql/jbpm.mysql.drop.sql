alter table Attachment
	drop
	foreign key FK1C935438EF5F064;

alter table Attachment
	drop
	foreign key FK1C93543F21826D9;

alter table BooleanExpression
	drop
	foreign key FKE3D208C0AFB75F7D;

alter table Deadline
	drop
	foreign key FK21DF3E78684BACA3;

alter table Deadline
	drop
	foreign key FK21DF3E7827ABEB8A;

alter table Delegation_delegates
	drop
	foreign key FK47485D572C122ED2;

alter table Delegation_delegates
	drop
	foreign key FK47485D5736B2F154;

alter table Escalation
	drop
	foreign key FK67B2C6B5C7A04C70;

alter table EventTypes
	drop
	foreign key FKB0E5621F7665489A;

alter table I18NText
	drop
	foreign key FK2349686B2162DFB4;

alter table I18NText
	drop
	foreign key FK2349686BD488CEEB;

alter table I18NText
	drop
	foreign key FK2349686B5EEBB6D9;

alter table I18NText
	drop
	foreign key FK2349686B3330F6D9;

alter table I18NText
	drop
	foreign key FK2349686B8046A239;

alter table I18NText
	drop
	foreign key FK2349686B69B21EE8;

alter table I18NText
	drop
	foreign key FK2349686BB2FA6B18;

alter table I18NText
	drop
	foreign key FK2349686B98B62B;

alter table I18NText
	drop
	foreign key FK2349686BF952CEE4;

alter table Notification
	drop
	foreign key FK2D45DD0B3E0890B;

alter table Notification_BAs
	drop
	foreign key FK2DD68EE02C122ED2;

alter table Notification_BAs
	drop
	foreign key FK2DD68EE09C76EABA;

alter table Notification_Recipients
	drop
	foreign key FK98FD214E2C122ED2;

alter table Notification_Recipients
	drop
	foreign key FK98FD214E9C76EABA;

alter table Notification_email_header
	drop
	foreign key FKF30FE3441F7B912A;

alter table Notification_email_header
	drop
	foreign key FKF30FE34430BE501C;

alter table PeopleAssignments_BAs
	drop
	foreign key FK9D8CF4EC2C122ED2;

alter table PeopleAssignments_BAs
	drop
	foreign key FK9D8CF4EC36B2F154;

alter table PeopleAssignments_ExclOwners
	drop
	foreign key FKC77B97E42C122ED2;

alter table PeopleAssignments_ExclOwners
	drop
	foreign key FKC77B97E436B2F154;

alter table PeopleAssignments_PotOwners
	drop
	foreign key FK1EE418D2C122ED2;

alter table PeopleAssignments_PotOwners
	drop
	foreign key FK1EE418D36B2F154;

alter table PeopleAssignments_Recipients
	drop
	foreign key FKC6F615C22C122ED2;

alter table PeopleAssignments_Recipients
	drop
	foreign key FKC6F615C236B2F154;

alter table PeopleAssignments_Stakeholders
	drop
	foreign key FK482F79D52C122ED2;

alter table PeopleAssignments_Stakeholders
	drop
	foreign key FK482F79D536B2F154;

alter table Reassignment
	drop
	foreign key FK724D0560A5C17EE0;

alter table Reassignment_potentialOwners
	drop
	foreign key FK90B59CFF2C122ED2;

alter table Reassignment_potentialOwners
	drop
	foreign key FK90B59CFFE17E130F;

alter table SubTasksStrategy
	drop
	foreign key FKDE5DF2E136B2F154;

alter table Task
	drop
	foreign key FK27A9A59E619A0;

alter table Task
	drop
	foreign key FK27A9A56CE1EF3A;

alter table Task
	drop
	foreign key FK27A9A5F213F8B5;

alter table task_comment
	drop
	foreign key FK61F475A5B35E68F5;

alter table task_comment
	drop
	foreign key FK61F475A52FF04688;

drop table if exists Attachment;

drop table if exists BooleanExpression;

drop table if exists Content;

drop table if exists Deadline;

drop table if exists Delegation_delegates;

drop table if exists Escalation;

drop table if exists EventTypes;

drop table if exists I18NText;

drop table if exists NodeInstanceLog;

drop table if exists Notification;

drop table if exists Notification_BAs;

drop table if exists Notification_Recipients;

drop table if exists Notification_email_header;

drop table if exists OrganizationalEntity;

drop table if exists PeopleAssignments_BAs;

drop table if exists PeopleAssignments_ExclOwners;

drop table if exists PeopleAssignments_PotOwners;

drop table if exists PeopleAssignments_Recipients;

drop table if exists PeopleAssignments_Stakeholders;

drop table if exists ProcessInstanceEventInfo;

drop table if exists ProcessInstanceInfo;

drop table if exists ProcessInstanceLog;

drop table if exists Reassignment;

drop table if exists Reassignment_potentialOwners;

drop table if exists SessionInfo;

drop table if exists SubTasksStrategy;

drop table if exists Task;

drop table if exists VariableInstanceLog;

drop table if exists WorkItemInfo;

drop table if exists email_header;

drop table if exists task_comment;