package org.example.controllers.card;

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

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Value("C:\\Users\\Noobok\\Pictures\\test") // путь к папке для загрузки
    private String uploadPath;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/upload/{cardId}")
    public String showUploadForm(Model model, @PathVariable("cardId") int cardId) {
        model.addAttribute("cardId", cardId);
        return "cards/uploadImage";
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("cardId") Long cardId,
                                   RedirectAttributes redirectAttributes) {
        try {
            imageService.addImageToCard(cardId, file);
            redirectAttributes.addFlashAttribute("message",
                    "Файл " + file.getOriginalFilename() + " успешно загружен!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message",
                    "Ошибка при загрузке файла " + file.getOriginalFilename());
        }
        return "redirect:/cards";
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = new FileSystemResource(uploadPath + "/" + filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
