-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` (`accountid`, `username`, `email`, `password`, `enabled`, `expired`, `locked`, `credentialsexpired`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'admin', 'admin@pousheng.com', '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', b'1', b'0', b'0', b'0', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` (`roleid`, `code`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('jdT73oQKFu', 'ROLE_ADMIN', '系統管理員', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
INSERT INTO `role` (`roleid`, `code`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('G9Re5tYP40', 'ROLE_USER', '一般用戶', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` (`serid`, `accountid`, `roleid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('1CwBFqdJ4l', '0000000000', 'jdT73oQKFu', CURRENT_TIMESTAMP, '0000000000', CURRENT_TIMESTAMP, '0000000000');
