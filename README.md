[![JUnit Tests](https://github.com/kyhol/sd11-final-sprint-be/actions/workflows/ci.yml/badge.svg)](https://github.com/kyhol/sd11-final-sprint-be/actions/workflows/ci.yml)

# Cineflex
## Final Sprint Project for Software Design, Architecture, and Testing + DevOps

For this sprint, our team developed and deployed Cineflex, a full-stack movie theatre web application.
The backend API was built using Java with Spring Boot, and the frontend was developed using React. 
A SQL database was used to manage all application data.
The entire project was containerized with Docker, thoroughly tested using a combination of unit and manual testing, and deployed to AWS using EC2.

Feel free to read the assignment [here](https://github.com/user-attachments/files/19738321/SDATSprint.pdf).

Our application is currently not congigured to run locally, but feel free to read through our code and check out the deployed project [here](http://sd11-final-sprint-fe.s3-website-us-east-1.amazonaws.com/).

## Meet the Team

[Brad Ayers](https://www.linkedin.com/in/bradley-ayers/)
[Kyle Hollett](https://www.linkedin.com/in/kyle-hollett-8558842a8/)
[Brian Janes](https://www.linkedin.com/in/briandjanes/): 
[Michael O'Brien](https://www.linkedin.com/in/michael-obrien05/)
[Adam Sparkes](https://www.linkedin.com/in/adam-sparkes-software/)

## 🔍 Project Summary
This project provides RESTful APIs for a movie ticket booking application, allowing users to:

### 🎬 Movies & Showtimes
Browse all movies and view detailed info (GET /api/movies)

Search by title or filter by genre/upcoming (/search, /genre/{genre}, /upcoming)

View showtimes for a movie (GET /api/movies/{movieId}/showtimes)

Create, update, or delete movie entries (POST, PUT, DELETE endpoints)

### 🎟️ Bookings
Book seats for a movie showtime (POST /api/bookings)

Sends showTimeId and list of seatIds

Returns booking confirmation, ticket number, and booked seat IDs

Retrieve a booking by ID (GET /api/bookings/{id})
