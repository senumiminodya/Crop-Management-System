package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.EquipmentStatus;
import lk.ijse.cropmanagementsystem.dto.impl.EquipmentDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.EquipmentNotFoundException;
import lk.ijse.cropmanagementsystem.service.EquipmentService;
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
@RequestMapping("api/v1/equipments")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class EquipmentApi {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentApi.class);
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        logger.info("Received request to save equipment: {}", equipmentDTO);
        try {
            equipmentService.saveEquipment(equipmentDTO);
            logger.info("Equipment saved successfully.");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            logger.error("Error persisting equipment data: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error occurred while saving equipment: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{equipmentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public EquipmentStatus getSelectedEquipment(@PathVariable ("equipmentId") String equipmentId){
        logger.info("Fetching equipment with ID: {}", equipmentId);
        if (!RegexProcess.equipmentIdMatcher(equipmentId)) {
            logger.warn("Invalid Equipment ID: {}", equipmentId);
            return new SelectedClassesErrorStatus(1,"Equipment ID is not valid");
        }
        EquipmentStatus equipment = equipmentService.getEquipment(equipmentId);
        logger.info("Equipment retrieved successfully: {}", equipment);
        return equipment;
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getALlEquipments(){
        logger.info("Fetching all equipments");
        return equipmentService.getAllEquipments();
    }
    @DeleteMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable ("equipmentId") String equipmentId){
        logger.info("Request to delete equipment with ID: {}", equipmentId);
        try {
            if (!RegexProcess.equipmentIdMatcher(equipmentId)) {
                logger.warn("Invalid Equipment ID: {}", equipmentId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            equipmentService.deleteEquipment(equipmentId);
            logger.info("Equipment deleted successfully.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (EquipmentNotFoundException e){
            logger.error("Equipment not found: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Unexpected error occurred while deleting equipment: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> updateEquipment(@PathVariable ("equipmentId") String equipmentId,
                                           @RequestBody EquipmentDTO updatedEquipmentDTO){
        logger.info("Request to update equipment with ID: {}", equipmentId);
        try {
            if(!RegexProcess.equipmentIdMatcher(equipmentId) || updatedEquipmentDTO == null){
                logger.warn("Invalid data for equipment update. ID: {}, Data: {}", equipmentId, updatedEquipmentDTO);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            equipmentService.updateEquipment(equipmentId,updatedEquipmentDTO);
            logger.info("Equipment updated successfully.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (EquipmentNotFoundException e){
            logger.error("Equipment not found for update: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error occurred while updating equipment: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
