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
public class TeacherReadOnlyDTO extends BaseDTO {


    @NotNull(message = "Firstname should not be null")
    @Size(min = 2, max = 255, message = "Error in firstname length")
    private String firstname;

    @NotNull(message = "Lastname should  not null")
    @Size(min = 2, max = 255, message = "Error in lastname length")
    private String lastname;

    @NotNull(message = "Registration Number should  not null")
    @Size(min = 9, max = 9, message = "Registration Number must be exactly 9 characters")
    private String registrationNumber;

    @Email(message = "Email should be valid")
    private String email;

    public TeacherReadOnlyDTO(Long id, String firstName, String lastname, String teacherRegistrationNumber, String email) {
        this.setId(id);
        this.setFirstname(firstName);
        this.setLastname(lastname);
        this.setRegistrationNumber(teacherRegistrationNumber);
        this.setEmail(email);
    }
}
