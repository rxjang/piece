-- users 테이블 생성
CREATE TABLE IF NOT EXISTS users (
       id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(100) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       name VARCHAR(100) NOT NULL,
       user_type ENUM('TEACHER', 'STUDENT') NOT NULL,
       status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- unit_code 테이블 생성
CREATE TABLE IF NOT EXISTS unit_code (
    id INT AUTO_INCREMENT PRIMARY KEY,
    unit_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- problem 테이블 생성
CREATE TABLE IF NOT EXISTS problem (
   id INT AUTO_INCREMENT PRIMARY KEY,
   unit_code VARCHAR(20) NOT NULL,
    level INT NOT NULL,
    problem_type VARCHAR(20) NOT NULL,
    answer VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- piece 테이블 생성
CREATE TABLE IF NOT EXISTS piece (
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL,
   title VARCHAR(100) NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- piece_problem 테이블 생성
CREATE TABLE IF NOT EXISTS piece_problem (
   piece_id INT NOT NULL,
   problem_id INT NOT NULL,
   problem_order INT NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_piece_problem PRIMARY KEY (piece_id, problem_id)
);

-- piece_assignment 테이블 생성
CREATE TABLE IF NOT EXISTS piece_assignment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    piece_id INT NOT NULL,
    student_id INT NOT NULL,
    status ENUM('ASSIGNED', 'COMPLETED') DEFAULT 'ASSIGNED',
    score INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- problem_scoring 테이블 생성
CREATE TABLE IF NOT EXISTS problem_scoring (
    piece_assignment_id INT NOT NULL,
    problem_id INT NOT NULL,
    student_answer VARCHAR(1000) NOT NULL,
    correct_answer VARCHAR(1000) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    score INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_problem_scoring PRIMARY KEY (piece_assignment_id, problem_id)
);

-- 학습지 통계 조회 성능 최적화를 위한 인덱스
-- problem_scoring 테이블을 piece_assignment_id로 필터링할 때 IN 절과 LEFT JOIN 조건에서 Full Table Scan을 방지
CREATE INDEX idx_problem_scoring_assignment_id
    ON problem_scoring(piece_assignment_id);
