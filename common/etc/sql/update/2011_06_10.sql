alter table JBPM_ACTION
DROP INDEX IDX_ACTION_EVENT,
DROP INDEX IDX_ACTION_ACTNDL,
DROP INDEX IDX_ACTION_PROCDF,
DROP INDEX FK_ACTION_EVENT,
DROP INDEX FK_ACTION_EXPTHDL,
DROP INDEX FK_ACTION_PROCDEF,
DROP INDEX FK_CRTETIMERACT_TA,
DROP INDEX FK_ACTION_ACTNDEL,
DROP INDEX FK_ACTION_REFACT,
drop foreign key FK_ACTION_EVENT,
drop foreign key FK_ACTION_EXPTHDL,
drop foreign key FK_ACTION_PROCDEF,
drop foreign key FK_CRTETIMERACT_TA,
drop foreign key FK_ACTION_ACTNDEL,
drop foreign key FK_ACTION_REFACT;

alter table JBPM_BYTEARRAY
DROP INDEX FK_BYTEARR_FILDEF,
drop foreign key FK_BYTEARR_FILDEF;

alter table JBPM_BYTEBLOCK
DROP INDEX FK_BYTEBLOCK_FILE,
drop foreign key FK_BYTEBLOCK_FILE;

alter table JBPM_COMMENT
DROP INDEX IDX_COMMENT_TOKEN,
DROP INDEX IDX_COMMENT_TSK,
DROP INDEX FK_COMMENT_TOKEN,
DROP INDEX FK_COMMENT_TSK,
drop foreign key FK_COMMENT_TOKEN,
drop foreign key FK_COMMENT_TSK;

alter table JBPM_DECISIONCONDITIONS
DROP INDEX FK_DECCOND_DEC,
drop foreign key FK_DECCOND_DEC;

alter table JBPM_DELEGATION
DROP INDEX IDX_DELEG_PRCD,
DROP INDEX FK_DELEGATION_PRCD,
drop foreign key FK_DELEGATION_PRCD;

alter table JBPM_EVENT
DROP INDEX FK_EVENT_PROCDEF,
DROP INDEX FK_EVENT_NODE,
DROP INDEX FK_EVENT_TRANS,
DROP INDEX FK_EVENT_TASK,
drop foreign key FK_EVENT_PROCDEF,
drop foreign key FK_EVENT_NODE,
drop foreign key FK_EVENT_TRANS,
drop foreign key FK_EVENT_TASK;

alter table JBPM_ID_GROUP
DROP INDEX FK_ID_GRP_PARENT,
drop foreign key FK_ID_GRP_PARENT;

alter table JBPM_ID_MEMBERSHIP
DROP INDEX FK_ID_MEMSHIP_GRP,
DROP INDEX FK_ID_MEMSHIP_USR,
drop foreign key FK_ID_MEMSHIP_GRP,
drop foreign key FK_ID_MEMSHIP_USR;

alter table JBPM_JOB
DROP INDEX IDX_JOB_TSKINST,
DROP INDEX IDX_JOB_PRINST,
DROP INDEX IDX_JOB_TOKEN,
DROP INDEX FK_JOB_TOKEN,
DROP INDEX FK_JOB_NODE,
DROP INDEX FK_JOB_PRINST,
DROP INDEX FK_JOB_ACTION,
DROP INDEX FK_JOB_TSKINST,
drop foreign key FK_JOB_TOKEN,
drop foreign key FK_JOB_NODE,
drop foreign key FK_JOB_PRINST,
drop foreign key FK_JOB_ACTION,
drop foreign key FK_JOB_TSKINST;

alter table JBPM_LOG
DROP INDEX FK_LOG_SOURCENODE,
DROP INDEX FK_LOG_TOKEN,
DROP INDEX FK_LOG_OLDBYTES,
DROP INDEX FK_LOG_NEWBYTES,
DROP INDEX FK_LOG_CHILDTOKEN,
DROP INDEX FK_LOG_DESTNODE,
DROP INDEX FK_LOG_TASKINST,
DROP INDEX FK_LOG_SWIMINST,
DROP INDEX FK_LOG_PARENT,
DROP INDEX FK_LOG_NODE,
DROP INDEX FK_LOG_ACTION,
DROP INDEX FK_LOG_VARINST,
DROP INDEX FK_LOG_TRANSITION,
drop foreign key FK_LOG_SOURCENODE,
drop foreign key FK_LOG_TOKEN,
drop foreign key FK_LOG_OLDBYTES,
drop foreign key FK_LOG_NEWBYTES,
drop foreign key FK_LOG_CHILDTOKEN,
drop foreign key FK_LOG_DESTNODE,
drop foreign key FK_LOG_TASKINST,
drop foreign key FK_LOG_SWIMINST,
drop foreign key FK_LOG_PARENT,
drop foreign key FK_LOG_NODE,
drop foreign key FK_LOG_ACTION,
drop foreign key FK_LOG_VARINST,
drop foreign key FK_LOG_TRANSITION;

