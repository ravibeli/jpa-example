package com.hr.app.jpaexample.i18n.exceptions.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class Violation {
    private final String fieldName;
    private final String message;
}
