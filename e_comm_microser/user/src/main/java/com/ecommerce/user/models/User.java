package com.ecommerce.user.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import org.springframework.stereotype.Indexed;

import java.time.LocalDateTime;

@Data
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
}
