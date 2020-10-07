package com.vega.springit.Repository;

import com.vega.springit.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//implementation for this repository will be provide by spring at runtime using something called proxy,
//and under te hood it figure out how to implements queries
public interface CourseRepository extends JpaRepository<Course,Long>{
    //JpaRepository extends tow interface: PagingAndSortingRepository and QueryByExampleExecutor
    //PagingAndSortingRepository extends CrudRepository

    List<Course> findAllByOrderByVoteCountDesc();
    List<Course> findTop10ByOrderByVoteCountDesc();
    List<Course> findByTitleContaining(String title);

}
