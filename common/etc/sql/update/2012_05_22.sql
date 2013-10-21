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

create table NodeInstanceLog (
    id bigint not null auto_increment,
    log_date datetime,
    nodeId varchar(255),
    nodeInstanceId varchar(255),
    nodeName varchar(255),
    processId varchar(255),
    processInstanceId bigint not null,
    type integer not null,
    primary key (id)
);

create table ProcessInstanceLog (
    id bigint not null auto_increment,
    end_date datetime,
    processId varchar(255),
    processInstanceId bigint not null,
    start_date datetime,
    primary key (id)
);

create table VariableInstanceLog (
    id bigint not null auto_increment,
    log_date datetime,
    processId varchar(255),
    processInstanceId bigint not null,
    value varchar(255),
    variableId varchar(255),
    variableInstanceId varchar(255),
    primary key (id)
);

create table EventTypes (
    InstanceId bigint not null,
    eventTypes varchar(255)
);

create table ProcessInstanceInfo (
    InstanceId bigint not null auto_increment,
    id bigint,
    lastModificationDate date,
    lastReadDate date,
    processId varchar(255),
    processInstanceByteArray longblob,
    startDate date,
    state integer not null,
    OPTLOCK integer,
    primary key (InstanceId)
);

create table SessionInfo (
    id integer not null auto_increment,
    lastModificationDate datetime,
    rulesByteArray longblob,
    startDate datetime,
    OPTLOCK integer,
    primary key (id)
);

create table WorkItemInfo (
    workItemId bigint not null auto_increment,
    creationDate datetime,
    name varchar(255),
    processInstanceId bigint not null,
    state bigint not null,
    OPTLOCK integer,
    workItemByteArray longblob,
    primary key (workItemId)
);

alter table EventTypes
    add index FKB0E5621F7665489A (InstanceId),
    add constraint FKB0E5621F7665489A
    foreign key (InstanceId)
    references ProcessInstanceInfo (InstanceId);

create table Attachment (
    id bigint not null auto_increment,
    accessType integer,
    attachedAt datetime,
    attachmentContentId bigint not null,
    contentType varchar(255),
    name varchar(255),
    attachment_size integer,
    attachedBy_id varchar(255),
    TaskData_Attachments_Id bigint,
    primary key (id)
);

create table BooleanExpression (
    id bigint not null auto_increment,
    expression longtext,
    type varchar(255),
    Escalation_Constraints_Id bigint,
    primary key (id)
);

create table Content (
    id bigint not null auto_increment,
    content longblob,
    primary key (id)
);

create table Deadline (
    id bigint not null auto_increment,
    deadline_date datetime,
    escalated smallint,
    Deadlines_StartDeadLine_Id bigint,
    Deadlines_EndDeadLine_Id bigint,
    primary key (id)
);

