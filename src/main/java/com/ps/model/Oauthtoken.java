package com.ps.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samchu on 2017/2/15.
 */
@Data
@Entity
@Table(name = "oauthtoken")
@EntityListeners(AuditingEntityListener.class) //加這行 CreatedBy 才會生效
public class Oauthtoken {
    @Id
    @Column(name = "serid")
    private String serid;
    @Column(name = "tokenid")
    private String tokenid;
    @Column(name = "refreshid")
    private String refreshid;
    @Column(name = "clientid")
    private String clientid;
    @Column(name = "granttype")
    private String granttype;
    @Column(name = "resourceids")
    private String resourceids;
    @Column(name = "scopes")
    private String scopes;
    @Column(name = "username")
    private String username;
    @Column(name = "redirecturi")
    private String redirecturi;
    @Lob
    @Column(name = "accesstoken")
    private String accesstoken;
    @Lob
    @Column(name = "refreshtoken")
    private String refreshtoken;
    @Column(name = "refreshed")
    private Boolean refreshed;
    @Column(name = "locked")
    private Boolean locked;
    @Lob
    @Column(name = "authentication")
    private byte[] authentication;
    @CreatedDate
    @Column(name = "createddate")
    private Date createddate;
    @CreatedBy
    @Column(name = "createdby")
    private String createdby;
    @LastModifiedDate
    @Column(name = "lastmodifieddate")
    private Date lastmodifieddate;
    @LastModifiedBy
    @Column(name = "lastmodifiedby")
    private String lastmodifiedby;
}
