# The Last Call — Game Architecture

## Overview
"The Last Call" is a data-driven narrative detective investigation game built on Jetpack Compose and Kotlin for Android.

The architecture enforces a strict boundary between:
1. **Static Case Definitions (`CaseDefinition`)**: Pure, immutable content specifying evidence items, suspects, questions, reactions, contradictions, deduction rules, objectives, motives, weapons, and culprit solutions.
2. **Runtime Game State (`CaseState`)**: Mutable, serialized player progress representing discovered evidence, recorded statements, unlocked contradictions/deductions, interview history, current screen, and player theory.
3. **Pure Logic Engines (`com.example.thelastcall.engine.*`)**:
   - `InvestigationEngine`: Handles hotspot inspections, evidence detail inspection, question asking, and evidence presentation.
   - `ReasoningEngine`: Evaluates contradiction assertions, deduction rule triggers, objective completions, and case readiness calculations.
   - `AccusationEngine`: Evaluates indictment submissions, checks culprit identity, motive, weapon, and critical evidence requirements, producing structured scoring feedback.
4. **State Coordinator (`CaseRepository`)**: Bridges the UI and logic engines, managing Coroutine state flows and persistence with `SharedPreferences`.

---

## Architectural Diagram
```
                           +------------------------+
                           |  CaseRegistry (Cases)  |
                           +-----------+------------+
                                       |
                                       v
+------------------+         +--------------------+         +-------------------+
|  Compose UI      | <-----> |   CaseRepository   | <-----> | SharedPreferences |
|  Screens & Views | (State) | (State Flow & API) |  (Save) | (case_state_001)  |
+------------------+         +---------+----------+         +-------------------+
                                       |
               +-----------------------+-----------------------+
               |                       |                       |
               v                       v                       v
     +--------------------+  +-------------------+  +--------------------+
     |InvestigationEngine |  |  ReasoningEngine  |  |  AccusationEngine  |
     | (Inspection & Q&A) |  | (Deductions & Obj)|  | (Verdict & Scoring)|
     +--------------------+  +-------------------+  +--------------------+
```

---

## Key Modules & Classes

### 1. Data Models (`com.example.thelastcall.data.Models`)
- `CaseDefinition`: Immutable registry entry for a single case.
- `EvidenceItem`: Clue metadata, discoverability, category, and inspection unlocks.
- `Suspect`: Name, bio, relationship, dialogue, and presentation reactions.
- `Question`: Dialogue node unlocking statements and evidence.
- `Contradiction`: Discrepancy between a suspect statement and physical evidence.
- `Deduction`: Logical synthesis of two or more clues/statements.
- `CaseObjective`: Multi-stage investigation goals evaluated via `ObjectiveCondition`.
- `CulpritSolution`: Verified answer key containing culprit ID, motive key, weapon key, and critical evidence IDs.
- `CaseState`: Runtime snapshot saved to local storage.

### 2. Logic Engines (`com.example.thelastcall.engine`)
- **`InvestigationEngine`**: Implements deterministic state mutations when players interact with crime scene objects, interrogate suspects, or present evidence.
- **`ReasoningEngine`**: Evaluates active contradictions and deductions. Automatically triggers auto-deductions and updates objective completion status based on `ObjectiveCondition`.
- **`AccusationEngine`**: Validates the final accusation against the `CulpritSolution` and computes readiness scores.

### 3. Registry (`com.example.thelastcall.data.CaseRegistry`)
Manages all available case definitions. Cases are registered via `registerCase(caseDef)` and queried by ID. Unknown case IDs produce a safe fallback without crashing.
