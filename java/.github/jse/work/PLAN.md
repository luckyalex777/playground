# Implementation Plan: HttpClient Dependency Injection

## Goal

Refactor `HttpClient` to support dependency injection of `ISocket` instances. This enables:
- Testing with mock or custom socket implementations
- Runtime socket strategy selection
- Cleaner separation of concerns between HTTP logic and socket creation

## Current State

### Architecture
`HttpClient` is a stateless, immutable class that:
- Creates a new `PlainSocket` instance on every `get()` call (line 57)
- Executes the HTTP request using that socket
- Closes the socket in a finally block
- Provides no mechanism to inject custom socket implementations

### Current Implementation (Relevant Code)
```java
public final class HttpClient {
  // ... constants ...

  // No fields or constructors (uses implicit default constructor)

  public HttpResponse get(HttpRequest request) throws IOException {
    // ... setup ...
    ISocket socket = new PlainSocket();  // Creates new socket every time
    try {
      socket.connect(address, port);
      // ... execute request ...
    } finally {
      socket.close();
    }
  }
  // ... helper methods ...
}
```

### Testing Gaps
- `HttpClientTest` only verifies instantiation
- No tests for `get()` method behavior
- No ability to test with mock sockets due to hard-coded `PlainSocket` creation

## Proposed Design

### Dependency Injection Pattern
Introduce optional socket injection while maintaining backward compatibility:

1. **Private Field**: Store socket instance as instance state
2. **Default Constructor**: Creates a new `PlainSocket` for backward compatibility
3. **Parameterized Constructor**: Accepts `ISocket` parameter for testing/custom implementations
4. **Modified `get()` Method**: Uses the private field instead of creating local socket

### Implementation Details

#### New Private Field
```java
private final ISocket socket;
```

#### Default Constructor
```java
/**
 * Constructs an HttpClient with a default PlainSocket.
 *
 * @throws IOException if PlainSocket creation fails
 */
public HttpClient() throws IOException {
  this.socket = new PlainSocket();
}
```

#### Parameterized Constructor
```java
/**
 * Constructs an HttpClient with the provided socket implementation.
 *
 * @param socket the socket implementation to use
 * @throws NullPointerException if socket is null
 */
public HttpClient(ISocket socket) {
  this.socket = Objects.requireNonNull(socket, "socket cannot be null");
}
```

#### Modified `get()` Method
- Remove: `ISocket socket = new PlainSocket();`
- Use: `this.socket` instead
- Rationale: Each `get()` call reuses the same socket instance
  - **Consideration**: This changes the socket lifecycle from "one socket per request" to "one socket per HttpClient instance"
  - **Impact**: If multiple requests are made with the same client, the socket is reused; socket state carries between requests
  - **Mitigation**: Document this behavior; consider cleanup semantics if needed
  - **Alternative**: Keep creating new sockets but pass socket factory instead of instance (more complex)

### Design Trade-offs

| Aspect | Current | Proposed | Trade-off |
|--------|---------|----------|-----------|
| Socket lifetime | Per-request (created/destroyed each call) | Per-client (shared across calls) | Allows injection but changes resource semantics |
| Memory efficiency | Creates many short-lived sockets | Reuses one socket | Better efficiency; requires careful cleanup |
| Testability | Not testable with mock sockets | Fully testable with mocks | Enables comprehensive testing |
| Backward compatibility | N/A | Default constructor mirrors old behavior | Preserves API for existing code |

## Files to Create
None. All changes are to existing files.

## Files to Modify

### 1. `top/network/src/main/java/com/alexswd/top/network/http/HttpClient.java`
**Changes:**
- Add private final field: `ISocket socket`
- Add default constructor: `public HttpClient() throws IOException`
- Add parameterized constructor: `public HttpClient(ISocket socket)`
- Modify `get(HttpRequest)` method:
  - Remove: `ISocket socket = new PlainSocket();`
  - Replace with: Use `this.socket` throughout the method
  - Keep try-finally structure and socket.close()
- Add import: `java.util.Objects` (for `requireNonNull()`)

**Impacts:**
- Backward compatible: existing `new HttpClient()` calls work unchanged
- Forward compatible: new code can inject mock sockets
- No changes to method signatures (except constructors)
- No changes to public API surface

### 2. `top/network/src/test/java/com/alexswd/network/http/HttpClientTest.java`
**Changes:**
- Add test for default constructor: verifies PlainSocket is created
- Add test for parameterized constructor: verifies custom socket is stored
- Add test(s) for `get()` method with mock socket:
  - Verify the stored socket is used (not a new one)
  - Verify connect/getOutputStream/getInputStream are called on the injected socket

