# 使用 JWT 跟 OAuth2 來實作授權系統(spring-security)

改了一版 SpringCloud 的版本, 配置也稍微簡化過, 如果要用可以這邊去 [Demo SpringCloud OAuth2 Jwt](https://github.com/samzhu/demo-springcloud-oauth-jwt) 

## 什麼是 JWT ?

 - [JWT 官網](https://jwt.io/)

JWT 介紹網路有很多
 - [JSON Web Token - 在Web应用间安全地传递信息](http://blog.leapoahead.com/2015/09/06/understanding-jwt/)
 - [為什麼該改用 JSON Web Token 替代傳統 Token？](https://yami.io/jwt/)


## 什麼是 OAuth2 ?

這邊有很詳細的 Oauth 說明 [OAuth 2.0 筆記 (1) 世界觀](https://blog.yorkxin.org/2013/09/30/oauth2-1-introduction) 但目前我們不會全部都用到 目前只用以下兩個 可以參考看看

- [Implicit Grant Flow](https://blog.yorkxin.org/2013/09/30/oauth2-4-2-implicit-grant-flow)
- [Resource Owner Credentials Grant Flow](https://blog.yorkxin.org/2013/09/30/oauth2-4-3-resource-owner-credentials-grant-flow)

### OAuth 中的角色定義
 - **Resource Owner** - 可以授權別人去存取 Protected Resource 。如果這個角色是人類的話，則就是指使用者 (end-user)。
 - **Resource Server** - 存放 Protected Resource 的伺服器，可以根據 Access Token 來接受 Protected Resource 的請求。
 - **Client** - 代表 Resource Owner 去存取 Protected Resource 的應用程式。 “Client” 一詞並不指任何特定的實作方式（可以在 Server 上面跑、在一般電腦上跑、或是在其他的設備）。
 - **Authorization Server** - 在認證過 Resource Own



### Implicit Grant Flow
是你常見的像 FB 那樣，當別人的問券或是網站要用的你資料，則會回到 FB 取得授權後才能繼續玩
![Implicit Grant Flow](http://i.imgur.com/dCgeBdm.png)
關於 Implicit Grant Flow 注意幾點

 - Authorization Server 直接向 Client 核發 Access Token （一步）。
 - 適合非常特定的 Public Clients ，例如跑在 Browser 裡面的應用程式。
 - Authorization Server 不必（也無法）驗證 Client 的身份。
 - 禁止核發 Refresh Token。

### Resource Owner Credentials Grant Flow
是比較會偏內部可信任的應用在取得授權，因為會經手用戶的帳號密碼
![Resource Owner Credentials Grant Flow](http://i.imgur.com/1Oiw4T7.png)
關於 Resource Owner Credentials Grant Flow 注意幾點

 - Resource Owner 的帳號密碼直接拿來當做 Grant。
 - 適用於 Resource Owner 高度信賴的 Client （像是 OS 內建的）或是官方應用程式。
 - 其他流程不適用時才能用。
 - 可以核發 Refresh Token。
 - 沒有 User-Agent Redirection。

## 實做一個用戶管理

### 資料庫表格

 - 請參考 initalize\schema.sql
 - 初始化數據 initalize\import.sql

會建立一個用戶 admin 密碼為 123456

新增加的資料表可以控制 scop 跟 client
![schema](http://i.imgur.com/4agX5Dk.png)

### OAuth 流程
其實 Spring Security 有個預設的流程 [org.springframework.security.oauth2.provider.token.DefaultTokenService](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/DefaultTokenServices.java) 可以去看看
我們現在要客製化自己的流程，讓 AuthService 可以依照用戶實際關聯的權限給予 scop
實作請參考 com.ps.security.CustomTokenServices

### 實作 TokenStore
Spring Security 預設的 [org.springframework.security.oauth2.provider.token.store.JdbcTokenStore](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/store/JdbcTokenStore.java) 管理方式是 Single sign-on 也就是會踢掉前一次登入的 Token ，但是這並不符合我們要的
當你是登入的時候，會依照上面 [DefaultTokenServices](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/DefaultTokenServices.java) 的流程跑這幾個方法

```
getAccessToken >> storeAccessToken >> storeRefreshToken
```

當你是 Refresh Token 的時候會依序執行以下方法
```
eadRefreshToken >> readAuthenticationForRefreshToken >> removeAccessTokenUsingRefreshToken >> storeAccessToken
```

所以我們實作以上幾個動作就可以了 請參考 ps-authservice\src\main\java\com\ps\security\CustomTokenStore.java

### 實作 UserDetailsService

介面 [UserDetailsService.java](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/core/userdetails/UserDetailsService.java)

這是介面提供 security 來讀取用戶資料 請參考 ps-authservice\src\main\java\com\ps\security\CustomUserDetailsService.java


### 繼承 AbstractUserDetailsAuthenticationProvider.java
繼承 [org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/dao/AbstractUserDetailsAuthenticationProvider.java)
這支是在驗證用戶帳密，我們使用 org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder 來做密碼的儲存 相關範例請參考 [Spring BCryptPasswordEncoder](http://samchu.logdown.com/posts/285566-spring-bcryptpasswordencoder)
BCryptPasswordEncoder 是 spring security 3 推薦的 

安全性更多閱讀 [在我的印象中，hash+salt已经足够好了。为什么我还要使用BCrypt？](https://segmentfault.com/a/1190000000401275)

實際程式碼部分在這 ps-authservice\src\main\java\com\ps\security\CustomUserDetailsAuthenticationProvider.java

### 如果要客製化 AccessTokenConverter

最後 AccessTokenConverter 不一定需要實作 這個是把原本亂數產生 Token 的方式轉成 JWT 格式

而我們這支 ps-authservice\src\main\java\com\ps\security\CustomAccessTokenConverter.java 是跟原本 [org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter.java](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/DefaultAccessTokenConverter.java) 的一模一樣
只是方便我們想去加些什麼在 JWT 內

如果 JWT 內的 exp 時間直接解開來看起來很怪是沒有問題的喔，因為在轉換過程中有處理過，你用其他套件他也會換算回來的
``` java
if (token.getExpiration() != null) {
    response.put(EXP, token.getExpiration().getTime() / 1000);
}
```

### 在 WebSecurityConfiguration 註冊元件

**請參考 com.ps.security.WebSecurityConfiguration.java 實作**

### 配置 AuthorizationServer 並把我們服務組件組裝起來

**請參考 com.ps.security.AuthorizationServerConfiguration.java 實作**


怎麼設計 Scope 也許可以參考 [https://developers.google.com/identity/protocols/googlescopes](https://developers.google.com/identity/protocols/googlescopes)
Client 其實也可以配置到資料庫中，不過我們還沒對外開放，所以還不需要。
我們配置了兩個客戶端 clientapp 是走 password 可信任的內部服務
web 則是 implicit 外部一次性授權 網頁方式授權
忘記了就回上面看吧

啟動主程式 AuthApplication.java

## 測試

### password Auth

Request
``` 
curl --request POST \
  --url http://localhost:8080/oauth/token \
  --header 'authorization: Basic Y2xpZW50YXBwOjEyMzQ1Ng==' \
  --header 'cache-control: no-cache' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data 'username=papidakos&password=papidakos123&grant_type=password&scope=account%20role'
```

response
```
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiIl0sInVzZXJfbmFtZSI6InBhcGlkYWtvcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE0ODcyMjIxNDMsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIzMWUzYzdiNi0zY2U4LTQ1YWMtOGU1Mi1lNzU0M2JhZTljMzUiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.tUCo7NUhMCZDz_CMyr9fsVSqwFoHEvkSOfZHAeMEmn8",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiIl0sInVzZXJfbmFtZSI6InBhcGlkYWtvcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIzMWUzYzdiNi0zY2U4LTQ1YWMtOGU1Mi1lNzU0M2JhZTljMzUiLCJleHAiOjE0ODcyMjIxNDMsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIxNDVhNjFkNi0wYzczLTQ4YzUtOWE0ZS1kNzNiNzI0MTY4YmYiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.zXdUTCdiXT5pOpjRanRkrGpiIG3p_C4AsiysjIWHtS8",
  "expires_in": 499,
  "scope": "read write",
  "jti": "31e3c7b6-3ce8-45ac-8e52-e7543bae9c35"
}
```

### password refresh
Request
```
curl --request POST \
  --url http://localhost:8080/oauth/token \
  --header 'authorization: Basic Y2xpZW50YXBwOjEyMzQ1Ng==' \
  --header 'cache-control: no-cache' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --header 'postman-token: f754a47d-f7b7-7ad7-c517-02969addfcbb' \
  --data 'grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiIl0sInVzZXJfbmFtZSI6InBhcGlkYWtvcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI0ZTY5ZmJmZS00ODAzLTQ0YTYtOTBkOC1hOTcwMDY2YjhlZTEiLCJleHAiOjE0ODcyMTQxNTUsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkMTM2OTExNS04NTIwLTRlMDctYTUzNS0yNTA3NDM0OTAxZWIiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.WaHrDJa2mgZxjUDZ2WRsB7_bQluF2HkVk0ILct7KZRA'
```

response
```
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiIl0sInVzZXJfbmFtZSI6InBhcGlkYWtvcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE0ODcyMTQxNjksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJjMzNjZGViMi00NjgyLTRkZTEtOWYwYy1kMWUyMGIxNzIyMDYiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.p7n8tOpAr6EKpdV47bo-re-qway2Zz59j0nj-4Fl-48",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiIl0sInVzZXJfbmFtZSI6InBhcGlkYWtvcyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiJjMzNjZGViMi00NjgyLTRkZTEtOWYwYy1kMWUyMGIxNzIyMDYiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiZDEzNjkxMTUtODUyMC00ZTA3LWE1MzUtMjUwNzQzNDkwMWViIiwiY2xpZW50X2lkIjoiY2xpZW50YXBwIn0.HaMmBQY7BRlcvjEHt4CVn4j3G74luN_7ZaqssC1XPlY",
  "expires_in": 499,
  "scope": "read write",
  "jti": "c33cdeb2-4682-4de1-9f0c-d1e20b172206"
}
```

### implicit
使用瀏覽器開啟 [http://localhost:8080/oauth/authorize?response_type=token&client_id=web](http://localhost:8080/oauth/authorize?response_type=token&client_id=web)
![](http://i.imgur.com/Oc533UM.png)

有點醜沒關係，這是可以客製的

再看一下原始碼這頁面是有擋 跨站請求偽造（Cross-site request forgery）
``` html
<html><head><title>Login Page</title></head><body onload='document.f.username.focus();'>
<h3>Login with Username and Password</h3><form name='f' action='/login' method='POST'>
<table>
    <tr><td>User:</td><td><input type='text' name='username' value=''></td></tr>
    <tr><td>Password:</td><td><input type='password' name='password'/></td></tr>
    <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
    <input name="_csrf" type="hidden" value="2c8806fa-ee70-44dc-b289-5dbc0df07ed9" />
</table>
</form></body></html>
```

輸入正確帳密之後後有個授權清單頁面

![](http://i.imgur.com/j86f7HR.png)

同意之後就會產生 Token 透過瀏覽器 轉回客戶端設定的 http://www.google.com.tw 網址如下
```
https://www.google.com.tw/?gws_rd=ssl#access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY29tbW9uIiwiZnJpZW5kIiwidXNlciJdLCJ1c2VyX25hbWUiOiJwYXBpZGFrb3MiLCJzY29wZSI6WyJjb21tb24iLCJ1c2VyLnJlYWRvbmx5IiwiZnJpZW5kIl0sImV4cCI6MTQ4NzIyNzI1MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjcxNjU3ZmNlLTdmNTktNDMwYi1hMjUzLTc5MmNiYzZjZmMyYSIsImNsaWVudF9pZCI6IndlYiJ9.xKktY90aizvAFaR7W1eJzn4NIQLuIaaG88lfTQzSNlQ&token_type=bearer&expires_in=3599&scope=common%20user.readonly%20friend&jti=71657fce-7f59-430b-a253-792cbc6cfc2a
```

AuthServer 這邊就已經可以用了
想簡單用可以走 implicit 想控制權高一點又可以 refresh 就用 password 

Resource Server 則不一定需要套 Spring Security 你也可以簡單使用 Filter 、 LocalThread 、 JWT 套件 就可以達成
那些 x-xss-protection 再自己加上也蠻快的

## 參考資料
 - [OAuth 2 Developers Guide](http://projects.spring.io/spring-security-oauth/docs/oauth2.html#oauth-2.0-client)
 - [Spring Security 4官方文档中文翻译与源码解读](http://www.tianshouzhi.com/api/tutorials/spring_security_4)
 - [spring boot 整合 spring security4](http://www.jianshu.com/p/95103b29c0c2)
 - [Spring Oauth2 入门](http://xiayule.net/notes/spring-oauth2-introduce)
 - [OAuth2RestTemplate is not thread-safe](https://github.com/spring-projects/spring-security-oauth/issues/554)
 
 ## Stargazers
 [![Stargazers repo roster for @samzhu/ps-authservice](https://reporoster.com/stars/samzhu/ps-authservice)](https://github.com/samzhu/ps-authservice/stargazers)
 
 ## Forkers
 [![Forkers repo roster for @samzhu/ps-authservice](https://reporoster.com/forks/samzhu/ps-authservice)](https://github.com/samzhu/ps-authservice/network/members)

## 程式碼
[https://github.com/samzhu/ps-authservice](https://github.com/samzhu/ps-authservice)
