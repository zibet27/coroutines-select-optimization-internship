# Single Consumer Multiple Producer Queue - Kotlin Multiplatform

This project implements a **Single Consumer Multiple Producer Queue** using Kotlin Multiplatform and . It allows multiple producers to concurrently enqueue items while only one consumer dequeues them. It is an example of classic Michael-Scott queue.

## Features

- **Concurrency**: Supports multiple producers with a single consumer.
- **Multiplatform**: Compatible with JVM, Native, and JavaScript targets.
- **Atomic Operations**: Uses atomic operations for thread-safe enqueuing and dequeuing.

### Prerequisites

To build and run the project, you need:

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Gradle](https://gradle.org/)

### Project Structure

The project has the following modules:

- `commonMain`: Contains platform-independent code.
- `commonTest`: Contains platform-independent tests.
- `jvmTest`: Contains Lincheck tests.
- `jsTest`: Contains simple non-parallel test. Just checks if it runs on JS platform.
- `nativeTest`: Contains very simple manual tests using kotlinx.coroutines.

### Running Tests

To run tests, use the following commands:

- ```./gradlew jvmTest```
- ```./gradlew nativeTest```
- ```./gradlew jsTest```
