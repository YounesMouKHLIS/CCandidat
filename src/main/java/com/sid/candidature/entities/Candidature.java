package com.sid.candidature.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Candidature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;
    private String description;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Master master;

    private Etat etat;
    private String justification;
    private String photoName;
    private String letterName;
    private String cvName;
}
