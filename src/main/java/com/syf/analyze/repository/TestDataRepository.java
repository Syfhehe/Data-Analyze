package com.syf.analyze.repository;

import com.syf.analyze.domain.TestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TestDataRepository extends JpaRepository<TestData, Long> {

}