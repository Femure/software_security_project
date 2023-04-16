
# Chirpchat

This is the stand-alone website realized for the software security project implemented in java throughout Spring Boot framework.

## Set up MySQL
By default, the project is configured to use the MySQL database, so  you need to create the db as shown below:

### Use Local MySQL Docker

Create the container

```bash
  docker run --name=mysql_chirpchat -e MYSQL_ROOT_PASSWORD=abc123 -e MYSQL_DATABASE=chirpchat -p 3307:3306 -d mysql
```

> :warning: Important you need to run the MySQL database before starting the application, otherwise the application couldn't lauch it correctly.

## Build and Deploy the Project

Clone the project

```bash
  git clone https://github.com/Femure/software_security_project
```

### Deployment without IDE

Install Apache Maven  :
https://maven.apache.org/download.cgi

Installation step : https://maven.apache.org/install.html

Once Apache Maven successfully installed. 

You need to create two terminals to run the **Frontend** and the **Backend** at the same time.

For the **Frontend** 

```bash
  cd software_security_project/Frontend
```

Build the Frontend

```bash
  mvn verify
```
Start the Frontend app

```bash
  mvn spring-boot:run
```

For the **Backend**

```bash
  cd software_security_project/Backend
  mvn verify
  mvn spring-boot:run
```

### Deployment with IDE

You can also use any other IDE you prefer such as Visual Studio Code, IntelliJ...

External ressource for deployement with VSCode :
- https://code.visualstudio.com/docs/java/java-spring-boot

For example with VSCode, it is very easy to lauch the app. Thanks to Spring Boot Dashboard, you can run the **Frontend** and the **Backend** at the same time, as shown below : 

![](https://github.com/Femure/software_security_project/blob/rename-repo/README_illustration/lauchSpringBootVSCode.gif)

Once deployed, you can access the app at :

http://localhost:8090

## Email SMTP service

> :warning: If you have an antivirus on your computer, it is possible that when you want to use the application, the email service does not work because it is blocked by your antivirus. So, you will need to allow SMTP protocole for outbound traffic. It can be also blocked by the configuration of your network like for example by your proxy. 

For example if you have Avast Antivirus, you can disabled scan outbound emails by doing this : Open **Avast Antivirus** > **Settings** > **Protection** > **Core Shields** > Scroll down and click on **Mail Shield** > Deselect :white_square_button: **Scan outbound emails(SMTP)**.

![](https://github.com/Femure/software_security_project/blob/rename-repo/README_illustration/disabledScanSMTPAntivirus.PNG)


