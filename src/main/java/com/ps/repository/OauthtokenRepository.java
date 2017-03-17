package com.ps.repository;

import com.ps.model.Oauthtoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by samchu on 2017/2/15.
 */
@Repository
public interface OauthtokenRepository extends JpaRepository<Oauthtoken, String> {

    Oauthtoken findByTokenid(String tokenid);

    Oauthtoken findByRefreshid(String refreshid);
}
