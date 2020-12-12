package com.vega.springit.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class EditUser {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String alias;

    public EditUser(Long id, String email, String firstName, String lastName, String alias) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
    }
}
