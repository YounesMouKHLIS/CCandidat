package com.sid.candidature.dao;

import com.sid.candidature.entities.ForeignStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface ForeignStdRepository extends JpaRepository<ForeignStudent,Long> {
}
