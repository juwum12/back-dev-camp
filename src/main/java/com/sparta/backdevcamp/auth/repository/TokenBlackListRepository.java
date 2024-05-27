package com.sparta.backdevcamp.auth.repository;

import com.sparta.backdevcamp.auth.entity.TokenBlackList;
import org.antlr.v4.runtime.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {
    Optional<TokenBlackList> findByJti(String jti);
    List<TokenBlackList> findAllByExpiresAtLessThan(Date now);
}
