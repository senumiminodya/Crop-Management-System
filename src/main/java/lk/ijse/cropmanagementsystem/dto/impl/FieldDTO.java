package lk.ijse.cropmanagementsystem.dto.impl;

import lk.ijse.cropmanagementsystem.dto.FieldStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements FieldStatus {
    private String fieldCode;
    private String fieldName;
    private Point fieldLocation;
    private double extentSize;
    private String fieldImage1;
    private String fieldImage2;
    private List<String> staff;
}
