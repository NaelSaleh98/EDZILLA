package com.vega.springit.Repository;

import com.vega.springit.model.CourseCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

public interface CourseCardRepository extends JpaRepository<CourseCard,Long> {
}
