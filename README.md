# Crypto Trading Sim


# INSTRUCTIONS:

1) Clone this repository
2) Run database.sql my mySQL Workbench (may need to run each CREATE TABLE statement separately)
3) Create application.properties file at "src/main/java/resources" with the following text:
    spring.application.name=demo
    spring.datasource.url=jdbc:mysql://localhost:3306/crypto_trading_sim?useSSL=false&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.initialization-mode=always

5) Load all Maven dependencies from pom.xml
6) Manually add the gson dependency
7) Run ApplicationDemo's main method
