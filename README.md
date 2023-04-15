
# Chirpchat

This is the stand-alone website realized for the software security project implemented in java throughout Spring Boot framework.


## Build and Deploy the Project

Clone the project

```bash
  git clone https://github.com/Femure/software_security_project
```

Go to the project directory

```bash
  cd software_security_project
```

### Deployment without IDE

Install Apache Maven  :
https://maven.apache.org/download.cgi

Installation step : https://maven.apache.org/install.html

Once Apache Maven successfully installed, build the project  


```bash
  mvn verify
```

Start the server

```bash
  mvn spring-boot:run
```

### Deployment with IDE

You can also use any other IDE you prefer such as Visual Studio Code, IntelliJ...

External ressource for deployement with VSCode :
- https://code.visualstudio.com/docs/java/java-spring-boot

For example with VSCode it's is to lauch the app thanks to Sping Boot Dashboard you can run  **Frontend** and **Backend** at the same time, as shown below : 

![](https://github.com/Femure/software_security_project/blob/rename-repo/lauchSpringBootVSCode.gif)

Once deployed, you can access the app at :

http://localhost:8090
## Set up MySQL
By default, the project is configured to use the MySQL database, so  you need to create the db as shown below:

### Use Local MySQL Docker

Create the container

```bash
  docker run --name=mysql_chirpchat -e MYSQL_ROOT_PASSWORD=abc123 -e MYSQL_DATABASE=chirpchat -p 3307:3306 -d mysql
```

> :warning: Important you need to run the MySQL database before starting the application, otherwise the application couldn't lauch it correctly.

