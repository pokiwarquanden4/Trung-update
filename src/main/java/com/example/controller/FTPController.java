package com.example.controller;

import com.example.dto.ResponseObject;
import com.example.service.ftpclient.IFTPReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("")
public class FTPController {
    @Autowired
    private IFTPReaderService ftpReaderService;
    @GetMapping("/bitelws/v1/ftpFile/{fileName}/{urlKey}")
    public ResponseEntity<ResponseObject> readFile(@PathVariable String fileName) throws IOException {
        return ftpReaderService.readFile(fileName);
    }
}
