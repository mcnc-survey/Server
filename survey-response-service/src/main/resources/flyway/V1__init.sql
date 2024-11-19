DROP TABLE IF EXISTS respondents CASCADE;

CREATE TABLE respondents (
     id VARCHAR(40) PRIMARY KEY,                        -- PK - UUID
     name VARCHAR(25),                                  -- 이름
     email VARCHAR(40) NOT NULL,                        -- 이메일 (필수)
     phone_number VARCHAR(20),                          -- 전화 번호
     provider VARCHAR(40) NOT NULL DEFAULT 'SYSTEM',    -- 인증 방법, SYSTEM, KAKAO, NAVER, GOOGLE
     survey_id VARCHAR(40) NOT NULL,                    -- 설문 ID, 어떤 설문에 대한 것인지
     created_at TIMESTAMP NOT NULL,                     -- 생성일
     created_by VARCHAR(40) NOT NULL                    -- 생성자
);

-- COMMENT 문으로 respondents 테이블에 주석 추가
COMMENT ON TABLE respondents IS '응답자 정보를 저장하는 테이블';
COMMENT ON COLUMN respondents.id IS 'PK - 응답자 UUID';
COMMENT ON COLUMN respondents.name IS '응답자 이름';
COMMENT ON COLUMN respondents.email IS '응답자 이메일, 필수 항목';
COMMENT ON COLUMN respondents.phone_number IS '응답자 전화 번호';
COMMENT ON COLUMN respondents.provider IS '응답 인증 방법: SYSTEM, KAKAO, NAVER, GOOGLE';
COMMENT ON COLUMN respondents.survey_id IS '응답자가 참여한 설문 ID';
COMMENT ON COLUMN respondents.created_at IS '응답자 정보 생성일';
COMMENT ON COLUMN respondents.created_by IS '응답자 정보 생성자';


-- UNIQUE 제약 조건에 대한 설명을 인덱스를 통해 추가
CREATE UNIQUE INDEX respondents_email_survey_id_idx ON respondents(email, survey_id);
COMMENT ON INDEX respondents_email_survey_id_idx IS '응답자 테이블에서 이메일과 설문 ID의 고유성을 보장';

DROP TABLE IF EXISTS answers CASCADE;
CREATE TABLE answers (
     id VARCHAR(40) PRIMARY KEY,            -- PK - UUID
     survey_id VARCHAR(40) NOT NULL,        -- FK - surveys.id
     respondent_id VARCHAR(40) NOT NULL,    -- FK - respondents.id
     question_id VARCHAR(40) NOT NULL,      -- FK - questions.id
     question_type VARCHAR(20) NOT NULL,    -- 질문 타입, SINGLE_CHOICE, MULTIPLE_CHOICE, SHORT_ANSWER, TABLE_SELECT
     order_number INT NOT NULL,             -- 질문 순서
     answer VARCHAR(2000) NOT NULL,         -- 답변
     created_at TIMESTAMP NOT NULL,         -- 생성일
     created_by VARCHAR(40) NOT NULL,       -- 생성자
     modified_at TIMESTAMP NOT NULL,        -- 수정일
     modified_by VARCHAR(40) NOT NULL,      -- 수정자
     FOREIGN KEY (respondent_id) REFERENCES respondents(id)
);

-- COMMENT 문으로 각 열에 주석 추가
COMMENT ON COLUMN answers.id IS 'PK - UUID';
COMMENT ON COLUMN answers.survey_id IS 'FK - 설문 ID, surveys 테이블의 id';
COMMENT ON COLUMN answers.respondent_id IS 'FK - 응답자 ID, respondents 테이블의 id';
COMMENT ON COLUMN answers.question_id IS 'FK - 질문 ID, questions 테이블의 id';
COMMENT ON COLUMN answers.question_type IS '질문 타입: SINGLE_CHOICE, MULTIPLE_CHOICE, SHORT_ANSWER, TABLE_SELECT';
COMMENT ON COLUMN answers.order_number IS '질문 순서 번호';
COMMENT ON COLUMN answers.answer IS '응답 내용, 최대 2000자';
COMMENT ON COLUMN answers.created_at IS '응답 내용 생성일';
COMMENT ON COLUMN answers.created_by IS '응답 내용 생성자';
COMMENT ON COLUMN answers.modified_at IS '응답 내용 수정일';
COMMENT ON COLUMN answers.modified_by IS '응답 내용 수정자';