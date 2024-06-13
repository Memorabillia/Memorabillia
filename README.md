
# Memorabilia
**Increasing Reader Engagement via Progress Tracking and Smart Personalized Recommendations**

Capstone Team ID: C241-PS182

---

## Table of Contents
1. [Introduction](#introduction)
2. [Tools](#tools)
3. [Cloud Architecture](#cloud-architecture)
4. [Entity Relationship Diagram](#entity-relationship-diagram)
5. [List of Endpoints](#list-of-endpoints)

---

## Introduction
Memorabilia is a project aimed at enhancing reader engagement by leveraging progress tracking and personalized recommendations. Our platform integrates various cloud services and tools to provide a seamless and efficient user experience.

---

## Tools
The tools we use for this project include:

- [Google Cloud Platform](https://console.cloud.google.com)
- [Visual Studio Code](https://code.visualstudio.com)
- [Flask](https://flask.palletsprojects.com/en/3.0.x/)
- [Gunicorn](https://gunicorn.org/)
- [Docker](https://www.docker.com)
- [Firebase](https://firebase.google.com)
- [Node.js](https://nodejs.org/en)
- [Postman](https://www.postman.com)

---

## Cloud Architecture
<p align="center">
  <img src="documentation/Cloud Architecture.png" alt="Cloud Architecture" width="700" height="auto" />
</p>

Our cloud architecture is designed to be scalable and robust, utilizing Google Cloud Platform services to host and manage our applications efficiently.

---

## Entity Relationship Diagram
<p align="center">
  <img src="documentation/Entity Relationship Diagram.png" alt="Entity Relationship Diagram" width="700" height="auto" />
</p>

The Entity Relationship Diagram (ERD) provides a visual representation of the database structure, showing the relationships between different entities within the system.

---

## List of Endpoints

| Method | Endpoint            | Description                                |
|--------|---------------------|--------------------------------------------|
| POST   | /register           | Register a new user                        |
| POST   | /login              | Log in a user                              |
| POST   | /logout             | Log out the current user                   |
| POST   | /recommend          | Get personalized book recommendations      |
| GET    | /books              | Retrieve a list of all books               |
| GET    | /books/:id          | Retrieve details of a specific book by ID  |
| GET    | /users              | Retrieve a list of all users               |
| GET    | /user/books         | Retrieve books associated with the user    |
| PUT    | /books/status       | Update the reading status of a book        |



---

## Contact
For any questions or feedback, please reach out to us at [thatmemorabilia@gmail.com](mailto:thatmemorabilia@gmail.com).
