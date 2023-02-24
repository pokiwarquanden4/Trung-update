package com.example.service.ftpclient;


import com.example.dto.ResponseObject;
import com.example.dto.ResponseUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

@Service
public class IFTPReaderServiceImpl implements IFTPReaderService {
    private final String server = "127.0.0.1";
    private final int port = 21;
    private final String user = "admin";
    private final String pass = "quang01239748392";
    private String remoteFilePath = "src/main/java/com/example/saleservice/constant/PNG" ;
    private FTPClient ftpClient;
    private InputStream inputStream;
    private String status = "";

    @PostConstruct
    public void init() {
        ftpClient = new FTPClient();

        getConnection();
    }

    @PreDestroy
    public void cleanUp() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        }catch (IOException ex) {

        }
    }

    public boolean getConnection() {
        try {
            if(!ftpClient.isConnected()){
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
            }
            return true;
        }catch (IOException e) {
            return false;
        }
    }

    @Scheduled(fixedDelay = 900000l)
    void handleDisconnectSchedule(){
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        }catch (IOException ex) {

        }
    }


    public InputStream Reader(String serial) throws IOException {
        boolean checkConnection = getConnection();
        if (checkConnection){
            inputStream = ftpClient.retrieveFileStream("/" + remoteFilePath + "/" + serial + ".png");
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                ftpClient.completePendingCommand();
                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            }
            return inputStream;
        }else {
            status = "FTP Connection Error";
            return inputStream;
        }
    }



    public ResponseEntity<ResponseObject> readFile(String serial) throws IOException {
        InputStream convertBefore = Reader(serial);
        switch (status){
            case "FTP Connection Error":
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(3,  "", "Lỗi Connect")
                );
            default:
                break;
        }
        String base64String = FTPConvert.convertToBase64(convertBefore);

        return base64String != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(0,  new ResponseUtils(base64String), "Thành Công")
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(4, "", "Không tìm thấy file")
                );
    }
}
