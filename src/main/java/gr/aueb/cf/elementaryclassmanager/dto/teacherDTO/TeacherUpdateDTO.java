package gr.aueb.cf.elementaryclassmanager.dto.teacherDTO;

import gr.aueb.cf.elementaryclassmanager.dto.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherUpdateDTO extends BaseDTO {

    @NotNull(message = "Firstname should not be null")
    @Size(min = 2, max = 255, message = "Error in firstname length")
    private String firstname;

    @NotNull(message = "Lastname should  not null")
    @Size(min = 2, max = 255, message = "Error in lastname length")
    private String lastname;

    @Email(message = "Email should be valid")
    private String email;
}
