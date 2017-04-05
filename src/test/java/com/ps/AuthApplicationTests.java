package com.ps;

import com.ps.model.Account;
import com.ps.model.Role;
import com.ps.repository.AccountRepository;
import com.ps.repository.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles(profiles = "test")
public class AuthApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setup() {

    }

    @Test
    public void accountIsExist() throws Exception {
        // 確認帳號存在
        Account account = accountRepository.findOne("0000000000");
        assertNotNull(account);
    }

    @Test
    public void accountHasRole() throws Exception {
        // 確認帳號可以取得角色資訊
        List<Role> roleList = accountRepository.findRoleListByUsername("admin");
        assertThat(roleList.size()).isEqualTo(1);
    }

    @Test
    public void roleIsExist() throws Exception {
        // 確認角色存在
        Role role = roleRepository.findOne("0000000001");
        assertThat(role.getCode()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void testOauthToken() {
        // 確認取得 token
        String clientid = "accountservice";
        String clientpw = "123456";
        String username = "admin";
        String password = "123456";
        String granttype = "password";
        String scope = "account role";
        ResponseEntity<Map> response = this.getOauthToken(clientid, clientpw, username, password, granttype, scope);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testRefreshToken() {
        String clientid = "accountservice";
        String clientpw = "123456";
        String username = "admin";
        String password = "123456";
        String scope = "account role";
        ResponseEntity<Map> response = this.getOauthToken(clientid, clientpw, username, password, "password", scope);
        String refreshToken = null;
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            refreshToken = (String) response.getBody().get("refresh_token");
        }
        response = this.refreshNewToken(clientid, clientpw, "refresh_token", refreshToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // 使用 TestRestTemplate
    private ResponseEntity<Map> getOauthToken(String clientid, String clientpw, String username, String password, String granttype, String scope) {
        TestRestTemplate basicAuthTemplate = restTemplate.withBasicAuth(clientid, clientpw);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", granttype);
        map.add("scope", scope);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<Map> response = basicAuthTemplate.exchange("/oauth/token", HttpMethod.POST, entity, Map.class);
        return response;
    }

    // 更新 token
    private ResponseEntity<Map> refreshNewToken(String clientid, String clientpw, String granttype, String refreshtoken) {
        TestRestTemplate basicAuthTemplate = restTemplate.withBasicAuth(clientid, clientpw);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", granttype);
        map.add("refresh_token", refreshtoken);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<Map> response = basicAuthTemplate.exchange("/oauth/token", HttpMethod.POST, entity, Map.class);
        return response;
    }

    // 自己配置比較多的細節
    private ResponseEntity<Map> getOauthTokenCust(String clientid, String clientpw, String username, String password, String granttype, String scope) {
        Base64.Encoder encoder = Base64.getEncoder();
        String normalString = clientid + ":" + clientpw;
        String basicAuthEncoded = encoder.encodeToString(normalString.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", String.format("%s %s", "Basic", basicAuthEncoded));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", granttype);
        map.add("scope", scope);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<Map> response = restTemplate.exchange("/oauth/token", HttpMethod.POST, entity, Map.class);
        return response;
    }


//    @Test
//    public void testItemGroupReport() throws Exception {
//        ResponseEntity<byte[]> response = this.restTemplate.getForEntity(
//                "/api/v1/ItemGroupReport?id={id}&itemGroupName={itemGroupName}&shopIdPmt={shopIdPmt}&brand={brand}&isSpecial={isSpecial}", byte[].class,
//                "1", "itemGroupName", "1", "brand", "1");
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        //assertThat(entity.getBody()).isEqualTo("Hello, world");
//    }
//
//    @Test
//    public void testItemReport() throws Exception {
//        ResponseEntity<byte[]> entity = this.restTemplate.getForEntity(
//                "/api/v1/ItemReport?id=1&upcCode=1&itemName=1&type=1&itemGroupName=1", byte[].class);
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//	@Test
//	public void contextLoads() {
//
//	}

}
