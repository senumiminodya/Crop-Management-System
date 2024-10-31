package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.MonitoringLogStatus;
import lk.ijse.cropmanagementsystem.dto.impl.MonitoringLogDTO;

import java.util.List;

public interface MonitoringLogService {
    void saveLog(MonitoringLogDTO monitoringLogDTO);
    List<MonitoringLogDTO> getAllLogs();
    MonitoringLogStatus getLog(String logCode);
    void deleteLog(String logCode);
    void updateLog(String logCode, MonitoringLogDTO monitoringLogDTO);
}
