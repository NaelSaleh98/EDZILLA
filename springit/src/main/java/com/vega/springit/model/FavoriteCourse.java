package com.vega.springit.model;

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

    @NonNull
    @ManyToOne
    private Course course;

    @NonNull
    @ManyToOne
    private User user;
}
