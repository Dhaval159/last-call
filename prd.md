# THE LAST CALL
## Product Requirements Document (PRD)

**Version:** 1.0  
**Platform:** Android  
**Development Environment:** Google AI Studio Web  
**Project Type:** Single-player detective mystery game  
**Genre:** Detective / Investigation / Mystery  
**Orientation:** Portrait  
**Primary Input:** Touch  
**Internet Requirement:** None after installation  
**Target:** A complete, playable first detective case

---

# 1. Product Vision

**The Last Call** is a mobile detective game focused on investigation, evidence collection, suspect interrogation, deduction, and accusation.

The player takes the role of an investigator assigned to a suspicious death inside an apartment.

The game should make the player feel like they are actually building a case rather than simply reading a predetermined story.

The player must:

1. Investigate the crime scene.
2. Discover evidence.
3. Interview suspects.
4. Detect contradictions.
5. Connect evidence with statements.
6. Determine what actually happened.
7. Accuse the correct suspect.
8. Provide enough evidence to justify the accusation.

The first release contains **one complete case**.

The project must prioritize **completion, reliability, and polish over scope**.

---

# 2. Core Design Principle

The entire project must be built around one principle:

> **The player should solve the mystery, not the game.**

The game must not simply reveal the murderer after the player clicks through enough dialogue.

Important information should be distributed across:

- Crime-scene evidence
- Suspect statements
- Phone records
- Physical objects
- Timelines
- Contradictions
- Relationships between clues

The player should have enough information to logically reach the correct conclusion.

---

# 3. Project Scope

## Version 1.0 contains exactly:

- 1 detective case
- 1 crime scene
- 1 victim
- 4 primary suspects
- Approximately 15–20 meaningful clues
- Suspect interrogation system
- Evidence inventory
- Evidence inspection
- Dialogue system
- Contradiction system
- Case notes
- Evidence/case board
- Final accusation system
- Multiple ending outcomes
- Save/load functionality
- Main menu
- Case selection screen
- Settings screen
- Complete playable game loop

---

# 4. Explicit Non-Goals

The first version must NOT attempt to include:

- Multiple cases
- Multiplayer
- Online accounts
- Multiplayer investigation
- Procedural mystery generation
- AI-generated mysteries during gameplay
- Real-money systems
- Ads
- In-app purchases
- Cloud saves
- Complex 3D environments
- Open-world exploration
- Character customization
- Combat
- Character progression
- Inventory crafting
- Complex physics
- Real-time gameplay
- Voice acting
- External APIs unless absolutely required

The goal is to finish a polished detective game, not create a general-purpose detective engine.

---

# 5. Target User Experience

The game should feel:

- Mysterious
- Serious
- Atmospheric
- Intuitive
- Rewarding
- Slightly tense
- Easy to understand
- Difficult enough to require actual reasoning

The interface should not feel like a generic AI-generated application.

It should feel like a deliberately designed indie detective game.

---

# 6. Core Gameplay Loop

The primary gameplay loop is:

```text
START CASE
    ↓
Read Case Briefing
    ↓
Investigate Crime Scene
    ↓
Discover Evidence
    ↓
Review Evidence
    ↓
Interview Suspects
    ↓
Compare Statements With Evidence
    ↓
Discover Contradictions
    ↓
Build Case
    ↓
Review Timeline
    ↓
Accuse Suspect
    ↓
Explain Case
    ↓
CASE RESULT
```

The player should be able to return to previously investigated locations and review previously collected information.

---

# 7. Game Structure

The application should contain the following major screens:

## 7.1 Splash / Loading Screen

Displays the game identity while the application initializes.

Requirements:

- Fast loading
- Minimal animation
- No unnecessary waiting

---

# 8. Main Menu

The main menu should contain:

- Continue
- Cases
- New Game
- Settings
- About

If there is no active investigation:

**Continue** should be disabled or hidden.

The menu should establish the game's atmosphere immediately.

---

# 9. Case Selection

The first release contains one case:

### Case 001 — The Last Call

The case card should show:

- Case number
- Case title
- Status
- Short description

Possible statuses:

- Not Started
- In Progress
- Solved

Future cases can use locked cards, but they should not be playable in version 1.0.

---

# 10. Case Briefing

Before investigation begins, the player receives a short briefing.

The briefing introduces:

- Victim
- Location
- Approximate incident time
- Initial circumstances
- Known information
- Investigator's objective

The briefing must not reveal the solution.

