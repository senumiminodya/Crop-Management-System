package lk.ijse.cropmanagementsystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.MonitoringLogStatus;
import lk.ijse.cropmanagementsystem.dto.impl.MonitoringLogDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.MonitoringLogNotFoundException;
import lk.ijse.cropmanagementsystem.service.MonitoringLogService;
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
@RequestMapping("api/v1/logs")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class MonitoringLogApi {
    @Autowired
    private MonitoringLogService logService;
    private final String uploadDir = "src/main/resources/uploads/";

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
