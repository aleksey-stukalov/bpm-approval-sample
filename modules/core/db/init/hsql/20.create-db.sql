-- begin APPR_DOCUMENT
alter table APPR_DOCUMENT add constraint FK_APPR_DOCUMENT_ON_APPROVAL foreign key (APPROVAL_ID) references APPR_APPROVAL(ID)^
create index IDX_APPR_DOCUMENT_ON_APPROVAL on APPR_DOCUMENT (APPROVAL_ID)^
-- end APPR_DOCUMENT
-- begin APPR_APPROVAL
alter table APPR_APPROVAL add constraint FK_APPR_APPROVAL_ON_INITIATOR foreign key (INITIATOR_ID) references SEC_USER(ID)^
create index IDX_APPR_APPROVAL_ON_INITIATOR on APPR_APPROVAL (INITIATOR_ID)^
-- end APPR_APPROVAL
-- begin APPR_APPROVAL_USER_LINK
alter table APPR_APPROVAL_USER_LINK add constraint FK_APPUSE_ON_APPROVAL foreign key (APPROVAL_ID) references APPR_APPROVAL(ID)^
alter table APPR_APPROVAL_USER_LINK add constraint FK_APPUSE_ON_USER foreign key (USER_ID) references SEC_USER(ID)^
-- end APPR_APPROVAL_USER_LINK
