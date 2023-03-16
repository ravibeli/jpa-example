package com.hr.app.jpaexample.i18n.exceptions.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();
}
