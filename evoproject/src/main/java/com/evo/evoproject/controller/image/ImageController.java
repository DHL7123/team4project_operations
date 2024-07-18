package com.evo.evoproject.controller.image;

import com.evo.evoproject.mapper.product.ImageMapper;
import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;

    private ImageMapper imageMapper;

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {
        Image image = imageService.uploadImage(file);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable int id) {
        Image image = imageService.getImageById(id);
        return ResponseEntity.ok(image);
    }
}