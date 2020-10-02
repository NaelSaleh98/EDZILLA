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
public class Vote extends Auditable{

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private short direction;//can be 1 or -1

    @NonNull
    @ManyToOne
    private Course course;

    @NonNull
    @ManyToOne
    private User user;
}
