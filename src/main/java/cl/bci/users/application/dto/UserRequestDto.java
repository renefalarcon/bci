package cl.bci.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Solicitud de creación de usuario",
        example = """
        {
          "name": "Rene Alarcon",
          "email": "ralarcon@bci.cl",
          "password": "Password123",
          "phones": [
            {
              "number": "1234567",
              "citycode": "1",
              "contrycode": "57"
            },
            {
              "number": "7654321",
              "citycode": "2",
              "contrycode": "56"
            }
          ]
        }
        """
        )
public class UserRequestDto {

    @NotBlank
    private String name;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String email;

    @NotBlank
    private String password;

    @NotEmpty
    @Schema(
            description = "Lista de teléfonos del usuario",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<PhoneRequestDto> phones;
}
