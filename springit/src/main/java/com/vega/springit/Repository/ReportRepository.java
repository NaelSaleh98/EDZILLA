package com.vega.springit.Repository;

import com.vega.springit.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report , Long> {
    public Optional<Report> findByUserIdAndCourseId(long userId, long courseId);

}
