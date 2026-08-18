# THE LAST CALL
# GAMEPLAY SYSTEMS BIBLE

**Case ID:** CASE-001  
**Document Version:** 1.0  
**Status:** Canonical Gameplay Specification  
**Platform:** Android  
**Target Environment:** Google AI Studio Web Application Generation  
**Engine:** No Unity  
**Purpose:** Defines the complete gameplay systems and player interaction model for the game.

---

# 1. PURPOSE

This document defines how **The Last Call** actually plays.

The implementation AI should use this document to build:

- Main game loop
- Investigation
- Exploration
- Evidence collection
- Evidence inspection
- Suspect interviews
- Evidence presentation
- Timeline reconstruction
- Deduction
- Objectives
- Case progress
- Accusation
- Endings
- Save/load
- Mobile controls
- UI states

This document describes **systems**, not visual art direction.

---

# 2. CORE GAME CONCEPT

The player is a detective investigating a murder.

The player explores a crime scene, interviews suspects, collects evidence, connects clues, reconstructs the timeline, and eventually identifies the culprit.

The game is not primarily about combat.

There is:

- No combat system
- No weapon inventory
- No character leveling
- No XP
- No skill tree
- No random enemy encounters

The core gameplay is:

```text
EXPLORE
   ↓
OBSERVE
   ↓
INVESTIGATE
   ↓
COLLECT EVIDENCE
   ↓
QUESTION SUSPECTS
   ↓
PRESENT EVIDENCE
   ↓
CONNECT CLUES
   ↓
RECONSTRUCT TIMELINE
   ↓
ACCUSATION
   ↓
CASE RESOLUTION
```

---

# 3. DESIGN GOAL

The player should feel like they solved the case.

The game should never feel like:

> "The game told me who the killer was."

Instead it should feel like:

> "I noticed the contradiction."

---

# 4. GAME LOOP

The main loop is:

```text
CASE BRIEFING
      ↓
CRIME SCENE
      ↓
INITIAL INVESTIGATION
      ↓
EVIDENCE COLLECTION
      ↓
SUSPECT INTERVIEWS
      ↓
NEW EVIDENCE
      ↓
RETURN TO CRIME SCENE
      ↓
CONNECT EVIDENCE
      ↓
TIMELINE RECONSTRUCTION
      ↓
FINAL INTERVIEWS
      ↓
ACCUSATION
      ↓
CASE RESULT
```

---

# 5. GAME STRUCTURE

The game contains one complete playable case.

## Case 001

**The Last Call**

The first version should prioritize making one case polished rather than creating multiple incomplete cases.

---

# 6. GAME STATES

The game must have a global state machine.

Recommended states:

```text
BOOT
MAIN_MENU
CASE_BRIEFING
CRIME_SCENE
INSPECTING
EVIDENCE_VIEWER
SUSPECT_SELECTION
INTERVIEW
EVIDENCE_PRESENTATION
TIMELINE
DEDUCTION_BOARD
CASE_FILE
FINAL_ACCUSATION
CASE_RESULT
PAUSED
```

---

# 7. MAIN MENU

The main menu should contain:

```text
THE LAST CALL

[NEW CASE]

[CONTINUE]

[SETTINGS]
```

If no save exists:

**CONTINUE** should be disabled or hidden.

---

# 8. NEW CASE

Selecting:

**NEW CASE**

starts Case 001.

If an existing save exists, display:

> "Start a new investigation? Existing progress will be overwritten."

Options:

```text
START NEW CASE
CANCEL
```

---

# 9. CONTINUE

Continue loads the latest saved investigation state.

The player should return to approximately the same gameplay state.

Save:

- Evidence
- Interviews
- Questions
- Timeline
- Objectives
- Case progress
- Current location
- Current suspect state
- Deduction connections

---

# 10. CASE BRIEFING

The opening briefing introduces:

- Elias Voss
- His death
- The apartment
- The known suspects
- The investigator's objective

The player then enters the apartment.

---

# 11. CRIME SCENE SYSTEM

The crime scene is the primary exploration environment.

The player should be able to interact with:

- Desk
- Phone
- Papers
- Broken glass
- Paperweight
- Door
- Window
- General apartment areas

Not every object needs to be evidence.

Some objects exist for atmosphere.

---

# 12. EXPLORATION MODEL

The game should use a simple mobile-friendly exploration system.

Recommended approach:

### Option A

