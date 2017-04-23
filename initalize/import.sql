-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` (`accountid`, `username`, `email`, `password`, `enabled`, `expired`, `locked`, `credentialsexpired`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'admin', 'admin@pousheng.com', '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', b'1', b'0', b'0', b'0', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` (`roleid`, `code`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', 'ROLE_ADMIN', '系統管理員', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO `role` (`roleid`, `code`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000002', 'ROLE_USER', '一般用戶', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` (`serid`, `accountid`, `roleid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', '0000000000', '0000000001', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` (`resourceid`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('account', '帳戶管理平台', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` (`clientid`, `client_secret`, `web_server_redirect_uri`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('accountservice', '123456', NULL, '86400', '604800', '{\"scopRangeBy\":\"role\"}', NULL, CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of oauth_client_grant_types
-- ----------------------------
INSERT INTO `oauth_client_grant_types` (`serid`, `clientid`, `granttype`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', 'accountservice', 'password', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO `oauth_client_grant_types` (`serid`, `clientid`, `granttype`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000002', 'accountservice', 'refresh_token', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of oauth_client_resource
-- ----------------------------
INSERT INTO `oauth_client_resource` (`serid`, `clientid`, `resourceid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', 'accountservice', 'account', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of scop
-- ----------------------------
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', 'account', 'account', '帳號存取', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000002', 'account', 'account.readonly', '帳號唯讀', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000003', 'account', 'role', '角色存取', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO `scop` (`scopid`, `resourceid`, `scopcode`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000004', 'account', 'role.readonly', '角色唯讀', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

-- ----------------------------
-- Records of role_scop
-- ----------------------------
INSERT INTO `role_scop` (`serid`, `roleid`, `scopid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000001', '0000000001', '0000000001', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO `role_scop` (`serid`, `roleid`, `scopid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000002', '0000000001', '0000000003', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin');

