INSERT INTO `account` (`accountid`, `username`, `email`, `password`, `enabled`, `expired`, `locked`, `credentialsexpired`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'admin', 'admin@pousheng.com', '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', 1 , 0 , 0, 0, '2017-02-20 03:10:53', '0000000000', '2017-02-20 03:15:02', '0000000000');

INSERT INTO `role` (`roleid`, `code`, `label`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', 'ROLE_ADMIN', '系統管理員', '2017-02-17 04:58:12', '0000000000', '2017-02-17 04:58:12', '0000000000');

INSERT INTO `account_role` (`serid`, `accountid`, `roleid`, `createddate`, `createdby`, `lastmodifieddate`, `lastmodifiedby`) VALUES ('0000000000', '0000000000', '0000000000', '2017-03-01 21:50:32', '0000000000', '2017-03-01 21:50:32', '0000000000');
