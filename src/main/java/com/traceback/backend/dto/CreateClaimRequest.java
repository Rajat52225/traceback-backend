package com.traceback.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClaimRequest {

    @NotBlank(message = "Message is required")
    private String message;
}