package cl.bci.users.application.dto;

import lombok.Data;

@Data
public class PhoneRequestDto {

    private String number;
    private String citycode;
    private String contrycode;
}
