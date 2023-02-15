# 똑딱

## 똑딱 의미

'똑' 닮았다. <br>'딱' 닮았다.

## 개요

친구들끼리 사람 혹은 사물을 보며 서로 자기가 더 닮았다고 설전을 펼치신 적 있으신가요?<br>
더 이상 그럴 필요 없습니다. '똑딱'이 4가지 이미지 분석 알고리즘을 이용해 누가 더 닮았는지 알려드리겠습니다.

## 주요 기능

- webRTC를 이용한 실시간 화상 게임
- 사용자가 직접 커스터마이징 가능한 문제
- 게임 과정에서 찍힌 사진을 확인, 공유 가능
- 게임 기록 보관, 확인

---

## 개발 환경

| Category        | Tech stack         | Version                    | Docker                |     |
| --------------- | ------------------ | -------------------------- | --------------------- | --- |
| Version Control | GitLab             |                            |                       |     |
|                 | Jira               |                            |                       |     |
| Documentation   | notion             |                            |                       |     |
| Front-End       | HTML5              |                            |                       |     |
|                 | CSS3               |                            |                       |     |
|                 | JavaScript(ES6)    |                            |                       |     |
|                 | vue/cli            | 5.0.8                      |                       |     |
|                 | Vue.js (Vue3)      | 3.2.45                     |                       |     |
|                 | node.js            | 14.19.0                    |                       |     |
|                 | Visual Studio Code | 1.74.2                     |                       |     |
| Back-End        | Java               | OpenJDK Azul zulu 11.60.19 | official docker image |     |
|                 | gradle             | 7.6                        | official docker image |     |
|                 | SpringBoot         | 2.7.7                      |                       |     |
|                 | Intellij           | 2022.3                     |                       |     |
| DB              | MySQL              | 8.0.31                     | official docker image |     |
| Server          | AWS EC2            |                            |                       |     |
|                 | AWS S3             |                            |                       |     |
|                 | Nginx              |                            | official docker image |     |
|                 | Ubuntu             | 22.04.1 LTS                | official docker image |     |
|                 | Openvidu           | 2.25.0                     | official docker image |     |
| CI/CD           | Docker             |                            |                       |     |
|                 | Jenkins            |                            |                       |     |
|                 | Ansible            |                            |                       |     |

---

## 서비스 아키텍쳐

![서비스 아키텍처](ReadMe_contents\Architecture.jpg)

## Jenkins를 이용한 CD 구축

Jenkins를 이용하여 빌드하고 전달, ansible playbook, dockerCompose를 이용하여 docker container로 배포하였습니다. <br>
letsencrypt를 이용하여 ssl 인증서를 적용하였고, 프론트엔드는 443(https)로 프록시로 분기, 백엔드는 /api 경로로 프록시를 걸어줬습니다.

---

## 이미지 분석 알고리즘

---

## 협업툴

- Git
- Jira
- Notion
- Mattermost
- Webex
