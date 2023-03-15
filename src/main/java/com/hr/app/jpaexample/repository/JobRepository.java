package com.hr.app.jpaexample.repository;


import com.hr.app.jpaexample.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    @Query("select j from Job j where j.id = :jobId")
    Job findByJobId(String jobId);
}
