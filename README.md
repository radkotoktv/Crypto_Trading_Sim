# Crypto Trading Sim


# SETUP INSTRUCTIONS:

1) Clone this repository
2) Run database.sql my mySQL Workbench (may need to run each CREATE TABLE statement separately)
3) Open your code editor (ex. IntelliJ IDEA) at the cloning path
4) Create application.properties file at "src/main/java/resources" with the following text:<br>
    spring.application.name=demo<br>
    spring.datasource.url=jdbc:mysql://localhost:3306/crypto_trading_sim?useSSL=false&serverTimezone=UTC<br>
    spring.datasource.username=root<br>
    spring.datasource.password=root<br>
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver<br>
    spring.datasource.initialization-mode=always<br>

5) Load all Maven dependencies from pom.xml
6) Manually add the gson dependency (example for IntelliJ IDEA)
   6.1) RMB on project name
   6.2) "Open Module Settings"
   6.3) Libraries -> New Project Library -> From Maven...
   6.4) Search for gson and install the latest available version (ex. com.google.code.gson:gson:2.13.1)
   6.5) Apply changes
8) Run ApplicationDemo's main method

# USAGE INSTRUCTIONS:

1) Go to http://localhost:8080/signup.html
2) Create an account
3) Log in
4) On the dashboard, you can see the top 20 cryptocurrencies by price, updated in real time
5) You can select the BUY/SELL options to respectively buy or sell a certain cryptocurrency in the top 20 list
6) You can view your own current holdings of different cryptocurrencies as well as your transaction history
