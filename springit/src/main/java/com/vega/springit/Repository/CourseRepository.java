package com.vega.springit.Repository;

import com.vega.springit.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

//implementation for this repository will be provide by spring at runtime using somting called proxy,
//and under te hood it figure out how to implements queries
public interface CourseRepository extends JpaRepository<Course,Long> {
    //JpaRepository extends tow interface: PagingAndSortingRepository and QueryByExampleExecutor
    //PagingAndSortingRepository extends CrudRepository




}
