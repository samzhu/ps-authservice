package com.ps.security;

import com.ps.model.Account;
import com.ps.model.AccountRole;
import com.ps.model.Role;
import com.ps.repository.AccountRepository;
import com.ps.repository.AccountRoleRepository;
import com.ps.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by samchu on 2017/2/15.
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(">> CustomUserDetailsService.loadUserByUsername username={}", username);
        Account account = userRepository.findByUsername(username);

        if (account == null) {
            // Not found...
            throw new BadCredentialsException("Invalid username or password");
            //throw new UsernameNotFoundException("User " + username + " not found.");
        }

        List<AccountRole> accountRoleList = accountRoleRepository.findByAccountid(account.getAccountid());
        if (accountRoleList == null || accountRoleList.size() < 1) {
            // No Roles assigned to user...
            throw new UsernameNotFoundException("User not authorized.");
        }
        // 取出角色清單
        List<String> roleidList = accountRoleList.stream().map(AccountRole::getRoleid).collect(Collectors.toList());

        List<Role> roleList = roleRepository.findByRoleidIn(roleidList);
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Role role : roleList) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
        }

        User userDetails = new User(account.getUsername(),
                account.getPassword(),
                account.isEnabled(), //是否可用
                !account.isExpired(), //是否過期
                !account.isCredentialsexpired(), //證書不過期為true
                !account.isLocked(), //帳號未鎖定為true
                grantedAuthorities);

        log.debug("<< CustomUserDetailsService.loadUserByUsername User={}", userDetails);
        return userDetails;
    }
}