**Test Structure:**
```java
@Test
void testDefaultConstructorCreatesPlainSocket() throws IOException {
  HttpClient client = new HttpClient();
  // Verify behavior: socket is initialized
  // Note: Field is private, so verify indirectly via get() with mock
}

@Test
void testConstructorAcceptsCustomSocket() {
  ISocket mockSocket = mock(ISocket.class);
  HttpClient client = new HttpClient(mockSocket);
  // Verify behavior: get() uses the injected socket
}

@Test
void testGetUsesStoredSocket() throws IOException {
  ISocket mockSocket = mock(ISocket.class);
  when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(...));
  when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
  
  HttpClient client = new HttpClient(mockSocket);
  HttpRequest request = new HttpRequest.Builder("http://example.com").build();
  client.get(request);
  
  verify(mockSocket).connect(any(), anyInt());
  verify(mockSocket).getOutputStream();
  // etc.
}
```

## Files to Delete
None.

## Data/API Changes

### Public API Changes
- **New Constructors:**
  - `public HttpClient()` — Creates client with default PlainSocket
  - `public HttpClient(ISocket socket)` — Creates client with custom socket
- **Behavioral Change:**
  - Socket instance is now stored and reused across `get()` calls
  - Multiple requests on the same client will reuse the same socket connection

### Backward Compatibility
- Fully backward compatible: existing code using default constructor continues to work
- Existing `HttpClient client = new HttpClient()` calls require no changes

### Deprecation
None — this is an enhancement, not a removal.

## Dependencies
- **New imports:**
  - `java.util.Objects` — for `requireNonNull()` null check
- **No new external dependencies required**
- **No changes to `pom.xml`**

## Implementation Steps

1. **Implement constructors and field** (single change to HttpClient.java):
   - Add private field declaration
   - Add default constructor (throws IOException)
   - Add parameterized constructor (NullPointerException if socket is null)
   - Add necessary imports

2. **Modify `get()` method**:
   - Replace `ISocket socket = new PlainSocket();` with reference to `this.socket`
   - Verify try-finally block structure remains intact
   - Confirm resource management unchanged

3. **Run Maven verify** to ensure code quality:
   - Formatting check
   - Compiler check
   - Checkstyle validation
   - PMD analysis
   - SpotBugs static analysis
   - Existing test execution

4. **Add unit tests** (HttpClientTest.java):
   - Default constructor test
   - Parameterized constructor test
   - Mock socket integration test (verify stored socket is used)

5. **Final verification**:
   - Run Maven verify again with all tests passing
   - Confirm all quality gates pass
   - Manual code review of socket reuse implications

## Test Strategy

### Unit Tests
1. **Constructor Tests:**
   - `testDefaultConstructor()` — Verifies HttpClient instantiates without errors
   - `testConstructorWithCustomSocket()` — Verifies custom socket is accepted
   - `testConstructorRejectsNullSocket()` — Verifies NPE thrown for null socket

2. **Integration Tests with Mock Socket:**
   - `testGetUsesStoredSocket()` — Verifies `get()` calls methods on the injected socket
   - `testGetConnectsToProvidedSocket()` — Verifies socket.connect() is called with correct address/port
   - `testGetReusesSocketAcrossMultipleCalls()` — Verifies same socket instance used in multiple get() calls (or close/recreate logic if applicable)

### Existing Test Compatibility
- `testHttpClientInstantiation()` should continue to pass (default constructor still works)
- `testCreateRequest()` and `testCreateResponse()` are unaffected

### Test Tools
- **JUnit 5** (already in use)
- **Hamcrest matchers** (already in use)
- **Mock library:** Consider Mockito (not currently in pom.xml; may need to add) or hand-crafted mock ISocket
  - **Decision needed:** Should we add Mockito to pom.xml for cleaner mocking?

## Quality Gates

### Formatting
- Run: `mvn spotless:check` (or maven-formatter-plugin if configured)
- Verify: Code follows eclipse-java-google-style.xml
- The new code must not introduce formatting violations

### Checkstyle
- Run: `mvn checkstyle:check`
- Verify: No new checkstyle violations
- Constructor Javadoc must be complete
- Field must have Javadoc

### PMD
- Run: `mvn pmd:check`
- Common issues to watch:
  - Ensure parameterized constructor does not trigger "DataflowAnomalyAnalysis"
  - Verify no unused imports
  - Confirm exception handling in constructors is appropriate

