package com.sid.candidature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.candidature.dao.CandidatureRepository;
import com.sid.candidature.dao.ForeignStdRepository;
import com.sid.candidature.dao.StudentRepository;
import com.sid.candidature.entities.Candidature;
import com.sid.candidature.entities.ForeignStudent;
import com.sid.candidature.entities.Student;
import com.sid.candidature.service.FilesStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@RestController
@CrossOrigin("*")
public class FilesController {

    private FilesStorageService storageService;
    private StudentRepository studentRepository;
    private CandidatureRepository candidatureRepository;
    private ForeignStdRepository foreignStdRepository;


    @Autowired
    public FilesController(FilesStorageService storageService, StudentRepository studentRepository, CandidatureRepository candidatureRepository, ForeignStdRepository foreignStdRepository) {
        this.storageService = storageService;
        this.studentRepository = studentRepository;
        this.candidatureRepository = candidatureRepository;
        this.foreignStdRepository = foreignStdRepository;
    }


    @PostMapping(value = "saveUserProfileServer")
    public Student uploadStudent(@RequestParam("file") MultipartFile file, @RequestParam("user") String user) {
        try {
            Student std = new ObjectMapper().readValue(user, Student.class);
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = std.getCne().concat("photo.").concat(extension);
            String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", fileName).build().toString();
            std.setFileName(url);
            storageService.save(file, fileName);
            Student save = studentRepository.save(std);
            return save;
        } catch (Exception e) {

            return null;
        }
    }
    @PostMapping(value = "saveForeignStudentServer")
    public ForeignStudent uploadForeignStudent(@RequestParam("file") MultipartFile file, @RequestParam("user") String user) {
        try {
            ForeignStudent std = new ObjectMapper().readValue(user, ForeignStudent.class);
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = std.getCodePassport().concat("photo.").concat(extension);
            String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", fileName).build().toString();
            std.setFileName(url);
            storageService.save(file, fileName);
            ForeignStudent save = foreignStdRepository.save(std);
            return save;
        } catch (Exception e) {

            return null;
        }
    }


    @GetMapping(value = "/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @PostMapping(value = "saveCandidatureServer/{id}")
    public Candidature SaveCandidatureServer(@PathVariable("id") Long id,
                                             @RequestParam("cv") MultipartFile cv,
                                             @RequestParam("cin") MultipartFile cin,
                                             @RequestParam("lettre") MultipartFile lettre
                                             ) {

        try {
            Student student = studentRepository.findById(id).get();
            Candidature candida = new Candidature();

            String extensionCv = FilenameUtils.getExtension(cv.getOriginalFilename());
            String fileNameCV = student.getCne().concat("cv.").concat(extensionCv);
            String urlCv = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", fileNameCV).build().toString();
            candida.setCvName(urlCv);


            String extensionCin = FilenameUtils.getExtension(cin.getOriginalFilename());
            String fileNameCin = student.getCne().concat("cin.").concat(extensionCin);
            String urlPhoto = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", fileNameCin).build().toString();
            candida.setPhotoName(urlPhoto);

            String extensionLetter = FilenameUtils.getExtension(lettre.getOriginalFilename());
            String fileNameLetter = student.getCne().concat("letter.").concat(extensionLetter);
            String urlLetter = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", fileNameLetter).build().toString();
            candida.setLetterName(urlLetter);

            storageService.save(cv, cin, lettre, fileNameCV, fileNameCin, fileNameLetter);
            candida.setStudent(student);
            return candidatureRepository.save(candida);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
