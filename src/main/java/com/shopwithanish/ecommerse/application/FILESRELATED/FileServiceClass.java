package com.shopwithanish.ecommerse.application.FILESRELATED;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileServiceClass {

    String uploadFileFn(String path, MultipartFile image) throws IOException;
}