### SpotBugs
- Run: `mvn spotbugs:check`
- Verify: No null-pointer dereference warnings
- The `Objects.requireNonNull()` call should satisfy null-safety analysis
- Note: Existing `@SuppressWarnings("PMD.CloseResource")` on `get()` may need review if socket creation/closing logic changes

### Compiler
- Run: `mvn clean compile`
- Verify: No compiler warnings
- Java 21+ compliance check

### Tests
- Run: `mvn test`
- All existing tests must pass
- New tests must pass
- No test regressions

### Maven Verify
- Run: `mvn clean verify`
- This aggregates all quality gates
- Must pass before code is considered done

## Security Considerations

### Null Safety
- Parameterized constructor uses `Objects.requireNonNull()` to validate socket parameter
- Eliminates risk of NullPointerException during `get()` execution

### Socket Lifecycle
- Default constructor creates new `PlainSocket()` — no security risk
- Parameterized constructor accepts any `ISocket` implementation — caller is responsible for providing safe socket
- **Assumption:** All ISocket implementations are trusted (they are internal TCP abstractions)

### No Breaking Changes to Security Posture
- Existing socket connection logic is unchanged
- SSL/TLS handling (if any) is delegated to PlainSocket or TorSocket implementations
- No new attack surface

## Risks and Mitigations

### Risk 1: Socket Reuse Semantics
**Description:** Changing from "one socket per request" to "one socket per client" alters resource lifecycle. Multiple `get()` calls on the same client will reuse the same socket.

**Impact:**
- Socket state carries between requests
- Connection may be closed after first request in finally block; subsequent calls will fail
- Existing code relying on default constructor may break if it makes multiple `get()` calls

**Mitigation:**
- Document the new socket lifecycle clearly in class Javadoc
- Add test that demonstrates socket reuse behavior
- Consider: Should the default constructor create a new socket on each `get()` call? (requires factory pattern instead)
- **Decision:** Use simple instance-per-client model; document the consequence

### Risk 2: Test Coverage Gaps
**Description:** New constructors and socket injection may not be adequately tested, leading to undetected bugs.

**Impact:**
- Regression in integrations tests that call `get()`
- Mock socket might not fully exercise all code paths

**Mitigation:**
- Write comprehensive unit tests for both constructors
- Write integration test with a mock socket that verifies socket methods are called
- Run full Maven verify to catch static analysis issues

### Risk 3: Backward Compatibility
**Description:** Code that expects `new HttpClient()` to work exactly as before might behave differently if it calls `get()` multiple times.

**Impact:**
- Existing code might break if it assumes each `get()` creates a fresh socket
- Socket state (e.g., if kept open) might cause unexpected behavior

**Mitigation:**
- Document behavioral change in Javadoc
- Add integration test demonstrating the reuse behavior
- Consider adding a factory method `HttpClient.withDefaultSocket()` as an explicit alternative
- **Current Decision:** Accept the change; document thoroughly

### Risk 4: IOException in Default Constructor
**Description:** Default constructor throws `IOException` (from `new PlainSocket()`), which is a checked exception.

**Impact:**
- Calling code must handle IOException: `HttpClient client = new HttpClient();` now requires try-catch
- Existing code might not be ready for this

**Mitigation:**
- Review existing instantiations of HttpClient
- Document the exception in Javadoc
- Consider: Should default constructor wrap IOException in RuntimeException? (No — preserve checked exception semantics)
- **Current Decision:** Default constructor throws IOException; this is intentional

## Acceptance Criteria

All criteria must be objectively verified before marking work complete.

### Functional Requirements
- [ ] Default constructor `public HttpClient()` exists and creates a new PlainSocket instance
- [ ] Parameterized constructor `public HttpClient(ISocket socket)` exists and stores the provided socket
- [ ] Parameterized constructor rejects null socket with `NullPointerException`
- [ ] `get()` method uses the stored `this.socket` instead of creating a local `new PlainSocket()`
- [ ] `get()` method still calls `socket.close()` in finally block

### Test Requirements
- [ ] Unit test: Default constructor creates HttpClient successfully
- [ ] Unit test: Parameterized constructor accepts and stores custom socket
- [ ] Unit test: Parameterized constructor throws NPE when socket is null
- [ ] Integration test: `get()` calls connect/getOutputStream/getInputStream on the injected socket
- [ ] Integration test: Stored socket is reused across multiple `get()` calls
- [ ] Existing tests (testHttpClientInstantiation, etc.) all pass

### Code Quality Requirements
- [ ] Maven `clean verify` passes without errors or warnings
- [ ] Formatting check passes (no formatting violations)
- [ ] Checkstyle passes (no style violations)
- [ ] PMD passes (no PMD violations)
- [ ] SpotBugs passes (no bug findings)
- [ ] Compiler produces no warnings
- [ ] All tests pass (unit and integration)

