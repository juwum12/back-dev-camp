package com.sparta.backdevcamp.auth.repository;

import com.sparta.backdevcamp.auth.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

}
