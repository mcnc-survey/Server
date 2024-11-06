-- 이미 존재하는 테이블을 삭제
DROP TABLE IF EXISTS admin CASCADE;
DROP TABLE IF EXISTS surveys CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS question_types CASCADE;
DROP TABLE IF EXISTS responses CASCADE;

-- admin 테이블 생성
CREATE TABLE ADMIN (
    ID VARCHAR(40) PRIMARY KEY,                    -- UUID를 기본값으로 설정
    EMAIL VARCHAR(25) NOT NULL UNIQUE,             -- 이메일 필드, 고유해야 함
    PASSWORD VARCHAR(100) NOT NULL,                -- 인코딩된 비밀번호
    ROLE VARCHAR(10) NOT NULL,                     -- 권한 정보
    CREATED_AT TIMESTAMP DEFAULT now(),            -- 생성 날짜, 기본값은 현재 시간
    CREATED_BY VARCHAR(255) NOT NULL,              -- 만든 사람의 UUID
    MODIFIED_AT TIMESTAMP DEFAULT now(),           -- 수정 날짜, 기본값은 현재 시간
    MODIFIED_BY VARCHAR(255)                       -- 수정한 사람의 UUID
);
-- surveys 테이블 생성
CREATE TABLE SURVEYS (
    ID VARCHAR(40) PRIMARY KEY,                           -- UUID를 기본값으로 설정
    ADMIN_ID VARCHAR(40) REFERENCES admin(ID),            -- admin 테이블의 id를 참조하는 외래 키
    TITLE VARCHAR(100) NOT NULL,                          -- 설문 제목
    DESCRIPTION VARCHAR(255),                             -- 설문 설명
    STATUS VARCHAR(50) NOT NULL,                          -- 설문 상태 (예: 진행 중, 만료됨 등)
    END_AT TIMESTAMP,                                     -- 설문 마감 날짜
    CREATED_AT VARCHAR(255) NOT NULL,                     -- 생성자 UUID
    CREATED_BY TIMESTAMP DEFAULT now(),                   -- 생성일, 기본값은 현재 시간
    MODIFIED_AT VARCHAR(255),                             -- 수정자 UUID
    MODIFIED_BY TIMESTAMP DEFAULT now()                   -- 수정일, 기본값은 현재 시간
);

CREATE TABLE QUESTION_TYPES (
    ID VARCHAR(40) PRIMARY KEY,     -- UUID 기본값
    TYPE VARCHAR(255) NOT NULL      -- 질문 유형 (예: 객관식, 서술형, 표형)
);

CREATE TABLE QUESTIONS (
    ID VARCHAR(40) PRIMARY KEY,                                                     -- UUID 기본값
    SURVEY_ID VARCHAR(40) REFERENCES surveys(ID) ON DELETE CASCADE,                 -- 설문 테이블 id를 참조하는 외래키
    QUESTION_TYPE_ID VARCHAR(40) REFERENCES question_types(ID) ON DELETE CASCADE,   -- 질문 유형을 참조하는 외래키
    QUESTION_TITLE VARCHAR(255) NOT NULL,                                           -- 질문 내용
    "ORDER" INT NOT NULL,                                                           -- 질문 순서
    QUESTION JSONB NOT NULL                                                         -- 선택 항목
);

CREATE TABLE RESPONSES (
    ID VARCHAR(40) PRIMARY KEY,                    -- uuid
    SURVEY_ID VARCHAR(40) NOT NULL REFERENCES surveys(ID) ON DELETE CASCADE,    -- 설문 테이블 id를 참조하는 외래키
    CREATOR_ID VARCHAR(255) NOT NULL,                                           -- 응답자 email
    CREATED_AT TIMESTAMP DEFAULT now(),                                         -- 응답 시간
    UNIQUE (SURVEY_ID, CREATOR_ID)                                              -- 하나의 설문에 하나의 응답자
);

CREATE TABLE ANSWERS (
    ID VARCHAR(40) PRIMARY KEY,                                                 -- uuid
    RESPONSE_ID VARCHAR(40) REFERENCES responses(ID) ON DELETE CASCADE,         -- 응답 테이블 id를 참조하는 외래키
    QUESTION_ID VARCHAR(40) REFERENCES questions(ID) ON DELETE CASCADE,         -- 설문 테이블 id를 참조하는 외래키
    QUEST_TYPE_ID VARCHAR(40) REFERENCES question_types(ID) ON DELETE CASCADE,  -- 설문 유형 id를 참조하는 외래키
    "ORDER" INT NOT NULL,                                                       -- 설문 순서와 매칭되는 응답 순서
    ANSWER JSONB NOT NULL                                                       -- 설문에 대한 응답 값
);