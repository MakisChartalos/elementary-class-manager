package gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO;

import gr.aueb.cf.elementaryclassmanager.dto.BaseDTO;
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
public class ClassGroupReadOnlyDTO extends BaseDTO {

    @NotNull(message = "Name should not be null")
    private Character name;

    @NotNull(message = "Grade should not be null")
    private Grade grade;


    public ClassGroupReadOnlyDTO(Long id, Character name, Grade grade) {
        this.setId(id);
        this.name = name;
        this.grade = grade;
    }
}

