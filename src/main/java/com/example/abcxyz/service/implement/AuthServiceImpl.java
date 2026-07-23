package com.example.abcxyz.service.implement;

import com.example.abcxyz.dto.request.*;
import com.example.abcxyz.dto.response.*;
import com.example.abcxyz.entity.Role;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.enums.RoleType;
import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.mapper.UserMapper;
import com.example.abcxyz.repository.RoleRepository;
import com.example.abcxyz.repository.UserRepository;
import com.example.abcxyz.service.AuthService;
import com.example.abcxyz.service.EmailService;
import com.example.abcxyz.service.JwtService;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService,
                           PasswordEncoder passwordEncoder,
                           RedisTemplate<String, Object> redisTemplate,
                           EmailService emailService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws MessagingException, IOException,
            TemplateException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String infoKey = String.format("register-info:%s", registerRequest.getEmail());
        String otpKey = String.format("register-otp:%s", registerRequest.getEmail());

        if (Boolean.TRUE.equals(this.redisTemplate.hasKey(infoKey))) {
            RegisterRequest updatedRegisterInfo = registerRequest;

            Long currentOtpRemain = this.redisTemplate.getExpire(otpKey, TimeUnit.SECONDS);
            long currentOtpExpireAt = 0;

            if (currentOtpRemain != null && currentOtpRemain > 0) {
                currentOtpExpireAt = Instant.now().getEpochSecond() + currentOtpRemain;
            }

            this.redisTemplate.opsForValue().set(infoKey, updatedRegisterInfo, Duration.ofMinutes(15));

            return RegisterResponse.builder()
                    .message("OTP has been send to email")
                    .otpExpireAt(currentOtpExpireAt)
                    .build();
        }

        int otpNum = 100000 + secureRandom.nextInt(900000);
        String otpCode = String.valueOf(otpNum);

        long otpExpireAt = Instant.now().getEpochSecond() + 300;

        this.redisTemplate.opsForValue().set(infoKey, registerRequest, Duration.ofMinutes(15));
        this.redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofMinutes(5));

        this.emailService.sendOtpEmail(registerRequest.getEmail(), otpCode);

        return RegisterResponse.builder()
                .message("OTP has been send to email")
                .otpExpireAt(otpExpireAt)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerifyOtpResponse verifyOtpAndCreateUser(VerifyOtpRequest verifyOtpRequest) {
        String infoKey = String.format("register-info:%s", verifyOtpRequest.getEmail());
        String otpKey = String.format("register-otp:%s", verifyOtpRequest.getEmail());

        String otpCodeNeedVerify = verifyOtpRequest.getOtpCode();

        RegisterRequest redisRegisterRequest = (RegisterRequest) this.redisTemplate.opsForValue().get(infoKey);
        String redisOtpCode = (String) this.redisTemplate.opsForValue().get(otpKey);

        if (redisRegisterRequest == null) {
            throw new AppException(ErrorCode.REGISTER_SESSION_EXPIRED);
        }

        if (redisOtpCode == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        if (!otpCodeNeedVerify.equals(redisOtpCode)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        User user = this.userMapper.toUser(redisRegisterRequest);

        Role role = this.roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(
                () -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR)
        );

        String hashPassword = this.passwordEncoder.encode(redisRegisterRequest.getPassword());

        user.setPassword(hashPassword);
        user.getRoles().add(role);

        User newUser = this.userRepository.save(user);

        this.redisTemplate.delete(infoKey);
        this.redisTemplate.delete(otpKey);

        String accessToken = this.jwtService.generateAccessToken(newUser);
        String refreshToken = this.jwtService.generateRefreshToken(newUser);

        String refreshTokenKey = String.format("refresh-token:%s", String.valueOf(newUser.getId()));

        this.redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, Duration.ofDays(7));

        return VerifyOtpResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(newUser.getId())
                .user(this.userMapper.toUserResponse(newUser))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = this.userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }

        String accessToken = this.jwtService.generateAccessToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);

        UserResponse userResponse = this.userMapper.toUserResponse(user);

        String refreshTokenKey = String.format("refresh-token:%s", String.valueOf(user.getId()));

        this.redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, Duration.ofDays(7));

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .user(userResponse)
                .build();
    }

    @Override
    public ResendOtpResponse resendOtp(ResendOtpRequest resendOtpRequest) throws MessagingException, IOException, TemplateException {
        String infoKey = String.format("register-info:%s", resendOtpRequest.getEmail());
        String otpKey = String.format("register-otp:%s", resendOtpRequest.getEmail());

        RegisterRequest redisRegisterRequest = (RegisterRequest) this.redisTemplate.opsForValue().get(infoKey);

        if (redisRegisterRequest == null) {
            throw new AppException(ErrorCode.REGISTER_SESSION_EXPIRED);
        }

        int otpNum = 100000 + this.secureRandom.nextInt(900000);
        String otpCode = String.valueOf(otpNum);

        long otpExpireAt = Instant.now().getEpochSecond() + 300;

        this.redisTemplate.opsForValue().set(infoKey, redisRegisterRequest, Duration.ofMinutes(15));
        this.redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofMinutes(5));

        this.emailService.sendOtpEmail(resendOtpRequest.getEmail(), otpCode);

        return ResendOtpResponse.builder()
                .message("OTP has been send to email")
                .otpExpireAt(otpExpireAt)
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException, IOException, TemplateException {
        String email = resetPasswordRequest.getEmail();

        User user = this.userRepository.findByEmail(email).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        String resetPasswordTimeKey = "reset-password-time:" + email;

        Integer resetPasswordTime = (Integer) this.redisTemplate.opsForValue().get(resetPasswordTimeKey);

        if (resetPasswordTime != null && resetPasswordTime >= 2) {
            throw new AppException(ErrorCode.RESET_PASSWORD_LIMITED);
        }

        String newRawPassword = UUID.randomUUID().toString().substring(0, 10);

        String newPasswordHash = this.passwordEncoder.encode(newRawPassword);

        user.setPassword(newPasswordHash);
        this.userRepository.save(user);

        if (resetPasswordTime == null) {
            this.redisTemplate.opsForValue().set(resetPasswordTimeKey, 1, Duration.ofHours(1));
        } else {
            this.redisTemplate.opsForValue().increment(resetPasswordTimeKey);
        }

        this.emailService.sendResetPasswordEmail(email, newRawPassword);
        return ResetPasswordResponse.builder()
                .message("Reset password successfully")
                .build();
    }

    @Override
    public RenewAccessTokenResponse renewAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Claims refreshTokenClaims = this.jwtService.extractRefreshTokenClaims(refreshToken);

        String userId = refreshTokenClaims.getSubject();

        String refreshTokenKey = String.format("refresh-token:%s", userId);

        String cachedRefreshToken = (String) this.redisTemplate.opsForValue().get(refreshTokenKey);

        if (!refreshToken.equals(cachedRefreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = this.userRepository.findById(Long.parseLong(userId)).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }

        String newAccessToken = this.jwtService.generateAccessToken(user);

        return RenewAccessTokenResponse.builder()
                .newAccessToken(newAccessToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        try {
            Claims refreshTokenCLaims = this.jwtService.extractRefreshTokenClaims(refreshToken);

            String userId = refreshTokenCLaims.getSubject();

            String refreshTokenKey = String.format("refresh-token:%s", userId);

            this.redisTemplate.delete(refreshTokenKey);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failure exception", ex);
        } catch (Exception ex) {
            log.warn("Warning in logout function of authService", ex);
        }
    }
}
