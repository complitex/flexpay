-- jBPM updates

alter table JBPM_ACTION modify column EXPRESSION_ text;
alter table JBPM_COMMENT modify column MESSAGE_ text;
alter table JBPM_DELEGATION modify column CLASSNAME_ text, modify column CONFIGURATION_ text;

create table JBPM_JOB (ID_ bigint not null auto_increment, CLASS_ char(1) not null, VERSION_ integer not null, DUEDATE_ datetime, PROCESSINSTANCE_ bigint, TOKEN_ bigint, TASKINSTANCE_ bigint, ISSUSPENDED_ bit, ISEXCLUSIVE_ bit, LOCKOWNER_ varchar(255), LOCKTIME_ datetime, EXCEPTION_ text, RETRIES_ integer, NAME_ varchar(255), REPEAT_ varchar(255), TRANSITIONNAME_ varchar(255), ACTION_ bigint, GRAPHELEMENTTYPE_ varchar(255), GRAPHELEMENT_ bigint, NODE_ bigint, primary key (ID_));

alter table JBPM_MODULEDEFINITION modify column NAME_ varchar(255);
alter table JBPM_MODULEINSTANCE add column VERSION_ integer not null;
alter table JBPM_NODE add column DESCRIPTION_ text, add column SUBPROCNAME_ varchar(255), add column SCRIPT_ bigint, add column PARENTLOCKMODE_ varchar(255);
alter table JBPM_POOLEDACTOR add column VERSION_ integer not null;
alter table JBPM_PROCESSDEFINITION add column CLASS_ char(1) not null;
alter table JBPM_PROCESSINSTANCE add column KEY_ varchar(255);
alter table JBPM_SWIMLANEINSTANCE add column VERSION_ integer not null;

alter table JBPM_TASK modify column DESCRIPTION_ text, add column CONDITION_ varchar(255), add column PRIORITY_ integer;
alter table JBPM_TASKINSTANCE add column VERSION_ integer not null, modify column DESCRIPTION_ text, add column PROCINST_ bigint;

insert into JBPM_JOB
    (ID_,
    CLASS_,
    VERSION_,
    DUEDATE_,
    PROCESSINSTANCE_,
    TOKEN_,
    TASKINSTANCE_,
    ISSUSPENDED_,
    ISEXCLUSIVE_,
    LOCKOWNER_,
    LOCKTIME_,
    EXCEPTION_,
    RETRIES_,
    NAME_,
    REPEAT_,
    TRANSITIONNAME_,
    ACTION_,
    GRAPHELEMENTTYPE_,
    GRAPHELEMENT_)
select
    ID_,
    'T',
    0,
    DUEDATE_,
    PROCESSINSTANCE_,
    TOKEN_,
    TASKINSTANCE_,
    ISSUSPENDED_,
    0,
    NULL,
    NULL,
    EXCEPTION_,
    0,
    NAME_,
    REPEAT_,
    TRANSITIONNAME_,
    ACTION_,
    GRAPHELEMENTTYPE_,
    GRAPHELEMENT_
from JBPM_TIMER;

alter table JBPM_TOKEN add column LOCK_ varchar(255);
alter table JBPM_TOKENVARIABLEMAP add column VERSION_ integer not null;
alter table JBPM_TRANSITION add column DESCRIPTION_ text, add column CONDITION_ varchar(255);
alter table JBPM_VARIABLEINSTANCE add column VERSION_ integer not null;

create index IDX_ACTION_EVENT on JBPM_ACTION (EVENT_);
create index IDX_ACTION_ACTNDL on JBPM_ACTION (ACTIONDELEGATION_);
create index IDX_ACTION_PROCDF on JBPM_ACTION (PROCESSDEFINITION_);

create index IDX_COMMENT_TOKEN on JBPM_COMMENT (TOKEN_);
create index IDX_COMMENT_TSK on JBPM_COMMENT (TASKINSTANCE_);

create index IDX_DELEG_PRCD on JBPM_DELEGATION (PROCESSDEFINITION_);



alter table JBPM_ID_GROUP drop foreign key FK_ID_GRP_PARENT;
alter table JBPM_ID_MEMBERSHIP drop foreign key FK_ID_MEMSHIP_GRP;
alter table JBPM_ID_MEMBERSHIP drop foreign key FK_ID_MEMSHIP_USR;


create index IDX_JOB_TSKINST on JBPM_JOB (TASKINSTANCE_);
create index IDX_JOB_PRINST on JBPM_JOB (PROCESSINSTANCE_);
create index IDX_JOB_TOKEN on JBPM_JOB (TOKEN_);

alter table JBPM_JOB add index FK_JOB_TOKEN (TOKEN_), add constraint FK_JOB_TOKEN foreign key (TOKEN_) references JBPM_TOKEN (ID_);
alter table JBPM_JOB add index FK_JOB_NODE (NODE_), add constraint FK_JOB_NODE foreign key (NODE_) references JBPM_NODE (ID_);
alter table JBPM_JOB add index FK_JOB_PRINST (PROCESSINSTANCE_), add constraint FK_JOB_PRINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE (ID_);
alter table JBPM_JOB add index FK_JOB_ACTION (ACTION_), add constraint FK_JOB_ACTION foreign key (ACTION_) references JBPM_ACTION (ID_);
alter table JBPM_JOB add index FK_JOB_TSKINST (TASKINSTANCE_), add constraint FK_JOB_TSKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE (ID_);



