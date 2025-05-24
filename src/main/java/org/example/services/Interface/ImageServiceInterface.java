package org.example.services.Interface;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageServiceInterface {
    void addImageToCard(Long cardId, MultipartFile image) throws IOException;
    Resource getImageOfCard(Long cardId);
}
