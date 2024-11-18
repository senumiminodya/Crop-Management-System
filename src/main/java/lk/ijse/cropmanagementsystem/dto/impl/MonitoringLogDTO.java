package lk.ijse.cropmanagementsystem.dto.impl;

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
    private List<FieldDTO> fields;
    private List<CropDTO> crops;
    private List<StaffDTO> staff;

    public MonitoringLogDTO(Date logDate, String observation, String observedImage, List<FieldDTO> fields, List<CropDTO> crops, List<StaffDTO> staff) {
        this.logDate = logDate;
        this.observation = observation;
        this.observedImage = observedImage;
        this.fields = fields;
        this.crops = crops;
        this.staff = staff;
    }
}
