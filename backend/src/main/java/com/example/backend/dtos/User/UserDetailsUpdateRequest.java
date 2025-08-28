package com.example.backend.dtos.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsUpdateRequest {

    private String name;

    private String profilePathUrl;
}
