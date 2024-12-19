CREATE TABLE delete_queue (
      id BIGSERIAL PRIMARY KEY,
      survey_id VARCHAR(255) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE delete_queue IS '응답자 정보를 저장하는 테이블';
COMMENT ON COLUMN delete_queue.id IS 'PK - 응답자 UUID';
COMMENT ON COLUMN delete_queue.name IS '응답자 이름';
COMMENT ON COLUMN delete_queue.email IS '응답자 이메일, 필수 항목';
COMMENT ON COLUMN delete_queue.phone_number IS '응답자 전화 번호';
COMMENT ON COLUMN delete_queue.provider IS '응답 인증 방법: SYSTEM, KAKAO, NAVER, GOOGLE';
COMMENT ON COLUMN delete_queue.survey_id IS '응답자가 참여한 설문 ID';
COMMENT ON COLUMN delete_queue.created_at IS '응답자 정보 생성일';
COMMENT ON COLUMN delete_queue.created_by IS '응답자 정보 생성자';