package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.StaffStatus;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.StaffNotFoundException;
import lk.ijse.cropmanagementsystem.service.StaffService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/staff")
public class StaffApi {
    @Autowired
    private StaffService staffService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@RequestBody StaffDTO staffDTO) {
        try {
            staffService.saveStaff(staffDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public StaffStatus getSelectedStaff(@PathVariable ("id") String staffId){
        if (!RegexProcess.staffIdMatcher(staffId)) {
            return new SelectedClassesErrorStatus(1,"Staff Id is not valid");
        }
        return staffService.getStaff(staffId);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDTO> getALlStaff(){
        return staffService.getAllStaff();
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable ("id") String staffId){
        try {
            if (!RegexProcess.staffIdMatcher(staffId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.deleteStaff(staffId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateStaff(@PathVariable ("id") String staffId,
                                           @RequestBody StaffDTO updatedStaffDTO){
        //validations
        try {
            if(!RegexProcess.staffIdMatcher(staffId) || updatedStaffDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.updateStaff(staffId,updatedStaffDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
