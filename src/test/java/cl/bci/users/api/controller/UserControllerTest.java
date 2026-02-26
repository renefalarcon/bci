package cl.bci.users.api.controller;

import cl.bci.users.application.service.UserService;
import cl.bci.users.shared.advice.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "cl.bci.users.shared.advice.*"
        )
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {

        when(userService.createUser(any()))
                .thenReturn(null); // válido para este test

        String request = """
        {
          "name": "Juan Perez",
          "email": "juan@test.cl",
          "password": "Password123",
          "phones": [
            {
              "number": "1234567",
              "citycode": "1",
              "countrycode": "56"
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenMissingFields() throws Exception {

        String request = """
        {
          "email": "juan@test.cl"
        }
        """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedError() throws Exception {

        when(userService.createUser(any()))
                .thenThrow(new RuntimeException("DB error"));

        String request = """
        {
          "name": "Juan Perez",
          "email": "juan@test.cl",
          "password": "Password123",
          "phones": [
            {
              "number": "1234567",
              "citycode": "1",
              "countrycode": "56"
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isInternalServerError());
    }
}