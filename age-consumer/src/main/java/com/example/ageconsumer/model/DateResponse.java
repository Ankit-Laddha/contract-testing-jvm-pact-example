package com.example.ageconsumer.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateResponse {
    private String givenDate;
    private int year;
    private int month;
    private int day;

    @Builder.Default
    private Boolean isValidDate = false;
    private String message;
}
