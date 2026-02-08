package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must contain atleast 5 characters")
    @Column(name = "street")
    private String stree;

    @NotBlank
    @Size(min = 5, message = "building name must contain atleast 5 characters")
    @Column(name = "building_name")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "city name must contain atleast 5 characters")
    @Column(name = "city")
    private String city;

    @NotBlank
    @Size(min = 5, message = "state name must contain atleast 5 characters")
    @Column(name = "state")
    private String state;

    @NotBlank
    @Size(min = 5, message = "country name must contain atleast 5 characters")
    @Column(name = "country")
    private String country;

    @NotBlank
    @Size(min = 5, message = "pincode name must contain atleast 5 characters")
    @Column(name = "pincode")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();


}
