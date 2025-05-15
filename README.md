# SPRING PLUS
## 🚀 EC2
### ⚙️ EC2 환경설정
![Image](https://github.com/user-attachments/assets/3fe79b2c-6108-4828-8f83-c989f4b744f4)

### 📋 health check API
- **Method**: GET
- **URL**: `http://3.38.114.122:8080/health`
- **Response**:
    - 200 OK + "OK"
- **Authentication**: ❌ 없음 (누구나 접근 가능)

![Image](https://github.com/user-attachments/assets/c9f00bdd-e14d-460a-bb1a-495d7838543f)

## 🚀 RDS
![Image](https://github.com/user-attachments/assets/b2f56216-475b-4c11-9725-de3fb52bad59)

## 🚀 S3
![Image](https://github.com/user-attachments/assets/78668de2-8a04-4f05-8044-248a5c7e7130)

## 🎯 대용량 데이터 처리
### ☑️ 기존 조회 결과
![Image](https://github.com/user-attachments/assets/db75c577-5b19-4dda-b501-40bf9a88329c)
![Image](https://github.com/user-attachments/assets/7d068436-6b13-47dd-af01-c4486a155dbb)

### ✅ 인덱스 적용 후 조회 결과
![Image](https://github.com/user-attachments/assets/fa50578c-0637-40eb-b5ae-97cfdc36e9b8)
![Image](https://github.com/user-attachments/assets/8404aaf2-0134-49e5-8696-adfb7c9f3230)

### 💡 개선 효과

| 항목 | 기존 성능      | 인덱싱 후 성능 | 개선 효과 |
| --- |------------|----------| --- |
| RPS | 약 25       | 약 36     | **+44% 향상** |
| 평균 응답 시간 | 최대 1,400ms | 약 10ms   | **극적으로 향상** |
| 최대 응답 시간 | 최대 1,900ms | 약 20ms   |  |