create table Delegation_delegates (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table Escalation (
    id bigint not null auto_increment,
    name varchar(255),
    Deadline_Escalation_Id bigint,
    primary key (id)
);

create table I18NText (
    id bigint not null auto_increment,
    language varchar(255),
    text longtext,
    Task_Subjects_Id bigint,
    Task_Names_Id bigint,
    Task_Descriptions_Id bigint,
    Reassignment_Documentation_Id bigint,
    Notification_Subjects_Id bigint,
    Notification_Names_Id bigint,
    Notification_Documentation_Id bigint,
    Notification_Descriptions_Id bigint,
    Deadline_Documentation_Id bigint,
    primary key (id)
);

create table Notification (
    DTYPE varchar(31) not null,
    id bigint not null auto_increment,
    priority integer not null,
    Escalation_Notifications_Id bigint,
    primary key (id)
);

create table Notification_BAs (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table Notification_Recipients (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table Notification_email_header (
    Notification_id bigint not null,
    emailHeaders_id bigint not null,
    mapkey varchar(255) not null,
    primary key (Notification_id, mapkey),
    unique (emailHeaders_id)
);

create table OrganizationalEntity (
    DTYPE varchar(31) not null,
    id varchar(255) not null,
    primary key (id)
);

create table PeopleAssignments_BAs (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table PeopleAssignments_ExclOwners (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table PeopleAssignments_PotOwners (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table PeopleAssignments_Recipients (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table PeopleAssignments_Stakeholders (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table Reassignment (
    id bigint not null auto_increment,
    Escalation_Reassignments_Id bigint,
    primary key (id)
);

create table Reassignment_potentialOwners (
    task_id bigint not null,
    entity_id varchar(255) not null
);

create table SubTasksStrategy (
    DTYPE varchar(100) not null,
    id bigint not null auto_increment,
    name varchar(255),
    Task_Id bigint,
    primary key (id)
);

create table Task (
    id bigint not null auto_increment,
    archived smallint,
    allowedToDelegate varchar(255),
    priority integer not null,
    activationTime datetime,
    createdOn datetime,
    documentAccessType integer,
    documentContentId bigint not null,
    documentType varchar(255),
    expirationTime datetime,
    faultAccessType integer,
    faultContentId bigint not null,
    faultName varchar(255),
    faultType varchar(255),
    outputAccessType integer,
    outputContentId bigint not null,
    outputType varchar(255),
    parentId bigint not null,
    previousStatus integer,
    processId varchar(255),
    processInstanceId bigint not null,
    processSessionId integer not null,
    skipable bit not null,
    status varchar(255),
    workItemId bigint not null,
    OPTLOCK integer,
    taskInitiator_id varchar(255),
    actualOwner_id varchar(255),
    createdBy_id varchar(255),
    primary key (id)
);

create table email_header (
    id bigint not null auto_increment,
    body longtext,
    fromAddress varchar(255),
    language varchar(255),
    replyToAddress varchar(255),
    subject varchar(255),
    primary key (id)
);

create table task_comment (
    id bigint not null auto_increment,
    addedAt datetime,
    text longtext,
    addedBy_id varchar(255),
    TaskData_Comments_Id bigint,
    primary key (id)
);

alter table Attachment
    add index FK1C935438EF5F064 (attachedBy_id),
    add constraint FK1C935438EF5F064
    foreign key (attachedBy_id)
    references OrganizationalEntity (id);

alter table Attachment
    add index FK1C93543F21826D9 (TaskData_Attachments_Id),
    add constraint FK1C93543F21826D9
    foreign key (TaskData_Attachments_Id)
    references Task (id);

alter table BooleanExpression
    add index FKE3D208C0AFB75F7D (Escalation_Constraints_Id),
    add constraint FKE3D208C0AFB75F7D
    foreign key (Escalation_Constraints_Id)
    references Escalation (id);

alter table Deadline
    add index FK21DF3E78684BACA3 (Deadlines_StartDeadLine_Id),
    add constraint FK21DF3E78684BACA3
    foreign key (Deadlines_StartDeadLine_Id)
    references Task (id);

alter table Deadline
    add index FK21DF3E7827ABEB8A (Deadlines_EndDeadLine_Id),
    add constraint FK21DF3E7827ABEB8A
    foreign key (Deadlines_EndDeadLine_Id)
    references Task (id);

alter table Delegation_delegates
    add index FK47485D572C122ED2 (entity_id),
    add constraint FK47485D572C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table Delegation_delegates
    add index FK47485D5736B2F154 (task_id),
    add constraint FK47485D5736B2F154
    foreign key (task_id)
    references Task (id);

alter table Escalation
    add index FK67B2C6B5C7A04C70 (Deadline_Escalation_Id),
    add constraint FK67B2C6B5C7A04C70
    foreign key (Deadline_Escalation_Id)
    references Deadline (id);

alter table I18NText
    add index FK2349686B2162DFB4 (Notification_Descriptions_Id),
    add constraint FK2349686B2162DFB4
    foreign key (Notification_Descriptions_Id)
    references Notification (id);

alter table I18NText
    add index FK2349686BD488CEEB (Notification_Names_Id),
    add constraint FK2349686BD488CEEB
    foreign key (Notification_Names_Id)
    references Notification (id);

alter table I18NText
    add index FK2349686B5EEBB6D9 (Reassignment_Documentation_Id),
    add constraint FK2349686B5EEBB6D9
    foreign key (Reassignment_Documentation_Id)
    references Reassignment (id);

alter table I18NText
    add index FK2349686B3330F6D9 (Deadline_Documentation_Id),
    add constraint FK2349686B3330F6D9
    foreign key (Deadline_Documentation_Id)
    references Deadline (id);

alter table I18NText
    add index FK2349686B8046A239 (Notification_Documentation_Id),
    add constraint FK2349686B8046A239
    foreign key (Notification_Documentation_Id)
    references Notification (id);

alter table I18NText
    add index FK2349686B69B21EE8 (Task_Descriptions_Id),
    add constraint FK2349686B69B21EE8
    foreign key (Task_Descriptions_Id)
    references Task (id);

alter table I18NText
    add index FK2349686BB2FA6B18 (Task_Subjects_Id),
    add constraint FK2349686BB2FA6B18
    foreign key (Task_Subjects_Id)
    references Task (id);

alter table I18NText
    add index FK2349686B98B62B (Task_Names_Id),
    add constraint FK2349686B98B62B
    foreign key (Task_Names_Id)
    references Task (id);

alter table I18NText
    add index FK2349686BF952CEE4 (Notification_Subjects_Id),
    add constraint FK2349686BF952CEE4
    foreign key (Notification_Subjects_Id)
    references Notification (id);

alter table Notification
    add index FK2D45DD0B3E0890B (Escalation_Notifications_Id),
    add constraint FK2D45DD0B3E0890B
    foreign key (Escalation_Notifications_Id)
    references Escalation (id);

alter table Notification_BAs
    add index FK2DD68EE02C122ED2 (entity_id),
    add constraint FK2DD68EE02C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table Notification_BAs
    add index FK2DD68EE09C76EABA (task_id),
    add constraint FK2DD68EE09C76EABA
    foreign key (task_id)
    references Notification (id);

alter table Notification_Recipients
    add index FK98FD214E2C122ED2 (entity_id),
    add constraint FK98FD214E2C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table Notification_Recipients
    add index FK98FD214E9C76EABA (task_id),
    add constraint FK98FD214E9C76EABA
    foreign key (task_id)
    references Notification (id);

alter table Notification_email_header
    add index FKF30FE3441F7B912A (emailHeaders_id),
    add constraint FKF30FE3441F7B912A
    foreign key (emailHeaders_id)
    references email_header (id);

alter table Notification_email_header
    add index FKF30FE34430BE501C (Notification_id),
    add constraint FKF30FE34430BE501C
    foreign key (Notification_id)
    references Notification (id);

alter table PeopleAssignments_BAs
    add index FK9D8CF4EC2C122ED2 (entity_id),
    add constraint FK9D8CF4EC2C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table PeopleAssignments_BAs
    add index FK9D8CF4EC36B2F154 (task_id),
    add constraint FK9D8CF4EC36B2F154
    foreign key (task_id)
    references Task (id);

alter table PeopleAssignments_ExclOwners
    add index FKC77B97E42C122ED2 (entity_id),
    add constraint FKC77B97E42C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table PeopleAssignments_ExclOwners
    add index FKC77B97E436B2F154 (task_id),
    add constraint FKC77B97E436B2F154
    foreign key (task_id)
    references Task (id);

alter table PeopleAssignments_PotOwners
    add index FK1EE418D2C122ED2 (entity_id),
    add constraint FK1EE418D2C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table PeopleAssignments_PotOwners
    add index FK1EE418D36B2F154 (task_id),
    add constraint FK1EE418D36B2F154
    foreign key (task_id)
    references Task (id);

alter table PeopleAssignments_Recipients
    add index FKC6F615C22C122ED2 (entity_id),
    add constraint FKC6F615C22C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table PeopleAssignments_Recipients
    add index FKC6F615C236B2F154 (task_id),
    add constraint FKC6F615C236B2F154
    foreign key (task_id)
    references Task (id);

alter table PeopleAssignments_Stakeholders
    add index FK482F79D52C122ED2 (entity_id),
    add constraint FK482F79D52C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table PeopleAssignments_Stakeholders
    add index FK482F79D536B2F154 (task_id),
    add constraint FK482F79D536B2F154
    foreign key (task_id)
    references Task (id);

alter table Reassignment
    add index FK724D0560A5C17EE0 (Escalation_Reassignments_Id),
    add constraint FK724D0560A5C17EE0
    foreign key (Escalation_Reassignments_Id)
    references Escalation (id);

alter table Reassignment_potentialOwners
    add index FK90B59CFF2C122ED2 (entity_id),
    add constraint FK90B59CFF2C122ED2
    foreign key (entity_id)
    references OrganizationalEntity (id);

alter table Reassignment_potentialOwners
    add index FK90B59CFFE17E130F (task_id),
    add constraint FK90B59CFFE17E130F
    foreign key (task_id)
    references Reassignment (id);

alter table SubTasksStrategy
    add index FKDE5DF2E136B2F154 (Task_Id),
    add constraint FKDE5DF2E136B2F154
    foreign key (Task_Id)
    references Task (id);

alter table Task
    add index FK27A9A59E619A0 (createdBy_id),
    add constraint FK27A9A59E619A0
    foreign key (createdBy_id)
    references OrganizationalEntity (id);

alter table Task
    add index FK27A9A56CE1EF3A (actualOwner_id),
    add constraint FK27A9A56CE1EF3A
    foreign key (actualOwner_id)
    references OrganizationalEntity (id);

alter table Task
    add index FK27A9A5F213F8B5 (taskInitiator_id),
    add constraint FK27A9A5F213F8B5
    foreign key (taskInitiator_id)
    references OrganizationalEntity (id);

alter table task_comment
    add index FK61F475A5B35E68F5 (TaskData_Comments_Id),
    add constraint FK61F475A5B35E68F5
    foreign key (TaskData_Comments_Id)
    references Task (id);

alter table task_comment
    add index FK61F475A52FF04688 (addedBy_id),
    add constraint FK61F475A52FF04688
    foreign key (addedBy_id)
    references OrganizationalEntity (id);

update common_version_tbl set last_modified_date='2012-05-22', date_version=0;