package com.enceladus.enceladus.service;

import com.enceladus.enceladus.Exeption.AccountLockedException;
import com.enceladus.enceladus.Exeption.InvalidCredentialsException;
import com.enceladus.enceladus.Exeption.UserAlreadyExistException;
import com.enceladus.enceladus.dto.SignInRequest;
import com.enceladus.enceladus.dto.SignInResponce;
import com.enceladus.enceladus.dto.SignUpRequest;
import com.enceladus.enceladus.model.User;
import com.enceladus.enceladus.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())
                || userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistException("Username or email already in use");
        }

        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getMobile(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );
        // role defaults to ROLE_USER in the entity — never trust a role field from the request body

        userRepository.save(user);
    }

    @Transactional
    public SignInResponce authenticateUser(SignInRequest signInRequest) {
        User user = userRepository.findByUsername(signInRequest.getUsername()).orElse(null);

        // still run a dummy check timing-wise isn't perfect here, but we avoid revealing
        // via early-return whether the account exists by always giving the same generic error
        if (user != null && user.getLockTime() != null) {
            if (user.isLockExpired()) {
                // lock window passed — reset before allowing another attempt
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepository.save(user);
            } else {
                throw new AccountLockedException(
                        "Account temporarily locked due to multiple failed login attempts. Try again later.");
            }
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
        } catch (LockedException e) {
            throw new AccountLockedException(
                    "Account temporarily locked due to multiple failed login attempts. Try again later.");
        } catch (BadCredentialsException e) {
            registerFailedAttempt(user);
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // successful login — reset any failed-attempt counter
        if (user != null && user.getFailedAttempts() > 0) {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        }

        String token = jwtService.generateToken(user.getUsername());
        return new SignInResponce(token, "Bearer", user.getId(), user.getUsername(), user.getEmail());
    }

    private void registerFailedAttempt(User user) {
        // user may be null if the username didn't exist at all — nothing to lock, and we
        // don't reveal that distinction to the caller (InvalidCredentialsException either way)
        if (user == null) {
            return;
        }
        int attempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);
        if (attempts >= User.MAX_FAILED_ATTEMPTS) {
            user.setLockTime(Instant.now());
        }
        userRepository.save(user);
    }
}

