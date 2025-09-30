package com.ntp.sasin_be.services;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    String uploadFile(MultipartFile file);
}
