package com.evo.evoproject.service.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.evo.evoproject.Mapper.board.BoardMapper;
import com.evo.evoproject.domain.board.BoardImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class NaverImageUploadService {

    private final AmazonS3 naverS3;
    private final String bucketName;
    private final BoardMapper boardMapper;

    @Autowired
    public NaverImageUploadService(AmazonS3 naverS3, @Value("${naver.cloud.bucket-name}") String bucketName, BoardMapper boardMapper) {
        this.naverS3 = naverS3;
        this.bucketName = bucketName;
        this.boardMapper = boardMapper;
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File file = convertMultipartFileToFile(multipartFile);
        String fileName = generateFileName(multipartFile);
        String fileUrl = "https://" + bucketName + ".kr.object.ncloudstorage.com/" + fileName;

        naverS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        file.delete();

        return fileUrl;
    }

    public void saveBoardImage(int boardNo, String imageUrl) {
        int imageId = UUID.randomUUID().hashCode();
        BoardImage boardImage = new BoardImage();
        boardImage.setBoardNo(boardNo);
        boardImage.setImageId(imageId);
        boardMapper.insertBoardImage(boardImage);
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID().toString() + "-" + multiPart.getOriginalFilename();
    }
}
