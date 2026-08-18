# THE LAST CALL
# TECHNICAL ARCHITECTURE SPECIFICATION

**Case ID:** CASE-001  
**Document Version:** 1.0  
**Platform:** Android  
**Development Environment:** Google AI Studio Web  
**Engine:** None required  
**Architecture Status:** Canonical Technical Specification

---

# 1. PURPOSE

This document defines how **The Last Call** should be implemented as a complete Android application/game.

The implementation AI must use this document together with:

- `prd.md`
- `game_design_bible.md`
- `gamedesign.md`
- `ui_ux_bible.md`
- `evidence_bible.md`
- `case_data.md`
- `dialogue_bible.md`

The technical architecture must implement the game design rather than redesign it.

---

# 2. CORE TECHNICAL PRINCIPLES

The application must be:

```text
Android-first
Touch-first
Offline-first
Data-driven
Deterministic
Saveable
Modular
Maintainable
Responsive
```

The game must not require:

- Unity
- Unreal Engine
- A desktop game engine
- A backend server
- User accounts
- Cloud storage
- Internet access during normal gameplay

---

# 3. APPLICATION MODEL

The game is an interactive investigation application.

Conceptually:

```text
App
 ├── Main Menu
 ├── Case Briefing
 ├── Investigation
 │    ├── Crime Scene
 │    ├── Interviews
 │    ├── Evidence
 │    ├── Timeline
 │    ├── Deductions
 │    └── Objectives
 ├── Final Accusation
 ├── Case Result
 └── Settings
```

---

# 4. RECOMMENDED TECHNOLOGY

If Google AI Studio is generating the application as a web-based Android-capable application, prefer:

```text
TypeScript
React
CSS
LocalStorage / IndexedDB
PWA support
Capacitor when Android packaging is required
```

If the selected project template already provides an equivalent Android-compatible architecture, do not rewrite the entire project merely to match this recommendation.

The priority is a reliable working application.

---

# 5. NO UNITY

Unity must not be introduced.

Do not create:

```text
Unity scenes
Unity prefabs
MonoBehaviours
ScriptableObjects
Unity Animator controllers
Unity packages
```

The entire game should function inside the chosen web/Android application architecture.

---

# 6. APPLICATION LAYERS

Use a layered architecture.

```text
UI Layer
    ↓
Game Controller Layer
    ↓
Case State Layer
    ↓
Data Layer
    ↓
Persistence Layer
```

---

# 7. UI LAYER

Responsible for:

- Rendering screens
- Buttons
- Touch interactions
- Dialogues
- Evidence cards
- Timeline
- Case file
- Animations
- Accessibility
- Navigation

UI components must not directly modify raw save data.

Instead:

```text
UI
 ↓
Game Controller
 ↓
Case State
```

---

# 8. GAME CONTROLLER LAYER

Responsible for:

- Starting cases
- Advancing investigation
- Discovering evidence
- Recording interviews
- Creating contradictions
- Creating deductions
- Checking accusation readiness
- Completing objectives
- Triggering saves

Recommended conceptual controllers:

```text
AppController
CaseController
InvestigationController
EvidenceController
DialogueController
TimelineController
DeductionController
AccusationController
SaveController
AudioController
```

These do not have to be separate files if that would create unnecessary complexity.

---

# 9. CASE STATE

Central state object:

```text
CaseState {
    caseId
    caseStatus

    currentScene
    currentObjective

    discoveredEvidence[]
    inspectedEvidence[]

    interviewedSuspects[]
    discoveredStatements[]

    timelineEvents[]
    deductions[]
    contradictions[]

    completedObjectives[]

    accusation
    investigationProgress
}
```

---

# 10. IMMUTABLE CASE DATA

Static authored data should be separated from mutable player state.

Example:

```text
CaseData
    evidence definitions
    suspect definitions
    dialogue definitions
    timeline definitions
    objective definitions
    deduction rules
    accusation rules
```

Player state contains:

```text
CaseState
    discovered IDs
    completed IDs
    current progress
```