Top-down / isometric room.

### Option B

2D side-view room with clickable hotspots.

### Option C

Point-and-click investigation scene.

The implementation may choose the easiest stable approach.

The important requirement is that exploration feels interactive.

---

# 13. MOBILE INPUT

Primary controls:

### Touch

Tap an object to inspect.

Tap a suspect to talk.

Tap UI buttons.

Swipe if required for navigation.

Avoid requiring:

- Keyboard
- Mouse
- Gamepad
- Multi-touch gestures

The game must be fully playable on Android touchscreens.

---

# 14. INTERACTION SYSTEM

Every interactable object should have:

```text
ID
Display Name
Interaction Type
Description
Required Condition
Evidence Reward
```

Example:

```text
ID:
victim_phone

Name:
Victim's Phone

Interaction:
Inspect

Reward:
E001
```

---

# 15. INTERACTION FEEDBACK

When the player taps an interactable object:

1. Show subtle interaction feedback.
2. Open the investigation panel.
3. Show a short description.
4. Allow deeper inspection if appropriate.
5. Add evidence if applicable.

Avoid huge popups covering the whole screen for simple interactions.

---

# 16. EVIDENCE COLLECTION SYSTEM

When the player discovers evidence:

```text
EVIDENCE DISCOVERED

Victim's Phone

Digital evidence collected.

[VIEW]
[CONTINUE]
```

The evidence is permanently added to the case file.

---

# 17. DUPLICATE EVIDENCE

If the player investigates an already collected item:

Do not create a duplicate.

Instead:

> "You've already documented this."

Allow the player to open the existing evidence card.

---

# 18. EVIDENCE INSPECTION

Every evidence item can be opened from the case file.

Evidence screen:

```text
EVIDENCE

Victim's Phone

Category:
Digital

Description:
Elias's phone was found on his desk.

DETAILS
[Show investigation result]

RELATED
[Last Call]
```

---

# 19. EVIDENCE STATES

Each evidence item can have:

```text
LOCKED
DISCOVERED
INSPECTED
CONNECTED
```

### LOCKED

Not discovered.

### DISCOVERED

Found but not fully examined.

### INSPECTED

Player has read the details.

### CONNECTED

Player has connected it to another case element.

---

# 20. CASE FILE

The case file is the player's main investigation hub.

Tabs:

```text
EVIDENCE
SUSPECTS
TIMELINE
DEDUCTIONS
OBJECTIVES
```

The UI should be compact.

Do not permanently occupy most of the screen with large objective or control panels.

---

# 21. EVIDENCE TAB

Displays all discovered evidence.

Each card:

```text
Evidence icon

Evidence name

Category

Short description

Status
```

Tap to inspect.

---

# 22. SUSPECTS TAB

Displays:

```text
MAYA VOSS
VICTOR HALE
NORA BENNETT
DANIEL MERCER
```

Each suspect shows:

- Portrait
- Relationship
- Interview status
- Alibi status
- Known secrets
- Evidence connections

Do not display:

**CULPRIT: YES/NO**

The player must deduce that.

---

# 23. SUSPECT STATUS

Use neutral investigation states:

```text
NOT INTERVIEWED
INTERVIEWED
QUESTIONED
ALIBI SUPPORTED
SUSPICIOUS
CLEARED
FINAL REVIEW
```

The system should not expose hidden truth.

---

# 24. OBJECTIVE SYSTEM

Objectives should be short.

Examples:

```text
Investigate the apartment.

Identify everyone who interacted with Elias.

Question the suspects.

Verify the suspects' timelines.

Investigate the missing financial material.

Reconstruct the final timeline.

Identify the culprit.
```

---

# 25. OBJECTIVE DISPLAY

Do not keep a huge objective window permanently open.

Recommended:

Small compact objective indicator:

```text
OBJECTIVE
Verify the suspects' timelines
```

Tapping it opens the full objective list.

---

# 26. OBJECTIVE COMPLETION

When completed:

```text
✓ Investigate the apartment
```

A small notification should appear.

Do not interrupt gameplay with giant mandatory screens for every minor objective.

---

# 27. INTERVIEW SYSTEM

Selecting a suspect opens the interview screen.

Example:

```text
DANIEL MERCER

[Ask about Elias]
[Ask about the argument]
[Ask about the timeline]
[Ask about the investigation]
[Present Evidence]
[End Interview]
```

---

# 28. DIALOGUE OPTIONS

