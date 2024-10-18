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
public class ClassGroupUpdateDTO extends BaseDTO {

    private Character name;

    private Grade grade;

}
