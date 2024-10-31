package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.MonitoringLogStatus;
import lk.ijse.cropmanagementsystem.dto.impl.MonitoringLogDTO;
import lk.ijse.cropmanagementsystem.entity.impl.MonitoringLogEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.MonitoringLogNotFoundException;
import lk.ijse.cropmanagementsystem.repository.MonitoringLogRepo;
import lk.ijse.cropmanagementsystem.service.MonitoringLogService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MonitoringLogServiceImpl implements MonitoringLogService {
    @Autowired
    private MonitoringLogRepo logRepo;
    @Autowired
    private Mapping logMapping;
    @Override
    public void saveLog(MonitoringLogDTO monitoringLogDTO) {
        monitoringLogDTO.setLogCode(AppUtil.generateLogCode());
        MonitoringLogEntity savedLog =
                logRepo.save(logMapping.toMonitoringLogEntity(monitoringLogDTO));
        if(savedLog == null){
            throw new DataPersistException("Log not saved");
        }
    }

    @Override
    public List<MonitoringLogDTO> getAllLogs() {
        return logMapping.asMonitoringLogDtoList( logRepo.findAll());
    }

    @Override
    public MonitoringLogStatus getLog(String logCode) {
        if(logRepo.existsById(logCode)){
            var selectedLog = logRepo.getReferenceById(logCode);
            return logMapping.toMonitoringLogDto(selectedLog);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected log not found");
        }
    }

    @Override
    public void deleteLog(String logCode) {
        Optional<MonitoringLogEntity> foundLog = logRepo.findById(logCode);
        if (!foundLog.isPresent()) {
            throw new MonitoringLogNotFoundException("Log not found");
        }else {
            logRepo.deleteById(logCode);
        }
    }

    @Override
    public void updateLog(String logCode, MonitoringLogDTO monitoringLogDTO) {
        Optional<MonitoringLogEntity> findLog = logRepo.findById(logCode);
        if (!findLog.isPresent()) {
            throw new MonitoringLogNotFoundException("Log not found");
        }else {
            findLog.get().setLogDate(monitoringLogDTO.getLogDate());
            findLog.get().setObservation(monitoringLogDTO.getLogDetails());
            findLog.get().setObservedImage(monitoringLogDTO.getObservedImage());
        }
    }
}
