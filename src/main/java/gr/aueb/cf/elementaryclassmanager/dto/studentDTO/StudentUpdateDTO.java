package gr.aueb.cf.elementaryclassmanager.dto.studentDTO;

import gr.aueb.cf.elementaryclassmanager.dto.BaseDTO;
import gr.aueb.cf.elementaryclassmanager.model.Grade;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentUpdateDTO extends BaseDTO {

    @Size(min = 2, max = 255, message = "Error in firstname length")
    private String firstname;

    @Size(min = 2, max = 255, message = "Error in lastname length")
    private String lastname;

    private Grade grade;


}


