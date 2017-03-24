package com.ps.repository;

import com.ps.model.OauthClientResource;
import com.ps.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by samchu on 2017/3/22.
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

    List<Resource> findByResourceidIn(List<String> resourceidList);
}
