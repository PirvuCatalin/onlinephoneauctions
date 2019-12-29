# OnlinePhoneAuctions

This project is made for the 2019-2020 Database Course in the 3rd year of Faculty of Automatic Control and Computer Science, Politehnica University of Bucharest.
<b>It is a simple online auction house only for mobile phones.</b>

It uses basic concepts of Spring Security, Relational Databases, an adapted MVC pattern (this adaptation is mainly used as we we're not allowed to use Persistence, but plain SQL syntax) and working with JSP and JS for the frontend development.

The javadoc will be re-generated under the /javadoc folder when new updates are brought to this project (if any).

Also, a new deploy version will be added under /deploy.

If you want to run this on your local machine, you will need at least Java 8 (JRE):

```
1. Clone this repo locally
2. Open a CMD (Win) or Terminal (Linux / MacOS)
3. Navigate to folder "deploy" under this repository
4. Run "java -jar SpringBoot-OnlinePhoneAuctions-0.0.1-SNAPSHOT.jar"
5. Server should start running in under 5 seconds under https://localhost:8080 
and the database can be found at https://localhost:8080/h2
```

You can also generate your own <b>jar</b> file using maven. Just run the maven goal `mvn clean package` and you're good to go, just follow the steps 4-5 above.

Some users that can also be found in data.sql file (username / password):
```
catalin / pirvu
carina / stoica
stefan / anca
admin / nimda (ADMIN role)
sa / password (DBA role)
```
