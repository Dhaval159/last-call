# THE LAST CALL
# CASE DATA MASTER SPECIFICATION

**Case ID:** CASE-001  
**Document Version:** 1.0  
**Status:** Canonical Implementation Reference  
**Platform:** Android  
**Purpose:** Master cross-reference for the entire playable detective case.

---

# 1. PURPOSE

This document connects all major game systems into one canonical case structure.

The implementation AI should use this document to understand:

- Case state
- Suspects
- Evidence
- Timeline
- Statements
- Objectives
- Interviews
- Contradictions
- Deductions
- Unlock conditions
- Final accusation
- Ending conditions

If two documents appear to conflict, the more specific canonical document should be checked first. This document defines how the systems connect.

---

# 2. CASE IDENTITY

```text
CASE ID:
CASE-001

TITLE:
THE LAST CALL

GENRE:
Detective / Investigation / Mystery

SETTING:
Modern apartment and connected investigation locations

PLAYER ROLE:
Detective investigating the death of Elias Voss

VICTIM:
Elias Voss

CULPRIT:
Daniel Mercer

MOTIVE:
Prevent exposure of financial misconduct

PRIMARY SOLUTION:
Daniel returned to Elias's apartment after claiming
he had already left, then confronted Elias.
```

---

# 3. CANONICAL CASE FACTS

These facts must never be contradicted by generated content.

```text
Elias Voss is the victim.

Elias was investigating suspicious financial activity.

Maya Voss argued with Elias earlier that evening.

Maya left around 9:05 PM.

Victor Hale had dinner during the critical period.

Nora Bennett remained at home during the critical period.

Daniel Mercer visited Elias earlier.

Daniel claimed he left before 10 PM.

Daniel claimed he did not return.

Evidence later places Daniel back at the apartment.

Elias made a final outgoing call at 10:42 PM.

Daniel had a motive connected to Elias's investigation.

Daniel is responsible for Elias's death.
```

---

# 4. CASE STATE

Recommended global state:

```text
CaseState
    caseId
    currentScene
    currentObjective
    discoveredEvidence[]
    inspectedEvidence[]
    interviewedSuspects[]
    discoveredStatements[]
    confirmedTimelineEvents[]
    suspectedTimelineEvents[]
    deductions[]
    contradictions[]
    unlockedQuestions[]
    completedObjectives[]
    currentAccusation
    caseStatus
    evidenceCount
    investigationProgress
```

---

# 5. CASE STATUS

Valid values:

```text
NOT_STARTED
IN_PROGRESS
READY_FOR_ACCUSATION
SOLVED
INCOMPLETE_ACCUSATION
```

---

# 6. SUSPECT MASTER LIST

```text
S001
MAYA VOSS
Relationship: Family
Role: Victim's wife
Status: Cleared by timeline evidence

S002
VICTOR HALE
Relationship: Business associate
Role: Financial contact
Status: Cleared by alibi

S003
NORA BENNETT
Relationship: Research associate
Role: Research assistant
Status: Cleared by timeline, but has suspicious behavior

S004
DANIEL MERCER
Relationship: Professional contact
Role: Investigator / financial contact
Status: CULPRIT
```

---

# 7. SUSPECT STATE

Each suspect should track:

```text
SuspectState
    suspectId
    introduced
    interviewed
    statements[]
    evidencePresented[]
    contradictions[]
    alibiStatus
    suspicionLevel
    cleared
```

Do not expose `culprit = true` to the player-facing UI.

---

# 8. EVIDENCE MASTER LIST

```text
E001  Victim's Phone
E002  Last Outgoing Call
E003  Phone Call History
E004  Investigation Notes
E005  Missing Financial File
E006  Desk Fingerprint
E007  Broken Glass
E008  Heavy Paperweight
E009  Apartment Door
E010  Window
E011  Elias's Research Notes
E012  Financial Transaction Record
E013  Elias's Personal Note
E014  Daniel's Connection to the Investigation
E015  Victor's Dinner Record
E016  Nora's Digital Activity
E017  Maya's Transportation Record
E018  Daniel's Return Evidence
E019  Financial Investigation File
E020  Daniel's Timeline Statement
```

---

# 9. EVIDENCE CLASSIFICATION

```text
CRITICAL:
E002
E018
E019
E020

SUPPORTING:
E001
E003
E004
E005
E006
E007
E008
E009
E010
E011
E012
E013
E014

SUSPECT-CLEARING:
E015
E016
E017
```

---

# 10. EVIDENCE DISCOVERY DEPENDENCIES

