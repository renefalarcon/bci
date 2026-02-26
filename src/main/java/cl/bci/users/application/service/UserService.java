package cl.bci.users.application.service;

import cl.bci.users.application.dto.PhoneRequestDto;
import cl.bci.users.application.dto.UserRequestDto;
import cl.bci.users.application.dto.UserResponseDto;
import cl.bci.users.domain.exception.EmailAlreadyExistsException;
import cl.bci.users.domain.exception.InvalidPasswordException;
import cl.bci.users.infrastructure.persistence.entity.PhoneEntity;
import cl.bci.users.infrastructure.persistence.entity.UserEntity;
import cl.bci.users.infrastructure.repository.UserRepository;
import cl.bci.users.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.password.regex}")
    private String passwordRegex;

    /**
     * ORCHESTRATION:
     * Controla todo el flujo de creación del usuario
     */

    @Transactional
    public UserResponseDto createUser(UserRequestDto request) {

        String email = request.getEmail().toLowerCase().trim();

        // Validar email duplicado
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("El correo ya registrado");
        }

        // Validar password por regex (configurable)
        if (!Pattern.matches(passwordRegex, request.getPassword())) {
            throw new InvalidPasswordException("Password no cumple el formato requerido");
        }

        // Crear entidad User
        LocalDateTime now = LocalDateTime.now();

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder()
                .email(email)
                .id(UUID.randomUUID())
                .name(request.getName())
                .password(encodedPassword)
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isActive(true)
                .build();

        // Mapear teléfonos
        List<PhoneEntity> phones = request.getPhones()
                .stream()
                .map(phone -> mapPhone(phone, user))
                .toList();

        user.setPhones(phones);

        // Persistir (SAGA: si algo falla, rollback)
        UserEntity savedUser = userRepository.save(user);

        // Generar token (placeholder por ahora)
        String token = jwtService.generateToken(user.getEmail());

        savedUser.setToken(token);

        // Respuesta
        return UserResponseDto.builder()
                .id(savedUser.getId())
                .created(savedUser.getCreated())
                .modified(savedUser.getModified())
                .lastLogin(savedUser.getLastLogin())
                .token(savedUser.getToken())
                .isActive(savedUser.isActive())
                .build();
    }

    private PhoneEntity mapPhone(PhoneRequestDto dto, UserEntity user) {
        return PhoneEntity.builder()
                .number(dto.getNumber())
                .cityCode(dto.getCitycode())
                .countryCode(dto.getContrycode())
                .user(user)
                .build();
    }

    private UserResponseDto createUserFallback(
            UserRequestDto request,
            Throwable ex) {

        throw new RuntimeException("Servicio temporalmente no disponible");
    }
}
