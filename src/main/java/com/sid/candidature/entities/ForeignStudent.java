package com.sid.candidature.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Data
@Entity @NoArgsConstructor @AllArgsConstructor @ToString
public class ForeignStudent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CodePassport;
    private String CodeNational;
    private String firstName;
    private String country;
    private String LastName;
    private String email;
    private int age;
    private long phoneNumber;

    private String fileName;
    @JsonIgnore
    @OneToMany(mappedBy="student")
    private List<Candidature> candidatureList;



}