```text
E001
    → available immediately

E002
    ← E001

E003
    ← E001

E004
    → initial investigation

E005
    ← deeper desk investigation

E006
    → desk inspection

E007
    → scene inspection

E008
    → scene inspection

E009
    → entrance inspection

E010
    → window inspection

E011
    → document inspection

E012
    ← E011

E013
    → drawer inspection

E014
    ← interviews + investigation records

E015
    ← Victor alibi investigation

E016
    ← Nora alibi investigation

E017
    ← Maya alibi investigation

E018
    ← Daniel timeline investigation

E019
    ← deeper financial investigation

E020
    ← Daniel interview
```

---

# 11. OBJECTIVE MASTER LIST

```text
O001
Investigate the apartment.

O002
Identify the people connected to Elias.

O003
Verify the suspects' statements.

O004
Reconstruct the final timeline.

O005
Investigate Elias's financial investigation.

O006
Identify contradictions.

O007
Establish who had motive and opportunity.

O008
Make a final accusation.
```

---

# 12. OBJECTIVE COMPLETION RULES

## O001

Complete when the player has investigated the initial crime-scene objects.

Minimum:

```text
E001
E004
E007
E008
```

---

## O002

Complete when all four primary suspects have been introduced.

```text
S001
S002
S003
S004
```

---

## O003

Complete when the player has enough information to evaluate:

- Maya
- Victor
- Nora
- Daniel

---

## O004

Complete when major timeline events are established.

Required:

```text
9:05 PM
9:25 PM
9:30 PM
10:00 PM
10:42 PM
Daniel's later return
```

---

## O005

Complete when:

```text
E012
E019
```

have been discovered.

---

## O006

Complete when:

```text
E018 + E020
```

creates the Daniel contradiction.

---

## O007

Complete when:

```text
Daniel motive
+
Daniel opportunity
+
Daniel contradiction
```

are established.

---

## O008

Available when the case is sufficiently established.

---

# 13. TIMELINE MASTER

Canonical timeline:

```text
8:40 PM
Maya argues with Elias.

9:05 PM
Maya leaves.

9:25 PM
Victor remains at dinner.

9:30 PM
Nora remains at home.

9:40 PM
Daniel arrives.

9:50 PM
Daniel leaves.

10:00 PM
Daniel claims he is no longer at the apartment.

10:20 PM
Daniel returns.

10:35 PM
A confrontation occurs.

10:42 PM
Elias makes his final outgoing call.

10:45 PM
Daniel leaves the apartment.
```

---

# 14. TIMELINE EVENTS

Use stable IDs.

```text
T001
8:40 PM
Maya argues with Elias.

T002
9:05 PM
Maya leaves.

T003
9:25 PM
Victor remains at dinner.

T004
9:30 PM
Nora remains at home.

T005
9:40 PM
Daniel arrives.

T006
9:50 PM
Daniel leaves.

T007
10:20 PM
Daniel returns.

T008
10:35 PM
Confrontation.

T009
10:42 PM
Elias makes final outgoing call.

T010
10:45 PM
Daniel leaves.
```

---

# 15. TIMELINE EVIDENCE LINKS

```text
T002 ← E017
T003 ← E015
T004 ← E016
T005 ← E006
T006 ← E020
T007 ← E018
T008 ← E007 + E008
T009 ← E002
T010 ← E018
```

---

# 16. STATEMENT MASTER LIST

## ST001 — MAYA

Question:

```text
When did you leave?
```

Answer:

```text
Around 9 PM.
```

---

## ST002 — MAYA

Question:

```text
Did you return?
```

Answer:

```text
No.
```

---

## ST003 — VICTOR

Question:

```text
Where were you during the evening?
```

Answer:

```text
At dinner.
```

---

## ST004 — NORA

Question:

```text
Where were you?
```

Answer:

```text
At home.
```

---

## ST005 — NORA

Question:

```text
Did you take anything from Elias's investigation?
```

Answer:

```text
I copied some material, but I never went to the apartment.
```

---

## ST006 — DANIEL

Question:

```text
When did you leave?
```

Answer:

```text
Before 10 PM.
```

---

## ST007 — DANIEL

Question:

```text
Did you return?
```

Answer:

```text
No.
```

---

## ST008 — DANIEL

Question:

```text
Why were you meeting Elias?
```

Answer:

```text
We were discussing his investigation.
```

---

# 17. STATEMENT → EVIDENCE LINKS

