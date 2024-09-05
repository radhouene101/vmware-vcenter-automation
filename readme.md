## to reuse this project please make sure to follow these steps
1. Clone the repository
2. make sure you are connected on same network with a vcenter server
3. inside /src/globalVars/GlobalVars.java file change the following variables:
    - public static String username="vSphere_username";
    -  public static String password="vSphere_password";
    - public static String serverIP="vSphere_serverIP";
4. inside application.properties file change the following variables:
    - spring.datasource.url=jdbc:mysql://mysql_serverIP:3306/db_name
    - spring.datasource.username=db_username
    - spring.datasource.password=db_password
    - spring.mail.username=email_address
    - spring.mail.password=email_password
    - note : email is configured to use gmail smtp server if you wish to use another email server change configurations inside application.properties file
5. run the project
6. check your database for the created tables and data being backed up , through it use grafana or your custom dashboard to see current state of your vcenter server
7. some crons are running to send emails and backup data every day at 7am and 5pm
8. navigate to https http://localhost:8081/UI-Access and insert client name or vm tags to generate pdf reports
# put a star if you like the project :)