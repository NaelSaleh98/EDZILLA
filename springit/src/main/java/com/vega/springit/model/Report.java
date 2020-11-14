package com.vega.springit.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Report {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String reportReason;//can be 1 or -1

    @NonNull
    @ManyToOne
    private Course course;

    @NonNull
    @ManyToOne
    private User user;
}
