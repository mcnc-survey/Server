CREATE TABLE delete_queue (
      id BIGSERIAL PRIMARY KEY,
      survey_id VARCHAR(255) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE delete_queue IS '삭제된 설문의 id 저장하는 테이블';
COMMENT ON COLUMN delete_queue.id IS 'PK - id';
COMMENT ON COLUMN delete_queue.survey_id IS '삭제된 survey_id';
COMMENT ON COLUMN delete_queue.created_at IS '생성일';
