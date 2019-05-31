# [springboot-multimodule] 프로젝트 설명문서

## 1. 프로젝트 생성 목적
스프링 부트 2.1.5 / maven 을 활용하여 멀티모듈 프로젝트를 만드는 방법을 스터디하기 위해 만든 것입니다.

## 2. 왜 멀티모듈 프로젝트를 만들어보려 한 것인가?
한 시스템 내에 여러가지 미들웨어를 작동시키는 곳(대표적인 예로 MSA로 구축된 경우)가 많습니다.

심지어 각 미들웨어를 같은 언어로 구현을 하였을 경우엔 필연적으로 각 미들웨어들 사이에 코드 중복이 발생합니다.

이런 코드 중복 상황을 합리적으로 관리하지 않는다면, 일종의 기술 부채로 이어지기 때문에, 시간이 지날 수록 시스템 유지운영 시 엄청난 관리력 낭비로 이어질 것입니다.


저는 이런 중복된 코드를 공통모듈로 분리한 후, 각 미들웨어에선 공통기능이 필요할 경우에 한하여 공통모듈을 참조하는 방식을 스터디해 봄으로써, 추후에 같은 상황 발생시 보다 능숙한 업무경험으로 활용하고자 합니다.

## 3. 프로젝트에 도입한 언어 및 의존성 관리모듈
* 언어 : 자바 11
* 의존성 관리 모듈 : Mavern

## 4. 프로젝트에 도입한 (또는 도입예정인) 기술 스택 및 현재 사용중인 모듈
* Spring WebMVC
    * auth-api
* Spring Security
    * auth-api
* Spring Data JPA
    * auth-api
* Swagger
    * auth-api
* Spring WebFlux (도입예정)


## 5. Special Thanks to: 
LINK : [Github/Jwt-Spring-Security-JPA](https://github.com/isopropylcyanide/Jwt-Spring-Security-JPA)