alter table JBPM_MESSAGE drop foreign key FK_MSG_TOKEN;
alter table JBPM_MESSAGE drop foreign key FK_CMD_NODE;
alter table JBPM_MESSAGE drop foreign key FK_CMD_ACTION;
alter table JBPM_MESSAGE drop foreign key FK_CMD_TASKINST;

create index IDX_MODDEF_PROCDF on JBPM_MODULEDEFINITION (PROCESSDEFINITION_);
create index IDX_MODINST_PRINST on JBPM_MODULEINSTANCE (PROCESSINSTANCE_);

create index IDX_PSTATE_SBPRCDEF on JBPM_NODE (SUBPROCESSDEFINITION_);
create index IDX_NODE_SUPRSTATE on JBPM_NODE (SUPERSTATE_);
create index IDX_NODE_PROCDEF on JBPM_NODE (PROCESSDEFINITION_);
create index IDX_NODE_ACTION on JBPM_NODE (ACTION_);

alter table JBPM_NODE add index FK_NODE_SCRIPT (SCRIPT_), add constraint FK_NODE_SCRIPT foreign key (SCRIPT_) references JBPM_ACTION (ID_);

create index IDX_TSKINST_SWLANE on JBPM_POOLEDACTOR (SWIMLANEINSTANCE_);
create index IDX_PROCDEF_STRTST on JBPM_PROCESSDEFINITION (STARTSTATE_);
create index IDX_PROCIN_ROOTTK on JBPM_PROCESSINSTANCE (ROOTTOKEN_);
create index IDX_PROCIN_SPROCTK on JBPM_PROCESSINSTANCE (SUPERPROCESSTOKEN_);
create index IDX_PROCIN_KEY on JBPM_PROCESSINSTANCE (KEY_);
create index IDX_PROCIN_PROCDEF on JBPM_PROCESSINSTANCE (PROCESSDEFINITION_);
create index IDX_RTACTN_PRCINST on JBPM_RUNTIMEACTION (PROCESSINSTANCE_);
create index IDX_RTACTN_ACTION on JBPM_RUNTIMEACTION (ACTION_);
create index IDX_SWIMLINST_SL on JBPM_SWIMLANEINSTANCE (SWIMLANE_);
create index IDX_TASK_TSKNODE on JBPM_TASK (TASKNODE_);
create index IDX_TASK_PROCDEF on JBPM_TASK (PROCESSDEFINITION_);
create index IDX_TASK_TASKMGTDF on JBPM_TASK (TASKMGMTDEFINITION_);

create index IDX_TASKINST_TOKN on JBPM_TASKINSTANCE (TOKEN_);
create index IDX_TASKINST_TSK on JBPM_TASKINSTANCE (TASK_, PROCINST_);
create index IDX_TSKINST_TMINST on JBPM_TASKINSTANCE (TASKMGMTINSTANCE_);
create index IDX_TSKINST_SLINST on JBPM_TASKINSTANCE (SWIMLANINSTANCE_);

alter table JBPM_TASKINSTANCE add index FK_TSKINS_PRCINS (PROCINST_), add constraint FK_TSKINS_PRCINS foreign key (PROCINST_) references JBPM_PROCESSINSTANCE (ID_);

alter table JBPM_TIMER drop foreign key FK_TIMER_TOKEN;
alter table JBPM_TIMER drop foreign key FK_TIMER_PRINST;
alter table JBPM_TIMER drop foreign key FK_TIMER_ACTION;
alter table JBPM_TIMER drop foreign key FK_TIMER_TSKINST;

create index IDX_TOKEN_PROCIN on JBPM_TOKEN (PROCESSINSTANCE_);
create index IDX_TOKEN_SUBPI on JBPM_TOKEN (SUBPROCESSINSTANCE_);
create index IDX_TOKEN_NODE on JBPM_TOKEN (NODE_);
create index IDX_TOKEN_PARENT on JBPM_TOKEN (PARENT_);

create index IDX_TKVARMAP_CTXT on JBPM_TOKENVARIABLEMAP (CONTEXTINSTANCE_);
create index IDX_TKVVARMP_TOKEN on JBPM_TOKENVARIABLEMAP (TOKEN_);
create index IDX_TRANSIT_TO on JBPM_TRANSITION (TO_);
create index IDX_TRANSIT_FROM on JBPM_TRANSITION (FROM_);
create index IDX_TRANS_PROCDEF on JBPM_TRANSITION (PROCESSDEFINITION_);
create index IDX_VARINST_TKVARMP on JBPM_VARIABLEINSTANCE (TOKENVARIABLEMAP_);
create index IDX_VARINST_PRCINS on JBPM_VARIABLEINSTANCE (PROCESSINSTANCE_);
create index IDX_VARINST_TK on JBPM_VARIABLEINSTANCE (TOKEN_);

drop table JBPM_MESSAGE;

update common_version_tbl set last_modified_date='2008-11-14', date_version=0;
