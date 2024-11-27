package lk.ijse.cropmanagementsystem.entity.impl;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.Gender;
import lk.ijse.cropmanagementsystem.entity.Role;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff")
public class StaffEntity implements SuperEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String designation;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date joinedDate;
    private Date dob;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;

    private String contactNo;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FieldEntity> fields;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<VehicleEntity> vehicles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    public String getAddressLine5() {
        return addressLine5;
    }

    public void setAddressLine5(String addressLine5) {
        this.addressLine5 = addressLine5;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<FieldEntity> getFields() {
        return fields;
    }

    public void setFields(List<FieldEntity> fields) {
        this.fields = fields;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }
}