### Documentation Requirements
- [ ] Class Javadoc updated to explain socket injection and reuse semantics
- [ ] Default constructor Javadoc complete (parameters, throws, description)
- [ ] Parameterized constructor Javadoc complete (parameters, throws, description)
- [ ] Private field has Javadoc comment

### Backward Compatibility
- [ ] Existing code using `new HttpClient()` continues to compile and run
- [ ] No breaking changes to public API
- [ ] No changes to method signatures (except new constructors)

## Open Questions

1. **Socket Reuse Lifecycle:** Should multiple `get()` calls on the same client reuse the socket, or should each call get a fresh connection?
   - Current proposal: Reuse the socket instance (simpler, testable)
   - Alternative: Use socket factory instead of instance (more complex but clearer semantics)
   - **Recommendation:** Proceed with reuse; document clearly

2. **Mockito Dependency:** Should we add Mockito to the test classpath for easier mock creation?
   - Current state: No mocking library in pom.xml
   - Options:
     - Add Mockito dependency (simplifies tests but adds dependency)
     - Use hand-crafted mock ISocket implementation (more verbose but no external deps)
   - **Recommendation:** Check if other tests already use mocking; if not, use hand-crafted mocks to keep dependencies minimal

3. **Default Constructor IOException:** Is it acceptable for the default constructor to throw IOException?
   - Impact: Callers must now handle IOException when instantiating HttpClient
   - Alternative: Wrap IOException in RuntimeException in default constructor
   - **Recommendation:** Keep IOException; this reflects the underlying PlainSocket creation cost

4. **Socket Cleanup:** Should HttpClient provide a close() method to explicitly clean up the socket?
   - Current state: Socket is only closed after each `get()` call
   - Consideration: If socket is reused, callers might want explicit lifecycle control
   - **Recommendation:** Not required for this iteration; document that socket is held until closed or garbage collected

---

## IMPLEMENTER HANDOFF

### Sequence to Follow

1. **Code Changes (Single Edit):**
   - Open `top/network/src/main/java/com/alexswd/top/network/http/HttpClient.java`
   - Add import: `import java.util.Objects;`
   - Add private final field immediately after class constants:
     ```java
     /** The socket instance used for all HTTP connections. */
     private final ISocket socket;
     ```
   - Add default constructor before `get()` method:
     ```java
     /**
      * Constructs an HttpClient with a default PlainSocket.
      *
      * @throws IOException if PlainSocket creation fails
      */
     public HttpClient() throws IOException {
       this.socket = new PlainSocket();
     }
     ```
   - Add parameterized constructor immediately after default constructor:
     ```java
     /**
      * Constructs an HttpClient with the provided socket implementation.
      *
      * @param socket the socket implementation to use for all connections
      * @throws NullPointerException if socket is null
      */
     public HttpClient(ISocket socket) {
       this.socket = Objects.requireNonNull(socket, "socket cannot be null");
     }
     ```
   - In the `get()` method, replace line 57: `ISocket socket = new PlainSocket();` with no initialization
   - Throughout the `get()` method, replace all references to local `socket` with `this.socket`
   - Update class Javadoc to mention socket injection capability

2. **Verify Code Changes:**
   - Run `mvn clean compile` in `top/network/` directory
   - Verify no compiler errors or warnings

3. **Add Tests:**
   - Open `top/network/src/test/java/com/alexswd/network/http/HttpClientTest.java`
   - Add test for default constructor (verify instantiation succeeds)
   - Add test for parameterized constructor with null socket (verify NPE)
   - Add test for parameterized constructor with mock socket (verify socket is stored)
   - Add test for `get()` method using injected mock socket

4. **Run Quality Gates:**
   - Run `mvn clean verify` in `top/network/` directory
   - Verify all tests pass
   - Verify formatting, checkstyle, PMD, SpotBugs all pass
   - Address any violations before proceeding

5. **Final Verification:**
   - Run `mvn clean verify` one more time
   - Confirm exit code is 0
   - No warnings or errors in output
   - All acceptance criteria met

### Command Reference
```bash
# In /home/alex/projects/playground/java/top/network/

# Compile only
mvn clean compile

# Run tests only
mvn test

# Full quality gate verification
mvn clean verify

# Format code (if needed)
mvn spotless:apply
```

### Expected Outcomes
- HttpClient now accepts optional ISocket injection
- All existing code continues to work (backward compatible)
- Code is fully testable with mock sockets
- All quality gates pass
- Test coverage includes constructors and socket injection scenarios
