-- 이미 존재하는 테이블을 삭제
DROP TABLE IF EXISTS admin CASCADE;
DROP TABLE IF EXISTS surveys CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS question_types CASCADE;
DROP TABLE IF EXISTS responses CASCADE;

-- admin 테이블 생성
CREATE TABLE admin (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- UUID를 기본값으로 설정
    email VARCHAR(255) NOT NULL UNIQUE,            -- 이메일 필드, 고유해야 함
    password VARCHAR(255) NOT NULL,                -- 인코딩된 비밀번호
    role VARCHAR(50) NOT NULL,                     -- 권한 정보
    created_at TIMESTAMP DEFAULT now(),            -- 생성 날짜, 기본값은 현재 시간
    creator_id VARCHAR(255) NOT NULL,              -- 만든 사람의 UUID
    updated_at TIMESTAMP DEFAULT now(),            -- 수정 날짜, 기본값은 현재 시간
    updated_id VARCHAR(255)                        -- 수정한 사람의 UUID
);
-- surveys 테이블 생성
CREATE TABLE surveys (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),        -- UUID를 기본값으로 설정
    admin_id UUID REFERENCES admin(id),                   -- admin 테이블의 id를 참조하는 외래 키
    title VARCHAR(255) NOT NULL,                          -- 설문 제목
    description VARCHAR(255),                             -- 설문 설명
    status VARCHAR(50) NOT NULL,                          -- 설문 상태 (예: 진행 중, 만료됨 등)
    end_at TIMESTAMP,                                     -- 설문 마감 날짜
    creator_id VARCHAR(255) NOT NULL,                     -- 생성자 UUID
    created_at TIMESTAMP DEFAULT now(),                   -- 생성일, 기본값은 현재 시간
    updated_at TIMESTAMP DEFAULT now(),                   -- 수정일, 기본값은 현재 시간
    updator_id VARCHAR(255)                               -- 수정자 UUID
);

CREATE TABLE question_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),       -- UUID 기본값
    type VARCHAR(255) NOT NULL                           -- 질문 유형 (예: 객관식, 서술형, 표형)
);

CREATE TABLE questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                          -- UUID 기본
    survey_id UUID REFERENCES surveys(id) ON DELETE CASCADE,                -- 설문 테이블 id를 참조하는 외래키
    question_type_id UUID REFERENCES question_types(id) ON DELETE CASCADE,  -- 질문 유형을 참조하는 외래키
    question_title VARCHAR(255) NOT NULL,                                   -- 질문 내용
    "order" INT NOT NULL,                                                   -- 질문 순서
    question JSONB NOT NULL                                                 -- 선택 항목
);

CREATE TABLE responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                    -- uuid
    survey_id UUID NOT NULL REFERENCES surveys(id) ON DELETE CASCADE, -- 설문 테이블 id를 참조하는 외래키
    creator_id VARCHAR(255) NOT NULL,                                 -- 응답자 email
    created_at TIMESTAMP DEFAULT now(),                               -- 응답 시간
    UNIQUE (survey_id, creator_id)                                    -- 하나의 설문에 하나의 응답자
);

CREATE TABLE answers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                      -- uuid
    response_id UUID REFERENCES responses(id) ON DELETE CASCADE,        -- 응답 테이블 id를 참조하는 외래키
    question_id UUID REFERENCES questions(id) ON DELETE CASCADE,        -- 설문 테이블 id를 참조하는 외래키
    quest_type_id UUID REFERENCES question_types(id) ON DELETE CASCADE, -- 설문 유형 id를 참조하는 외래키
    "order" INT NOT NULL,                                               -- 설문 순서와 매칭되는 응답 순서
    answer JSONB NOT NULL                                               -- 설문에 대한 응답 값
);