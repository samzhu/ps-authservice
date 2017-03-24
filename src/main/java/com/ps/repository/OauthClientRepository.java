package com.ps.repository;

import com.ps.model.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by samchu on 2017/3/22.
 */
@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {
}
