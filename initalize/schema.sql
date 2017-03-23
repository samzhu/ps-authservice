SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `accountid` varchar(10) NOT NULL COMMENT '亂數ID',
  `username` varchar(30) NOT NULL COMMENT '用戶帳號',
  `email` varchar(255) DEFAULT NULL COMMENT '用戶聯絡信箱',
  `password` varchar(60) NOT NULL COMMENT '用戶密碼',
  `enabled` bit(1) NOT NULL COMMENT '是否可用',
  `expired` bit(1) NOT NULL COMMENT '是否過期',
  `locked` bit(1) NOT NULL COMMENT '帳號是否鎖定為',
  `credentialsexpired` bit(1) NOT NULL COMMENT '證書是否過期',
  `createddate` datetime NOT NULL,
  `createdby` varchar(20) DEFAULT NULL,
  `lastmodifieddate` datetime NOT NULL,
  `lastmodifiedby` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`accountid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` varchar(10) NOT NULL COMMENT '亂數產生',
  `code` varchar(20) NOT NULL COMMENT '角色代碼',
  `label` varchar(50) NOT NULL COMMENT '角色名稱',
  `createddate` datetime NOT NULL,
  `createdby` varchar(10) DEFAULT NULL,
  `lastmodifieddate` datetime NOT NULL,
  `lastmodifiedby` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `serid` varchar(10) NOT NULL COMMENT '亂數產生',
  `accountid` varchar(10) NOT NULL COMMENT '帳號ID',
  `roleid` varchar(10) NOT NULL COMMENT '角色ID',
  `createddate` datetime NOT NULL,
  `createdby` varchar(10) DEFAULT NULL,
  `lastmodifieddate` datetime NOT NULL,
  `lastmodifiedby` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`serid`),
  KEY `account_id` (`accountid`),
  KEY `role_id` (`roleid`),
  CONSTRAINT `account_role_ibfk_1` FOREIGN KEY (`accountid`) REFERENCES `account` (`accountid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `account_role_ibfk_2` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=1;