package org.example.services;

import org.example.model.Card;
import org.example.repository.CardRepository;
import org.example.services.Interface.ImageServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления изображениями карточек.
 * Предоставляет методы для сохранения, загрузки и обработки изображений.
 */
@Service
public class ImageService implements ImageServiceInterface {

    @Value("C:/Users/Noobok/Pictures/test") // путь к папке для загрузки
    private String uploadPath;

    @Autowired
    private CardRepository cardRepository;

    /**
     * Сохраняет изображение карточки
     * @param file файл изображения
     * @param cardId ID карточки
     * @return путь к сохраненному изображению
     * @throws IOException если произошла ошибка при сохранении файла
     */
    @Override
    public void addImageToCard(Long cardId, MultipartFile image) throws IOException {
        if (!(image != null && !Objects.requireNonNull(image.getOriginalFilename()).isEmpty())) {
            return;
        }
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();

        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            return;
        }
        Card cardToAdd = card.get();
        String resultFilename =  cardToAdd.getNumber() + cardToAdd.getName();

        image.transferTo(new File(uploadPath + "/" + resultFilename));

        cardToAdd.setImage(resultFilename);

        cardRepository.save(cardToAdd);
    }

    /**
     * Загружает изображение карточки
     * @param cardId ID карточки
     * @return файл изображения
     * @throws IOException если произошла ошибка при загрузке файла
     */
    @Override
    public Resource getImageOfCard(Long cardId) {

        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            return null;
        }
        String filename = card.get().getImage();

        return new FileSystemResource(uploadPath + "/" + filename);
    }

    /*public List<Image> getAllImages() {
        return cardRepository.findAll();
    }

    public Image getImageById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }*/
}