alter table JBPM_MODULEDEFINITION
DROP INDEX IDX_MODDEF_PROCDF,
DROP INDEX FK_TSKDEF_START,
DROP INDEX FK_MODDEF_PROCDEF,
drop foreign key FK_TSKDEF_START,
drop foreign key FK_MODDEF_PROCDEF;

alter table JBPM_MODULEINSTANCE
DROP INDEX IDX_MODINST_PRINST,
DROP INDEX FK_TASKMGTINST_TMD,
DROP INDEX FK_MODINST_PRCINST,
drop foreign key FK_TASKMGTINST_TMD,
drop foreign key FK_MODINST_PRCINST;

alter table JBPM_NODE
DROP INDEX IDX_PSTATE_SBPRCDEF,
DROP INDEX IDX_NODE_SUPRSTATE,
DROP INDEX IDX_NODE_PROCDEF,
DROP INDEX IDX_NODE_ACTION,
DROP INDEX FK_PROCST_SBPRCDEF,
DROP INDEX FK_NODE_PROCDEF,
DROP INDEX FK_NODE_SCRIPT,
DROP INDEX FK_NODE_ACTION,
DROP INDEX FK_DECISION_DELEG,
DROP INDEX FK_NODE_SUPERSTATE,
drop foreign key FK_PROCST_SBPRCDEF,
drop foreign key FK_NODE_PROCDEF,
drop foreign key FK_NODE_SCRIPT,
drop foreign key FK_NODE_ACTION,
drop foreign key FK_DECISION_DELEG,
drop foreign key FK_NODE_SUPERSTATE;

alter table JBPM_POOLEDACTOR
DROP INDEX IDX_PLDACTR_ACTID,
DROP INDEX IDX_TSKINST_SWLANE,
DROP INDEX FK_POOLEDACTOR_SLI,
drop foreign key FK_POOLEDACTOR_SLI;

alter table JBPM_PROCESSDEFINITION
DROP INDEX IDX_PROCDEF_STRTST,
DROP INDEX FK_PROCDEF_STRTSTA,
drop foreign key FK_PROCDEF_STRTSTA;

alter table JBPM_PROCESSINSTANCE
DROP INDEX IDX_PROCIN_ROOTTK,
DROP INDEX IDX_PROCIN_SPROCTK,
DROP INDEX IDX_PROCIN_KEY,
DROP INDEX IDX_PROCIN_PROCDEF,
DROP INDEX FK_PROCIN_PROCDEF,
DROP INDEX FK_PROCIN_ROOTTKN,
DROP INDEX FK_PROCIN_SPROCTKN,
drop foreign key FK_PROCIN_PROCDEF,
drop foreign key FK_PROCIN_ROOTTKN,
drop foreign key FK_PROCIN_SPROCTKN;

alter table JBPM_RUNTIMEACTION
DROP INDEX IDX_RTACTN_PRCINST,
DROP INDEX IDX_RTACTN_ACTION,
DROP INDEX FK_RTACTN_PROCINST,
DROP INDEX FK_RTACTN_ACTION,
drop foreign key FK_RTACTN_PROCINST,
drop foreign key FK_RTACTN_ACTION;

alter table JBPM_SWIMLANE
DROP INDEX FK_SWL_ASSDEL,
DROP INDEX FK_SWL_TSKMGMTDEF,
drop foreign key FK_SWL_ASSDEL,
drop foreign key FK_SWL_TSKMGMTDEF;

alter table JBPM_SWIMLANEINSTANCE
DROP INDEX IDX_SWIMLINST_SL,
DROP INDEX FK_SWIMLANEINST_TM,
DROP INDEX FK_SWIMLANEINST_SL,
drop foreign key FK_SWIMLANEINST_TM,
drop foreign key FK_SWIMLANEINST_SL;

