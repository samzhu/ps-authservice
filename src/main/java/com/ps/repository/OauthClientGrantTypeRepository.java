package com.ps.repository;

import com.ps.model.OauthClientGrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by samchu on 2017/3/22.
 */
@Repository
public interface OauthClientGrantTypeRepository extends JpaRepository<OauthClientGrantType, String> {
    List<OauthClientGrantType> findByClientid(String clientid);
}
