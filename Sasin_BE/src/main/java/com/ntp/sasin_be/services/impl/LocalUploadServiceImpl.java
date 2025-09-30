package com.ntp.sasin_be.services.impl;

import com.ntp.sasin_be.services.IUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalUploadServiceImpl implements IUploadService {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // Tạo thư mục nếu chưa có
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Tạo tên file unique
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            // Lưu file
            file.transferTo(filePath.toFile());

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Upload file failed: " + e.getMessage(), e);
        }
    }
}