```text
ST001 ← E017
ST003 ← E015
ST004 ← E016
ST005 ← E011 / E012
ST006 ← E020
ST007 ← E020
ST008 ← E014 / E019
```

---

# 18. PRIMARY CONTRADICTION

Contradiction ID:

```text
C001
```

Requirements:

```text
ST006
ST007
E018
```

Result:

```text
Daniel's claim that he left before 10 PM
and never returned is contradicted.
```

Severity:

```text
CRITICAL
```

---

# 19. MOTIVE CONNECTION

Motive ID:

```text
M001
```

Requirements:

```text
E012
E019
E014
```

Result:

```text
Daniel had reason to fear Elias's investigation.
```

---

# 20. OPPORTUNITY CONNECTION

Opportunity ID:

```text
OP001
```

Requirements:

```text
E006
E018
T007
```

Result:

```text
Daniel had physical presence and access
during the critical period.
```

---

# 21. FINAL CASE LOGIC

The final solution requires:

```text
C001
+
M001
+
OP001
```

Result:

```text
DANIEL MERCER
```

---

# 22. DEDUCTION MASTER LIST

```text
D001
Elias was alive at 10:42 PM.

D002
The critical event occurred after the other
suspects had established stronger alibis.

D003
Daniel was present at the apartment earlier.

D004
Daniel claimed he left before 10 PM.

D005
Daniel later returned.

D006
Daniel lied about returning.

D007
Daniel had a motive connected to the investigation.

D008
Daniel is the only primary suspect whose timeline
is directly contradicted by evidence.
```

---

# 23. DEDUCTION DEPENDENCIES

```text
D001 ← E002

D002 ← E015 + E016 + E017 + E002

D003 ← E006

D004 ← E020

D005 ← E018

D006 ← D004 + D005

D007 ← E014 + E019

D008 ← D006 + D007 + D002
```

---

# 24. SUSPECT CLEARING LOGIC

## MAYA

```text
Maya has an argument with Elias.
+
Maya leaves at approximately 9:05 PM.
+
E017 supports her departure.
=
Maya remains suspicious emotionally,
but her timeline does not support the murder.
```

---

## VICTOR

```text
Victor has financial conflict.
+
Victor was at dinner.
+
E015 supports his location.
=
Victor is not supported as the killer.
```

---

## NORA

```text
Nora copied investigation material.
+
Nora has suspicious behavior.
+
E016 supports that she remained at home.
=
Nora is suspicious but not the killer.
```

---

## DANIEL

```text
Daniel had knowledge of the investigation.
+
Daniel was at the apartment.
+
Daniel claimed he left.
+
Evidence places him back at the apartment.
+
His return contradicts his statement.
=
Daniel is the culprit.
```

---

# 25. INTERVIEW UNLOCK LOGIC

## Maya Interview

Available after suspect introduction.

## Victor Interview

Available after suspect introduction.

## Nora Interview

Available after suspect introduction.

## Daniel Interview

Available after suspect introduction.

---

# 26. EVIDENCE PRESENTATION LOGIC

General rule:

```text
IF evidence is relevant
    show relevant reaction

IF evidence contradicts statement
    create contradiction

IF evidence has no meaningful connection
    show natural dismissal

IF evidence unlocks question
    add question to interview
```

Never reveal hidden case variables to the player.

---

# 27. KEY PRESENTATION EVENTS

## E002 → Daniel

Reaction:

```text
Daniel becomes uncomfortable.

He asks why the call matters.
```

This should not immediately expose the solution.

---

## E018 → Daniel

Reaction:

```text
Daniel denies returning.

His previous statement becomes suspicious.
```

Creates:

```text
C001
```

---

## E019 → Daniel

Reaction:

```text
Daniel becomes defensive.

He attempts to minimize his connection
to Elias's investigation.
```

Creates:

```text
M001
```

---

# 28. CASE PROGRESS CALCULATION

Recommended conceptual formula:

```text
investigationProgress =
    evidenceDiscoveredWeight
    + suspectInvestigationWeight
    + timelineWeight
    + deductionWeight
```

Do not make progress equal to "percentage chance the player has found the killer."

It represents investigation completeness.

---

# 29. ACCUSATION READINESS

The player should become eligible for a strong accusation when:

```text
E002 discovered
E018 discovered
E019 discovered
E020 recorded
C001 established
M001 established
```

Additional supporting evidence is recommended but not mandatory.

---

# 30. PERFECT CASE CONDITIONS

Perfect solve:

```text
All critical evidence discovered
All suspects investigated
All major alibis verified
C001 established
M001 established
OP001 established
Correct accusation
```

