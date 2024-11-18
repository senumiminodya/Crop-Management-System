package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.CropStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;
import lk.ijse.cropmanagementsystem.exception.CropNotFoundException;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.service.CropService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CropService cropService;
    // Directory for saving images
    private final String uploadDir = "src/main/resources/uploads/";

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
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private String saveImage(MultipartFile file) throws IOException {
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
        return filePath.toString();
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Save the image if provided
            String imagePath = null;
            if (cropImage != null) {
                imagePath = saveImage(cropImage);
            }

            // Create updated CropDTO
            CropDTO updatedCropDTO = new CropDTO(cropCode, commonName, scientificName, imagePath, category, season, fieldCode);

            cropService.updateCrop(cropCode,updatedCropDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (CropNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