alter table JBPM_TASK
DROP INDEX IDX_TASK_TSKNODE,
DROP INDEX IDX_TASK_PROCDEF,
DROP INDEX IDX_TASK_TASKMGTDF,
DROP INDEX FK_TSK_TSKCTRL,
DROP INDEX FK_TASK_ASSDEL,
DROP INDEX FK_TASK_TASKNODE,
DROP INDEX FK_TASK_PROCDEF,
DROP INDEX FK_TASK_STARTST,
DROP INDEX FK_TASK_TASKMGTDEF,
DROP INDEX FK_TASK_SWIMLANE,
drop foreign key FK_TSK_TSKCTRL,
drop foreign key FK_TASK_ASSDEL,
drop foreign key FK_TASK_TASKNODE,
drop foreign key FK_TASK_PROCDEF,
drop foreign key FK_TASK_STARTST,
drop foreign key FK_TASK_TASKMGTDEF,
drop foreign key FK_TASK_SWIMLANE;

alter table JBPM_TASKACTORPOOL
DROP INDEX FK_TSKACTPOL_PLACT,
DROP INDEX FK_TASKACTPL_TSKI,
drop foreign key FK_TSKACTPOL_PLACT,
drop foreign key FK_TASKACTPL_TSKI;

alter table JBPM_TASKCONTROLLER
DROP INDEX FK_TSKCTRL_DELEG,
drop foreign key FK_TSKCTRL_DELEG;

alter table JBPM_TASKINSTANCE
DROP INDEX IDX_TASKINST_TOKN,
DROP INDEX IDX_TASKINST_TSK,
DROP INDEX IDX_TSKINST_TMINST,
DROP INDEX IDX_TSKINST_SLINST,
DROP INDEX IDX_TASK_ACTORID,
DROP INDEX FK_TSKINS_PRCINS,
DROP INDEX FK_TASKINST_TMINST,
DROP INDEX FK_TASKINST_TOKEN,
DROP INDEX FK_TASKINST_SLINST,
DROP INDEX FK_TASKINST_TASK,
drop foreign key FK_TSKINS_PRCINS,
drop foreign key FK_TASKINST_TMINST,
drop foreign key FK_TASKINST_TOKEN,
drop foreign key FK_TASKINST_SLINST,
drop foreign key FK_TASKINST_TASK;

alter table JBPM_TOKEN
DROP INDEX IDX_TOKEN_PROCIN,
DROP INDEX IDX_TOKEN_SUBPI,
DROP INDEX IDX_TOKEN_NODE,
DROP INDEX IDX_TOKEN_PARENT,
DROP INDEX FK_TOKEN_PARENT,
DROP INDEX FK_TOKEN_NODE,
DROP INDEX FK_TOKEN_PROCINST,
DROP INDEX FK_TOKEN_SUBPI,
drop foreign key FK_TOKEN_PARENT,
drop foreign key FK_TOKEN_NODE,
drop foreign key FK_TOKEN_PROCINST,
drop foreign key FK_TOKEN_SUBPI;

alter table JBPM_TOKENVARIABLEMAP
DROP INDEX IDX_TKVARMAP_CTXT,
DROP INDEX IDX_TKVVARMP_TOKEN,
DROP INDEX FK_TKVARMAP_CTXT,
DROP INDEX FK_TKVARMAP_TOKEN,
drop foreign key FK_TKVARMAP_CTXT,
drop foreign key FK_TKVARMAP_TOKEN;

alter table JBPM_TRANSITION
DROP INDEX IDX_TRANSIT_TO,
DROP INDEX IDX_TRANSIT_FROM,
DROP INDEX IDX_TRANS_PROCDEF,
DROP INDEX FK_TRANSITION_TO,
DROP INDEX FK_TRANS_PROCDEF,
DROP INDEX FK_TRANSITION_FROM,
drop foreign key FK_TRANSITION_TO,
drop foreign key FK_TRANS_PROCDEF,
drop foreign key FK_TRANSITION_FROM;

alter table JBPM_VARIABLEACCESS
DROP INDEX FK_VARACC_TSKCTRL,
DROP INDEX FK_VARACC_SCRIPT,
DROP INDEX FK_VARACC_PROCST,
drop foreign key FK_VARACC_TSKCTRL,
drop foreign key FK_VARACC_SCRIPT,
drop foreign key FK_VARACC_PROCST;

