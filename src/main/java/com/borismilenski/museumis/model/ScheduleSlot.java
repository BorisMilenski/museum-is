package com.borismilenski.museumis.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record ScheduleSlot(UUID id, LocalDateTime from, LocalDateTime to, Employee employee) {
}
