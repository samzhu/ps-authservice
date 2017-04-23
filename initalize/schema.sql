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
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
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
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
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
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`serid`),
  KEY `account_id` (`accountid`),
  KEY `role_id` (`roleid`),
  CONSTRAINT `account_role_ibfk_1` FOREIGN KEY (`accountid`) REFERENCES `account` (`accountid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `account_role_ibfk_2` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 新增關於 role 跟 scop 對應的表格

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `resourceid` varchar(50) NOT NULL COMMENT '資源id',
  `label` varchar(255) DEFAULT NULL COMMENT '說明標籤',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`resourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `clientid` varchar(50) NOT NULL COMMENT '指定OAuth2 client ID',
  `client_secret` varchar(255) NOT NULL COMMENT '指定OAuth2 client secret',
  `web_server_redirect_uri` varchar(255) DEFAULT NULL COMMENT '服務端pre-established的跳轉URI',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '指定access token失效時長',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '指定refresh token的有效期',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '設定要添加的額外信息用JSON儲存',
  `autoapprove` varchar(255) DEFAULT NULL COMMENT '對客戶端自動授權的scope',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`clientid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_client_grant_types
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_grant_types`;
CREATE TABLE `oauth_client_grant_types` (
  `serid` varchar(10) NOT NULL COMMENT '隨機產生',
  `clientid` varchar(50) NOT NULL COMMENT '指定OAuth2 client ID',
  `granttype` varchar(50) NOT NULL COMMENT '指定獲取資源的access token的授權類型',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`serid`),
  KEY `clientid` (`clientid`),
  CONSTRAINT `oauth_client_grant_types_ibfk_1` FOREIGN KEY (`clientid`) REFERENCES `oauth_client_details` (`clientid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauth_client_resource
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_resource`;
CREATE TABLE `oauth_client_resource` (
  `serid` varchar(10) NOT NULL COMMENT '隨機產生',
  `clientid` varchar(50) NOT NULL COMMENT '指定OAuth2 client ID',
  `resourceid` varchar(50) NOT NULL COMMENT '指定客戶端相關的資源id',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`serid`),
  KEY `clientid` (`clientid`),
  KEY `resourceid` (`resourceid`),
  CONSTRAINT `oauth_client_resource_ibfk_1` FOREIGN KEY (`clientid`) REFERENCES `oauth_client_details` (`clientid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `oauth_client_resource_ibfk_2` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for scop
-- ----------------------------
DROP TABLE IF EXISTS `scop`;
CREATE TABLE `scop` (
  `scopid` varchar(10) NOT NULL COMMENT '亂數產生',
  `resourceid` varchar(20) NOT NULL COMMENT '對應的資源ID',
  `scopcode` varchar(50) NOT NULL COMMENT '功能代碼',
  `label` varchar(50) NOT NULL COMMENT '說明標籤文字',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`scopid`),
  UNIQUE KEY `scopcode` (`scopcode`),
  KEY `resourceid` (`resourceid`),
  CONSTRAINT `scop_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_scop
-- ----------------------------
DROP TABLE IF EXISTS `role_scop`;
CREATE TABLE `role_scop` (
  `serid` varchar(10) NOT NULL COMMENT '亂數產生',
  `roleid` varchar(10) NOT NULL COMMENT '角色ID',
  `scopid` varchar(10) NOT NULL COMMENT 'ScopID',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) NOT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`serid`),
  KEY `roleid` (`roleid`),
  KEY `scopid` (`scopid`),
  CONSTRAINT `role_scop_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `role_scop_ibfk_2` FOREIGN KEY (`scopid`) REFERENCES `scop` (`scopid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for oauthtoken
-- ----------------------------
DROP TABLE IF EXISTS `oauthtoken`;
CREATE TABLE `oauthtoken` (
  `serid` varchar(32) NOT NULL DEFAULT '' COMMENT '亂數產生',
  `tokenid` varchar(32) NOT NULL COMMENT 'TokenMD5',
  `refreshid` varchar(32) DEFAULT NULL COMMENT 'TokenMD5',
  `clientid` varchar(50) NOT NULL COMMENT '平台帳號',
  `granttype` varchar(50) NOT NULL COMMENT '授權方式',
  `resourceids` text COMMENT '可存取資源',
  `scopes` text COMMENT '執行權限範圍',
  `username` varchar(255) DEFAULT NULL COMMENT '用戶名稱',
  `redirecturi` varchar(255) DEFAULT NULL COMMENT '重新轉址',
  `accesstoken` blob NOT NULL COMMENT '真實Token',
  `refreshtoken` blob,
  `refreshed` bit(1) NOT NULL COMMENT '真實Token',
  `locked` bit(1) NOT NULL COMMENT '是否鎖定不給Refresh',
  `authentication` blob NOT NULL COMMENT '授權資料',
  `createddate` datetime NOT NULL,
  `createdby` varchar(30) DEFAULT NULL,
  `lastmodifieddate` datetime NOT NULL,
  `lastmodifiedby` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`serid`),
  UNIQUE KEY `tokenid` (`tokenid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=1;