Never save the entire authored database if only IDs are needed.

---

# 11. DATA-DRIVEN DESIGN

All major case content should be data-driven.

Do not hardcode every clue into UI components.

Bad:

```text
if player clicks phone:
    show Elias's phone text
```

Better:

```text
inspectEvidence("E001")
```

Then retrieve:

```text
EvidenceData["E001"]
```

This allows future cases to reuse the same systems.

---

# 12. RECOMMENDED DATA TYPES

## Evidence

```text
EvidenceData {
    id
    name
    category
    location
    discoveryText
    inspectionText
    significanceText

    requiredEvidence[]
    relatedSuspects[]
    relatedEvidence[]
    relatedTimelineEvents[]

    critical
    discoverable
}
```

---

# 13. SUSPECT

```text
SuspectData {
    id
    name
    role
    relationship
    description

    dialogueIds[]
    relevantEvidence[]
    alibiEvidence[]
}
```

---

# 14. STATEMENT

```text
StatementData {
    id
    suspectId
    question
    answer
    relatedEvidence[]
    contradictionRules[]
}
```

---

# 15. TIMELINE EVENT

```text
TimelineEventData {
    id
    time
    title
    description
    relatedEvidence[]
    confidence
}
```

---

# 16. OBJECTIVE

```text
ObjectiveData {
    id
    title
    description
    requirements[]
    completionCondition
}
```

---

# 17. DEDUCTION

```text
DeductionRule {
    id
    requiredEvidence[]
    requiredStatements[]
    requiredTimelineEvents[]
    result
}
```

---

# 18. CONTRADICTION

```text
ContradictionData {
    id
    suspectId
    statementIds[]
    evidenceIds[]
    title
    explanation
    severity
}
```

---

# 19. ACCUSATION

```text
AccusationData {
    suspectId
    selectedEvidence[]
    explanation
    valid
}
```

---

# 20. ROUTING ARCHITECTURE

Recommended routes/screens:

```text
/
 /menu
 /briefing
 /case
 /case/scene
 /case/evidence
 /case/evidence/:id
 /case/suspects
 /case/suspect/:id
 /case/interview/:id
 /case/timeline
 /case/deductions
 /case/objectives
 /case/accusation
 /case/result
 /settings
```

If the project uses a state-based single-page architecture instead of URL routing, preserve the same logical screen structure.

---

# 21. NAVIGATION RULE

The player should always know where they are.

Every investigation screen should provide access to:

```text
Back
Case File
```

The Case File should provide:

```text
Evidence
Suspects
Timeline
Deductions
Objectives
```

---

# 22. ANDROID BACK BUTTON

Android back behavior:

```text
Interview
    → Case File / previous screen

Evidence Detail
    → Evidence List

Suspect Detail
    → Suspect List

Timeline
    → Case File

Case File
    → Investigation

Investigation
    → Briefing / Menu depending on state
```

Do not exit the application immediately when the player presses Back.

If the player is at the root screen:

Show a normal Android exit confirmation if appropriate.

---

# 23. TOUCH INPUT

Every interactive control must have a comfortable touch target.

Minimum target recommendation:

```text
44px–48px minimum
```

Prefer larger targets on phones.

Do not rely on:

- Hover
- Right-click
- Keyboard-only interaction
- Tiny icons

---

# 24. RESPONSIVE DESIGN

Primary target:

```text
Android phone portrait
```

Secondary:

```text
Android tablet
Landscape phones
Desktop browser during development
```

The layout must adapt rather than simply stretch.

---

# 25. ORIENTATION

Preferred:

```text
Portrait
```

The game should remain usable if the implementation supports landscape.

Do not make core investigation information dependent on landscape-only layout.

---

# 26. CASE FILE ARCHITECTURE

The Case File is the central information hub.

Tabs:

```text
Evidence
Suspects
Timeline
Deductions
Objectives
```

The player should be able to switch tabs without losing progress.

---

# 27. EVIDENCE SYSTEM

Evidence lifecycle:

