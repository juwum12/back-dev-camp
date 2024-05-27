package com.sparta.backdevcamp.auth.service;

public interface TokenBlackListService {
    /**
     * 로그아웃 요청시 두 토큰 값을 블랙리스트에 저장
     * @param accessToken
     * @param refreshToken
     */
    public void addBlackList(String accessToken, String refreshToken);

    /**
     * 요청으로 들어온 Token ID가 블랙리스트에 저장되어 있는지 확인.
     * @param jti JWT ID
     * @return
     */
    public boolean isTokenBlackListed(String jti);

    /**
     * 현재 Date 기준으로 만료된 토큰들을 블랙리스트에서 삭제
     */
    public void removeExpiredToken();
}
