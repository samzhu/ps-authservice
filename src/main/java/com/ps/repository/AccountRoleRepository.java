package com.ps.repository;

import com.ps.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by samchu on 2017/3/17.
 */
@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, String> {

    List<AccountRole> findByAccountid(String accountid);
}
