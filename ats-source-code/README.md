# Applicant Tracking System (ATS)

This is a Java-based web application designed for deployment on a WildFly server.

## Features
- **Job Management**: Create, view, and manage job openings.
- **Candidate Management**: Track candidate information and resumes.
- **Application Pipeline**: Manage the recruitment process (Applied -> Interview -> Offer -> Hired/Rejected).
- **Dashboard**: Overview of recruitment metrics.

## Tech Stack
- **Backend**: Jakarta EE (JAX-RS, JPA, CDI)
- **Frontend**: HTML5, CSS3 (Tailwind CSS via CDN), and Vanilla JavaScript
- **Build Tool**: Maven
- **Deployment**: WAR file for WildFly

## Project Structure
- `src/main/java`: Backend source code
- `src/main/resources`: Configuration files (persistence.xml)
- `src/main/webapp`: Frontend assets and web.xml
- `pom.xml`: Maven configuration
