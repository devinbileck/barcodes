# Tests

These integration tests focus on UI testing. External dependencies are mocked, but none of the components of
the application under test are mocked.

The integration tests are in their own module to keep them isolated from the unit tests and are not ran along with the
unit tests - a specific maven profile must be activated that only runs the integration tests.
This is done to make a clear separation of tests.

## How to Run

```shell
./mvnw verify -pl tests -Pintegration-tests
```

### Headless

The tests can be run in headless mode, primarily for use in CI environments.

```shell
./mvnw verify -pl tests -Pintegration-tests -Dheadless
```
