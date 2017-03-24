package com.ps.service;

import com.ps.model.Role;
import com.ps.model.RoleScop;
import com.ps.model.Scop;
import com.ps.repository.RoleRepository;
import com.ps.repository.RoleScopRepository;
import com.ps.repository.ScopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by samchu on 2017/3/23.
 */
@Service
public class ScopService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleScopRepository roleScopRepository;
    @Autowired
    private ScopRepository scopRepository;

    public Set<String> generationByRole(List<String> resourceidList, List<String> rolecodeList) {
        // 先用角色代碼取出角色物件
        List<Role> roleList = roleRepository.findByCodeIn(rolecodeList);
        // 轉換成角色ID
        List<String> roleidList = roleList.stream().map(Role::getRoleid).collect(Collectors.toList());
        // 用角色ID 找出對應的 Scop 對應表
        List<RoleScop> roleScopList = roleScopRepository.findByRoleidIn(roleidList);
        // 取出 Scop ID
        List<String> scopidList = roleScopList.stream().map(RoleScop::getScopid).collect(Collectors.toList());
        // 去 Scop 表格找出可用 resourceid 跟 scopid
        List<Scop> scopList = scopRepository.findByResourceidInAndScopidIn(resourceidList, scopidList);
        // 轉成 scopcode
        Set<String> scopSet = scopList.stream().map(Scop::getScopcode).collect(Collectors.toSet());
        return scopSet;
    }
}
