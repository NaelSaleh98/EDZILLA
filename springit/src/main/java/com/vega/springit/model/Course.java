package com.vega.springit.model;


import com.vega.springit.service.BeanUtil;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Course extends Auditable{

        @Id //make the bellow member as primary key
        @GeneratedValue //The @GeneratedValue annotation specifies that the primary key is automatically allocated by ObjectDB.
        private Long id;

        @NonNull  //make the bellow member as no null(the constructor will return exception if we generate object and pass no argument to the constructor)
        @NotEmpty(message = "please enter a title.")// default error message
        private String title;

        @NonNull//server side protection
        @NotEmpty(message = "please enter a description.")
        private String description;

        //ON
        @ManyToOne
        private User user;

        //<for vote>
        @OneToMany(mappedBy = "course")
        private List<Vote> votes = new ArrayList<>();

                private int voteCount = 0;
        //</for vote>

        @OneToMany(mappedBy = "course")
        private List<Comment> comments = new ArrayList<>();

        @OneToMany(mappedBy = "course")
        private List<CourseCard> courseCards = new ArrayList<>();

        public void addComment(Comment comment){
                this.comments.add(comment);
        }

        public void addCard(CourseCard courseCard){
                this.courseCards.add(courseCard);
        }

///////////////////////// the following 3 method for pretty time

        public String getPrettyTime() {
                //here we use BeanUtil service by importing it, instead of importing it we can do (@Autowierd)
                PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
                return pt.format(convertToDateViaInstant(getCreationDate()));
        }

        private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
                return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
        }


}
