package com.sid.candidature.dao;

import com.sid.candidature.entities.Candidature;
import com.sid.candidature.entities.Master;
import com.sid.candidature.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    @RestResource(path = "/byMasterName")
    public List<Master> findByMasterContains(@Param("mc") String name);
    @RestResource(path = "/byMasterNamePage")
    public Page<Master> findByMasterContains(@Param("mc") String name, Pageable pageable);
}
