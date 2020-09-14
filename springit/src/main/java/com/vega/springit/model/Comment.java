package com.vega.springit.model;

import com.vega.springit.service.BeanUtil;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@Entity
public class Comment extends Auditable{

    @Id //make the bellow member as primary key
    @GeneratedValue //The @GeneratedValue annotation specifies that the primary key is automatically allocated by ObjectDB.
    private Long id;
    private String body;

    @ManyToOne
    private Link link;

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }


    public Comment() {
    }

    public Comment(Long id, String body, Link link) {
        this.id = id;
        this.body = body;
        this.link = link;
    }

    public Comment(String body, Link link) {
        this.body = body;
        this.link = link;
    }
}