```text
LOCKED
 ↓
DISCOVERED
 ↓
INSPECTED
 ↓
CONNECTED
 ↓
CRITICAL
```

Not every evidence item must reach every state.

---

# 28. DISCOVERING EVIDENCE

When the player discovers evidence:

```text
1. Validate evidence ID.
2. Check discovery requirements.
3. Add ID to discoveredEvidence.
4. Trigger feedback.
5. Update objective state.
6. Recalculate deductions.
7. Autosave.
```

---

# 29. INSPECTING EVIDENCE

When inspected:

```text
1. Load EvidenceData.
2. Show discovery/inspection information.
3. Mark as inspected.
4. Evaluate unlocks.
5. Recalculate deductions.
6. Save.
```

---

# 30. DUPLICATE DISCOVERY

Discovering the same evidence twice must not create duplicates.

Example:

```text
discoveredEvidence = ["E001", "E002"]
```

not:

```text
["E001", "E002", "E002"]
```

---

# 31. INTERVIEW SYSTEM

Interview flow:

```text
Select Suspect
    ↓
Suspect Overview
    ↓
Available Questions
    ↓
Question
    ↓
Answer
    ↓
Record Statement
    ↓
Check Evidence Reactions
    ↓
Unlock Questions / Contradictions
```

---

# 32. INTERVIEW STATE

Track:

```text
interviewedSuspects[]
askedQuestions[]
recordedStatements[]
unlockedQuestions[]
presentedEvidence[]
```

---

# 33. DIALOGUE SYSTEM

Dialogue content must come from `dialogue_bible.md`.

Do not generate new canonical dialogue dynamically.

The system should support:

```text
dialogue
question
answer
evidence reaction
contradiction
unlock
conditional branch
```

---

# 34. EVIDENCE PRESENTATION

Flow:

```text
Interview
 ↓
Present Evidence
 ↓
Select Evidence
 ↓
Validate relationship
 ↓
Show suspect reaction
 ↓
Update state
```

Evidence remains in inventory.

---

# 35. IRRELEVANT EVIDENCE

If evidence is unrelated:

Show a natural response.

Example:

> "I don't see how that relates to me."

Do not expose system terminology such as:

```text
INVALID
WRONG CLUE
FAIL
```

---

# 36. CONTRADICTION ENGINE

Contradictions should be rule-based.

Example:

```text
Rule C001

IF
    ST006 exists
    AND ST007 exists
    AND E018 exists

THEN
    create C001
```

Once created, it should not be recreated repeatedly.

---

# 37. DEDUCTION ENGINE

Whenever state changes:

```text
discover evidence
interview suspect
record statement
verify timeline
create contradiction
```

run:

```text
evaluateDeductionRules()
```

Only newly satisfied deductions should be added.

---

# 38. TIMELINE SYSTEM

The timeline is player-readable.

It should show:

```text
Time
Event
Source
Confidence
```

Example:

```text
10:42 PM
Elias makes final outgoing call.
Source: E002
```

---

# 39. TIMELINE CONFIDENCE

Use:

```text
CONFIRMED
SUPPORTED
UNCERTAIN
```

Do not show internal certainty percentages.

---

# 40. OBJECTIVE SYSTEM

Objectives should update automatically.

Example:

```text
O001:
Investigate the apartment.

Requirements:
E001
E004
E007
E008
```

Once complete:

```text
completedObjectives += O001
```

---

# 41. PROGRESSION SYSTEM

Do not use arbitrary XP.

Progress should represent investigation completion.

Recommended:

```text
Evidence progress
+
Interview progress
+
Timeline progress
+
Deduction progress
```

---

# 42. ACCUSATION SYSTEM

The accusation screen must contain:

```text
Suspect selection
Motive selection / explanation
Evidence selection
Final reasoning
Confirm accusation
```

The exact UI can vary.

---

# 43. ACCUSATION VALIDATION

Recommended validation:

```text
IF suspect == S004
AND C001 exists
AND M001 exists
THEN
    correct = true
```

Additional supporting evidence may improve result quality.

---

# 44. WRONG ACCUSATION

