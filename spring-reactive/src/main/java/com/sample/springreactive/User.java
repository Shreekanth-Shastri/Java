package com.sample.springreactive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int Id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String postalAddress;
}
