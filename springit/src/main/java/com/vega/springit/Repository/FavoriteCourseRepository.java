package com.vega.springit.Repository;

import com.vega.springit.model.FavoriteCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteCourseRepository extends JpaRepository<FavoriteCourse,Long> {

    public Optional<FavoriteCourse> findByUserIdAndCourseId(long userId, long courseId);
    public List<FavoriteCourse> findByUserId(long userId);

}

