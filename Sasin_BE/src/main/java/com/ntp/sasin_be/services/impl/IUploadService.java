package com.ntp.sasin_be.services.impl;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    String uploadFile(MultipartFile file);
}
