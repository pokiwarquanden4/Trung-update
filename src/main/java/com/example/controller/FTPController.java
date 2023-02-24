package com.example.controller;

import com.example.dto.ResponseObject;
import com.example.service.ftpclient.IFTPReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("")
public class FTPController {
    @Autowired
    private IFTPReaderService ftpReaderService;
    @GetMapping("/bitelws/v1/ftpFile/{fileName}")
    public ResponseEntity<ResponseObject> readFile(@PathVariable String fileName){
        try {
            return ftpReaderService.readFile(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(2,  "", "Lỗi hệ thống (exception)")
            );
        }
    }
}