Wrong accusation should not delete progress.

Return the player to the investigation.

Show:

```text
Your evidence does not sufficiently support
this accusation.
```

---

# 45. PREMATURE ACCUSATION

If the player tries to accuse Daniel too early:

Do not automatically solve the case.

Instead:

```text
The suspicion is strong,
but the case is not yet established.
```

---

# 46. SAVE SYSTEM

The game must save locally.

Recommended:

```text
LocalStorage
```

or:

```text
IndexedDB
```

Use IndexedDB if the state becomes large.

---

# 47. SAVE SCHEMA

Example:

```text
SaveData {
    version
    timestamp

    caseState
    settings
}
```

---

# 48. SAVE VERSIONING

Every save must include:

```text
version: 1
```

Future versions should support migration.

Example:

```text
if save.version === 1:
    migrateV1ToV2()
```

Do not discard old saves merely because the schema changes.

---

# 49. AUTOSAVE

Autosave after:

```text
Evidence discovery
Interview completion
Evidence presentation
Contradiction creation
Deduction creation
Objective completion
Scene transitions
```

---

# 50. SAVE FAILURE

If saving fails:

Do not crash.

Display a subtle warning:

```text
Progress could not be saved.
Please try again.
```

Continue allowing gameplay.

---

# 51. RESET

The player must have a clear:

```text
Restart Case
```

option.

Confirmation:

```text
Restart this case?

Your current investigation progress will be lost.
```

---

# 52. SETTINGS PERSISTENCE

Settings should survive case reset.

Settings may include:

```text
Text size
Sound
Music
Haptics
Animations
Reduced motion
```

---

# 53. AUDIO ARCHITECTURE

Separate:

```text
Music
Ambient
UI
Dialogue
Evidence
```

Recommended audio events:

```text
UI_CLICK
EVIDENCE_DISCOVERED
EVIDENCE_INSPECTED
DIALOGUE_OPEN
DIALOGUE_ADVANCE
CONTRADICTION_DISCOVERED
DEDUCTION_CREATED
CASE_SOLVED
CASE_INCOMPLETE
```

---

# 54. AUDIO FALLBACK

If audio assets are unavailable:

The game must remain fully playable.

Never block progression because an audio file failed.

---

# 55. HAPTICS

Use subtle haptics for:

```text
Evidence discovery
Important contradiction
Major deduction
Final confirmation
```

Provide a setting to disable haptics.

---

# 56. ASSET ARCHITECTURE

Recommended:

```text
assets/
    images/
        characters/
        environments/
        evidence/
        ui/

    audio/
        music/
        ambient/
        sfx/

    fonts/

    icons/
```

---

# 57. EVIDENCE IMAGES

Evidence should use stable IDs:

```text
e001_phone
e002_call
e003_call_history
...
```

Do not depend on display names for file lookup.

---

# 58. MISSING ASSETS

If an asset is missing:

Do not crash.

Use a controlled placeholder.

Example:

```text
Evidence image unavailable
```

during development.

Final build should replace all critical placeholders.

---

# 59. IMAGE OPTIMIZATION

Mobile images should be optimized.

Prefer:

```text
WebP
AVIF where supported
compressed PNG for transparency
```

Avoid unnecessarily huge source images.

---

# 60. PERFORMANCE TARGET

Target:

```text
Stable 60 FPS where practical
Fast screen transitions
No long blocking operations
No unnecessary re-renders
```

The game is primarily UI/investigation focused, so responsiveness is more important than graphical complexity.

---

# 61. MEMORY MANAGEMENT

Avoid:

- Loading every image at startup
- Duplicating large image data
- Recreating large objects every render
- Unbounded event listeners

Load heavy assets when needed.

---

# 62. STATE MANAGEMENT

There must be one authoritative case state.

Avoid:

```text
Evidence screen has one copy.
Timeline screen has another copy.
Interview screen has another copy.
```

Instead:

```text
CaseState
    ↓
all screens
```

---

# 63. EVENT SYSTEM

A lightweight event system can be used.

Recommended events:

