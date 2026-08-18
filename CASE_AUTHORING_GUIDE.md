# Case Authoring Guide

This guide explains how narrative designers and developers can author a new mystery case in **UNRESOLVED** without modifying the game engine or UI code.

---

## Steps to Author a New Case

### 1. Create a Case Definition Object
Define your case in a dedicated Kotlin file (e.g., `Case002Data.kt`):

```kotlin
val CASE_002_DEFINITION = CaseDefinition(
    id = "CASE_002",
    title = "The Midnight Express",
    subtitle = "A Murder in Carriage 4",
    difficulty = "MEDIUM",
    estimatedTimeMinutes = 25,
    victimName = "Julian Vance",
    victimOccupation = "Antiquities Dealer",
    victimPhoto = "victim_julian",
    briefingSummary = "Julian Vance was found dead in his private cabin...",
    briefingDetails = listOf(
        "Time of death estimated between 11:30 PM and 12:15 AM.",
        "The train was in transit between stations."
    ),
    evidence = listOf(/* EvidenceItem definitions */),
    suspects = listOf(/* Suspect definitions */),
    hotspots = listOf(/* CrimeSceneHotspot definitions */),
    questions = listOf(/* Question definitions */),
    reactions = listOf(/* SuspectReaction definitions */),
    contradictions = listOf(/* Contradiction definitions */),
    deductions = listOf(/* Deduction definitions */),
    objectives = listOf(/* CaseObjective definitions with ObjectiveCondition */),
    motives = listOf(
        MotiveOption("MOTIVE_GREED", "The Stolen Relic", "Killing to keep an invaluable antiquity."),
        MotiveOption("MOTIVE_REVENGE", "Family Vendetta", "Retaliation for past crimes.")
    ),
    weapons = listOf(
        WeaponOption("WEAPON_POISON", "Cyanide-Laced Tea", "Toxicological poisoning via beverage."),
        WeaponOption("WEAPON_DAGGER", "Ornate Antique Dagger", "Stab wound to the chest.")
    ),
    culpritSolution = CulpritSolution(
        culpritSuspectId = "S001",
        motiveKey = "MOTIVE_GREED",
        weaponKey = "WEAPON_POISON",
        criticalEvidenceIds = listOf("E001", "E005", "E009"),
        requiredContradictionIds = listOf("C001"),
        requiredDeductionIds = listOf("D001", "D003"),
        solutionNarrative = "Julian Vance was poisoned by..."
    )
)
```

---

## 2. Register the Case
Register the case definition into `CaseRegistry`:

```kotlin
CaseRegistry.registerCase(CASE_002_DEFINITION)
```

---

## 3. Data Integrity Validation
Every authored case should satisfy:
- All `requiredEvidenceId` in `Question` reference valid `EvidenceItem.id`s.
- All `unlocksEvidenceIds` reference valid `EvidenceItem.id`s.
- All `contradictions` reference valid `statementId`s and `evidenceId`s.
- All `deductions` reference valid clue IDs.
- All `culpritSolution` IDs (suspect, motive, weapon, critical evidence) exist in the case definition.
- Run `Case001DataIntegrityTest` or write a new test for your case using the provided test harness.
