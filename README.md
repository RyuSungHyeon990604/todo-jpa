## ERD
![image](https://github.com/user-attachments/assets/6c4a4b5d-f37e-4cf2-9517-d824edb255e0)

## DDL
```sql
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `deleted_at` DATETIME(6) DEFAULT NULL,
  `last_logout_time` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) 

CREATE TABLE `todo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `task` VARCHAR(255) NOT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `deleted_at` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_todo_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) 

CREATE TABLE `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT DEFAULT NULL,
  `todo_id` BIGINT DEFAULT NULL,
  `user_id` BIGINT DEFAULT NULL,
  `comment` VARCHAR(255) NOT NULL,
  `level` INT NOT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `deleted_at` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_comment_todo` FOREIGN KEY (`todo_id`) REFERENCES `todo` (`id`),
  CONSTRAINT `FK_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) 
) 

CREATE TABLE `user_refresh_token` (
  `user_id` BIGINT NOT NULL,
  `refresh_token` VARCHAR(255) DEFAULT NULL UNIQUE,
  `created_at` DATETIME(6) DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `deleted_at` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FK_refresh_token_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) 
```
## 📌 프로젝트 소개
이 프로젝트는 JWT 기반 인증/인가를 적용한 **일정 관리 REST API**입니다.
사용자는 회원가입 후 로그인하여 자신의 일정 리스트를 관리할 수 있으며,
각 일정에 댓글을 추가하거나 삭제할 수 있습니다.

---

## 📌 API 기능 개요

- **Post man**: [https://documenter.getpostman.com/view/40450969/2sAYX5Jgu5](https://documenter.getpostman.com/view/40450969/2sAYXBGzUY)

### **1. 인증/인가 (Auth)**
| 메서드 | URL | 설명 |
|--------|----------------|----------------|
| POST | `/auth/login` | 로그인 후 Access/Refresh Token 발급 |
| POST | `/auth/logout` | 로그아웃 처리 |
| POST | `/auth/reissue` | Access Token 재발급 |

### **2. 사용자 (User)**
| 메서드 | URL | 설명 |
|--------|----------------|----------------|
| GET | `/users` | 사용자 목록 조회 |
| GET | `/users/{id}` | 특정 사용자 조회 |
| POST | `/users` | 사용자 등록 |
| PATCH | `/users/{id}` | 사용자 정보 수정 |
| POST | `/users/{id}` | 사용자 삭제  |

### **3. TODO 관리 (Todo)**
| 메서드 | URL | 설명 |
|--------|----------------|----------------|
| GET | `/todos` | TODO 목록 조회 |
| GET | `/todos/{id}` | 특정 TODO 조회 |
| POST | `/todos` | 새로운 TODO 생성 |
| PATCH | `/todos/{id}` | TODO 수정 |
| DELETE | `/todos/{id}` | TODO 삭제 |

### **4. 댓글 관리 (Comment)**
| 메서드 | URL | 설명 |
|--------|----------------|----------------|
| GET | `/comments/{todoId}` | 특정 TODO에 대한 댓글 조회 |
| POST | `/comments/{todoId}` | 댓글 추가 |
| DELETE | `/comments/{id}` | 댓글 삭제 |

---






