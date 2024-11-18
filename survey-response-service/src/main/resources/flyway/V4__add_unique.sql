-- UNIQUE 제약조건 추가
ALTER TABLE responses
ADD CONSTRAINT unique_survey_response UNIQUE (survey_id, respondent_id, question_id);