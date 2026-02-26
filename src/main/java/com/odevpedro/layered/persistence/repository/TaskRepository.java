package com.odevpedro.layered.persistence.repository;

import com.odevpedro.layered.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
