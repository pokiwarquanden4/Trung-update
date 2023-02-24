package com.example.service.ftpclient;

import com.example.config.ConfigUtils;
import com.example.dto.ResponseObject;
import com.example.dto.ResponseUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
@Service
public class IFTPReaderServiceImpl implements IFTPReaderService {
    @Autowired
    private ConfigUtils configUtils;
    private final String server = "127.0.0.1";
    private final int port = 21;
    private final String user = "admin";
    private final String pass = "quang01239748392";
    private String remoteFilePath = "saleservice/src/main/java/com/example/saleservice/constant/PNG" ;
    private FTPClient ftpClient = new FTPClient();
    private InputStream inputStream;

    @PostConstruct
    public void init() {
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setConnectTimeout(15*60*1000);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanUp() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getConnection() throws IOException {
        System.out.println(ftpClient.isConnected());
        if(!ftpClient.isConnected()){
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setConnectTimeout(15*60*1000);
        }
    }


    public InputStream Reader(String serial) throws IOException {
        getConnection();
        inputStream = ftpClient.retrieveFileStream("/" + remoteFilePath + "/" + serial + ".png");
        System.out.println(inputStream);
        System.out.println("/" + remoteFilePath + "/" + serial + ".png");
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
    }



    public ResponseEntity<ResponseObject> readFile(String serial) throws IOException {
        InputStream convertBefore = Reader(serial);
        String base64String = FTPConvert.convertToBase64(convertBefore);

        return base64String != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(0,  new ResponseUtils(base64String), "Convert PNG Successfully")
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(1, "", "Convert PNG Failed")
                );
    }
}
