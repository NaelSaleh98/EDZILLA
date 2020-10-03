package com.vega.springit.Repository;

import com.vega.springit.model.User;
import com.vega.springit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    public Optional<Vote> findByUserIdAndCourseId(long userId, long courseId);
    public List<Vote> findAllByUserId(long userId);

}
