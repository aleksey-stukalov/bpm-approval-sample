INSERT INTO PUBLIC.SEC_FILTER (ID, CREATE_TS, CREATED_BY, VERSION, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, COMPONENT, NAME, CODE, XML, USER_ID, GLOBAL_DEFAULT) VALUES ('2e844882-e959-1e82-39f3-b8eb2353d0b1', '2018-08-06 16:27:07.049000', 'admin', 2, '2018-08-06 16:28:46.504000', 'admin', null, null, '[appr$Approval.browse].filter', 'Requires process action', 'APPROVAL_REQUIRE_PROCESS_ACTION', '<?xml version="1.0" encoding="UTF-8"?>

<filter>
  <and>
    <c name="requiresProcessAction" caption="msg://requiresProcessAction" unary="true" hidden="true" width="1" type="CUSTOM" entityAlias="e"><![CDATA[pt.procActor.user.id = :session$userId and pt.endDate is null]]>
      <param name="component$filter.requiresProcessAction41119" javaClass="java.lang.Boolean">true</param>
      <join><![CDATA[join bpm$ProcInstance pi on pi.entity.entityId = {E}.id join pi.procTasks pt]]></join>
    </c>
  </and>
</filter>
', null, false);

insert into SYS_SCHEDULED_TASK
(ID, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, DEFINED_BY, BEAN_NAME, METHOD_NAME, CLASS_NAME, SCRIPT_NAME, USER_NAME, IS_SINGLETON, IS_ACTIVE, PERIOD, TIMEOUT, START_DATE, CRON, SCHEDULING_TYPE, TIME_FRAME, START_DELAY, PERMITTED_SERVERS, LOG_START, LOG_FINISH, LAST_START_TIME, LAST_START_SERVER, METHOD_PARAMS, DESCRIPTION)
values ('d63ce0da-87dc-8a9e-7bdb-ee18a291e8aa', '2018-08-06 17:11:01', 'admin', '2018-08-06 17:11:03', 'admin', null, null, 'B', 'cuba_Emailer', 'processQueuedEmails', null, null, null, true, true, 5, null, null, null, 'P', null, null, null, null, null, '2018-08-06 17:11:03', 'localhost:8080/app-core', '<?xml version="1.0" encoding="UTF-8"?>

<params/>
', null);