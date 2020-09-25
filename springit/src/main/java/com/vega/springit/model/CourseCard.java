package com.vega.springit.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class CourseCard extends Auditable{

    @Id //make the bellow member as primary key
    @GeneratedValue   //The @GeneratedValue annotation specifies that the primary key is automatically allocated by ObjectDB.
    private Long id;

    @NonNull//server side protection
    @NotEmpty(message = "Please Enter subtitle")
    private String subTitle;

    @NonNull//server side protection
    private String text;

    @NonNull//server side protection
    private String videoUrl;


    @NonNull//server side protection
    private String imagePath;

    @ManyToOne
    private Course course ;

}
