package com.vega.springit.model;

import com.vega.springit.service.BeanUtil;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Getter
@Setter
@Entity
public class Comment extends Auditable{

    @Id //make the bellow member as primary key
    @GeneratedValue //The @GeneratedValue annotation specifies that the primary key is automatically allocated by ObjectDB.
    private Long id;
    private String body;


    @ManyToOne
    private Course course;

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }


    public Comment() {
    }

    public Comment(Long id, String body, Course course) {
        this.id = id;
        this.body = body;
        this.course = course;
    }

    public Comment(String body, Course course) {
        this.body = body;
        this.course = course;
    }
}