Result:

```text
PERFECT INVESTIGATION
```

---

# 31. CORRECT BUT INCOMPLETE SOLUTION

If:

```text
Correct suspect
+
Correct motive
+
Critical contradiction
```

but some optional evidence is missing:

Result:

```text
CASE SOLVED
```

Not perfect.

---

# 32. INCORRECT ACCUSATION

If the player selects:

```text
MAYA
VICTOR
NORA
```

without sufficient evidence:

Show:

```text
ACCUSATION NOT SUPPORTED

Your current evidence does not establish
this suspect as the culprit.

Continue investigating.
```

Return to case file.

---

# 33. PREMATURE DANIEL ACCUSATION

If Daniel is selected before the case is ready:

```text
STRONG SUSPICION

Daniel remains highly suspicious.

However, the evidence is not yet
strong enough to support a final accusation.

Continue investigating.
```

---

# 34. SAVE DATA

Save at minimum:

```text
caseStatus
currentObjective
discoveredEvidence
inspectedEvidence
interviewedSuspects
statements
timelineEvents
deductions
contradictions
settings
```

---

# 35. SAVE TRIGGER POINTS

Autosave after:

```text
Evidence discovery
Interview completion
Contradiction discovery
Timeline update
Deduction creation
Major objective completion
```

---

# 36. RESET CASE

Reset should clear:

```text
CaseState
EvidenceState
InterviewState
TimelineState
DeductionState
```

It should not reset:

```text
Settings
```

unless the player explicitly chooses a full application reset.

---

# 37. UI STATE IDs

Recommended stable screen IDs:

```text
SCREEN_MAIN_MENU
SCREEN_BRIEFING
SCREEN_CRIME_SCENE
SCREEN_CASE_FILE
SCREEN_EVIDENCE_LIST
SCREEN_EVIDENCE_DETAIL
SCREEN_SUSPECT_LIST
SCREEN_SUSPECT_DETAIL
SCREEN_INTERVIEW
SCREEN_EVIDENCE_PRESENTATION
SCREEN_TIMELINE
SCREEN_DEDUCTIONS
SCREEN_OBJECTIVES
SCREEN_ACCUSATION
SCREEN_RECONSTRUCTION
SCREEN_RESULT
SCREEN_SETTINGS
```

---

# 38. GLOBAL ACTION IDs

```text
ACTION_NEW_CASE
ACTION_CONTINUE
ACTION_OPEN_CASE_FILE
ACTION_INSPECT
ACTION_VIEW_EVIDENCE
ACTION_INTERVIEW
ACTION_PRESENT_EVIDENCE
ACTION_ADD_TIMELINE
ACTION_CREATE_DEDUCTION
ACTION_FINALIZE_ACCUSATION
ACTION_REVIEW_CASE
ACTION_SAVE
ACTION_EXIT
```

---

# 39. CASE FILE TAB IDS

```text
TAB_EVIDENCE
TAB_SUSPECTS
TAB_TIMELINE
TAB_DEDUCTIONS
TAB_OBJECTIVES
```

---

# 40. DEBUG INFORMATION

Development builds may display:

```text
CASE STATE
Evidence: 12/20
Suspects: 4/4
Contradictions: 1
Timeline Events: 7/10
Deduction State: IN_PROGRESS
```

This should not appear in the final player build unless debug mode is enabled.

---

# 41. DEBUG CASE CONTROLS

Optional developer-only controls:

```text
Unlock All Evidence
Unlock All Interviews
Complete All Objectives
Set Case Ready
Trigger Final Accusation
Reset Case
```

These controls must never be exposed accidentally to normal players.

---

# 42. MASTER CASE DEPENDENCY GRAPH

```text
CASE START
   |
   v
BRIEFING
   |
   v
CRIME SCENE
   |
   +--> E001
   |      |
   |      +--> E002
   |      +--> E003
   |
   +--> E004
   |      |
   |      +--> E011
   |             |
   |             +--> E012
   |                    |
   |                    +--> E019
   |
   +--> E006
   |
   +--> E007
   |
   +--> E008
   |
   +--> E009
   |
   +--> E010
   |
   +--> E013
   |
   v
SUSPECT INTERVIEWS
   |
   +--> MAYA
   |      |
   |      +--> E017
   |
   +--> VICTOR
   |      |
   |      +--> E015
   |
   +--> NORA
   |      |
   |      +--> E016
   |
   +--> DANIEL
          |
          +--> E020
          |
          +--> E014
          |
          +--> E018
                 |
                 v
             CONTRADICTION
                 |
                 v
              DEDUCTION
                 |
                 v
            FINAL ACCUSATION
                 |
                 v
              RESULT
```

