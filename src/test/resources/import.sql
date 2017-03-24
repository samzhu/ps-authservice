INSERT INTO `account` (`accountid`, `username`, `email`, `password`, `enabled`, `expired`, `locked`, `credentialsexpired`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'admin', 'admin@pousheng.com', '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', 1 , 0 , 0, 0, CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `role` (`roleid`, `code`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'ROLE_ADMIN', '系統管理員', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `account_role` (`serid`, `accountid`, `roleid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', '0000000000', '0000000000', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `resource` (`resourceid`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('account', '帳戶管理平台', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `oauth_client_details` (`clientid`, `client_secret`, `web_server_redirect_uri`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('clientapp', '123456', NULL, '3600', '7200', NULL, NULL, CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `oauth_client_grant_types` (`serid`, `clientid`, `granttype`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'clientapp', 'password', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
INSERT INTO `oauth_client_grant_types` (`serid`, `clientid`, `granttype`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', 'clientapp', 'refresh_token', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `oauth_client_resource` (`serid`, `clientid`, `resourceid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('000000000', 'clientapp', 'account', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'account', 'account', '帳號存取', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', 'account', 'account.readonly', '帳號唯讀', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000002', 'account', 'role', '角色存取', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000003', 'account', 'role.readonly', '角色唯讀', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

INSERT INTO `role_scop` (`serid`, `roleid`, `scopid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', '0000000000', '0000000000', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
INSERT INTO `role_scop` (`serid`, `roleid`, `scopid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', '0000000000', '0000000002', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
