# Yavin

## ToDo
- Add authentication including the additionally required services.
  https://spring.io/guides/gs/securing-web/

- Add HATEOAS support, see further https://spring.io/guides/tutorials/bookmarks/

- Create a documentation service which holds API documentation + error
  descriptions as stated in RFC-7807 *type* member. 
  https://tools.ietf.org/html/rfc7807
  https://spring.io/guides/gs/testing-restdocs/

- Extend the error handling controller for snippets.

- Different database configurations depending on test or production.
  - https://spring.io/guides/gs/accessing-data-mysql/
  - h2 database for tests
  - Don't mind production database yet
- Github Action for running checkstyle, unit tests and integration tests
  - Add integration test config in Gradle for XXXIT
  - Add Github Action for checkstyle
  - Add Github Action for unit tests
  - Add Github Action for integration tests
  - Add JaCoCo test coverage to Gradle
  - Add Github Action for SonarCloud