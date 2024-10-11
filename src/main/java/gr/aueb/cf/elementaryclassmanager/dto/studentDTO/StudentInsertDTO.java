package gr.aueb.cf.elementaryclassmanager.dto.studentDTO;

import gr.aueb.cf.elementaryclassmanager.model.Grade;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentInsertDTO {

    @NotNull(message = "Firstname should not be null")
    @Size(min = 2, max = 255, message = "Error in firstname length")
    private String firstname;

    @NotNull(message = "Lastname should not be null")
    @Size(min = 2, max = 255, message = "Error in lastname length")
    private String lastname;

    @NotNull(message = "Ssn should not be null")
    @Size(min = 9, max = 9, message = "Ssn must be exactly 9 characters")
    private String ssn;

    @NotNull(message = "Grade should not be null")
    private Grade grade;
}
