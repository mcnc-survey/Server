CREATE TABLE respondents (
     id VARCHAR(40) PRIMARY KEY,        -- PK - UUID
     name VARCHAR(25),                  -- 이름
     email VARCHAR(40) NOT NULL,        -- 이메일 (필수)
     phone_number VARCHAR(20),          -- 전화 번호
     provider VARCHAR(40) NOT NULL,     -- 인증 방법, SYSTEM, KAKAO, NAVER, GOOGLE
     survey_id VARCHAR(40) NOT NULL,    -- 설문 ID, 어떤 설문에 대한 것인지
     created_at TIMESTAMP NOT NULL,     -- 생성일
     created_by VARCHAR(40) NOT NULL    -- 생성자
); -- 응답자에 대한 정보 수정은 일어나지 않아야함 - 인증 한 번만 하면 끝나고 부가 서비스가 존재하지 않음

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