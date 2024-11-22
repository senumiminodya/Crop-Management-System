package lk.ijse.cropmanagementsystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.FieldStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;
import lk.ijse.cropmanagementsystem.dto.impl.FieldDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.FieldNotFoundException;
import lk.ijse.cropmanagementsystem.service.FieldService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/fields")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class FieldApi {
    @Autowired
    private FieldService fieldService;
    // Directory for saving images
    @Value("${upload.dir:src/main/resources/uploads/}")
    private String uploadDir;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveField(
            @RequestParam("fieldData") String fieldData,
            @RequestParam("fieldImage1") MultipartFile fieldImage1,
            @RequestParam("fieldImage2") MultipartFile fieldImage2) {
        try {
            // Ensure upload directory exists
            createUploadDirectory();

            // Save images
            String imagePath1 = saveFile(fieldImage1);
            String imagePath2 = saveFile(fieldImage2);

            // Parse JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            FieldDTO fieldDTO = objectMapper.readValue(fieldData, FieldDTO.class);

            // Set image paths in FieldDTO
            fieldDTO.setFieldImage1(imagePath1);
            fieldDTO.setFieldImage2(imagePath2);
            fieldService.saveField(fieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private void createUploadDirectory() throws IOException {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
    private String saveFile(MultipartFile file) throws IOException {
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }

        if (file.getSize() > 2 * 1024 * 1024) { // 2 MB limit
            throw new IllegalArgumentException("File size exceeds the maximum allowed size of 2 MB.");
        }
        // Ensure the directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Save the file
        Files.copy(file.getInputStream(), filePath);

        // Return the relative path for saving in the database
        return fileName;
    }
    private void deleteImage(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
    @GetMapping(value = "/image/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<UrlResource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{fieldCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public FieldStatus getSelectedField(@PathVariable ("fieldCode") String fieldCode){
        if (!RegexProcess.fieldCodeMatcher(fieldCode)) {
            return new SelectedClassesErrorStatus(1,"Field Code is not valid");
        }
        return fieldService.getField(fieldCode);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FieldDTO> getALlFields(){
        return fieldService.getAllFields();
    }
    @DeleteMapping(value = "/{fieldCode}")
    public ResponseEntity<Void> deleteField(@PathVariable ("fieldCode") String fieldCode){
        try {
            if (!RegexProcess.fieldCodeMatcher(fieldCode)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.deleteField(fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{fieldCode}")
    public ResponseEntity<Void> updateField(
            @PathVariable ("fieldCode") String fieldCode,
            @RequestParam("fieldData") String fieldData,
            @RequestParam("fieldImage1") MultipartFile fieldImage1,
            @RequestParam("fieldImage2") MultipartFile fieldImage2) {
        //validations
        try {
            if(!RegexProcess.fieldCodeMatcher(fieldCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Save the image if provided
            String imagePath1 = null;
            String imagePath2 = null;
            if (fieldImage1 != null || fieldImage2!= null) {
                FieldDTO existingField = (FieldDTO) fieldService.getField(fieldCode);
                deleteImage(existingField.getFieldImage1());
                deleteImage(existingField.getFieldImage2());
                imagePath1 = saveFile(fieldImage1);
                imagePath2 = saveFile(fieldImage2);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            FieldDTO updatedFieldDTO = objectMapper.readValue(fieldData, FieldDTO.class);

            // Set image paths in FieldDTO
            updatedFieldDTO.setFieldImage1(imagePath1);
            updatedFieldDTO.setFieldImage2(imagePath2);

            fieldService.updateField(fieldCode,updatedFieldDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }catch (FieldNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
