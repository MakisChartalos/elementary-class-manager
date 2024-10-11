package gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO;

import gr.aueb.cf.elementaryclassmanager.model.Grade;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassGroupInsertDTO {

    @NotNull(message = "Name should not be null")
    private Character name;

    @NotNull(message = "Grade should not be null")
    private Grade grade;
}
