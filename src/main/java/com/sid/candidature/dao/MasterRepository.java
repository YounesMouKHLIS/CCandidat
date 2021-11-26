package com.sid.candidature.dao;

import com.sid.candidature.entities.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface MasterRepository extends JpaRepository<Master,Long> {
}