The player should be able to review the briefing later.

---

# 11. Crime Scene Investigation

The player investigates the victim's apartment.

The crime scene is presented as an interactive visual environment.

The player can tap objects or areas of interest.

Examples:

- Desk
- Phone
- Laptop
- Window
- Door
- Kitchen
- Bedroom
- Bookshelf
- Glass
- Other relevant objects

Interactive objects should provide:

- Description
- Evidence discovery
- Environmental information
- Optional case notes

Not every object needs to contain evidence.

Some objects should exist simply to make the environment believable.

---

# 12. Investigation Interaction

The player should not need pixel-perfect tapping.

Interactive areas should have reasonably large touch targets.

When the player taps an interactive object:

```text
OBJECT DESCRIPTION
        ↓
Investigation Result
        ↓
Evidence discovered?
        ↓
YES → Add to evidence
NO  → Add observation or nothing
```

Collected evidence should be clearly communicated.

---

# 13. Evidence System

Every meaningful piece of evidence must have a unique identifier.

Example:

```text
EVIDENCE_001
EVIDENCE_002
EVIDENCE_003
```

Each evidence item should contain:

- ID
- Name
- Short description
- Detailed description
- Discovery location
- Discovery condition
- Related suspects
- Related statements
- Relevance
- Whether it can contradict testimony
- Whether it is required for the final accusation

Evidence should never disappear after being collected.

---

# 14. Evidence Inventory

The player should have an evidence screen.

Evidence can be categorized into:

- Physical Evidence
- Documents
- Digital Evidence
- Statements
- Observations

The player can:

- View evidence
- Read descriptions
- Search/filter evidence
- Mark important evidence
- Return to previous evidence

The interface must remain usable on a phone screen.

---

# 15. Evidence Details

Selecting an evidence item opens a dedicated detail view.

Example structure:

```text
PHONE RECORD

Collected:
Crime Scene — Study

Description:
A recent outgoing call was discovered
in the victim's phone records.

Related:
• Victim
• Suspect 03
• Timeline
```

The game should reveal information progressively where appropriate.

---

# 16. Suspect System

There are four primary suspects.

Each suspect must have:

- Name
- Portrait
- Relationship to victim
- Basic biography
- Initial statement
- Dialogue tree
- Hidden information
- Lies or inconsistencies
- Evidence reactions
- Relationship to other suspects

Suspects should not simply have random dialogue.

Their dialogue must be connected to the case's actual logic.

---

# 17. Interrogation System

The player can interview suspects.

A conversation may contain:

- Questions
- Responses
- Follow-up questions
- Evidence presentation
- Contradiction opportunities

Example:

```text
DETECTIVE:
Where were you at 10:30 PM?

SUSPECT:
I was at home.

[Ask about the victim]

[Present evidence]

[Continue]
```

The player should control the pace of the interrogation.

---

# 18. Evidence Presentation

During interrogation, the player can present relevant evidence.

When the correct evidence is presented:

- The suspect reacts
- New information may become available
- A contradiction may be recorded
- The conversation may branch

When irrelevant evidence is presented:

- The suspect may reject its relevance
- The game should not punish the player severely

The system should encourage experimentation.

---

# 19. Contradiction System

A contradiction is created when a suspect's statement conflicts with established evidence or another verified statement.

Example:

```text
SUSPECT:
"I never entered the apartment."

EVIDENCE:
Fingerprint found inside the apartment.

RESULT:
Contradiction discovered.
```

Contradictions should be recorded in the case file.

Each contradiction should have:

- ID
- Suspect
- Statement
- Contradicting evidence
- Explanation

---

# 20. Case Notes

The player should have a notes section.

Notes may include:

- Important observations
- Suspect information
- Timeline events
- Contradictions
- Automatically unlocked facts

The player should not be required to manually type everything.

The game should automatically record important discoveries.

---

# 21. Timeline

The investigation should include a timeline of important events.

Example:

```text
9:30 PM
Suspect 01 claims to leave.

10:00 PM
Victim receives a message.

10:42 PM
Outgoing call from victim's phone.

10:50 PM
Neighbor reports hearing a noise.

11:05 PM
Victim discovered.
```

The timeline should update as new evidence is discovered.

Some timeline entries may initially be uncertain.

---

# 22. Case Board

The case board is where the player connects information.

It should allow the player to review:

- Suspects
- Evidence
- Timeline events
- Statements
- Contradictions

Connections may be visual or represented through a clean relationship interface.

