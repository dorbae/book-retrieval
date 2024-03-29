# 도서 검색 시스템

## 기능
* 회원가입/로그인
* 도서 검색
* 도서 상세 검색
* 검색 이력
* 인기 키워드

<br />

------------

## 실행

### 실행 환경
* Java 버전: 8 이상
* Web Port: 8080
* 기본 접속 경로: http://localhost:8080

<br />

### 실행 파일 다운로드
* [구글드라이브 다운로드](https://drive.google.com/open?id=1SKypZC4C-lwwrHmQX_aFYP_yewx2Z8BG)

<br />

### 실행 방법
* java -jar book-retrieval-{version}.jar

> eg. java -jar book-retreival-1.3.0.jar

<br />

----------------

## 사용 라이브러리

> Spring Boot 관련은 제외

| GROUP_ID | ARITIFACT_ID | 설명 |
| ---- | ---- | ---- |
| org.thymeleaf.extras | thymeleaf-extras-java8time | Thymeleaf LocalDatetime 호환 라이브러리 |
| org.apache.commons | commons-lang3 | 문자열 처리 라이브러리 |
| com.squareup.okhttp | okhttp | HTTP Client 라이브러리 |
| com.h2database | h2 | In-Memory DB로 사용 |
| org.projectlombok  | lombok | Entity 객체 코드 간소화 |

<br />

--------

## 수정이력

| 버전 | 수정일시 | 설명 |
| :----: | :----: | :---- |
| 1.3.0 | 2019.11.17 19:19 | 네이버 Failover 개선 |
| 1.2.5 | 2019.11.17 19:11 | 네이버 페이징 오류 수정 |
| 1.2.4 | 2019.11.17 14:51 | ISBN 2개일 경우, 세부검색에서 발생하던 오류 수정 |
| 1.2.3 | 2019.11.17 14:16 | 검색이력 날짜 포맷 수정 |
| 1.2.2 | 2019.11.17 14:03 | 테스트 케이스 추가 |
| 1.2.1 | 2019.11.17 10:26 | ObjectMapper Bean 적용 |
| 1.2.0 | 2019.11.17 06:26 | 책 검색 API 서비스 Failover 구성 |
| 1.1.0 | 2019.11.17 04:34 | 인기검색어 검색 횟수 배타락 적용 |
| 1.0.4 | 2019.11.17 03:20 | 회원가입 ID 중복 에러 화면 수정 |
| 1.0.3 | 2019.11.17 02:44 | HTML 파일 불필요 코드 정리 |
| 1.0.2 | 2019.11.17 02:22 | 도서 페이징 개수, 순서 오류 수정 |
| 1.0.1 | 2019.11.17 01:28 | 도서 페이징 포맷 오류 수정 |
| 1.0.0 | 2019.11.16 23:06 | 기본 기능 구현 완료 |
| 0.9.1 | 2019.11.16 21:33 | 책 출판일 적용 |
| 0.9.0 | 2019.11.16 17:49 | 도서 검색 기능 구현 |
| 0.8.1 | 2019.11.16 11:35 | 도서 검색 기능 테스트 |
| 0.8.0 | 2019.11.15 16:53 | 최근 검색어 화면 구현 |
| 0.7.3 | 2019.11.15 16:47 | 검색 href 오류 수 |
| 0.7.2 | 2019.11.15 16:22 | Member Serialize 오류 수정 |
| 0.7.1 | 2019.11.15 15:23 | 계정 중복 오류 수정 |
| 0.7.0 | 2019.11.15 12:05 | 인기 검색어 화면 구현. 메뉴 개선 |
| 0.6.3 | 2019.11.15 05:38 | 빌드 후, URI 문제 수정 |
| 0.6.2 | 2019.11.15 05:21 | 로그인, 회원가입 화면 개선 |
| 0.6.1 | 2019.11.15 04:21 | DB 생성, 수정일시 컬럼 문제 수정 |
| 0.6.0 | 2019.11.14 18:25 | 백엔드 기본 기능 구현 완료 |
| 0.5.2 | 2019.11.14 15:39 | 로그인, 가입 기능 수정. 암호화 변경 |
| 0.5.1 | 2019.11.14 03:23 | 백엔드 기본 기능 구현 및 정리 |
| 0.5.0 | 2019.11.13 18:43 | 로그인 화면 구현 |
| 0.4.0 | 2019.11.13.13:10 | 키워드 검색 기능 구현 |
| 0.3.0 | 2019.11.13 12:09 | 회원 가입 기능 구현 |
| 0.2.0 | 2019.11.12 20:57 | 책 검색 이력 서버 기능 구현 |
| 0.1.0 | 2019.11.12 15:14 | 사용자 관련 서버 기능 구현 |
| 0.0.0 | 2019.11.11 22:37 | 최초 생성 |

<br />

--------------

## 사용 화면

* 메인(로그인 이전)

![메인](/img/screenshot001.png)

<br />

* 회원가입

![회원가입](/img/screenshot002.png)

<br />

* 로그인

![로그인](/img/screenshot003.png)

<br />

* 로그인 후 메
![로그인메인](/img/screenshot004.png)

<br />

* 도서 검색

![도서검색](/img/screenshot005.png)

<br />

* 검색 도서 리스트

![도서리스트](/img/screenshot006.png)

<br />

* 도서 상세 정보

![도서상세](/img/screenshot007.png)

<br />

* 인기 검색어

![인기검색어](/img/screenshot008.png)

<br />

* 검색 이력

![검색 이력](/img/screenshot009.png)


