package com.vega.springit.Repository;

import com.vega.springit.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//implementation for this repository will be provide by spring at runtime using something called proxy,
//and under te hood it figure out how to implements queries
public interface CourseRepository extends JpaRepository<Course,Long>{
    //JpaRepository extends tow interface: PagingAndSortingRepository and QueryByExampleExecutor
    //PagingAndSortingRepository extends CrudRepository

    List<Course> findAllByOrderByVoteCountDesc();
    @Query(nativeQuery =true,value = "SELECT TOP 10 * FROM Course as c WHERE c.id NOT IN (:coursesIDS) ORDER BY VOTE_COUNT DESC")   // Spring JPA In cause using native query
    List<Course> findTop10ByOrderByVoteCountDescNot(@Param("coursesIDS") List<Long> courseListIDS);
    List<Course> findByTitleContaining(String title);
    Optional<Course> findById(Long Id);

    @Query(nativeQuery = true, value = "SELECT * FROM Course as c WHERE c.REPORT_COUNT > 2")
    List<Course> findReportedCourses();


}