The board should help the player understand the case without automatically solving it.

---

# 23. Deduction System

The game should distinguish between:

### Evidence Collection

Finding information.

### Deduction

Understanding what the information means.

The player should not be forced to collect every possible piece of information in a single linear sequence.

However, the final case must remain logically solvable.

---

# 24. Final Accusation

Once the player believes they understand the case, they can enter the accusation screen.

The player must select:

### 1. Suspect

Who committed the crime?

### 2. Motive

Why did they do it?

### 3. Method

How did the crime happen?

### 4. Key Evidence

Which evidence proves the accusation?

The exact options and answers will be defined separately in the case design documentation.

---

# 25. Case Evaluation

The game evaluates the player's conclusion.

Possible results:

## Perfect Solve

Correct:

- Culprit
- Motive
- Method
- Supporting evidence

Result:

**CASE SOLVED**

---

## Correct Culprit

The player identifies the correct suspect but gives incomplete or incorrect reasoning.

Result:

**CULPRIT IDENTIFIED — CASE INCOMPLETE**

---

## Wrong Accusation

The player accuses the wrong suspect.

Result:

**CASE UNSOLVED**

The player should be allowed to return to the investigation rather than permanently lose the case.

---

# 26. Multiple Endings

The first case should contain multiple outcomes.

At minimum:

1. Correct full solution
2. Correct culprit but incomplete case
3. Incorrect accusation

The exact narrative endings will be specified in the case documentation.

---

# 27. Save System

The game must automatically save progress.

Save information should include:

- Current case
- Investigation progress
- Discovered evidence
- Interview progress
- Unlocked dialogue
- Contradictions
- Timeline progress
- Case-board progress
- Current investigation state

The player should be able to close the application and continue later.

---

# 28. Settings

Settings should include only useful options.

Minimum:

- Sound on/off
- Music on/off
- Haptic feedback on/off
- Text speed
- Reset current case

Do not create unnecessary settings.

---

# 29. Visual Direction

The visual style should be:

**Dark, cinematic, clean, modern detective interface.**

Suggested visual characteristics:

- Dark backgrounds
- Strong contrast
- Subtle accent color
- Clean typography
- Evidence-card UI
- Detective-board visual language
- Subtle shadows
- Restrained animations
- Minimal clutter

The game should avoid looking like a generic dashboard.

---

# 30. Animation

Animations should be subtle and purposeful.

Examples:

- Screen transitions
- Evidence discovery animation
- Dialogue appearance
- Evidence card opening
- Case-board connections
- Notification effects
- Final accusation reveal

Animations should never interfere with usability.

---

# 31. Audio

Audio should support atmosphere.

Potential categories:

- Ambient room noise
- UI clicks
- Evidence discovery sound
- Page/document sounds
- Dialogue interaction sounds
- Suspense music
- Case solved music

All audio must be optional through settings.

The game must remain playable with audio disabled.

---

# 32. Accessibility

The game should support:

- Readable text
- Large enough touch targets
- High contrast
- No information conveyed exclusively through color
- Adjustable text speed
- Optional haptics
- No rapid tapping requirements

---

# 33. Technical Requirements

The application must be a native or web-based Android-compatible application generated through Google AI Studio's supported environment.

The implementation must:

- Be self-contained
- Avoid unnecessary external dependencies
- Work offline where possible
- Persist game data locally
- Avoid requiring a backend for the first release
- Use deterministic case logic
- Keep all case content separate from UI logic where practical

---

# 34. Architecture Principle

Separate the project into logical layers.

Recommended structure:

```text
UI
│
├── Main Menu
├── Case Selection
├── Briefing
├── Investigation
├── Evidence
├── Interrogation
├── Case Board
├── Timeline
├── Accusation
└── Results

GAME LOGIC
│
├── Case Manager
├── Evidence Manager
├── Suspect Manager
├── Dialogue Manager
├── Contradiction Manager
├── Timeline Manager
├── Deduction Manager
└── Save Manager

CASE DATA
│
├── Case
├── Suspects
├── Evidence
├── Dialogue
├── Contradictions
├── Timeline
└── Endings
```

The exact implementation technology may differ depending on what Google AI Studio generates, but the separation of concerns should remain.

---

# 35. Content/Data Principle

**Game content must not be invented by the implementation AI.**

The project will receive separate documentation containing the authoritative case information.

This includes:

