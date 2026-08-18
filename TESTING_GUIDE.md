# Testing Guide

This guide explains how to run and write automated tests for **UNRESOLVED**.

---

## Test Suites

1. **`Case001DataIntegrityTest.kt`**
   - Validates that Case 001 data definitions are internally consistent.
   - Ensures no dangling references exist across evidence, statements, questions, reactions, contradictions, deductions, and culprit solutions.
   - Confirms that `CaseRegistry` correctly resolves cases.

2. **`CaseRepositoryTest.kt`**
   - Tests individual state operations in `CaseRepository`:
     - Initial state and screen transitions
     - Crime scene hotspot inspection and evidence unlocking
     - Interrogation statement recording and suspect clearing
     - Manual contradiction checking and error handling
     - Deduction deduction testing and rule triggers
     - Theory builder updates and readiness evaluation
     - Resetting case state

3. **`Case001PlaythroughTest.kt`**
   - Integration tests covering end-to-end user journeys:
     - Full canonical playthrough from scene investigation through perfect conviction
     - Incomplete/wrong accusation handling and non-fatal feedback loop
     - Multi-stage save and reload state persistence

---

## Running Unit & Robolectric Tests

Run unit tests via Gradle:
```bash
gradle :app:testDebugUnitTest
```
