package com.example.dateprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateResponse {
    private String givenDate;
    private int year;
    private int month;
    private int day;
    private Boolean isValidDate = false;
    private String message;
}