```text
CASE_STARTED
EVIDENCE_DISCOVERED
EVIDENCE_INSPECTED
SUSPECT_INTERVIEWED
STATEMENT_RECORDED
EVIDENCE_PRESENTED
CONTRADICTION_CREATED
DEDUCTION_CREATED
OBJECTIVE_COMPLETED
ACCUSATION_SUBMITTED
CASE_SOLVED
CASE_RESET
```

---

# 64. EVENT RULE

Events are notifications.

They should not become the only source of truth.

Example:

```text
EVIDENCE_DISCOVERED
```

should trigger UI updates, but:

```text
discoveredEvidence[]
```

remains the authoritative state.

---

# 65. ERROR HANDLING

Errors should be:

```text
Contained
Recoverable
User-friendly
Logged in development
```

Never show raw stack traces to players.

---

# 66. DEBUG LOGGING

Development mode may log:

```text
[CASE] Evidence discovered: E018
[CASE] Contradiction created: C001
[CASE] Deduction created: D006
[CASE] Objective completed: O006
```

Disable verbose logging in production.

---

# 67. SECURITY / DATA INTEGRITY

This is an offline single-player game.

Do not over-engineer security.

However:

- Validate IDs
- Validate save data
- Handle corrupted saves
- Prevent duplicate IDs
- Prevent impossible state transitions

---

# 68. CORRUPTED SAVE

If a save cannot be parsed:

Show:

```text
Your saved investigation could not be loaded.

You can start a new investigation.
```

Do not crash.

---

# 69. OFFLINE REQUIREMENT

After installation, normal gameplay must work without internet.

No critical game logic should depend on:

```text
API calls
Remote databases
Cloud AI
External authentication
```

---

# 70. AI RUNTIME RULE

The finished game should not need an AI model to solve or generate the case.

The case is authored and deterministic.

Google AI Studio is the development environment, not an in-game dependency.

---

# 71. PERSISTENCE OF CANONICAL DATA

Static case data can be:

```text
JSON
TypeScript objects
local bundled data
```

Choose the simplest reliable approach.

Do not put all case data into a remote server.

---

# 72. CONTENT LOADING

Recommended:

```text
case-data/
    case.json
    evidence.json
    suspects.json
    dialogue.json
    timeline.json
    objectives.json
    deductions.json
```

Alternatively, TypeScript modules may be used if they are easier for the project.

---

# 73. CONTENT VALIDATION

During development, automatically validate:

```text
Every evidence ID is unique.
Every suspect ID is unique.
Every referenced evidence ID exists.
Every referenced suspect ID exists.
Every referenced timeline ID exists.
Every dialogue ID exists.
Every objective ID exists.
```

---

# 74. ORPHAN DETECTION

Development validation should identify:

```text
Evidence with no reference
Dialogue with missing suspect
Objective with impossible requirement
Deduction referencing missing evidence
Timeline event referencing missing evidence
```

---

# 75. CASE VALIDATOR

Recommended development function:

```text
validateCaseData()
```

It should report:

```text
ERROR:
E018 references missing suspect S999
```

rather than allowing silent failure.

---

# 76. STARTUP VALIDATION

On development startup:

```text
load static data
validate static data
initialize case state
load save
validate save
render menu
```

Do not perform expensive validation repeatedly during normal gameplay.

---

# 77. CASE INITIALIZATION

New case:

```text
createEmptyCaseState()
loadCaseData()
setCaseStatus(IN_PROGRESS)
setCurrentObjective(O001)
save()
```

---

# 78. CASE COMPLETION

When correct accusation is submitted:

```text
caseStatus = SOLVED
save()
navigateToResult()
```

Do not erase investigation data.

The player should be able to review the completed case.

---

# 79. RESULT SCREEN DATA

Result screen should know:

```text
correct
selectedSuspect
evidenceCount
objectivesCompleted
deductionsFound
caseCompleteness
```

---

# 80. REVIEW AFTER SOLUTION

After solving, allow:

