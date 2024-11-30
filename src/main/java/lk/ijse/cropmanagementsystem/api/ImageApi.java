package lk.ijse.cropmanagementsystem.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ImageApi {
    @GetMapping("/api/v1/*/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        File file = new File("uploads/" + filename);
        if (file.exists()) {
            try {
                byte[] imageData = Files.readAllBytes(file.toPath());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Adjust content type as needed
                        .body(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
