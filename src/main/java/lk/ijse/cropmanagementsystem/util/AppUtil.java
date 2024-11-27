package lk.ijse.cropmanagementsystem.util;

import org.springframework.data.geo.Point;

import java.util.Base64;
import java.util.UUID;

public class AppUtil {
    public static String generateUserId() {
        return "USER-"+ UUID.randomUUID();
    }
    public static String generateCropCode() {
        return "CROP-" + UUID.randomUUID().toString().substring(0, 4);
    }
    public static String generateEquipmentId() {
        return "EQUIP-" + UUID.randomUUID().toString().substring(0, 4);
    }
    public static String generateFieldCode() {
        return "FIELD-" + UUID.randomUUID().toString().substring(0, 4);
    }
    public static String generateLogCode() {
        return "LOG-" + UUID.randomUUID().toString().substring(0, 4);
    }
    public static String generateStaffId() {
        return "STAFF-" + UUID.randomUUID().toString().substring(0, 4);
    }
    public static String generateVehicleCode() {
        return "VEHICLE-" + UUID.randomUUID().toString().substring(0, 4);
    }

    public static String cropImageToBase64(byte[] cropImage) {
        return Base64.getEncoder().encodeToString(cropImage);
    }
    public static String locationToBase64(Point location) {
        // Convert latitude and longitude to a comma-separated string
        String locationString = location.getX() + "," + location.getY();
        // Encode the string to Base64
        return Base64.getEncoder().encodeToString(locationString.getBytes());
    }
    public static String fieldImage1ToBase64(byte[] fieldImage1) {
        return Base64.getEncoder().encodeToString(fieldImage1);
    }
    public static String fieldImage2ToBase64(byte[] fieldImage2) {
        return Base64.getEncoder().encodeToString(fieldImage2);
    }
    public static String observedImageToBase64(byte[] observedImage) {
        return Base64.getEncoder().encodeToString(observedImage);
    }
}
