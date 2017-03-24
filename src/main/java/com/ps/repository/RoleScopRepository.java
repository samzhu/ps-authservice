package com.ps.repository;

import com.ps.model.RoleScop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by samchu on 2017/3/23.
 */
@Repository
public interface RoleScopRepository extends JpaRepository<RoleScop, String> {

    List<RoleScop> findByRoleidIn(List<String> roleidList);
}