Questions are unlocked according to investigation progress.

A question can require:

- Evidence
- Previous question
- Previous interview
- Timeline discovery
- Objective completion

---

# 29. QUESTION STATUS

Questions may display:

```text
NEW
ASKED
LOCKED
```

Asked questions remain accessible for reference.

---

# 30. EVIDENCE PRESENTATION

During interviews, the player can select:

**PRESENT EVIDENCE**

This opens the evidence inventory.

The player selects one evidence item.

The suspect responds according to the Dialogue Bible.

---

# 31. EVIDENCE RESPONSE SYSTEM

Each evidence/suspect pair can produce:

```text
IRRELEVANT
RELEVANT
DEFENSIVE
CONTRADICTION
REVELATION
FINAL CONTRADICTION
```

The game should select the canonical response.

---

# 32. NO RANDOM RESPONSES FOR IMPORTANT CLUES

Critical evidence reactions must be deterministic.

Do not randomly select:

- Confession
- Denial
- New motive
- New timeline
- New suspect

for story-critical evidence.

---

# 33. CONTRADICTION SYSTEM

A contradiction occurs when:

```text
Suspect Statement
+
Evidence
=
Incompatible facts
```

Example:

```text
Daniel:
"I never returned."

Evidence:
Daniel's return evidence.

Result:
CONTRADICTION
```

---

# 34. CONTRADICTION UI

When a contradiction is discovered:

```text
CONTRADICTION FOUND

Daniel claimed he never returned.

Evidence places him at the apartment later.

[ADD TO CASE]
```

The player should then be able to view it in the deduction board.

---

# 35. DEDUCTION SYSTEM

The deduction system connects facts.

It should not be a simple "click all correct clues" quiz.

The player should construct relationships between:

- Evidence
- Suspects
- Timeline events
- Statements
- Motives

---

# 36. DEDUCTION BOARD

Example:

```text
[LAST CALL]
      |
      |
[Elias alive at 10:42]
      |
      |
[DANIEL'S TIMELINE]
      |
      |
[RETURN EVIDENCE]
      |
      |
[FALSE STATEMENT]
```

---

# 37. DEDUCTION CONNECTIONS

Connections should be made by:

1. Selecting evidence.
2. Selecting a related fact.
3. Confirming the connection.

If correct:

```text
CONNECTION ESTABLISHED
```

If incorrect:

```text
These pieces don't establish a meaningful connection yet.
```

No punishment.

---

# 38. REQUIRED FINAL CONNECTIONS

The final deduction must establish:

```text
E002
+
E018
+
E019
+
E014
+
E006
```

leading to:

```text
Daniel's alibi is false.
Daniel had motive.
Daniel was physically connected to the scene.
Daniel returned during the critical period.
```

---

# 39. TIMELINE SYSTEM

The timeline is a separate investigation screen.

It displays confirmed and unknown events.

Example:

```text
9:05 PM
Maya leaves

9:40 PM
Daniel visits

9:50 PM
Daniel claims he leaves

10:20 PM
???

10:42 PM
Elias's last outgoing call

10:45 PM
???
```

---

# 40. TIMELINE INTERACTION

The player can:

- Tap events
- View supporting evidence
- Add confirmed events
- Connect evidence
- Review contradictions

---

# 41. TIMELINE RECONSTRUCTION

When enough evidence is discovered, the player can reconstruct missing events.

Example:

```text
10:20 PM
Daniel returned to the apartment.
```

The player selects:

**ADD EVENT**

Then selects supporting evidence.

---

# 42. TIMELINE VALIDATION

If the event is correct:

```text
TIMELINE CONFIRMED
```

If unsupported:

```text
The available evidence doesn't establish this event yet.
```

Do not tell the player exactly which clue they are missing.

---

# 43. CASE PROGRESS

Use a logical progress system.

Suggested internal stages:

```text
STAGE_01_BRIEFING
STAGE_02_SCENE
STAGE_03_FIRST_INTERVIEWS
STAGE_04_ALIBIS
STAGE_05_MISSING_FILE
STAGE_06_RETURN_EVIDENCE
STAGE_07_TIMELINE
STAGE_08_FINAL_INTERVIEWS
STAGE_09_ACCUSATION
STAGE_10_RESULT
```

---

# 44. PROGRESSION RULE

Do not force a rigid sequence if the player can logically discover something earlier.

For example:

