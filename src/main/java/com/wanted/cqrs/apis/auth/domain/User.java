package com.wanted.cqrs.apis.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("User")
public class User {
    private long id;
    private String name;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
