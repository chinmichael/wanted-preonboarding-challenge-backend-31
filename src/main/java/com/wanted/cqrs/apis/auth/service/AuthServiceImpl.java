package com.wanted.cqrs.apis.auth.service;

import com.wanted.cqrs.apis.auth.domain.User;
import com.wanted.cqrs.apis.auth.domain.dto.UserWithRoleDto;
import com.wanted.cqrs.apis.auth.repository.AuthRepository;
import com.wanted.cqrs.common.auth.Roles;
import com.wanted.cqrs.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("AuthService")
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final JwtUtils jwtUtils;

    @Override
    public String login(String email, String password) throws UsernameNotFoundException {
        UserWithRoleDto user = this.searchUserWithRoleByEmail(email);
        if(user.getUser() == null) throw new UsernameNotFoundException("가입되지 않은 이메일입니다.");

        return jwtUtils.createAccessJwt(user.getUser().getEmail(), new String[]{user.getRole().name()});
    }

    @Override
    public User searchUserByEmail(String email) {
        return authRepository.findByEmail(email).orElseGet(null);
    }

    @Override
    public UserWithRoleDto searchUserWithRoleByEmail(String email) {
        User user = this.searchUserByEmail(email);
        Roles role = user == null ? null
                : user.getId() < 6 ? Roles.SELLER : Roles.USER;

        return new UserWithRoleDto(user, role);
    }
}
