package org.example.controllers.card;

import org.example.model.dto.CardFilter;
import org.example.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.example.controllers.card.CardController.buildQueryParams;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Value("C:\\Users\\Noobok\\Pictures\\test") // путь к папке для загрузки
    private String uploadPath;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/upload/{cardId}")
    public String showUploadForm(Model model, @PathVariable("cardId") int cardId,
                                 @RequestParam(required = false) String query,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "12") int size,
                                 @RequestParam(defaultValue = "id,asc") String sort,
                                 @RequestParam(required = false) String rank,
                                 @RequestParam(required = false) Integer minNumber,
                                 @RequestParam(required = false) Integer maxNumber) {
        model.addAttribute("cardId", cardId);

        CardController.addPaginationAttributes(model, page, size, sort, query, rank, minNumber, maxNumber);
        return "cards/uploadImage";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("cardId") Long cardId,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam(required = false) String query,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "12") int size,
                                   @RequestParam(defaultValue = "id,asc") String sort,
                                   @RequestParam(required = false) String rank,
                                   @RequestParam(required = false) Integer minNumber,
                                   @RequestParam(required = false) Integer maxNumber) {
        try {
            imageService.addImageToCard(cardId, file);
            redirectAttributes.addFlashAttribute("message",
                    "Файл " + file.getOriginalFilename() + " успешно загружен!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message",
                    "Ошибка при загрузке файла " + file.getOriginalFilename());
        }
        return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = new FileSystemResource(uploadPath + "/" + filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    static void setModelAttribute(Model model, @ModelAttribute String rank, @ModelAttribute Integer minNumber, @ModelAttribute Integer maxNumber, @RequestParam(required = false) String query, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "12") int size, @RequestParam(defaultValue = "id,asc") String sort) {
        CardController.addPaginationAttributes(model, page, size, sort, query, rank, minNumber, maxNumber);
    }
}
