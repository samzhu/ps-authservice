package com.ps.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Created by samchu on 2017/2/15.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("clientapp")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("account", "account.readonly", "role", "role.readonly")
                .resourceIds("account")
                .secret("123456").accessTokenValiditySeconds(3600).refreshTokenValiditySeconds(3600)
                .and()
                .withClient("clientkpi")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("account", "account.readonly", "role", "role.readonly")
                .resourceIds("account", "kpi")
                .secret("123456").accessTokenValiditySeconds(3600).refreshTokenValiditySeconds(3600)
                .and()
                .withClient("web")
                .redirectUris("http://www.google.com.tw")
                .secret("123456")
                .authorizedGrantTypes("implicit")
                .scopes("account", "account.readonly", "role", "role.readonly")
                .resourceIds("friend", "common", "user")
                .accessTokenValiditySeconds(3600);
        //http://localhost:8081/oauth/authorize?response_type=token&client_id=web
    }
}