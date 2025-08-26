package com.example.backend.entities;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

@RedisHash("refreshToken")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String token;

    @Indexed
    private String userEmail;

    private Date expiration;

}
