package com.wanted.cqrs.apis.auth.service;

import com.wanted.cqrs.apis.auth.domain.User;
import com.wanted.cqrs.apis.auth.domain.dto.UserWithRoleDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService {

    // TokenDto login(String email, String password);
    // TokenDto reissue(String refreshToken);

    // 테스트용 간단히 accessToken만 전달 (기간 24시간), 패스워드는 실제 안 쓰임
    String login(String username, String password) throws UsernameNotFoundException;

    User searchUserByEmail(String email);
    UserWithRoleDto searchUserWithRoleByEmail(String email);
}
