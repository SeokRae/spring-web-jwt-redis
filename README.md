# 스프링 Web

## Intro

## 요구사항

- 스프링 웹 기반 사용자 인증 및 인가 처리
- Json Web Token
- Redis

## 구현 사항

|FUNCTION|METHOD|URL|Authentication|Authorization|
|:---:|:---:|:---|:---:|:---:|
|**사용자 로그인**|POST|`/login`|O|X|
|**사용자 등록**|POST|`/users`|O|X|
|**사용자 수정**|PUT|`/users/{memberId}`|X|O|
|**사용자 삭제**|DELETE|`/users/{memberId}`|X|O|

## JWT 토큰 로직

> **인증 로직**

- 로그인
    - AccessToken 발급
    - RefreshToken 발급

> **인가 로직**

- 토큰 유효성 검사
    - 토큰 부재시 예외(NullPointerException)
    - 토큰의 구조적인 예외(SignatureException, MalformedJwtException)
    - 토큰 유효시간 예외(ExpiredJwtException)
