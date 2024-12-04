package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.StaffStatus;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.StaffNotFoundException;
import lk.ijse.cropmanagementsystem.service.StaffService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/staff")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class StaffApi {
    private static final Logger logger = LoggerFactory.getLogger(StaffApi.class);
    @Autowired
    private StaffService staffService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@RequestBody StaffDTO staffDTO) {
        try {
            staffService.saveStaff(staffDTO);
            logger.info("Staff saved successfully with ID: {}", staffDTO.getId());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            logger.error("Error persisting staff with ID: {}", staffDTO.getId(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error while saving staff with ID: {}", staffDTO.getId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public StaffStatus getSelectedStaff(@PathVariable ("id") String staffId){
        if (!RegexProcess.staffIdMatcher(staffId)) {
            logger.warn("Invalid staff ID: {}", staffId);
            return new SelectedClassesErrorStatus(1,"Staff Id is not valid");
        }
        logger.info("Fetching staff details for ID: {}", staffId);
        return staffService.getStaff(staffId);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDTO> getALlStaff(){
        logger.info("Fetching all staff records");
        return staffService.getAllStaff();
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable ("id") String staffId){
        try {
            if (!RegexProcess.staffIdMatcher(staffId)) {
                logger.warn("Invalid staff ID: {}", staffId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.deleteStaff(staffId);
            logger.info("Staff deleted successfully with ID: {}", staffId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            logger.error("Staff with ID: {} not found", staffId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Unexpected error while deleting staff with ID: {}", staffId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateStaff(@PathVariable ("id") String staffId,
                                           @RequestBody StaffDTO updatedStaffDTO){
        //validations
        try {
            if(!RegexProcess.staffIdMatcher(staffId) || updatedStaffDTO == null){
                logger.warn("Invalid input for staff update: staffId = {}, updatedStaffDTO = {}", staffId, updatedStaffDTO);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.updateStaff(staffId,updatedStaffDTO);
            logger.info("Staff updated successfully with ID: {}", staffId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            logger.error("Staff with ID: {} not found for update", staffId, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error while updating staff with ID: {}", staffId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
