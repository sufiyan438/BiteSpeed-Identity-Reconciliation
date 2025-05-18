package com.example.Identity_Reconciliation.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

@RestController
public class IdentifyController {
    
    // @Autowired
    // private IdentityService IdentityService;

    // @PostMapping("/identify")
    // public ResponseEntity<PostExamResponse> createExam(@Valid @RequestBody PostExamRequest postExamRequest){
    //     PostExamResponse exam = examService.createExam(postExamRequest.getExamId(), postExamRequest.getSubjectId());
    //     return ResponseEntity.ok().body(exam);
    // }

    @GetMapping("/identity")
    public ResponseEntity<?> createExam(){
        return ResponseEntity.ok().body("Hello");
    }

}