package lk.ijse.cropmanagementsystem.dto.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lk.ijse.cropmanagementsystem.dto.FieldStatus;
import lk.ijse.cropmanagementsystem.util.PointDeserializer;
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
    @JsonDeserialize(using = PointDeserializer.class)
    private Point fieldLocation;
    private double extentSize;
    private String fieldImage1;
    private String fieldImage2;
    private List<String> staff;

    public FieldDTO(String fieldName, Point fieldLocation, double extentSize, String fieldImage1, String fieldImage2, List<String> staff) {
        this.fieldName = fieldName;
        this.fieldLocation = fieldLocation;
        this.extentSize = extentSize;
        this.fieldImage1 = fieldImage1;
        this.fieldImage2 = fieldImage2;
        this.staff = staff;
    }
}
