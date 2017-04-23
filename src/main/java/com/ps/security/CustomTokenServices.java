package com.ps.security;

import com.ps.service.ScopService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by samchu on 2017/3/22.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class CustomTokenServices extends DefaultTokenServices {
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 30; // default 30 days.

    private int accessTokenValiditySeconds = 60 * 60 * 12; // default 12 hours.

    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private TokenStore tokenStore;
    private TokenEnhancer accessTokenEnhancer;
    private boolean supportRefreshToken = false;
    private boolean reuseRefreshToken = true;

    private ScopService scopService;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenStore, "tokenStore must be set");
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        log.debug(">> CustomTokenServices.createAccessToken authentication={}", authentication);
        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null) {
            if (existingAccessToken.isExpired()) {
                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the
                    // access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            } else {
                // Re-store the access token in case the authentication has changed
                tokenStore.storeAccessToken(existingAccessToken, authentication);
                return existingAccessToken;
            }
        }

        // Only create a new refresh token if there wasn't an existing one
        // associated with an expired access token.
        // Clients might be holding existing refresh tokens, so we re-use it in
        // the case that the old access token
        // expired.
        if (refreshToken == null) {
            refreshToken = createRefreshToken(authentication);
        }
        // But the refresh token itself might need to be re-issued if it has
        // expired.
        else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication);
            }
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        // In case it was modified
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        log.debug("<< CustomTokenServices.createAccessToken accessToken={}", accessToken);
        return accessToken;
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        log.debug(">> CustomTokenServices.createAccessToken private authentication={}, refreshToken={}", authentication, refreshToken);
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);

        Boolean scopRangeByRole = false;
        // 依照資料庫來判斷
        ClientDetails client = clientDetailsService.loadClientByClientId(authentication.getOAuth2Request().getClientId());
        if (client.getAdditionalInformation() != null) {
            Map<String, Object> additionalInformation = client.getAdditionalInformation();
            if (additionalInformation.get("scopRangeBy") != null) {
                String scopRangeBy = (String) additionalInformation.get("scopRangeBy");
                if ("role".equalsIgnoreCase(scopRangeBy)) {
                    scopRangeByRole = true;
                }
            }
        }
        // 依照輸入來判斷
        // 這樣可以取得原始請求資料 ex:{grant_type=password, username=sam.chu0, scope=account role, scop_range_by=role}
//        LinkedHashMap<String, String> map = (LinkedHashMap) authentication.getUserAuthentication().getDetails();
//        if (map.get("scop_range_by") != null) {
//            String scopRangeBy = map.get("scop_range_by");
//            if ("role".equalsIgnoreCase(scopRangeBy)) {
//                scopRangeByRole = true;
//            }
//        }
        log.debug("CustomTokenServices.createAccessToken scopRangeByRole={}", scopRangeByRole);
        if (scopRangeByRole == Boolean.TRUE) {
            // 這邊依照角色資料庫實際的權限來核發
            List<String> resourceidList = authentication.getOAuth2Request().getResourceIds().stream().collect(Collectors.toList());
            log.debug("CustomTokenServices.createAccessToken resourceidList={}", resourceidList);
            List<String> rolecodeList = authentication.getUserAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            Set<String> scopSet = scopService.generationByRole(resourceidList, rolecodeList);
            log.debug("CustomTokenServices.createAccessToken scopSet={}", scopSet);
            token.setScope(scopSet);
        } else {
            // 如果沒有特殊需求則依照申請範圍
            token.setScope(authentication.getOAuth2Request().getScope());
        }
        log.debug("<< CustomTokenServices.createAccessToken private OAuth2AccessToken={}", accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token);
        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
    }

    @Transactional(noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class})
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        log.debug(">> CustomTokenServices.refreshAccessToken refreshTokenValue={}, tokenRequest={}", refreshTokenValue, tokenRequest);

        log.debug(">> CustomTokenServices.refreshAccessToken supportRefreshToken={}", supportRefreshToken);
        log.debug(">> CustomTokenServices.refreshAccessToken authenticationManager={}", authenticationManager);
        if (!supportRefreshToken) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        if (refreshToken == null) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        // 這一段用不到
//        if (this.authenticationManager != null && !authentication.isClientOnly()) {
//            // The client has already been authenticated, but the user authentication might be old now, so give it a
//            // chance to re-authenticate.
//            Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "", authentication.getAuthorities());
//            log.debug(">> CustomTokenServices.refreshAccessToken user={}", user);
//            user = authenticationManager.authenticate(user);
//            log.debug(">> CustomTokenServices.refreshAccessToken 3");
//            Object details = authentication.getDetails();
//            authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
//            authentication.setDetails(details);
//        }
//        String clientId = authentication.getOAuth2Request().getClientId();
//        if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
//            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
//        }

        // clear out any access tokens already associated with the refresh
        // token.
//        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

        if (isExpired(refreshToken)) {
            tokenStore.removeRefreshToken(refreshToken);
            throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
        }

        authentication = createRefreshedAuthentication(authentication, tokenRequest);

        if (!reuseRefreshToken) {
            tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(authentication);
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        if (!reuseRefreshToken) {
            tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
        }
        return accessToken;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }

    /**
     * Create a refreshed authentication.
     *
     * @param authentication The authentication.
     * @param request        The scope for the refreshed token.
     * @return The refreshed authentication.
     * @throws InvalidScopeException If the scope requested is invalid or wider than the original scope.
     */
    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
        OAuth2Authentication narrowed = authentication;
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
                        + ".", originalScope);
            } else {
                clientAuth = clientAuth.narrowScope(scope);
            }
        }
        narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
        return narrowed;
    }

    protected boolean isSupportRefreshToken(OAuth2Request clientAuth) {
        log.debug(">> CustomTokenServices.isSupportRefreshToken clientAuth={}", clientAuth);
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            return client.getAuthorizedGrantTypes().contains("refresh_token");
        }
        return this.supportRefreshToken;
    }

    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        log.debug(">> CustomTokenServices.getAccessTokenValiditySeconds clientAuth={}", clientAuth);
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return accessTokenValiditySeconds;
    }

    /**
     * The refresh token validity period in seconds
     *
     * @param clientAuth the current authorization request
     * @return the refresh token validity period in seconds
     */
    protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
        log.debug(">> CustomTokenServices.getRefreshTokenValiditySeconds clientAuth={}", clientAuth);
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getRefreshTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return refreshTokenValiditySeconds;
    }
}
