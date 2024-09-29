# <img height="30" src="https://github.com/Quad8/quad8-front/assets/110798291/a6ed5540-77a8-4cba-962d-49d143756499"> 커스텀 키보드 구매 사이트 [Keydeuk](https://keydeuk.com/)


<div align="center">
  <img width="900" src="https://github.com/Quad8/quad8-front/assets/110798291/cf8a90c4-4cfa-4416-a8e2-5719f7a4ffc7">
</div>
<br><br>

<table>
  <tbody>
    <tr>
      <td>개발 기간</td>
      <td>2024.05.18 ~ 2024.07.30</td>
    </tr>
    <tr>
      <td>URL</td>
      <td>https://keydeuk.com</td>
    </tr>
  </tbody>
</table>

<br>

## 목차
>1. [keydeuk 소개](#keydeuk-소개)
>2. [협업 관련](#협업-관련)
>3. [기술 스택](#기술-스택)
>4. [아키텍처](#아키텍처)
>5. [데이터베이스 모델링](#데이터베이스-모델링)
>6. [API 명세](#api-명세)
>7. [구현 기능](#구현-기능)
>8. [트러블 슈팅](#트러블-슈팅)
>9. [이슈 관리](#이슈-관리)
>10. [팀원 소개](#팀원-소개-및-역할)

<br/>

## [keydeuk 소개]
키득은 '키보드 득템'의 줄임말로 다양한 키보드 제품 뿐만 아니라 키캡, 스위치 등 다양한 부품을 구매할 수 있는 커머스 사이트입니다.
'커머스 키보드에 대해 잘 알지 못하는 사람들도 쉽게 접근 가능한 사이트를 만들면 어떨까?'라는 생각에서 시작되어, 기획부터 디자인, 개발까지 모두 저희 quad-8 팀이 함께 힘을 합쳐 만든 프로젝트입니다.
초보자들에 대한 진입 장벽을 낮추고, 커스텀 키보드에 대한 흥미를 가질 수 있도록 Three.js를 통해 사용자가 직접 3D 커스터마이징이 가능하도록 개발하여 키득만의 특성을 살렸습니다.

현실적으로 모든 기능을 실제로 운영하기는 어렵지만, 저희의 목표는 그러한 부분을 제외한 기능들을 운영이 가능한 수준으로 구현하는 것이었습니다. 이를 통해 사용자들이 실제로 경험할 수 있는 커머스 환경을 제공하고, 키보드 커스터마이징의 재미와 편리함을 느낄 수 있도록 했습니다.

<br>

## [협업 관련]
### 📚 [notion](https://www.notion.so/Quad8-Codeit-Sprint-Part4-4-082738892f994e359fcdda15270959c3)

### 깃 컨벤션
<details>
  <summary><b> Git Branch: 이슈 번호를 이용하여 main, develop, feature, hotfix 로 나눠서 진행</b></summary>
  <img width="30%" alt="image" src="https://github.com/user-attachments/assets/eed6d4ba-ca9f-4753-b527-360b450a037e">
</details>

<details>
  <summary><b> Commit : Git Commit 컨벤션을 따라 작성</b></summary>

  
| Types    | 설명                                     |
|----------|------------------------------------------|
| feat     | 새로운 기능 추가                        |
| fix      | 버그 수정                               |
| docs     | 문서 수정                               |
| style    | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| refactor | 코드 리펙토링                           |
| test     | 테스트 코드, 리펙토링 테스트 코드 추가 |
| chore    | 빌드 업무 수정, 패키지 매니저 수정    |


</details>

<br/>

## [기술 스택]

**언어 및 프레임워크** : ![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Springboot-3.3.0-red)

**데이터 베이스** : ![MySQL](https://img.shields.io/badge/Mysql-8.0.1-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0.5-green)

**배포** :
![AWS EC2](https://img.shields.io/badge/AWS-EC2-orange)
![GitHub Actions](https://img.shields.io/badge/Github-Actions-black)
![S3](https://img.shields.io/badge/S3-skyblue)
![CodeDeploy](https://img.shields.io/badge/CodeDeploy-blue)


**ETC** : ![Redis](https://img.shields.io/badge/Redis-red)
![Static Badge](https://img.shields.io/badge/nginx-darkgreen)

<br/>

## [아키텍처]

<img src= "https://github.com/user-attachments/assets/53e15031-ea8d-43e2-877a-f1d38d500911" width="60%"/>

- CI/CD 파이프라인을 구축하고, AWS를 활용해 배포하였으며, DB 서버와 애플리케이션 서버를 분리해 안정성을 높임.
  
<br/>

## [데이터베이스 모델링]

<img src= "https://github.com/user-attachments/assets/25b8f80a-478e-482a-815b-5c7fa1829ace" width="60%"/>

<br/>

## [API 명세]

### 🖥 swagger 문서<br>
배포 경로: [https://keydeuk-be.shop/swagger-ui/index.html#/](https://keydeuk-be.shop/swagger-ui/index.html#/)<br>
localhost: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html#/)

<br/>

## [구현 기능]

### User

- 회원가입, 내 정보 조회/수정, 타인 정보 조회, OAuth 회원가입

### Shipping

- 유저의 배송지 조회/저장/수정/삭제

### Search

- 검색창에 입력된 검색어를 포함한 삼품 목록을 조회

### Review

- 리뷰 작성/수정/삭제, 사용자 리뷰 조회, 제품 리뷰 조회
- 리뷰 좋아요 등록/취소

### Product

- 상품 조회, 메인페이지 키득 PICK, BEST 상품 목록 조회

### Payment

- 결제 승인, 실패, 성공

### Order

- 주문 생성,삭제, 조회, 결제 정보 조회/수정

### Custom

- 커스텀 키보드 장바구니 담기, 옵션 상품 목록(랜덤)

### Coupon

- 쿠폰 생성/저장, 조회

### Community

- 커스텀키보드 구매 시 게시글 작성 가능, 게시글 수정/삭제/수정/조회
- 댓글 작성/수정/삭제/조회
- 게시글 좋아요 등록/취소

### Cart

- 장바구니 담기/삭제/조회, 커스텀 키보드 수정

### Likes
- 상품 찜 등록, 조회, 다중 삭제

### Alarm

- 커뮤니티 작성한 글의 댓글 달릴 시 알림 전송

### 최근 본 상품 목록

- 최근 본 상품(최대 8개) 조회

<br/>

## [트러블 슈팅]
<br/>

## [이슈 관리]
<details>
<summary><b>이슈 관리</b></summary>
<img width="60%" alt="image" src="https://github.com/user-attachments/assets/f5ff15b7-b0b0-4ab4-9a53-74ebcf57675d">
</details>
- 이슈 템플릿을 활용해 일관성있고 명확한 이슈를 작성하여 공유함.

<br/>

## [팀원 소개 및 역할]

### 👻김수빈 [Github](https://github.com/Su-daa)
- **역할**
    - 회원 & 마이페이지
    - 리뷰 
    - 찜(좋아요)
    - 주문
    - 결제 

### ⚽️김윤설 [Github](https://www.github.com/seoseo17)

- **역할** 
  - 상품 & 커스텀 키보드
  - 장바구니
  - 커뮤니티
  - 알림
  - CI/CD & 배포
