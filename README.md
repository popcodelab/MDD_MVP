# MDD
## Table of contents

- Description and goals
- Installation
- Database setup
- Run application
- Run tests
- Technologies
- Compodoc
- Screenshots

## Description and goal

MDD (Monde de Dév) is a social network project dedicated to developers:
Project developed as part of the development of a project for my master's degree.
 This repository contains both the frontend and backend code for a Minimum Viable Product version.

## Installation

### Clone the Project

Clone these repositories :
> git clone https://github.com/popcodelab/MDD_MVP

### Install Dependencies

#### Back-End:

> mvn clean install

#### Front-End:

> npm install

### Configuring the Back-End

Open the `application.properties` file located in the `back/src/main/resources` directory to Replace the properties with your parameters:

```properties
spring.datasource.url=${APP_DB_URL}
spring.datasource.username=${APP_DB_USERNAME}
spring.datasource.password=${APP_DB_PASSWORD}
```

## Database setup 

Make sure that you have MySQL installed on your system.

1. Log into MySQL using the following command :
> mysql -u `<username>` -p

2. Create the database :

> CREATE DATABASE `<database_name>`;

3. Select the created database :
> USE `<database_name>`;


### Build the database

Use the SQL script located in `ressources\sql\creates_db.sql`  to create the schema :

> SOURCE `<path_to_script.sql>`;
     



## Run application

1. Frontend
   
   - In your terminal, run the command below.
    
        ```bash
        cd front
        npm run start
        ```

     The frontend will launch in your browser at `http://localhost:4400`
  
2. Backend

     - In a separate terminal, run the command below.

          ```bash
          cd back
          mvn spring-boot:run
          ```

        The backend server will launch at `http://localhost:3005`

---

## Technologies
Front-end :  
![Static Badge](https://img.shields.io/badge/Angular-14.2.0-red)


Back-end :  
![Static Badge](https://img.shields.io/badge/Java-17.0.10-orange)
![Static Badge](https://img.shields.io/badge/Spring_Boot-2.6.1-green)
![Static Badge](https://img.shields.io/badge/Maven-4.0.0-purple)

<br>
<hr>

 <div align="center">

 [![forthebadge](https://forthebadge.com/images/badges/build-with-spring-boot.svg)](https://forthebadge.com)
 [![forthebadge](https://forthebadge.com/images/badges/uses-git.svg)](https://forthebadge.com)
 [![forthebadge](https://forthebadge.com/images/badges/made-with-typescript.svg)](https://forthebadge.com)
 [![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
</div>
<hr/>