The player may interview Daniel before Maya.

That is allowed.

However, critical information should unlock only when its actual prerequisites are satisfied.

---

# 45. CASE FILE LOGIC

The case file should automatically update when new information is discovered.

Examples:

```text
Suspect cleared
New evidence
New contradiction
Timeline event confirmed
New question unlocked
```

---

# 46. NOTIFICATION SYSTEM

Use small notifications such as:

```text
Evidence discovered
New question available
Timeline updated
New contradiction
Objective completed
```

Notifications should not stop the entire game unless necessary.

---

# 47. FINAL ACCUSATION

The final accusation screen appears when the player has enough evidence.

The player must select:

### Suspect

```text
Maya Voss
Victor Hale
Nora Bennett
Daniel Mercer
```

### Motive

```text
Financial investigation
Personal revenge
Family conflict
Unknown
```

### Key reasoning

The player selects evidence supporting the accusation.

---

# 48. FINAL ACCUSATION REQUIREMENTS

The correct accusation requires:

### Culprit

Daniel Mercer.

### Motive

Prevent exposure of financial misconduct.

### Core reasoning

Daniel returned to the apartment after claiming he had left and had the motive and physical connection to the crime.

---

# 49. ACCUSATION VALIDATION

The system should evaluate:

```text
culpritCorrect
motiveCorrect
requiredEvidencePresent
timelineEstablished
```

Recommended:

```text
culpritCorrect = true
motiveCorrect = true
requiredEvidencePresent = true
timelineEstablished = true
```

for a perfect solve.

---

# 50. WRONG ACCUSATION

If the player selects an innocent suspect:

Display:

```text
THE CASE DOES NOT SUPPORT THIS ACCUSATION

Your conclusion conflicts with the established timeline.

Review the evidence and try again.
```

The player may return to investigation.

Do not delete progress.

---

# 51. PARTIAL ACCUSATION

If the player selects Daniel but lacks enough reasoning:

Display:

```text
Daniel remains the strongest suspect.

But the evidence is not yet sufficient to prove the accusation.

Continue the investigation.
```

---

# 52. PERFECT SOLVE

If all required elements are correct:

```text
CASE SOLVED

CULPRIT:
DANIEL MERCER

MOTIVE:
Prevent exposure of financial misconduct

KEY DISCOVERY:
Daniel returned after claiming he had left.
```

Then begin the final case reconstruction.

---

# 53. CASE RESULT SCREEN

Display:

```text
CASE SOLVED

The Last Call

CULPRIT
Daniel Mercer

MOTIVE
Financial misconduct

EVIDENCE FOUND
18 / 20

INTERVIEWS COMPLETED
4 / 4

TIMELINE
Complete

DEDUCTIONS
Complete

[VIEW CASE FILE]
[RETURN TO TITLE]
```

---

# 54. COMPLETION RATING

Optional rating:

```text
CASE SOLVED
```

Do not use combat-style stars.

Recommended ratings:

```text
SOLVED
STRONG CASE
PERFECT INVESTIGATION
```

A perfect investigation requires discovering and connecting all critical evidence.

---

# 55. OPTIONAL HINT SYSTEM

The game may include a hint button.

Hints should not immediately reveal the culprit.

Use three levels:

### Hint 1

> "Someone's timeline doesn't fit."

### Hint 2

> "Review the people who claimed they left."

### Hint 3

> "Compare Daniel's statement with the evidence surrounding the later part of the evening."

Hints may be limited.

---

# 56. HINT RULES

Hints must never say:

> "Daniel is the murderer."

before the player reaches the appropriate stage.

The hint system should guide deduction rather than replace it.

---

# 57. SAVE SYSTEM

Use local device/browser persistence.

The game should save automatically after:

- Evidence discovery
- Interview completion
- New contradiction
- Timeline confirmation
- Objective completion
- Major scene transition

---

# 58. SAVE DATA STRUCTURE

Recommended conceptual structure:

```text
CaseSaveData
    caseId
    currentStage
    currentLocation
    discoveredEvidence[]
    inspectedEvidence[]
    connectedEvidence[]
    interviewedSuspects[]
    askedQuestions[]
    revealedSecrets[]
    confirmedTimelineEvents[]
    deductions[]
    completedObjectives[]
    accusationState
    caseResult
```

The exact implementation language/framework may differ.

---

# 59. RESET CASE

Settings or title screen may provide:

**RESET CASE**

