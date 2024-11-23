-- admin 테이블 생성
CREATE TABLE ADMIN
(
    ID          VARCHAR(40)  PRIMARY KEY,     -- UUID를 기본값으로 설정
    NAME        VARCHAR(20)  NOT NULL,        -- 이름 필드, 고유해야 함
    EMAIL       VARCHAR(50)  NOT NULL UNIQUE, -- 이메일 필드, 고유해야 함
    PASSWORD    VARCHAR(80)  NOT NULL,        -- 인코딩된 비밀번호
    ROLE        VARCHAR(20)  NOT NULL,        -- 권한 정보
    PROVIDER    VARCHAR(20)  NOT NULL,        -- 제공자 정보
    CREATED_AT  TIMESTAMP    NOT NULL,        -- 생성 날짜, 기본값은 현재 시간
    CREATED_BY  VARCHAR(40)  NOT NULL,        -- 만든 사람의 UUID
    MODIFIED_AT TIMESTAMP    NOT NULL,        -- 수정 날짜, 기본값은 현재 시간
    MODIFIED_BY VARCHAR(40)  NOT NULL         -- 수정한 사람의 UUID
);

COMMENT ON TABLE ADMIN IS '관리자 정보를 저장하는 테이블';
COMMENT ON COLUMN ADMIN.ID IS 'PK - 관리자 UUID';
COMMENT ON COLUMN ADMIN.NAME IS '관리자 이름';
COMMENT ON COLUMN ADMIN.EMAIL IS '관리자 이메일, 필수 항목';
COMMENT ON COLUMN ADMIN.PASSWORD IS '관리자 비밀 번호 encoding';
COMMENT ON COLUMN ADMIN.ROLE IS '관리자 권한 정보';
COMMENT ON COLUMN ADMIN.PROVIDER IS '관리자 인증 방법: SYSTEM, KAKAO, NAVER, GOOGLE';
COMMENT ON COLUMN ADMIN.created_at IS '관리자 정보 생성일';
COMMENT ON COLUMN ADMIN.created_by IS '관리자 정보 생성자';
COMMENT ON COLUMN ADMIN.MODIFIED_AT IS '관리자 정보 생성자';
COMMENT ON COLUMN ADMIN.MODIFIED_BY IS '관리자 정보 생성자';


CREATE TABLE ADMIN_HISTORIES
(
    ID                VARCHAR(40)  NOT NULL,
    ADMIN_ID          VARCHAR(40)  NOT NULL,
    ADMIN_ROLE        VARCHAR(40)  NOT NULL,
    REQ_IP            VARCHAR(50)  NOT NULL,
    REQ_METHOD        VARCHAR(20)  NOT NULL,
    REQ_URL           VARCHAR(255) NOT NULL,
    REQ_HEADER        TEXT         NOT NULL,
    REQ_PAYLOAD       TEXT         NOT NULL,
    CREATED_AT        TIMESTAMP    NOT NULL,
    CREATED_BY        VARCHAR(40)  NOT NULL,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE ADMIN_HISTORIES IS '관리자 이력 정보를 저장하는 테이블';
COMMENT ON COLUMN ADMIN_HISTORIES.ID is '관리자 이력 ID';
COMMENT ON COLUMN ADMIN_HISTORIES.ADMIN_ID is '관리자 ID';
COMMENT ON COLUMN ADMIN_HISTORIES.ADMIN_ROLE is '관리자 역할';
COMMENT ON COLUMN ADMIN_HISTORIES.REQ_IP is '요청 IP';
COMMENT ON COLUMN ADMIN_HISTORIES.REQ_METHOD is '요청 메소드';
COMMENT ON COLUMN ADMIN_HISTORIES.REQ_URL is '요청 URL';
COMMENT ON COLUMN ADMIN_HISTORIES.REQ_HEADER is '요청 헤더';
COMMENT ON COLUMN ADMIN_HISTORIES.REQ_PAYLOAD is '요청 바디';
COMMENT ON COLUMN ADMIN_HISTORIES.CREATED_AT is '생성일자';
COMMENT ON COLUMN ADMIN_HISTORIES.CREATED_BY is '생성자';