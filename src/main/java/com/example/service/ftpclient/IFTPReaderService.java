package com.example.service.ftpclient;

import com.example.dto.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface IFTPReaderService {
    ResponseEntity<ResponseObject> readFile(String serial) throws IOException;

}
