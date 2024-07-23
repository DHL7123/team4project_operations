package com.evo.evoproject.service.board;

import com.evo.evoproject.domain.board.BoardImage;
import com.evo.evoproject.mapper.board.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class LocalImageUploadService {

    private final String boardUploadDir;

    @Autowired
    public LocalImageUploadService(@Value("${board.image.upload.dir}") String boardUploadDir) {
        this.boardUploadDir = boardUploadDir;
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File uploadDirectory = new File(boardUploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        String fileName = generateFileName(multipartFile);
        File file = new File(uploadDirectory, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        // 웹 브라우저에서 접근 가능한 URL 반환
        return "/boardimage/" + fileName;
    }

    private String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID().toString() + "-" + multiPart.getOriginalFilename();
    }
}
