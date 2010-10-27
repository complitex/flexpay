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
