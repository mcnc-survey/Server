ALTER TABLE admin ADD COLUMN phone_number VARCHAR(40);
COMMENT ON COLUMN admin.phone_number IS '관리자 이름';
