package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.StaffStatus;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;
import lk.ijse.cropmanagementsystem.entity.impl.StaffEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.StaffNotFoundException;
import lk.ijse.cropmanagementsystem.repository.StaffRepo;
import lk.ijse.cropmanagementsystem.service.StaffService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private Mapping staffMapping;
    @Override
    public StaffDTO saveStaff(StaffDTO staffDTO) {
        staffDTO.setId(AppUtil.generateStaffId());
        StaffEntity savedStaff =
                staffRepo.save(staffMapping.toStaffEntity(staffDTO));
        if(savedStaff == null){
            throw new DataPersistException("Staff not saved");
        }
        return staffMapping.toStaffDto(savedStaff);
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return staffMapping.asStaffDtoList( staffRepo.findAll());
    }

    @Override
    public StaffStatus getStaff(String staffId) {
        if(staffRepo.existsById(staffId)){
            var selectedStaff = staffRepo.getReferenceById(staffId);
            return staffMapping.toStaffDto(selectedStaff);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected staff not found");
        }
    }

    @Override
    public void deleteStaff(String staffId) {
        Optional<StaffEntity> foundStaff = staffRepo.findById(staffId);
        if (!foundStaff.isPresent()) {
            throw new StaffNotFoundException("Staff not found");
        }else {
            staffRepo.deleteById(staffId);
        }
    }

    @Override
    public void updateStaff(String staffId, StaffDTO staffDTO) {
        Optional<StaffEntity> findStaff = staffRepo.findById(staffId);
        if (!findStaff.isPresent()) {
            throw new StaffNotFoundException("Staff not found");
        }else {
            findStaff.get().setFirstName(staffDTO.getFirstName());
            findStaff.get().setLastName(staffDTO.getLastName());
            findStaff.get().setDesignation(staffDTO.getDesignation());
            findStaff.get().setGender(staffDTO.getGender());
            findStaff.get().setJoinedDate(staffDTO.getJoinedDate());
            findStaff.get().setDob(staffDTO.getDob());
            findStaff.get().setAddressLine1(staffDTO.getAddressLine1());
            findStaff.get().setAddressLine2(staffDTO.getAddressLine2());
            findStaff.get().setAddressLine3(staffDTO.getAddressLine3());
            findStaff.get().setAddressLine4(staffDTO.getAddressLine4());
            findStaff.get().setAddressLine5(staffDTO.getAddressLine5());
            findStaff.get().setContactNo(staffDTO.getContactNo());
            findStaff.get().setEmail(staffDTO.getEmail());
            findStaff.get().setRole(staffDTO.getRole());
        }
    }

    @Override
    public Optional findByEmail(String email) {
        Optional<StaffEntity> byEmail = staffRepo.findByEmail(email);

        return byEmail.map(staffMapping::toStaffDto);
    }
}
