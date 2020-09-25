package com.vega.springit.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CourseCardService {

    public void saveImage(MultipartFile imageFile , String fileName) throws Exception {
        String folder = "./springit/src/main/resources/static/";
        byte[] bytes= imageFile.getBytes();
        Path path = Paths.get(folder + fileName );
        Files.write(path, bytes);
    }
}