Require confirmation.

Example:

> "This will erase your current investigation."

Buttons:

```text
RESET
CANCEL
```

---

# 60. PAUSE SYSTEM

Pause menu:

```text
CASE FILE
SETTINGS
SAVE
RETURN TO TITLE
```

Because saving is automatic, a manual save button is optional.

---

# 61. SETTINGS

Minimum settings:

```text
Music
Sound Effects
Text Speed
Hints
Reset Case
```

All settings should persist locally.

---

# 62. AUDIO SYSTEM

Audio should support:

- Ambient room sound
- Subtle investigation sounds
- UI clicks
- Evidence discovery sound
- Contradiction sound
- Timeline confirmation sound
- Case solved sound

Avoid excessive dramatic effects.

---

# 63. FEEDBACK SYSTEM

Every meaningful action should have feedback.

Examples:

### Evidence

Small sound + evidence notification.

### Correct deduction

Connection animation + confirmation.

### Wrong deduction

Small neutral response.

### New objective

Objective notification.

### Final accusation

Distinct confirmation.

---

# 64. ACCESSIBILITY

The game should support:

- Large readable text
- High contrast
- Tap targets large enough for mobile
- Adjustable text speed
- No essential information conveyed by color alone
- Optional sound
- Pause at any time

---

# 65. MOBILE UI RULES

Avoid tiny buttons.

Recommended minimum interactive target:

**44–48 CSS pixels** where practical.

Use:

- Bottom navigation
- Cards
- Panels
- Modal overlays
- Large touch targets

---

# 66. ORIENTATION

Preferred:

**Landscape**

Reason:

The investigation UI benefits from horizontal space.

If portrait support is implemented, it should be treated as an enhancement rather than a requirement.

---

# 67. RESPONSIVE DESIGN

The game should adapt to:

- Phones
- Tablets
- Different screen ratios
- Android browser/webview dimensions

Do not hard-code the interface for one screen size.

---

# 68. PERFORMANCE REQUIREMENTS

The game should remain lightweight.

Avoid:

- Large unnecessary assets
- Heavy 3D scenes
- Complex physics
- Continuous expensive animations
- Excessive particle effects

The mystery itself is the primary experience.

---

# 69. OFFLINE / NETWORK REQUIREMENT

Once the game has loaded, the core case should not require a continuous network connection.

Case data should be local.

If Google AI Studio generates an app architecture requiring an online backend, the backend should not be necessary for basic Case 001 gameplay.

---

# 70. ERROR HANDLING

If an evidence interaction fails:

Do not soft-lock the case.

If dialogue fails:

Allow the player to return to the suspect list.

If a save fails:

Keep the current in-memory state.

If an invalid deduction is selected:

Do not erase evidence.

---

# 71. SOFT-LOCK PREVENTION

The player must never be permanently prevented from solving the case because they:

- Asked questions in the wrong order
- Presented evidence early
- Visited the wrong suspect first
- Made an incorrect deduction
- Left the crime scene
- Closed the case file

The investigation should always remain recoverable.

---

# 72. INFORMATION HIERARCHY

The game should separate:

### FACT

Directly established information.

### STATEMENT

Something a suspect claims.

### INFERENCE

Something the player concludes.

### TRUTH

The canonical hidden reality.

The UI should not expose hidden truth directly.

---

# 73. GAMEPLAY DIFFICULTY

Case 001 should be designed for a first-time detective-game player.

The player should understand the mechanics quickly.

Difficulty should come from:

- Connecting information
- Remembering statements
- Comparing timelines
- Recognizing contradictions

Not from:

- Pixel hunting
- Random guessing
- Extremely obscure clues
- Timed dialogue
- Punishment for experimentation

---

# 74. EXPECTED PLAYTIME

Target:

**30–60 minutes**

for a first complete investigation.

A thorough player may spend longer reviewing evidence.

---

# 75. REPLAYABILITY

Case 001 does not need procedural generation.

Replay value comes from:

- Trying different interview orders
- Discovering missed evidence
- Achieving a perfect investigation
- Reviewing the timeline
- Testing alternative accusations

---

# 76. GAMEPLAY LOOP EXAMPLE

Example player session:

