**Server application for Gift Certificates system with Certificate and Tag entities.**

**1 step**

System exposes REST API:
- CRD operations for Tag
- CRUD operations for Certificate, search by part of name/description and by tag

Database implemented in docker (postgres and pgAdmin).

Custom Connection Pool.

Testing with JUnit & Mockito

Repository is tested in embedded database.

Multi-module project

JSON includes HATEOAS links

There are no:
- Sprig Boot
- JPA/Hibernate
- Lombok

**2 step**

Maven changed to Gradle

Spring Boot was added

JPA and auditing was added (without Spring Data)

Spring Security with OAuth2 and JWT was added

Database has been filled

Database stores hashes

HTTPS protocol added.

Logger fixed.

Postman Collection with APIs added to 'data' folder.
