package lk.ijse.cropmanagementsystem.api;

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

import java.util.List;

@RestController
@RequestMapping("api/v1/logs")
public class MonitoringLogApi {
    @Autowired
    private MonitoringLogService logService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveLogs(@RequestBody MonitoringLogDTO monitoringLogDTO) {
        try {
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
    public ResponseEntity<Void> updateLog(@PathVariable ("logCode") String logCode,
                                           @RequestBody MonitoringLogDTO updatedLogDTO){
        //validations
        try {
            if(!RegexProcess.logCodeMatcher(logCode) || updatedLogDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
