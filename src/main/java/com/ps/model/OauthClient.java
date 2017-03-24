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
 * Created by samchu on 2017/3/22.
 */
@Data
@Entity
@Table(name = "oauth_client_details")
@EntityListeners(AuditingEntityListener.class) //加這行 CreatedBy 才會生效
public class OauthClient {
    @Id
    private String clientid;
    @Column(name = "client_secret")
    private String clientSecret;
    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;
    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;
    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;
    @Column(name = "additional_information")
    private String additionalInformation;

    private String autoapprove;

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
