package com.sid.candidature;

import com.sid.candidature.dao.StudentRepository;
import com.sid.candidature.entities.Candidature;
import com.sid.candidature.entities.Student;
import com.sid.candidature.web.EmailServiceSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CandidatureApplication implements CommandLineRunner {

    @Autowired
    private StudentRepository stdRepository;
    @Autowired
    private EmailServiceSender serviceSender;
    @Autowired
    private RepositoryRestConfiguration restConfiguration;
    public static void main(String[] args) {
        SpringApplication.run(CandidatureApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        restConfiguration.exposeIdsFor(Student.class, Candidature.class);
    }
  /*  @EventListener(ApplicationReadyEvent.class)
    public  void triggerMail(){
        serviceSender.sendSimpleEmail("younes.moukhlis@gmail.com",
                "this the emal body","subject bb");
    }*/
}
