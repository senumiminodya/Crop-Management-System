package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.FieldStatus;
import lk.ijse.cropmanagementsystem.dto.impl.FieldDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.FieldNotFoundException;
import lk.ijse.cropmanagementsystem.service.FieldService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/fields")
public class FieldApi {
    @Autowired
    private FieldService fieldService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveField(@RequestBody FieldDTO fieldDTO) {
        try {
            fieldService.saveField(fieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
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
    public ResponseEntity<Void> updateField(@PathVariable ("fieldCode") String fieldCode,
                                           @RequestBody FieldDTO updatedFieldDTO){
        //validations
        try {
            if(!RegexProcess.fieldCodeMatcher(fieldCode) || updatedFieldDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.updateField(fieldCode,updatedFieldDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