```text
Review Evidence
Review Suspects
Review Timeline
Review Deductions
Review Final Reasoning
Restart Case
Return to Menu
```

---

# 81. ACCESSIBILITY

Support:

```text
Readable text
High contrast
Scalable text where practical
Clear focus states
No information conveyed only by color
Large touch targets
Reduced motion option
Sound optional
Haptics optional
```

---

# 82. COLOR ACCESSIBILITY

Do not use only:

```text
red = contradiction
green = correct
```

Use:

```text
icon
label
text
```

alongside color.

---

# 83. TYPOGRAPHY

The interface should prioritize:

```text
Readable body text
Clear headings
Strong hierarchy
Comfortable line height
Adequate spacing
```

Do not use overly decorative fonts for important information.

---

# 84. LOADING STATES

If a screen requires asynchronous loading:

Show a lightweight loading state.

Never leave the player with a blank screen.

---

# 85. EMPTY STATES

Examples:

Evidence:

```text
No evidence discovered yet.
Search the scene carefully.
```

Deductions:

```text
No deductions formed yet.
Continue investigating.
```

Timeline:

```text
Your timeline is still incomplete.
```

---

# 86. MOBILE PERFORMANCE RULE

Do not render hundreds of unnecessary DOM elements at once.

For long lists:

Use:

```text
virtualization
pagination
or simple compact lists
```

as appropriate.

Case 001 is small enough that simple lists are acceptable.

---

# 87. ANIMATION RULE

Animations should communicate:

```text
Navigation
Discovery
Importance
Feedback
```

Avoid animations that delay investigation.

Every major action should feel immediate.

---

# 88. INPUT FEEDBACK

Buttons should visibly respond to:

```text
tap
disabled state
selected state
completed state
```

---

# 89. DISABLED ACTIONS

If an action is unavailable:

Prefer explaining why.

Example:

```text
You need more evidence before making
a final accusation.
```

Avoid silently disabling everything.

---

# 90. CONTENT / CODE SEPARATION

Whenever practical:

```text
Code = mechanics
Data = case content
```

This is critical because future cases should be possible without rebuilding the entire application.

---

# 91. FUTURE CASE SUPPORT

Architecture should allow:

```text
CASE-001
CASE-002
CASE-003
...
```

without changing core systems.

Case loader concept:

```text
loadCase(caseId)
```

---

# 92. MULTI-CASE STRUCTURE

Recommended:

```text
cases/
    CASE-001/
        case.json
        evidence.json
        suspects.json
        dialogue.json
        timeline.json
        objectives.json
        deductions.json
```

---

# 93. CASE SWITCHING

The app should not assume CASE-001 is permanently the only case.

The menu can initially show:

```text
THE LAST CALL
Available
```

Future cases may be:

```text
Locked
Coming Soon
```

Do not implement fake cases.

---

# 94. BUILD TARGET

The final application should be capable of being packaged for Android.

Possible route:

```text
Web application
↓
PWA
↓
Capacitor
↓
Android project
↓
APK / AAB
```

The exact packaging process may depend on Google AI Studio's generated project environment.

---

# 95. PWA REQUIREMENTS

If the generated app supports PWA:

Provide:

```text
manifest
service worker
offline assets
app icons
theme metadata
```

The application should remain usable offline.

---

# 96. ANDROID SAFE AREAS

The UI must account for:

```text
status bar
navigation bar
display cutouts
gesture areas
```

Do not place critical buttons against screen edges.

---

# 97. APP ICON

Use a simple detective-themed icon.

It should remain recognizable at small size.

Do not use copyrighted logos.

---

# 98. SPLASH SCREEN

Keep it short.

Recommended:

```text
THE LAST CALL
```

followed by the main menu.

Do not create an unnecessary long cinematic intro.

---

# 99. FIRST LAUNCH

First launch:

```text
Splash
↓
Main Menu
↓
New Case
↓
Briefing
```

Do not immediately throw the player into complex menus.

---

# 100. RETURNING PLAYER

Returning player:

```text
Splash
↓
Main Menu
↓
Continue Case
```

if a save exists.

---

