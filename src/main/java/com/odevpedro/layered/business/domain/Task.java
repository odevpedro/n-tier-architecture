package com.odevpedro.layered.business.domain;

import lombok.Value;

@Value
public class Task {
    Long id;
    String title;
    boolean done;
}
