package lk.ijse.cropmanagementsystem.dto.impl;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lk.ijse.cropmanagementsystem.dto.MonitoringLogStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonitoringLogDTO implements MonitoringLogStatus {
    private String logCode;
    private Date logDate;
    private String observation;
    private String observedImage;
    private List<String> fields;
    private List<String> crops;
    private List<String> staff;

    public MonitoringLogDTO(Date logDate, String observation, String observedImage, List<String> fields, List<String> crops, List<String> staff) {
        this.logDate = logDate;
        this.observation = observation;
        this.observedImage = observedImage;
        this.fields = fields;
        this.crops = crops;
        this.staff = staff;
    }
}
