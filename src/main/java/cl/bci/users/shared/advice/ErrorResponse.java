package cl.bci.users.shared.advice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta de error")
public class ErrorResponse {

    @Schema(description = "Mensaje de error")
    private String mensaje;
}
