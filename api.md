# API 문서

## 개요

학습지 관리 시스템의 REST API 문서입니다. 모든 API는 JWT 토큰 기반 인증이 필요합니다.

## 인증

### 헤더 설정
```http
Authorization: Bearer {token}
Content-Type: application/json
```

### 권한 구분
- **TEACHER**: 선생님 권한
- **STUDENT**: 학생 권한

---

## 인증 API

### 로그인
사용자 인증 후 JWT 토큰을 발급받습니다.

```http
POST /auth/login
Content-Type: application/json
```

**요청 본문**
```json
{
  "username": "teacher1",
  "password": "password123"
}
```

**응답 (200 OK)**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9ZXhwIjoxNzU1NzAxODg2fQ...",
  "userId": 1,
  "username": "teacher1",
  "name": "김선생",
  "userType": "TEACHER"
}
```

---

## 문제 관리 API

### 문제 검색
조건에 맞는 문제들을 검색합니다.

```http
GET /problems?totalCount={count}&unitCodeList={codes}&level={level}&problemType={type}
Authorization: Bearer {token}
```

**권한**: TEACHER

**쿼리 매개변수**
- `totalCount`: 요청할 문제 개수 (최소 1개)
- `unitCodeList`: 단원 코드 목록
- `level`: 문제 난이도 (LOW, MEDIUM, HIGH)
- `problemType`: 주관식, 객관식, 전체 (ALL, SUBJECTIVE, SELECTION)

**요청 예시**
```http
GET /problems?totalCount=5&unitCodeList=uc1523,uc1521&level=MEDIUM&problemType=SUBJECTIVE
Authorization: Bearer {token}
```

**응답 (200 OK)**
```json
{
  "problemList": [
    {
      "id": 1401,
      "answer": "2",
      "level": 3,
      "unitCode": "uc1536",
      "problemType": "SUBJECTIVE"
    },
    {
      "id": 1402,
      "answer": "2",
      "level": 3,
      "unitCode": "uc1536",
      "problemType": "SUBJECTIVE"
    },
    {
      "id": 1403,
      "answer": "3",
      "level": 3,
      "unitCode": "uc1536",
      "problemType": "SUBJECTIVE"
    }
  ]
}
```

---

## 학습지 관리 API

### 1. 학습지 생성
선택한 문제들로 새로운 학습지를 생성합니다.

```http
POST /piece
Authorization: Bearer {token}
Content-Type: application/json
```

**권한**: TEACHER

**요청 본문**
```json
{
  "title": "123",
  "problemIds": [
    "1007",
    "1008",
    "1362"
  ]
}
```

**응답 (200 OK)**
```json
{
  "pieceId": 1
}
```
---

### 2. 문제 순서 변경
학습지 내 문제들의 출제 순서를 변경합니다.

```http
PATCH /piece/{pieceId}/order
Authorization: Bearer {token}
Content-Type: application/json
```

**권한**: TEACHER

**경로 매개변수**
- `pieceId`: 학습지 ID (예: 1)

**요청 본문**
```json
{
  "problemOrders": [
    {
      "problemId": 1007,
      "order" : 1
    },
    {
      "problemId": 1008,
      "order" : 2
    },
    {
      "problemId": 1362,
      "order" : 3
    }
  ]
}
```

**응답 (200 OK)**
```json
{
  "pieceId": 1,
  "problemOrders": [
    {
      "problemId": 1007,
      "order": 1
    },
    {
      "problemId": 1008,
      "order": 2
    },
    {
      "problemId": 1362,
      "order": 3
    }
  ]
}
```

---

### 3. 학습지 출제
선택한 학생들에게 학습지를 출제합니다.

```http
POST /piece/{pieceId}
Authorization: Bearer {token}
Content-Type: application/json
```

**권한**: TEACHER

**경로 매개변수**
- `pieceId`: 출제할 학습지 ID

**요청 본문**
```json
{
  "studentIds": [10, 11, 12, 13]
}
```

**응답 (200 OK)**
```json
{
  "studentIds": [10, 11, 12, 13]
}
```

---

### 4. 학습지 문제 조회
학생이 출제받은 학습지의 문제들을 조회합니다.

```http
GET /piece/{pieceId}/problems
Authorization: Bearer {token}
```

**권한**: STUDENT

**경로 매개변수**
- `pieceId`: 조회할 학습지 ID

**응답 (200 OK)**
```json
[
  {
    "id": 1362,
    "level": 2,
    "unit": "uc1534",
    "problemType": "SUBJECTIVE"
  },
  {
    "id": 1007,
    "level": 2,
    "unit": "uc1581",
    "problemType": "SELECTION"
  },
  {
    "id": 1008,
    "level": 2,
    "unit": "uc1581",
    "problemType": "SELECTION"
  }
]
```

---

### 5. 학습지 채점
학생이 제출한 답안을 채점하고 점수를 계산합니다.

```http
PUT /piece/{pieceId}/score
Authorization: Bearer {token}
Content-Type: application/json
```

**권한**: STUDENT

**경로 매개변수**
- `pieceId`: 채점할 학습지 ID

**요청 본문**
```json
{
  "answers": [
    {
      "problemId" : "1362",
      "answer": "2"
    },
    {
      "problemId" : "1007",
      "answer": "3"
    },
    {
      "problemId" : "1008",
      "answer": "2"
    }
  ]
}
```

**응답 (200 OK)**
```json
{
  "score": 1
}
```

---

### 6. 학습지 통계 분석
출제된 학습지의 전체 통계를 조회합니다.

```http
GET /piece/{pieceId}/analyze
Authorization: Bearer {token}
```

**권한**: TEACHER

**경로 매개변수**
- `pieceId`: 분석할 학습지 ID

**응답 (200 OK)**
```json
{
  "pieceId": 1,
  "title": "수학 1",
  "totalAssignments": 3,
  "completedAssignments": 2,
  "averageScore": 49.99999999999999,
  "studentStatistics": [
    {
      "studentId": 3,
      "status": "COMPLETED",
      "score": 2,
      "correctRate": 66.66666666666666
    },
    {
      "studentId": 4,
      "status": "COMPLETED",
      "score": 1,
      "correctRate": 33.33333333333333
    },
    {
      "studentId": 5,
      "status": "ASSIGNED",
      "score": 0,
      "correctRate": 0.0
    }
  ],
  "problemStatistics": [
    {
      "problemId": 1362,
      "totalAttempts": 2,
      "correctAnswers": 2,
      "correctRate": 100.0
    },
    {
      "problemId": 1007,
      "totalAttempts": 2,
      "correctAnswers": 1,
      "correctRate": 0.0
    },
    {
      "problemId": 1008,
      "totalAttempts": 2,
      "correctAnswers": 0,
      "correctRate": 0.0
    }
  ]
}
```
---

## ❌ 공통 에러 응답

### 에러 응답 형식
```json
{
  "errorCode": "PIECE_002", 
  "message": "학습지를 찾을 수 없습니다.",
  "detail": {}
}
```

### 인증 관련 에러 (AUTH)
| 에러 코드 | 메시지 | 발생 상황 |
|-----------|--------|-----------|
| `AUTH_001` | 유효하지 않은 토큰입니다. | JWT 토큰이 만료되었거나 잘못된 경우 |
| `AUTH_002` | 아이디 또는 비밀번호가 올바르지 않습니다. | 로그인 정보가 틀린 경우 |

###  학습지 관련 에러 (PIECE)
| 에러 코드 | 메시지 | 발생 상황 |
|-----------|--------|-----------|
| `PIECE_001` | 존재하지 않는 문제 id가 있습니다. | 학습지 생성 시 잘못된 문제 ID 포함 |
| `PIECE_002` | 학습지를 찾을 수 없습니다. | 존재하지 않는 학습지 조회/수정 시도 |
| `PIECE_003` | 학습지에 등록된 문제 id와 순서 수정 요청한 문제 id가 일치하지 않습니다. | 문제 순서 변경 시 잘못된 문제 ID |
| `PIECE_004` | 문제 순서를 확인해 주세요 | 중복되거나 잘못된 순서 값 |
| `PIECE_005` | 해당 학습지 출제를 찾을 수 없습니다. | 출제되지 않은 학습지 접근 시도 |
| `PIECE_006` | 이미 완료된 학습지입니다. | 완료된 학습지 재채점 시도 |
| `PIECE_007` | 제출된 답변과 문제가 일치하지 않습니다. | 채점 시 문제-답변 불일치 |
| `PIECE_008` | 해당 학습지에 대한 접근 권한이 없습니다. | 다른 선생님의 학습지 접근 시도 |

### 사용자 관련 에러 (USER)
| 에러 코드 | 메시지 | 발생 상황 |
|-----------|--------|-----------|
| `USER_001` | 존재하지 않는 사용자가 있습니다. | 출제 시 잘못된 학생 ID 포함 |
| `USER_002` | 사용자를 찾을 수 없습니다. | 존재하지 않는 사용자 정보 조회 |


