CREATE TABLE SURVEYS (
     ID VARCHAR(40) PRIMARY KEY,          -- UUID를 기본값으로 설정
     ADMIN_ID VARCHAR(40) NOT NULL,       -- admin 테이블의 id를 참조하는 외래 키
     TITLE VARCHAR(100) NOT NULL,         -- 설문 제목
     DESCRIPTION VARCHAR(255),            -- 설문 설명
     STATUS VARCHAR(50) NOT NULL,         -- 설문 상태 (예: 진행 중, 만료됨 등)
     START_AT TIMESTAMP NOT NULL,         -- 설문 시작 날짜
     END_AT TIMESTAMP NOT NULL,           -- 설문 마감 날짜
     CREATED_AT VARCHAR(255) NOT NULL,    -- 생성자 UUID
     CREATED_BY TIMESTAMP NOT NULL,       -- 생성일, 기본값은 현재 시간
     MODIFIED_AT VARCHAR(255) NOT NULL,   -- 수정자 UUID
     MODIFIED_BY TIMESTAMP NOT NULL       -- 수정일, 기본값은 현재 시간
);

COMMENT ON TABLE  SURVEYS IS '설문 정보를 저장하는 테이블';
COMMENT ON COLUMN SURVEYS.ID IS 'PK - 설문 UUID';
COMMENT ON COLUMN SURVEYS.ADMIN_ID IS '설문 생성자 아이디';
COMMENT ON COLUMN SURVEYS.TITLE IS '설문 제목';
COMMENT ON COLUMN SURVEYS.DESCRIPTION IS '설문 설명';
COMMENT ON COLUMN SURVEYS.STATUS IS '설문 상태 - 진행 중, 중단 됨, 마감 됨';
COMMENT ON COLUMN SURVEYS.START_AT IS '설문 시작 날짜';
COMMENT ON COLUMN SURVEYS.END_AT IS '설문 종료 날짜';
COMMENT ON COLUMN SURVEYS.CREATED_AT IS '설문 정보 생성일';
COMMENT ON COLUMN SURVEYS.CREATED_BY IS '설문 정보 생성자';
COMMENT ON COLUMN SURVEYS.MODIFIED_AT IS '설문 정보 수정일';
COMMENT ON COLUMN SURVEYS.MODIFIED_BY IS '설문 정보 수정자';



CREATE TABLE QUESTIONS (
    ID VARCHAR(40) PRIMARY KEY,                                         -- UUID 기본
    SURVEY_ID VARCHAR(40) REFERENCES SURVEYS(ID) ON DELETE CASCADE,     -- 설문 테이블 id를 참조하는 외래키
    TYPE VARCHAR(40) NOT NULL,                                          -- 질문 유형을 참조하는 외래키
    TITLE VARCHAR(255) NOT NULL,                                        -- 질문 내용
    "ORDER" INT NOT NULL,                                               -- 질문 순서
    COLUMNS VARCHAR(255) NOT NULL,                                      -- TABLE_SELECT에서의 컬럼형 항목
    ROWS VARCHAR(255) NOT NULL,                                         -- 항목
    CREATED_AT VARCHAR(255) NOT NULL,                                   -- 생성자 UUID
    CREATED_BY TIMESTAMP NOT NULL,                                      -- 생성일, 기본값은 현재 시간
    MODIFIED_AT VARCHAR(255) NOT NULL,                                  -- 수정자 UUID
    MODIFIED_BY TIMESTAMP NOT NULL                                      -- 수정일, 기본값은 현재 시간
);

COMMENT ON TABLE  QUESTIONS IS '질문 항목 정보를 저장하는 테이블';
COMMENT ON COLUMN QUESTIONS.ID IS 'PK - 질문 항목 UUID';
COMMENT ON COLUMN QUESTIONS.SURVEY_ID IS 'FK - 설문 아이디';
COMMENT ON COLUMN QUESTIONS.TITLE IS '질문 제목';
COMMENT ON COLUMN QUESTIONS."ORDER" IS '질문 순서';
COMMENT ON COLUMN QUESTIONS.COLUMNS IS 'TABLE_SELECT에 쓰일 컬럼 데이터';
COMMENT ON COLUMN QUESTIONS.ROWS IS '질문 내용 - 선택 항목';
COMMENT ON COLUMN QUESTIONS.CREATED_AT IS '질문 정보 생성일';
COMMENT ON COLUMN QUESTIONS.CREATED_BY IS '질문 정보 생성자';
COMMENT ON COLUMN QUESTIONS.MODIFIED_AT IS '질문 정보 수정일';
COMMENT ON COLUMN QUESTIONS.MODIFIED_BY IS '질문 정보 수정자';