# 101. NO ACCOUNT SYSTEM

No login is required.

No:

```text
Email
Password
Google account
Social account
```

---

# 102. NO PAYWALL

Case 001 should be fully playable without:

```text
In-app purchases
Ads
Energy systems
Premium currency
Subscriptions
```

---

# 103. NO NETWORK DEPENDENCY

The game must not stop working because:

```text
Wi-Fi is off
Mobile data is off
Server is unavailable
```

---

# 104. DEVELOPMENT ORDER

Recommended implementation order:

```text
PHASE 1
Project setup

PHASE 2
Global navigation

PHASE 3
Case state

PHASE 4
Save system

PHASE 5
Crime scene

PHASE 6
Evidence system

PHASE 7
Case file

PHASE 8
Suspect system

PHASE 9
Dialogue/interviews

PHASE 10
Timeline

PHASE 11
Deduction engine

PHASE 12
Accusation

PHASE 13
Result/review

PHASE 14
Audio/haptics

PHASE 15
Polish

PHASE 16
Android packaging
```

---

# 105. DEVELOPMENT RULE

Do not implement the entire application in one giant uncontrolled component.

Break the system into understandable modules.

---

# 106. DEVELOPMENT RULE: WORKING FIRST

First achieve:

```text
Start case
→ inspect evidence
→ interview suspect
→ record evidence
→ create contradiction
→ solve case
→ save/load
```

Then improve visual polish.

---

# 107. DEVELOPMENT RULE: NO PREMATURE POLISH

Do not spend large amounts of implementation effort on:

- elaborate animations
- particle effects
- advanced shaders
- complex transitions

until the investigation loop works.

---

# 108. DEVELOPMENT RULE: TEST EACH SYSTEM

After implementing each system:

```text
Build
Run
Test
Fix
Continue
```

Do not stack ten untested systems together.

---

# 109. SMOKE TEST

The minimum smoke test:

```text
Launch app
Start case
Open crime scene
Inspect phone
Find E002
Open case file
Interview Daniel
Record E020
Find E018
See contradiction
Find E019
Make correct accusation
See solved result
Close app
Reopen app
Continue case
```

---

# 110. FULL CASE TEST

Verify:

```text
All 20 evidence items
All suspects
All interview branches
All critical reactions
All timeline events
All deductions
Wrong accusation
Premature accusation
Save/load
Reset
Back navigation
Offline behavior
```

---

# 111. CRITICAL BUGS

These are release blockers:

```text
Case cannot start
Evidence cannot be saved
Evidence disappears
Dialogue cannot advance
Critical contradiction cannot trigger
Correct accusation fails
Wrong accusation incorrectly solves case
Save corrupts progress
App crashes on Android Back
Critical text becomes unreadable
```

---

# 112. NON-CRITICAL BUGS

Examples:

```text
Minor animation glitch
Non-critical image placeholder
Small spacing issue
Minor audio timing issue
```

These can be fixed after the core game works.

---

# 113. ARCHITECTURAL GOLDEN RULE

The player should experience:

```text
Observe
→ Investigate
→ Record
→ Question
→ Connect
→ Contradict
→ Deduce
→ Accuse
```

The software architecture should support exactly this loop.

---

# 114. FINAL TECHNICAL REQUIREMENT

The finished implementation must be a complete playable Android detective game, not a prototype that only demonstrates UI screens.

At minimum, the player must be able to:

```text
START THE CASE
INVESTIGATE
DISCOVER EVIDENCE
INTERVIEW SUSPECTS
PRESENT EVIDENCE
RECORD STATEMENTS
BUILD THE TIMELINE
CREATE DEDUCTIONS
IDENTIFY CONTRADICTIONS
MAKE AN ACCUSATION
RECEIVE A RESULT
SAVE AND RESUME
```

---

# 115. FINAL IMPLEMENTATION PRINCIPLE

Do not build the application around the documents as static pages.

Build the actual game systems described by the documents.

The `.md` files are the specification.

The application is the implementation.

The player should never need to read these documents to understand the game.