```text
Player enters apartment
        ↓
Finds phone
        ↓
Discovers 10:42 call
        ↓
Checks desk
        ↓
Finds financial documents
        ↓
Interviews Maya
        ↓
Verifies Maya's timeline
        ↓
Interviews Victor
        ↓
Verifies Victor's dinner
        ↓
Interviews Nora
        ↓
Discovers copied files
        ↓
Interviews Daniel
        ↓
Notices "left before 10"
        ↓
Finds return evidence
        ↓
Timeline contradiction
        ↓
Connects motive
        ↓
Accuses Daniel
        ↓
CASE SOLVED
```

---

# 77. CORE SYSTEM DEPENDENCIES

The implementation should conceptually follow:

```text
CASE DATA
   ↓
GAME STATE
   ↓
EXPLORATION
   ↓
EVIDENCE SYSTEM
   ↓
DIALOGUE SYSTEM
   ↓
DEDUCTION SYSTEM
   ↓
TIMELINE SYSTEM
   ↓
ACCUSATION SYSTEM
   ↓
RESULT SYSTEM
```

---

# 78. DATA-DRIVEN DESIGN

Where possible, story content should be stored separately from gameplay code.

Recommended data structures:

```text
EvidenceData
SuspectData
DialogueNode
QuestionData
TimelineEvent
DeductionRule
ObjectiveData
CaseData
```

This makes it easier to add future cases.

---

# 79. CASE DATA SHOULD BE SEPARATE

Do not hard-code every dialogue line directly into UI logic.

Instead:

```text
Case 001
    Evidence
    Suspects
    Dialogue
    Timeline
    Deductions
    Objectives
    Ending
```

The application should load the case data.

---

# 80. FUTURE CASE SUPPORT

The architecture should make it possible to eventually add:

```text
CASE-001
CASE-002
CASE-003
...
```

without rebuilding the entire application.

However:

**Do not build Case 002 now.**

Finish Case 001 first.

---

# 81. IMPLEMENTATION PRIORITY

Build systems in this order:

## Phase 1

App shell

## Phase 2

Main menu

## Phase 3

Crime scene

## Phase 4

Interaction system

## Phase 5

Evidence system

## Phase 6

Case file

## Phase 7

Suspect interviews

## Phase 8

Evidence presentation

## Phase 9

Timeline

## Phase 10

Deduction board

## Phase 11

Final accusation

## Phase 12

Case result

## Phase 13

Save/load

## Phase 14

Polish

---

# 82. MINIMUM PLAYABLE VERSION

Before visual polish, the game must support:

```text
Start Case
↓
Explore Scene
↓
Collect Evidence
↓
Open Case File
↓
Interview Suspects
↓
Present Evidence
↓
Discover Contradiction
↓
Reconstruct Timeline
↓
Accuse Suspect
↓
Receive Result
```

If this works end-to-end, the project has a playable foundation.

---

# 83. IMPORTANT AI IMPLEMENTATION RULE

Do not attempt to build every advanced feature simultaneously.

Implement one complete vertical slice first:

```text
Crime Scene
+
One Evidence Item
+
One Suspect
+
One Interview
+
Evidence Presentation
+
Save State
```

Then expand using the canonical data.

---

# 84. NO UNITY

This project does not use Unity.

Do not:

- Create Unity scenes
- Create Unity prefabs
- Create Unity C# scripts
- Assume Unity APIs
- Reference Unity packages

The final application must be web/Android compatible using the environment selected by Google AI Studio.

---

# 85. FINAL GAMEPLAY PRINCIPLE

The most important system is not the UI.

It is the relationship between:

```text
WHAT SOMEONE SAID
        +
WHAT THE PLAYER FOUND
        +
WHEN IT HAPPENED
        =
WHAT THE PLAYER CAN PROVE
```

The game succeeds when the player feels:

> "I solved this."

not:

> "The game eventually told me."

---

# 86. CANONICAL END-TO-END LOOP

The complete intended player experience is:

```text
START
  ↓
READ BRIEFING
  ↓
ENTER APARTMENT
  ↓
SEARCH
  ↓
COLLECT CLUES
  ↓
OPEN CASE FILE
  ↓
INTERVIEW SUSPECTS
  ↓
COMPARE STORIES
  ↓
PRESENT EVIDENCE
  ↓
FIND CONTRADICTIONS
  ↓
BUILD TIMELINE
  ↓
CONNECT MOTIVE + PRESENCE + LIE
  ↓
ACCUSATION
  ↓
CASE RECONSTRUCTION
  ↓
CASE SOLVED
```

This is the core gameplay loop of **The Last Call**.
