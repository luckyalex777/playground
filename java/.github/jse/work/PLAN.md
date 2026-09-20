# PLAN: Java Socket Abstraction Layer

## Goal

Create a new package `com.alexswd.network.tcp` in the network module with:
1. Interface `ISocket` containing all public methods from `java.net.Socket`
2. Class `SocketBase` implementing `ISocket`
3. Both classes follow Java 25 standards and quality gates

## Current State

### Repository Structure
- Single-module Maven project: `network/`
- Location: `/home/alex/projects/playground/java/network/`
- Java version: 25 (configured in pom.xml)
- Source structure:
  - Main sources: `src/main/java/com/alexswd/network/`
  - Test sources: `src/test/java/com/alexswd/network/`
- Current packages: empty (to be populated)

### Dependencies
- SLF4J API 2.0.13
- JUnit 5.10.2 (test)
- Hamcrest 3.0 (test)
- SpotBugs annotations 4.9.7

### Quality Gates & Configuration
- Code must pass formatter
- Checkstyle checks must pass
- PMD analysis must pass
- SpotBugs must pass
- Maven verify must pass

## Proposed Design

### Package: `com.alexswd.network.tcp`

Package structure:
```
com.alexswd.network.tcp
├── ISocket.java (interface)
└── SocketBase.java (implementing class)
```

### Interface: `ISocket`
- Public interface for socket abstraction
- Includes all public methods from `java.net.Socket` (Java 25)
- Will include methods such as:
  - Connection methods: `connect()`, `bind()`
  - Getter methods: `getInetAddress()`, `getLocalAddress()`, `getPort()`, `getLocalPort()`, etc.
  - Stream methods: `getInputStream()`, `getOutputStream()`
  - Socket options: `setReuseAddress()`, `getReuseAddress()`, `setSoTimeout()`, `getSoTimeout()`, etc.
  - Utility methods: `close()`, `isClosed()`, `isConnected()`, etc.
- Comprehensive Javadoc for all methods
- Follows Java 25 best practices

### Class: `SocketBase`
- Implements `ISocket` interface
- Stub implementations for all interface methods
- Provides foundation for concrete implementations
- Could wrap or extend `java.net.Socket` in future implementations
- Proper exception handling with `throws` clauses matching interface

## Files to Create

1. **network/src/main/java/com/alexswd/network/tcp/ISocket.java**
   - Interface with all public Socket methods
   - ~150-200 lines (including Javadoc)

2. **network/src/main/java/com/alexswd/network/tcp/SocketBase.java**
   - Class implementing ISocket
   - ~100-150 lines with stub implementations

3. **network/src/test/java/com/alexswd/network/tcp/SocketBaseTest.java**
   - Basic tests for SocketBase
   - Verify instantiation and basic behavior
   - ~30-50 lines

## Implementation Steps

### Phase 1: Create Package Structure
1. Create directory: `src/main/java/com/alexswd/network/tcp/`
2. Create directory: `src/test/java/com/alexswd/network/tcp/`

### Phase 2: Implement ISocket Interface
1. Create `ISocket.java` interface
2. Add all public methods from `java.net.Socket`
3. Include proper Javadoc for each method
4. Add throws clauses for all checked exceptions

### Phase 3: Implement SocketBase Class
1. Create `SocketBase.java` class
2. Implement all methods from ISocket interface
3. Add stub implementations returning appropriate default values
4. Add proper error handling

### Phase 4: Create Tests
1. Create `SocketBaseTest.java`
2. Test instantiation
3. Test that all interface methods are callable (no compilation errors)

### Phase 5: Quality Gates
1. Run `mvn clean verify` to:
   - Compile all sources
   - Run formatter
   - Run Checkstyle
   - Run PMD
   - Run SpotBugs
   - Run tests
   - Package JAR
2. Fix any violations
3. Verify clean build

## Acceptance Criteria

- [ ] Package `com.alexswd.network.tcp` created successfully
- [ ] Interface `ISocket` created with all public methods from `java.net.Socket`
- [ ] Class `SocketBase` created and implements `ISocket`
- [ ] All code compiles without errors or warnings
- [ ] Code passes formatter
- [ ] Code passes Checkstyle
- [ ] Code passes PMD
- [ ] Code passes SpotBugs
- [ ] Maven verify succeeds
- [ ] All tests pass
- [ ] Package can be used as a foundation for concrete socket implementations

## Risks

- `java.net.Socket` has many public methods; must ensure comprehensive coverage
- Some methods may have complex signatures with multiple overloads
- Network-related methods throw checked exceptions that must be properly declared
- Some deprecated methods may exist; will focus on current recommended methods

## Notes

- Java 25 available for use (modern language features)
- Will use current public API of `java.net.Socket`
- Stub implementations will be provided for all methods
- Future implementations can extend or wrap Socket as needed