---

# 43. IMPLEMENTATION RULE: SINGLE SOURCE OF TRUTH

The implementation should avoid duplicating critical case facts across unrelated systems.

Recommended:

```text
CaseData
    EvidenceData[]
    SuspectData[]
    TimelineData[]
    StatementData[]
    ObjectiveData[]
    DeductionRules[]
    AccusationRules[]
```

Dialogue and UI should reference these IDs instead of creating duplicate versions of the same facts.

---

# 44. IMPLEMENTATION RULE: STABLE IDS

Never use display names as internal identifiers.

Correct:

```text
E018
S004
T007
ST007
C001
D008
```

Incorrect:

```text
"Daniel's Return Evidence"
"Daniel Mercer"
"Daniel returns"
```

Names can change later without breaking relationships.

---

# 45. IMPLEMENTATION RULE: NO ORPHAN DATA

Every critical evidence item must connect to at least one:

- Suspect
- Timeline event
- Objective
- Deduction
- Dialogue
- Other evidence

If an item has no meaningful relationship, it should not be considered critical.

---

# 46. IMPLEMENTATION RULE: NO DEAD-ENDS

The player must always have at least one meaningful investigation path.

If the player has not found required evidence:

Provide another valid investigative route.

Do not permanently soft-lock the case.

---

# 47. IMPLEMENTATION RULE: NO RANDOMIZED SOLUTION

Do not randomize:

- Culprit
- Motive
- Critical evidence
- Critical timeline
- Suspect identities

The case is authored and deterministic.

---

# 48. IMPLEMENTATION RULE: OPTIONAL ORDER

The player may:

- Inspect objects in different orders
- Interview suspects in different orders
- Review evidence at any time
- Return to previous scenes

The underlying solution remains deterministic.

---

# 49. IMPLEMENTATION RULE: INFORMATION SHOULD ACCUMULATE

New discoveries should add information.

They should not randomly replace old facts.

For example:

```text
Daniel said he left before 10 PM.
```

must remain in the statement log even after:

```text
Daniel returned later.
```

The contradiction depends on both facts existing simultaneously.

---

# 50. FINAL MASTER SOLUTION

The complete case is:

```text
Elias Voss was investigating suspicious financial activity.

Several people had reasons to dislike or distrust him.

Maya argued with Elias but left around 9:05 PM.

Victor was at dinner during the critical period.

Nora remained at home, although she had copied some
investigation material.

Daniel Mercer had direct knowledge of Elias's investigation.

Daniel visited Elias earlier and later claimed he had
left before 10 PM and never returned.

Evidence places Daniel back at the apartment later.

Elias was still alive at 10:42 PM, when he made his
final outgoing call.

Daniel's return contradicts his statement.

The financial investigation gave Daniel a motive to
prevent Elias from exposing the misconduct.

Daniel is therefore the only suspect whose timeline,
motive, and physical opportunity form a complete
evidence-supported case.
```

---

# 51. MASTER IMPLEMENTATION CHECKLIST

Before considering Case 001 complete, verify:

```text
[ ] Main menu works
[ ] New case works
[ ] Continue works
[ ] Briefing works
[ ] Crime scene loads
[ ] Objects can be inspected
[ ] E001 can be discovered
[ ] E002 can be discovered
[ ] Evidence is stored
[ ] Case file opens
[ ] Suspect list works
[ ] All four suspects can be interviewed
[ ] Statements are recorded
[ ] Evidence can be presented
[ ] E018 creates contradiction
[ ] Timeline works
[ ] Deduction system works
[ ] Final accusation works
[ ] Correct accusation solves case
[ ] Wrong accusation returns to investigation
[ ] Save works
[ ] Load works
[ ] Reset works
[ ] Android back navigation works
[ ] Touch targets are usable
[ ] No critical information is lost
[ ] No critical clue is randomly generated
[ ] No critical fact contradicts this document
```

---

# 52. FINAL RULE

**This document is the master integration layer for Case 001.**

The implementation AI should not invent new critical relationships.

It may improve:

- Visual presentation
- Animations
- Microcopy
- Non-critical environmental details
- UI polish
- Accessibility
- Performance

It must not change:

```text
THE CULPRIT
THE MOTIVE
THE CRITICAL TIMELINE
THE CRITICAL EVIDENCE
THE PRIMARY CONTRADICTION
THE FINAL SOLUTION
```