alter table JBPM_VARIABLEINSTANCE
DROP INDEX IDX_VARINST_TKVARMP,
DROP INDEX IDX_VARINST_PRCINS,
DROP INDEX IDX_VARINST_TK,
DROP INDEX FK_VARINST_TK,
DROP INDEX FK_VARINST_TKVARMP,
DROP INDEX FK_VARINST_PRCINST,
DROP INDEX FK_VAR_TSKINST,
DROP INDEX FK_BYTEINST_ARRAY,
drop foreign key FK_VARINST_TK,
drop foreign key FK_VARINST_TKVARMP,
drop foreign key FK_VARINST_PRCINST,
drop foreign key FK_VAR_TSKINST,
drop foreign key FK_BYTEINST_ARRAY;

drop table if exists JBPM_ACTION;
drop table if exists JBPM_BYTEARRAY;
drop table if exists JBPM_BYTEBLOCK;
drop table if exists JBPM_COMMENT;
drop table if exists JBPM_DECISIONCONDITIONS;
drop table if exists JBPM_DELEGATION;
drop table if exists JBPM_EVENT;
drop table if exists JBPM_EXCEPTIONHANDLER;
drop table if exists JBPM_JOB;
drop table if exists JBPM_LOG;
drop table if exists JBPM_MODULEDEFINITION;
drop table if exists JBPM_MODULEINSTANCE;
drop table if exists JBPM_NODE;
drop table if exists JBPM_POOLEDACTOR;
drop table if exists JBPM_PROCESSDEFINITION;
drop table if exists JBPM_PROCESSINSTANCE;
drop table if exists JBPM_RUNTIMEACTION;
drop table if exists JBPM_SWIMLANE;
drop table if exists JBPM_SWIMLANEINSTANCE;
drop table if exists JBPM_TASK;
drop table if exists JBPM_TASKACTORPOOL;
drop table if exists JBPM_TASKCONTROLLER;
drop table if exists JBPM_TASKINSTANCE;
drop table if exists JBPM_TOKEN;
drop table if exists JBPM_TOKENVARIABLEMAP;
drop table if exists JBPM_TRANSITION;
drop table if exists JBPM_VARIABLEACCESS;
drop table if exists JBPM_VARIABLEINSTANCE;
drop table if exists JBPM_ID_GROUP;
drop table if exists JBPM_ID_MEMBERSHIP;
drop table if exists JBPM_ID_PERMISSIONS;
drop table if exists JBPM_ID_USER;

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
  escalated bit not null,
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

create table EventTypes (
  InstanceId bigint not null,
  element varchar(255)
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
  emailHeaders_KEY varchar(255),
  primary key (Notification_id, emailHeaders_KEY),
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

create table ProcessInstanceEventInfo (
  id bigint not null auto_increment,
  eventType varchar(255),
  processInstanceId bigint not null,
  OPTLOCK integer,
  primary key (id)
);

create table ProcessInstanceInfo (
  InstanceId bigint not null auto_increment,
  lastModificationDate datetime,
  lastReadDate datetime,
  processId varchar(255),
  processInstanceByteArray longblob,
  startDate datetime,
  state integer not null,
  OPTLOCK integer,
  primary key (InstanceId)
);

create table ProcessInstanceLog (
  id bigint not null auto_increment,
  end_date datetime,
  processId varchar(255),
  processInstanceId bigint not null,
  start_date datetime,
  primary key (id)
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

create table SessionInfo (
  id integer not null auto_increment,
  lastModificationDate datetime,
  rulesByteArray longblob,
  startDate datetime,
  OPTLOCK integer,
  primary key (id)
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
  processInstanceId bigint not null,
  skipable bit not null,
  status varchar(255),
  workItemId bigint not null,
  taskInitiator_id varchar(255),
  actualOwner_id varchar(255),
  createdBy_id varchar(255),
  primary key (id)
);

create table VariableInstanceLog (
  id bigint not null auto_increment,
  log_date datetime,
  processId varchar(255),
  processInstanceId bigint not null,
  value varchar(4000),
  variableId varchar(255),
  variableInstanceId varchar(255),
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

alter table EventTypes
add index FKB0E5621F7665489A (InstanceId),
add constraint FKB0E5621F7665489A
foreign key (InstanceId)
references ProcessInstanceInfo (InstanceId);

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
