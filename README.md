📦src  
 ┣ 📂main  
 ┃ ┣ 📂java  
 ┃ ┃ ┗ <span style="font-weight:bold">📂 org</span>  
 ┃ ┃ ┃ ┗ <span style="font-weight:bold">📂 duckdns</span>  
 ┃ ┃ ┃ ┃ ┗ <span style="font-weight:bold">📂 bitwatchu</span>  
 ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:red">📂 app</span>       - 도메인별로 패키징된 패키지  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ <span style="color:orange">📂 user</span>       - 사용자 도메인  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller     - 요청을 받아 처리하는 컨트롤러(@RestController) 클래스가 위치  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain         - 도메인 모델 클래스가 위치  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto            - 관련 DTO   
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception      - 관련 예외 클래스  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository     - 데이터 접근을 담당하는 리포지토리 인터페이스  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service        - 비즈니스 로직을 처리하는 서비스 클래스  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ <span style="color:orange">📂 다른도메인 . . .</span>  
 ┃ ┃ ┃ ┃ ┃ ┃  
 ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:red">📂 global</span>   - 애플리케이션 전역에서 사용되는 공통 기능을 담당하는 패키지  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:orange">📂 auth</span>       - 인증 관련 기능을 담당하는 패키지  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂oauth  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:orange">📂 common</span> - 공통으로 사용되는 유틸리티  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:orange">📂 config</span> - 애플리케이션 설정 관련  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 security  
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂 swagger  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:orange">📂 error</span> - 에러 처리 관련  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ <span style="color:orange">📂 util</span> - 유틸리티 클래스  
 ┃ ┃ ┃ ┃ ┃ ┃  
 ┃ ┃ ┃ ┃ ┃ ┣ <span style="color:red">📂 infra</span> - 인프라 관련  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂 email  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜BitwatchuApplication.java - 애플리케이션의 실행 클래스  
 ┃ ┃  
 ┃ ┗ 📂resources - 애플리케이션 리소스 파일이 위치  
 ┃ ┃ ┣ 📂static - 정적 리소스 파일이 위치  
 ┃ ┃ ┣ 📂templates - 템플릿 파일이 위치  
 ┃ ┃ ┣ 📜application-dev.yml - 개발 환경 설정 파일  
 ┃ ┃ ┣ 📜application-test.yml - 테스트 환경 설정 파일  
 ┃ ┃ ┣ 📜application.yml - 기본 애플리케이션 설정 파일  
 ┃ ┃ ┗ 📜secret.yml - 민감한 정보를 담고 있는 설정 파일  
 ┃  
 ┗ 📂test - 테스트 코드가 위치하는 디렉토리  
 ┃ ┗ 📂java  
 ┃ ┃ ┗ 📂org  
 ┃ ┃ ┃ ┗ 📂duckdns  
 ┃ ┃ ┃ ┃ ┗ 📂bitwatchu  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜BitwatchuApplicationTests.java - 애플리케이션 테스트 클래스  
