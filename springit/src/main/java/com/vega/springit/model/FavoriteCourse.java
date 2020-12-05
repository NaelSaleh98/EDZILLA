package com.vega.springit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class FavoriteCourse extends Auditable{

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @NonNull
    @ManyToOne
    private Course course;

    @JsonIgnore
    @NonNull
    @ManyToOne
    private User user;
}
