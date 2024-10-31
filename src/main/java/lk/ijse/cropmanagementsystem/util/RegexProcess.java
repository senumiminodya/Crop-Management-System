package lk.ijse.cropmanagementsystem.util;

import java.util.regex.Pattern;

public class RegexProcess {
    public static boolean cropCodeMatcher(String cropCode) {
        String regexForCropCode = "^CROP-[a-fA-F0-9]{4}$";
        Pattern regexPattern = Pattern.compile(regexForCropCode);
        return regexPattern.matcher(cropCode).matches();
    }
    public static boolean equipmentIdMatcher(String equipmentId) {
        String regexForEquipmentId = "^EQUIP-[a-fA-F0-9]{4}$";
        Pattern regexPattern = Pattern.compile(regexForEquipmentId);
        return regexPattern.matcher(equipmentId).matches();
    }
    public static boolean fieldCodeMatcher(String fieldCode) {
        String regexForFieldCode = "^FIELD-[a-fA-F0-9]{4}$";
        Pattern regexPattern = Pattern.compile(regexForFieldCode);
        return regexPattern.matcher(fieldCode).matches();
    }
    public static boolean logCodeMatcher(String logCode) {
        String regexForLogCode = "^LOG-[a-fA-F0-9]{4}$";
        Pattern regexPattern = Pattern.compile(regexForLogCode);
        return regexPattern.matcher(logCode).matches();
    }
    public static boolean staffIdMatcher(String id) {
        String regexForStaffId = "^STAFF-[a-fA-F0-9]{4}$";
        Pattern regexPattern = Pattern.compile(regexForStaffId);
        return regexPattern.matcher(id).matches();
    }
    public static boolean vehicleCodeMatcher(String vehicleCode) {
        String regexForVehicleCode = "^VEHICLE-[a-fA-F0-9]{4}$";
        Pattern regexPattern = Pattern.compile(regexForVehicleCode);
        return regexPattern.matcher(vehicleCode).matches();
    }
}