- Exact suspect identities
- Exact evidence
- Exact dialogue
- Exact timeline
- Exact contradictions
- Exact solution
- Exact endings

The implementation must faithfully implement those documents.

If information is missing, the AI must not silently invent important story content.

Instead, it should identify the missing requirement.

---

# 36. Deterministic Mystery

The solution to the mystery must be deterministic.

There must be one canon solution.

The player may reach that solution through different investigation paths, but the underlying facts must remain consistent.

The game must never randomly change:

- Culprit
- Motive
- Method
- Important evidence
- Timeline
- Suspect alibis

---

# 37. Anti-Frustration Design

The game should avoid arbitrary puzzle logic.

A player should never need to:

- Tap the exact pixel of an object
- Guess an unrelated word
- Randomly select dialogue
- Repeat conversations without reason
- Find an invisible clue with no indication
- Know information that the game never provided

Every major deduction must be supported by discoverable information.

---

# 38. Player Freedom

The player should be able to investigate in a flexible order where practical.

For example:

```text
Crime Scene
     ↓
Evidence
     ↓
Suspect 2
     ↓
Back to Crime Scene
     ↓
Suspect 4
     ↓
Review Timeline
     ↓
Suspect 2 again
```

The player should not be forced through a rigid sequence unless narrative progression requires it.

---

# 39. Error Handling

The application must handle:

- Missing save data
- Corrupted/incomplete case state
- Reopening the application
- Rotation or screen-size differences where applicable
- Back navigation
- Accidental taps
- Repeated evidence collection
- Repeated dialogue
- Resetting a case

No single invalid action should crash the application.

---

# 40. Performance

The game should be lightweight enough for ordinary Android devices.

Priorities:

1. Fast startup
2. Smooth UI
3. Low memory usage
4. Reliable save system
5. No unnecessary network requests

---

# 41. First Playable Milestone

The first successful milestone is NOT the finished game.

It is:

```text
Open App
   ↓
Main Menu
   ↓
Start Case
   ↓
Case Briefing
   ↓
Crime Scene
   ↓
Tap Object
   ↓
Discover Evidence
   ↓
Evidence Appears in Inventory
```

Once this works reliably, investigation can be expanded.

---

# 42. Definition of Done

Version 1.0 is considered complete only when a new player can:

1. Install the application.
2. Start Case 001.
3. Read the briefing.
4. Investigate the crime scene.
5. Collect evidence.
6. Review evidence.
7. Interview all relevant suspects.
8. Discover contradictions.
9. Review the timeline.
10. Build an understanding of the case.
11. Make an accusation.
12. Receive an appropriate ending.
13. Close the application.
14. Reopen it.
15. Continue from the saved state.

The entire process must work without developer intervention.

---

# 43. Development Philosophy

The project should be built incrementally.

Never attempt to generate the entire finished game in one step.

Development order:

```text
FOUNDATION
    ↓
MAIN MENU
    ↓
CASE SYSTEM
    ↓
INVESTIGATION
    ↓
EVIDENCE
    ↓
DIALOGUE
    ↓
SUSPECTS
    ↓
CONTRADICTIONS
    ↓
TIMELINE
    ↓
CASE BOARD
    ↓
ACCUSATION
    ↓
ENDINGS
    ↓
SAVE/LOAD
    ↓
POLISH
```

Each stage must be tested before moving to the next.

---

# 44. AI Development Rules

When implementing this PRD, the development AI must:

- Read all provided project documentation before modifying the project.
- Preserve working functionality.
- Avoid unnecessary rewrites.
- Avoid deleting existing functionality without justification.
- Keep code modular.
- Avoid hardcoding UI behavior where data-driven logic is appropriate.
- Never invent missing case facts.
- Clearly report assumptions.
- Test each major feature.
- Fix compilation/runtime errors before proceeding.
- Prefer simple reliable implementations over unnecessarily complex architectures.

---

# 45. Future Expansion

The architecture should allow future additions such as:

- Case 002
- Case 003
- Additional locations
- More suspects
- More complex evidence relationships
- Advanced deduction mechanics
- Achievement system
- Case ratings
- Optional hints

However, **future features must not compromise version 1.0**.

---

# 46. Final Product Goal

The final experience should leave the player thinking:

> "I actually solved that."

Not:

> "The game told me who did it."

The first case should be short enough to finish, deep enough to require thought, and polished enough to demonstrate that a complete detective game can be built successfully.

**One case. One mystery. One complete game.**

That is the foundation of **The Last Call**.