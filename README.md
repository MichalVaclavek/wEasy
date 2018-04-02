## wEasy (Wicket Easy) Enhancement
This is a fork of **wEasy** https://github.com/vitfo/wEasy - a simple CMS (Content Management System) written in Java using the [Apache Wicket framework!](http://wicket.apache.org/) with some enhancements.

## Getting Started

The wEasy project's original purpose is to allow practising on Apache Wicket development creating simple CMS web application (see https://github.com/vitfo/wEasy for original source).
The application can be used for creating and managing simple web pages then. See http://fungisoft.cz web pages as an example of the web created by this application.


Following are enhancements added to the original project:

 Backend:
  - DAO level changed from JDBC to JPA Hibernate with some new DB entities and methods
  - user authentication added
  - new DB tables added
  - Service level added
  
 Frontend (Wicket):
  - Contact message form added
  - list of Categories added
  - Registration form extension (editing of user data allowed)
  - logged-in user label added
  
 Unit tests for testing work with DB.


Further possible enhancements:

- https support on Login page
- UI for deleting selected Article, Category and Directory


Basic steps to make app. runnig: 

- import like git project from GitHub into your IDE.
- create DB and configure DB connection.
- run on local IDE Tomcat server or using Maven build ... goal jetty:run

see Installing paragraph for details.

### Prerequisites

Java 8 installed, Maven, created PostgreSQL DB, Tomcat 8.5 installed within IDE.

### Installing

1] Cloning project from GitHub to your local Git repository:

Open Git Repositories View. Window -> Show View -> Other -> Git -> Git Repositories
In that new View click on "Clone a Git repository", fill in URI: https://github.com/MichalVaclavek/wEasy and click "Next". If not selected, select "master" branch, and click "Next". Select directory where the local repository will be located/cloned to, click "Finish". 

2] Importing project from local Git repository to Eclipse:
Go to File -> Import -> Maven -> Existing Maven Project -> Next, select Directory where you cloned the project (step 1). Click "Finish" and your project is imported into Eclipse and ready to run.

After importing/creating project from GitHub, a DB must be created. If the PostgreSQL DB is used, create DB named "weasy" and create tables using /scripts/postgres_script.sql file included in the source code.

Then go to src/main/resources/, open hibernate.cfg.xml and edit your local (or remote) PostgreSQL DB connection here.

Then edit src/main/resources/log4j.properties file to configure app. loging environment, especially  then name and location of the log file.
  
After that, project should be ready to run either on Eclipse installed Tomcat 8.0 or 8.5 server or using Maven build ... goal jetty:run


## Running the tests

In Eclipse Project Explorer find src/test/java/cz.zutrasoft.mainweb.database.AllObjectsTests.java then select Run as JUnit Test on this class.

Separately you can also run TestCreateAdminUser.java in the same package as JUnit test to create a user with ADMIN role - you can choose username and password in the test's source code. The coding password can be also edited. 

## Deployment

Export project into war file and deploy using standard Tomcat manager admin page.
 
You can find http://fungisoft.cz web pages as an example of the deployed application. Application was deployed on cloud VPS server with Java 8, Tomcat 8.5 and PostgreSQL 10 installed.

## Built With

* [Eclipse] - IDE
* [Maven](https://maven.apache.org/) - Dependency Management
* [Apache Wicket](http://wicket.apache.org/)
* [Hibernate]
* [PostgreSQL]

## Authors

* **Michal Václavek** - Hibernate DB access, Unit tests for testing work with DB. Wicket UI: Contact message form, list of Categories
* **vitfo** - *Initial work* - Frontend. [wEasy](https://github.com/vitfo/wEasy)  

