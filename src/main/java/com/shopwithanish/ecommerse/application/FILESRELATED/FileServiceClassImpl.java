package com.shopwithanish.ecommerse.application.FILESRELATED;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceClassImpl implements FileServiceClass{

    @Override
    public String uploadFileFn(String path, MultipartFile image) throws IOException {

        String originalImageName = image.getOriginalFilename();
        // generate random name
        String randomId = UUID.randomUUID().toString();
        String randomName = randomId.concat(originalImageName);

        // create folder
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs(); // better than mkdir()
        }

        String fullPath = path + File.separator + randomName;

        Files.copy(image.getInputStream(), Paths.get(fullPath));

        return randomName; // store only file name in DB
    }
}
