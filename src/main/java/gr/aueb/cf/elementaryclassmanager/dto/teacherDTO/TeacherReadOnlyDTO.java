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

    @NotNull
    private Long classGroupId;

    public TeacherReadOnlyDTO(Long id, String firstName, String lastname, String teacherRegistrationNumber, String email, Long classGroupId) {
        this.setId(id);
        this.firstname = firstName;
        this.lastname = lastname;
        this.registrationNumber = teacherRegistrationNumber;
        this.email = email;
        this.classGroupId = classGroupId;
    }
}
