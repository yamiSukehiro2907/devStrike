package com.example.backend.dtos.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDetailsResponse {

    private String name;

    private String profilePathUrl;
}
