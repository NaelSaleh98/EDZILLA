package com.vega.springit.Repository;

import com.vega.springit.model.Course;
import com.vega.springit.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report , Long> {
    public Optional<Report> findByUserIdAndCourseId(long userId, long courseId);
    public List<Report> findByCourseId(long courseId);
    @Query(nativeQuery =true,value ="SELECT DISTINCT COURSE_ID FROM Report")
    public List<Long> findReportsByCourseId();
}
