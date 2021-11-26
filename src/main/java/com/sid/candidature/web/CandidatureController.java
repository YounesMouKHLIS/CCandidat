package com.sid.candidature.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.candidature.dao.CandidatureRepository;
import com.sid.candidature.dao.StudentRepository;
import com.sid.candidature.entities.Candidature;
import com.sid.candidature.entities.Etat;
import com.sid.candidature.entities.Student;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin("*")
public class CandidatureController {
    private StudentRepository studentRepository;
    private CandidatureRepository candidatureRepository;
    private EmailServiceSender serviceSender;
    private ServletContext context;

    @Autowired
    public CandidatureController(StudentRepository studentRepository, CandidatureRepository candidatureRepository, EmailServiceSender serviceSender, ServletContext context) {
        this.studentRepository = studentRepository;
        this.candidatureRepository = candidatureRepository;
        this.serviceSender = serviceSender;
        this.context = context;
    }

    /* @GetMapping(path ="/imageStudent/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable (name = "id") Long id ) throws Exception{
        Student student = studentRepository.findById(id).get();
        String photoName = student.getPhoto();
        File file = new File(System.getProperty("user.home")+"/candidature/images/"+photoName);
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }*/

    /*
    // Save Students file in DB    @PostMapping(value = "saveUserProfile")
    public Student SaveUserProfile (@RequestParam("file")MultipartFile file, @RequestParam("user") String user) throws IOException {
        Student std = new ObjectMapper().readValue(user,Student.class);
        std.setPhoto(file.getBytes());
        std.setFileName(file.getOriginalFilename());
        Student dbStudent = studentRepository.save(std);

        return dbStudent;
    }*/

    /* // Save candidature std files in DB
    @PostMapping(value = "saveCandidature/{id}")
    public Candidature SaveCandidature(@PathVariable("id") Long id, @RequestParam("cv")MultipartFile cv, @RequestParam("photo")MultipartFile photo ,  @RequestParam("lettre")MultipartFile lettre, @RequestParam("user") String user) throws IOException {
        Candidature candida =new ObjectMapper().readValue(user,Candidature.class);
        Student std = studentRepository.findById(id).get();

        candida.setCinRectoName(photo.getOriginalFilename());
        candida.setPhotoCinRecto(photo.getBytes());

        candida.setCv(cv.getBytes());
        candida.setCvName(cv.getOriginalFilename());

        candida.setCinVersoName(lettre.getOriginalFilename());
        candida.setPhotoCinVerso(lettre.getBytes());

        candida.setStudent(std);
        return candidatureRepository.save(candida);
    }*/


    //Send email to students
    @PostMapping(value = "sendEmail")
    public ResponseEntity<Response>SendEmail(@RequestParam("email") String email,
                                             @RequestParam("body") String body,
                                             @RequestParam("subject") String subject){
        serviceSender.sendSimpleEmail(email,body,subject);
        return new ResponseEntity<Response>(new Response("Email sent successfully"), HttpStatus.OK);
    }

    // Save Students file  in server
    @PostMapping(value = "saveUserProfileServer2")
    public Student SaveUserProfileServer (@RequestParam("file")MultipartFile file,
                                          @RequestParam("user") String user) throws IOException {
        Student std = new ObjectMapper().readValue(user,Student.class);
        boolean isExist = new File(context.getRealPath("/userprofile/")).exists();
        if(!isExist){
            new File(context.getRealPath("/userprofile/")).mkdir();
        }

        //String url = MvcUriComponentsBuilder.fromMethodName(CandidatureController.class, "getFile", std.getId()).build().toString();
        //System.out.println(url);
        String filename = file.getOriginalFilename();
        String modifiedFileName = FilenameUtils.getBaseName(filename)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(filename);
        File serverfile = new File(context.getRealPath("/userprofile/"+File.separator+modifiedFileName));
        try {
            FileUtils.writeByteArrayToFile(serverfile,file.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        std.setFileName(modifiedFileName);
        Student dbStudent = studentRepository.save(std);
        return dbStudent;
    }


    // Save candidature std files in Sever
    @PostMapping(value = "saveCandidatureServer2/{id}")
    public Candidature SaveCandidatureServer(@PathVariable("id") Long id,
                                             @RequestParam("cv")MultipartFile cv,
                                             @RequestParam("photo")MultipartFile photo ,
                                             @RequestParam("lettre")MultipartFile lettre,
                                             @RequestParam("candidature") String candidature) throws IOException {
        Candidature candida =new ObjectMapper().readValue(candidature,Candidature.class);

        boolean isExist = new File(context.getRealPath("/candidatprofile/")).exists();
        if(!isExist){
            new File(context.getRealPath("/candidatprofile/")).mkdir();
        }

        String CvName = cv.getOriginalFilename();
        String modifileCvName = FilenameUtils.getBaseName(CvName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(CvName);
        File serverfileCV = new File(context.getRealPath("/candidatprofile/"+File.separator+modifileCvName));

        String LetterName = lettre.getOriginalFilename();
        String modifiedLetterName = FilenameUtils.getBaseName(LetterName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(LetterName);
        File serverFileLetter = new File(context.getRealPath("/candidatprofile/"+File.separator+modifiedLetterName));

        String photoName = photo.getOriginalFilename();
        String modifiedPhotoName = FilenameUtils.getBaseName(photoName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(photoName);
        File serverFilePhoto = new File(context.getRealPath("/candidatprofile/"+File.separator+modifiedPhotoName));

        try {
            FileUtils.writeByteArrayToFile(serverfileCV,cv.getBytes());
            FileUtils.writeByteArrayToFile(serverFileLetter,lettre.getBytes());
            FileUtils.writeByteArrayToFile(serverFilePhoto,photo.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        candida.setCvName(modifileCvName);
        candida.setPhotoName(modifiedPhotoName);
        candida.setLetterName(modifiedLetterName);

        candida.setStudent(studentRepository.findById(id).get());
        return candidatureRepository.save(candida);
    }

    //Return file students
    private String FILE_PATH_ROOT ="C:\\Users\\Y.MOUKLIS\\IdeaProjects\\candidature\\uploads\\";
    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        byte[] image = new byte[0];
        try {

            image = FileUtils.readFileToByteArray(new File(FILE_PATH_ROOT+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        //return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);
    }
    /*
    //Return file candidature :
    private String FILE_PATH_ROOTT ="C:\\Users\\Y.MOUKLIS\\IdeaProjects\\candidature\\src\\main\\webapp\\candidatprofile\\";
    @GetMapping("/fileUser/{filename}")
    public ResponseEntity<byte[]> getFileCandida(@PathVariable("filename") String filename) {
        byte[] image = new byte[0];
        try {

            image = FileUtils.readFileToByteArray(new File(FILE_PATH_ROOTT+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        //return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);
    }
*/
    @PostMapping(value = "/candidature/accepter/{id}")
    public Candidature ChangeStateCandidature(@PathVariable("id") Long id) {
        Candidature candidature = candidatureRepository.getOne(id);
        candidature.setEtat(Etat.accepter);
        return candidatureRepository.save(candidature);

    }

    @PostMapping(value = "/candidature/refuser/{id}")
    public Candidature refuserdemande(@PathVariable("id") Long id,@RequestParam("justif") String justification) {
        Candidature candidature = candidatureRepository.getOne(id);
        candidature.setEtat(Etat.rejetée);
        candidature.setJustification(justification);
        return candidatureRepository.save(candidature);
    }
}
