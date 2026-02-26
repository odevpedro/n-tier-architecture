package com.odevpedro.layered.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank String title
) {}
