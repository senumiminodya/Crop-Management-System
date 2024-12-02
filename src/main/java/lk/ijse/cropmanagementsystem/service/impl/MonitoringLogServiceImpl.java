package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.MonitoringLogStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;
import lk.ijse.cropmanagementsystem.dto.impl.FieldDTO;
import lk.ijse.cropmanagementsystem.dto.impl.MonitoringLogDTO;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;
import lk.ijse.cropmanagementsystem.entity.impl.CropEntity;
import lk.ijse.cropmanagementsystem.entity.impl.FieldEntity;
import lk.ijse.cropmanagementsystem.entity.impl.MonitoringLogEntity;
import lk.ijse.cropmanagementsystem.entity.impl.StaffEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.MonitoringLogNotFoundException;
import lk.ijse.cropmanagementsystem.repository.CropRepo;
import lk.ijse.cropmanagementsystem.repository.FieldRepo;
import lk.ijse.cropmanagementsystem.repository.MonitoringLogRepo;
import lk.ijse.cropmanagementsystem.repository.StaffRepo;
import lk.ijse.cropmanagementsystem.service.MonitoringLogService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MonitoringLogServiceImpl implements MonitoringLogService {
    @Autowired
    private MonitoringLogRepo logRepo;
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private CropRepo cropRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private Mapping logMapping;
    @Override
    public void saveLog(MonitoringLogDTO monitoringLogDTO) {
        /*monitoringLogDTO.setLogCode(AppUtil.generateLogCode());
        MonitoringLogEntity savedLog =
                logRepo.save(logMapping.toMonitoringLogEntity(monitoringLogDTO));
        if(savedLog == null){
            throw new DataPersistException("Log not saved");
        }*/
        monitoringLogDTO.setLogCode(AppUtil.generateLogCode());
        MonitoringLogEntity entity = logMapping.toMonitoringLogEntity(monitoringLogDTO);
        MonitoringLogEntity savedLog = logRepo.save(entity);

        if (savedLog == null) {
            throw new DataPersistException("Log not saved");
        }
    }

    @Override
    public List<MonitoringLogDTO> getAllLogs() {

        //return logMapping.asMonitoringLogDtoList( logRepo.findAll());
        return logRepo.findAll().stream()
                .map(logMapping::toMonitoringLogDto)
                .toList();
    }

    @Override
    public MonitoringLogStatus getLog(String logCode) {
        /*if(logRepo.existsById(logCode)){
            var selectedLog = logRepo.getReferenceById(logCode);
            return logMapping.toMonitoringLogDto(selectedLog);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected log not found");
        }*/
        return logRepo.findById(logCode)
                .map(logMapping::toMonitoringLogDto)
                .orElseThrow(() -> new MonitoringLogNotFoundException("Selected log not found"));
    }

    @Override
    public void deleteLog(String logCode) {
        /*Optional<MonitoringLogEntity> foundLog = logRepo.findById(logCode);
        if (!foundLog.isPresent()) {
            throw new MonitoringLogNotFoundException("Log not found");
        }else {
            logRepo.deleteById(logCode);
        }*/
        MonitoringLogEntity foundLog = logRepo.findById(logCode)
                .orElseThrow(() -> new MonitoringLogNotFoundException("Log not found"));
        logRepo.delete(foundLog);
    }

    @Override
    public void updateLog(String logCode, MonitoringLogDTO monitoringLogDTO) {
        Optional<MonitoringLogEntity> findLog = logRepo.findById(logCode);
        if (!findLog.isPresent()) {
            throw new MonitoringLogNotFoundException("Log not found");
        }else {
            findLog.get().setLogDate(monitoringLogDTO.getLogDate());
            findLog.get().setObservation(monitoringLogDTO.getObservation());
            findLog.get().setObservedImage(monitoringLogDTO.getObservedImage());
            List<FieldEntity> fieldEntities = new ArrayList<>();
            for (String field : monitoringLogDTO.getFields()) {
                FieldEntity fieldEntity = fieldRepo.findById(field)
                        .orElseThrow(() -> new DataPersistException("Field not found"));
                fieldEntities.add(fieldEntity);
            }
            findLog.get().setFields(fieldEntities);
            List<CropEntity> cropEntities = new ArrayList<>();
            for (String crop : monitoringLogDTO.getCrops()) {
                CropEntity cropEntity = cropRepo.findById(crop)
                        .orElseThrow(() -> new DataPersistException("Crop not found"));
                cropEntities.add(cropEntity);
            }
            findLog.get().setCrops(cropEntities);
            List<StaffEntity> staffEntities = new ArrayList<>();
            for (String staff: monitoringLogDTO.getStaff()) {
                StaffEntity staffEntity = staffRepo.findById(staff)
                        .orElseThrow(() -> new DataPersistException("Staff not found"));
                staffEntities.add(staffEntity);
            }
            findLog.get().setStaff(staffEntities);
        }
    }
}
