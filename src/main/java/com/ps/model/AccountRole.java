package com.ps.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by samchu on 2017/2/16.
 */
@Data
@Entity
@Table(name = "account_role")
public class AccountRole {
    @Id
    private String serid;
    private String accountid;
    private String roleid;
    @CreatedDate
    private Date createddate;
    @CreatedBy
    private String createdby;
    @LastModifiedDate
    private Date lastmodifieddate;
    @LastModifiedBy
    private String lastmodifiedby;
}
