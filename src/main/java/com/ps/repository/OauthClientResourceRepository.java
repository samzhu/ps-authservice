package com.ps.repository;

import com.ps.model.OauthClientResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by samchu on 2017/3/22.
 */
@Repository
public interface OauthClientResourceRepository extends JpaRepository<OauthClientResource, String> {

    // 取出有有對應的 resourceid
    @Query("select cr.resourceid from OauthClientResource cr where cr.clientid = :clientid")
    List<String> findResourceidByClientid(@Param("clientid") String clientid);
}
