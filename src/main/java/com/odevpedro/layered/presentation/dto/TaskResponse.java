package com.odevpedro.layered.presentation.dto;

public record TaskResponse(
        Long id,
        String title,
        boolean done
) {}
