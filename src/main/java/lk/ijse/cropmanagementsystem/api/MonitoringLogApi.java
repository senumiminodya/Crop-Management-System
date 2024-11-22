package lk.ijse.cropmanagementsystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.MonitoringLogStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;
import lk.ijse.cropmanagementsystem.dto.impl.MonitoringLogDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.MonitoringLogNotFoundException;
import lk.ijse.cropmanagementsystem.service.MonitoringLogService;
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
@RequestMapping("api/v1/logs")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class MonitoringLogApi {
    @Autowired
    private MonitoringLogService logService;
    @Value("${upload.dir:src/main/resources/uploads/}")
    private String uploadDir;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveLogs(
            @RequestParam("logData") String logData, // JSON Data as String
            @RequestParam(value = "observedImage", required = false) MultipartFile logImage ) {
        try {
            // Convert logData JSON string to MonitoringLogDTO object
            ObjectMapper objectMapper = new ObjectMapper();
             MonitoringLogDTO monitoringLogDTO = objectMapper.readValue(logData, MonitoringLogDTO.class);

            // If logImage is provided, process it (e.g., save to file system or database)
            if (logImage != null && !logImage.isEmpty()) {
                String imagePath = saveFile(logImage);
                monitoringLogDTO.setObservedImage(imagePath);
            }

            // Save the log data
            logService.saveLog(monitoringLogDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Save file to the server (example function)
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
    @GetMapping(value = "/{logCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringLogStatus getSelectedLog(@PathVariable ("logCode") String logCode){
        if (!RegexProcess.logCodeMatcher(logCode)) {
            return new SelectedClassesErrorStatus(1,"Log Code is not valid");
        }
        return logService.getLog(logCode);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getALlLogs(){
        return logService.getAllLogs();
    }
    @DeleteMapping(value = "/{logCode}")
    public ResponseEntity<Void> deleteLog(@PathVariable ("logCode") String logCode){
        try {
            if (!RegexProcess.logCodeMatcher(logCode)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            logService.deleteLog(logCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (MonitoringLogNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{logCode}")
    public ResponseEntity<Void> updateLog(
            @PathVariable ("logCode") String logCode,
            @RequestParam("logData") String logData, // JSON Data as String
            @RequestParam(value = "observedImage", required = false) MultipartFile logImage ) {
        //validations
        try {
            if(!RegexProcess.logCodeMatcher(logCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Convert logData JSON string to MonitoringLogDTO object
            ObjectMapper objectMapper = new ObjectMapper();
            MonitoringLogDTO updatedLogDTO = objectMapper.readValue(logData, MonitoringLogDTO.class);

            // If logImage is provided, process it (e.g., save to file system or database)
            if (logImage != null && !logImage.isEmpty()) {
                MonitoringLogDTO existingLog = (MonitoringLogDTO) logService.getLog(logCode);
                deleteImage(existingLog.getObservedImage());
                String imagePath = saveFile(logImage);
                updatedLogDTO.setObservedImage(imagePath);
            }
            logService.updateLog(logCode,updatedLogDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (MonitoringLogNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
