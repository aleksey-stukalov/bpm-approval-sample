-- begin APPR_DOCUMENT
create table APPR_DOCUMENT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    DESCRIPTION longvarchar,
    APPROVAL_ID varchar(36),
    --
    primary key (ID)
)^
-- end APPR_DOCUMENT
-- begin APPR_APPROVAL
create table APPR_APPROVAL (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    TYPE_ varchar(50) not null,
    DUE_DATE timestamp,
    PROCESS_STATE varchar(255),
    INITIATOR_ID varchar(36),
    --
    primary key (ID)
)^
-- end APPR_APPROVAL
-- begin APPR_APPROVAL_USER_LINK
create table APPR_APPROVAL_USER_LINK (
    APPROVAL_ID varchar(36) not null,
    USER_ID varchar(36) not null,
    primary key (APPROVAL_ID, USER_ID)
)^
-- end APPR_APPROVAL_USER_LINK
