package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.CropStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;
import lk.ijse.cropmanagementsystem.exception.CropNotFoundException;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.service.CropService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("api/v1/crops")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CropApi {
    private static final Logger logger = LoggerFactory.getLogger(CropApi.class);
    @Autowired
    private CropService cropService;
    // Directory for saving images
    @Value("${upload.dir:src/main/resources/uploads/}")
    private String uploadDir;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCrop(
            @RequestParam("cropCommonName") String commonName,
            @RequestParam("cropScientificName") String scientificName,
            @RequestParam("cropImage") MultipartFile cropImage,
            @RequestParam("category") String category,
            @RequestParam("cropSeason") String season,
            @RequestParam("fieldCode") String fieldCode) {
        try {
            // Save the image and get its path
            String imagePath = saveImage(cropImage);
            // Create CropDTO
            CropDTO cropDTO = new CropDTO(commonName, scientificName, imagePath, category, season, fieldCode);
            cropService.saveCrop(cropDTO);
            logger.info("Crop saved successfully: {}", cropDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            logger.error("Unsupported media type for crop image", e);
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }catch (DataPersistException e){
            logger.error("Error persisting crop data", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error occurred while saving crop", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private String saveImage(MultipartFile file) throws IOException {
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
    @GetMapping(value = "/{cropCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CropStatus getSelectedCrop(@PathVariable ("cropCode") String cropCode){
        if (!RegexProcess.cropCodeMatcher(cropCode)) {
            return new SelectedClassesErrorStatus(1,"Crop code is not valid");
        }
        return cropService.getCrop(cropCode);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getALlCrops(){
        return cropService.getAllCrops();
    }
    @DeleteMapping(value = "/{cropCode}")
    public ResponseEntity<Void> deleteCrop(@PathVariable ("cropCode") String cropCode){
        try {
            if (!RegexProcess.cropCodeMatcher(cropCode)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            cropService.deleteCrop(cropCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (CropNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{cropCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCrop(
            @PathVariable ("cropCode") String cropCode,
            @RequestParam("cropCommonName") String commonName,
            @RequestParam("cropScientificName") String scientificName,
            @RequestParam(value = "cropImage", required = false) MultipartFile cropImage,
            @RequestParam("category") String category,
            @RequestParam("cropSeason") String season,
            @RequestParam("fieldCode") String fieldCode) {
        //validations
        try {
            if(!RegexProcess.cropCodeMatcher(cropCode)){
                logger.warn("Invalid crop code: {}", cropCode);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Save the image if provided
            String imagePath = null;
            if (cropImage != null) {
                CropDTO existingCrop = (CropDTO) cropService.getCrop(cropCode);
                deleteImage(existingCrop.getCropImage()); // Delete the old image
                imagePath = saveImage(cropImage);
                logger.debug("Updated image saved at path: {}", imagePath);
            }

            // Create updated CropDTO
            CropDTO updatedCropDTO = new CropDTO(cropCode, commonName, scientificName, imagePath, category, season, fieldCode);

            cropService.updateCrop(cropCode,updatedCropDTO);
            logger.info("Crop updated successfully: {}", updatedCropDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e) {
            logger.error("Unsupported media type for crop image", e);
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }catch (CropNotFoundException e){
            logger.error("Crop not found: {}", cropCode, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error occurred while updating crop: {}", cropCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
