# THE LAST CALL
# GAME DESIGN BIBLE

**Document Version:** 1.0  
**Project Status:** Pre-Production  
**Platform:** Android  
**Genre:** Detective Mystery / Investigation  
**Perspective:** First-person investigation through interactive 2D/2.5D scenes and UI-driven investigation  
**Players:** Single-player  
**Session Length:** Approximately 20–45 minutes for Case 001  
**Primary Goal:** Solve a complete murder mystery through observation, evidence, interrogation, contradiction, and deduction.

---

# 1. DOCUMENT PURPOSE

This document is the **master game-design reference** for *The Last Call*.

The PRD describes what the product must contain.

This document describes **how the game should feel, behave, and function as an actual game**.

Every future gameplay implementation should be checked against this document.

If another project document conflicts with this document, the most recently provided authoritative case/content specification takes priority for story facts, while this document remains the authority for general gameplay philosophy and systems.

The game is intentionally designed around a small scope.

The objective is not to create the largest detective game possible.

The objective is to create **one genuinely satisfying detective case from beginning to end**.

---

# 2. GAME IDENTITY

## 2.1 Title

**THE LAST CALL**

## 2.2 Genre

Primary:

- Detective mystery
- Investigation
- Narrative puzzle

Secondary:

- Interrogation
- Deduction
- Evidence management
- Interactive storytelling

## 2.3 Platform

Android phones.

The interface must be designed for touch first.

Desktop-style controls should not be required.

---

# 3. HIGH-LEVEL PLAYER FANTASY

The player should feel like:

> **A real investigator who notices things other people overlook.**

The game should make the player feel intelligent when they:

- Notice a suspicious detail.
- Remember something a suspect said.
- Find evidence contradicting that statement.
- Connect two seemingly unrelated clues.
- Reconstruct the timeline.
- Identify a lie.
- Correctly identify the culprit.

The game should NOT make the player feel like they are simply completing a checklist.

---

# 4. DESIGN PILLARS

Everything in the game should follow five pillars.

## PILLAR 1 — OBSERVATION

Important information is hidden in plain sight.

The player must pay attention.

The game rewards curiosity.

---

## PILLAR 2 — LOGIC

Evidence must mean something.

A clue should not exist merely because the game needs another collectible.

Important discoveries should contribute to:

- A timeline
- A suspect theory
- A contradiction
- A motive
- A method
- The final solution

---

## PILLAR 3 — PLAYER AGENCY

The player should feel responsible for solving the case.

The game should provide information.

The player should provide the conclusion.

---

## PILLAR 4 — CONSISTENCY

The mystery must follow its own rules.

The game must never contradict itself.

If the victim died at a particular time, later evidence cannot casually establish an impossible timeline.

If a suspect says something, that statement becomes part of the case logic.

---

## PILLAR 5 — COMPLETION

The entire first case must be finishable.

Scope must never be allowed to destroy the project.

---

# 5. GAME LOOP

The complete gameplay loop is:

```text
CASE BRIEFING
      ↓
ARRIVE AT CRIME SCENE
      ↓
OBSERVE
      ↓
INVESTIGATE
      ↓
COLLECT EVIDENCE
      ↓
FORM INITIAL THEORIES
      ↓
INTERVIEW SUSPECTS
      ↓
COMPARE STATEMENTS
      ↓
PRESENT EVIDENCE
      ↓
DISCOVER CONTRADICTIONS
      ↓
RECONSTRUCT TIMELINE
      ↓
CONNECT FACTS
      ↓
FORM FINAL THEORY
      ↓
ACCUSATION
      ↓
CASE EVALUATION
      ↓
ENDING
```

This loop should remain understandable throughout the game.

---

# 6. CASE STRUCTURE

Case 001 is divided into phases.

## PHASE 1 — BRIEFING

The player learns:

- What happened
- Where it happened
- Who the victim is
- What is currently known

The briefing establishes the mystery.

It does not establish the solution.

---

## PHASE 2 — INITIAL INVESTIGATION

The player explores the crime scene.

The player discovers:

- Physical evidence
- Digital evidence
- Environmental observations
- Timeline information

This phase should create questions.

---

## PHASE 3 — SUSPECT INTERVIEWS

The player meets the suspects.

Each suspect provides:

- Information
- An alibi
- Their relationship to the victim
- Their version of events

Some information is true.

Some information is misleading.

Some information is deliberately false.

---

## PHASE 4 — FOLLOW-UP INVESTIGATION

New evidence allows the player to ask better questions.

The player returns to:

- Evidence
- Suspects
- Crime scene
- Timeline

This is where the investigation becomes deeper.

---

## PHASE 5 — DEDUCTION

The player should have enough information to reconstruct what happened.

The player identifies:

- Who
- Why
- How
- When

---

## PHASE 6 — ACCUSATION

The player commits to a theory.

The game evaluates the theory.

---

# 7. INVESTIGATION PHILOSOPHY

The game should distinguish between:

### Looking

The player notices an object.

### Investigating

The player interacts with the object.

### Discovering

The player learns something new.

### Interpreting

The player understands why it matters.

Example:

```text
LOOK
↓
A phone is visible.

INVESTIGATE
↓
The player opens the phone.

DISCOVER
↓
There is an unusual call record.

INTERPRET
↓
The call conflicts with a suspect's statement.
```

The game becomes interesting when the player reaches the fourth stage.

---

# 8. CRIME SCENE DESIGN

The crime scene is not a collection of random clickable objects.

Every important object must have a purpose.

Objects can belong to one of four categories.

## CATEGORY A — CORE EVIDENCE

Directly important to solving the case.

Examples:

- Phone record
- Physical trace
- Document
- Weapon-related evidence

---

## CATEGORY B — CONTEXTUAL EVIDENCE

Helps explain the situation.

Examples:

- Calendar
- Photograph
- Letter
- Work document

---

## CATEGORY C — ATMOSPHERE

Makes the location believable.

Examples:

- Books
- Furniture
- Decorations
- Personal belongings

---

## CATEGORY D — MISDIRECTION

Looks potentially important but does not directly solve the case.

Misdirection must be fair.

It should not make the player feel tricked.

---

# 9. INTERACTION DESIGN

Every interactive object should communicate its state.

Possible states:

```text
UNINSPECTED
INSPECTED
EVIDENCE_FOUND
FULLY_INVESTIGATED
```

The game may use subtle visual indicators.

However, indicators should not reveal the solution.

---

# 10. TOUCH DESIGN

Because this is an Android game:

- Interactive targets must be large enough.
- Buttons must have sufficient spacing.
- Important actions must not require precision.
- Swipe gestures should be optional where possible.
- Back navigation must be predictable.

The player should never lose because they accidentally tapped beside a tiny object.

---

# 11. EVIDENCE DESIGN

Every evidence item has two purposes:

1. Information
2. Gameplay consequence

Evidence should answer at least one question.

Examples:

- Where was someone?
- When did something happen?
- Who had access?
- What was the victim doing?
- What is someone hiding?
- What happened before the crime?

---

# 12. EVIDENCE TYPES

## PHYSICAL

Examples:

- Fingerprints
- Objects
- Documents
- Personal belongings
- Physical traces

## DIGITAL

Examples:

- Phone records
- Messages
- Emails
- Computer files
- Call logs

## TESTIMONY

Information provided by:

- Suspects
- Witnesses
- Other characters

## ENVIRONMENTAL

Information discovered from the scene.

Examples:

- Open window
- Displaced object
- Broken lock
- Unusual room condition

---

# 13. EVIDENCE QUALITY

Not all evidence should have equal importance.

Use three levels.

### Minor

Adds context.

### Relevant

Helps narrow possibilities.

### Critical

Required to establish the correct solution.

Critical evidence should be discoverable without unreasonable difficulty.

---

# 14. EVIDENCE RELATIONSHIPS

Evidence can connect to:

- Suspects
- Timeline events
- Other evidence
- Statements
- Locations
- Motives

Example:

```text
PHONE RECORD
     ↓
10:42 PM CALL
     ↓
SUSPECT 03
     ↓
CONTRADICTS ALIBI
```

These relationships power the deduction system.

---

# 15. SUSPECT DESIGN PHILOSOPHY

Every major suspect should feel like a real person rather than a puzzle container.

Each suspect needs:

- Personality
- Relationship with victim
- Public story
- Private information
- Emotional response
- Reason to hide something
- Distinct speaking style

Not every suspect should be equally suspicious.

---

# 16. SUSPECT ARCHETYPES

The four suspects should occupy different narrative roles.

For example:

### THE OBVIOUS SUSPECT

Has a visible motive.

The player may suspect them immediately.

But suspicion does not equal guilt.

### THE UNLIKELY SUSPECT

Appears cooperative.

May have information the player needs.

### THE SECRETIVE SUSPECT

Clearly hides something.

But hiding something does not automatically mean murder.

### THE TRUSTWORTHY SUSPECT

Appears credible.

Their information can help establish the true timeline.

The exact identities are defined in the case content documentation.

---

# 17. SUSPECT DIALOGUE

Dialogue must accomplish at least one of these:

- Reveal information
- Establish character
- Create suspicion
- Provide an alibi
- Introduce a contradiction
- Answer an existing question
- Create a new question

Avoid dialogue that exists only to fill space.

---

# 18. DIALOGUE STYLE

Dialogue should be:

- Natural
- Concise
- Character-specific
- Believable
- Easy to read on mobile

Avoid excessively long paragraphs.

The player should be able to quickly understand who said what.

---

# 19. QUESTION TYPES

Questions fall into categories.

## GENERAL

"What happened?"

## TIMELINE

"Where were you at 10 PM?"

## RELATIONSHIP

"What was your relationship with the victim?"

## OBJECT

"Why is your fingerprint here?"

## CONTRADICTION

"You said you never entered the apartment."

## MOTIVE

"Why did you argue with the victim?"

Different question types should become available based on investigation progress.

---

# 20. INTERROGATION FLOW

Typical interrogation:

```text
OPEN CONVERSATION
       ↓
GENERAL QUESTIONS
       ↓
SUSPECT STATEMENT
       ↓
NEW INFORMATION
       ↓
PLAYER REVIEWS EVIDENCE
       ↓
FOLLOW-UP QUESTION
       ↓
PRESENT EVIDENCE
       ↓
REACTION
       ↓
NEW INFORMATION / CONTRADICTION
```

The player should feel that evidence gives them leverage.

---

# 21. CONTRADICTION DESIGN

Contradictions are one of the most important mechanics.

A contradiction must have:

1. A statement.
2. A verified fact.
3. A logical conflict.

Example:

```text
STATEMENT:
"I was never inside the apartment."

FACT:
The suspect's fingerprint is found on the victim's desk.

CONFLICT:
The suspect's statement is false.
```

The game should explicitly recognize when a valid contradiction is found.

---

# 22. FAIR CONTRADICTIONS

A contradiction must never depend on information the player has not been given.

Bad:

> Suspect's story conflicts with a hidden fact.

Good:

> Suspect's story conflicts with evidence the player discovered.

The player must be able to explain **why** the contradiction matters.

---

# 23. FALSE STATEMENTS VS LIES

Not every incorrect statement is necessarily a lie.

A suspect might:

- Misremember
- Guess
- Be mistaken
- Protect someone
- Hide an unrelated secret
- Deliberately lie

The game should allow these distinctions where relevant.

---

# 24. TIMELINE SYSTEM

The timeline is the backbone of the mystery.

Important events should have:

- Timestamp or approximate time
- Description
- Source
- Confidence level
- Related evidence

Example:

```text
10:15 PM
Victim receives a message.

SOURCE:
Phone record.

10:42 PM
Outgoing call.

SOURCE:
Phone record.

11:05 PM
Neighbor hears a noise.

SOURCE:
Witness statement.
```

---

# 25. TIMELINE CONFIDENCE

Events may initially be:

### CONFIRMED

Supported by strong evidence.

### PROBABLE

Supported by credible testimony.

### UNCERTAIN

Based on an unverified statement.

The player can gradually upgrade uncertain events as evidence is discovered.

---

# 26. CASE BOARD

The case board represents the player's mental model of the investigation.

It should connect:

```text
SUSPECT
   ↕
STATEMENT
   ↕
EVIDENCE
   ↕
TIMELINE
   ↕
MOTIVE
```

The board should not automatically draw every correct conclusion.

It should organize information.

---

# 27. PLAYER DEDUCTION

The player should perform the final reasoning.

The game may provide subtle assistance through:

- Related evidence
- Timeline organization
- Contradiction markers
- Suspect profiles

But the game should never display:

> "The murderer is Suspect 3."

before the accusation.

---

# 28. HINT SYSTEM

A hint system may be included if needed.

Hints should operate in levels.

### HINT 1

Points toward an area.

### HINT 2

Points toward relevant evidence.

### HINT 3

Explains the relationship.

Hints should never directly reveal the culprit unless the player has essentially completed the investigation.

Hints may be added after the core case is functional.

---

# 29. PLAYER PROGRESSION

There is no traditional XP or level system.

Progress is measured through knowledge.

The player's progression is:

```text
UNKNOWN
↓
SUSPICIOUS
↓
INFORMED
↓
CONNECTED
↓
CERTAIN
```

The player should feel more knowledgeable rather than more powerful.

---

# 30. FAILURE DESIGN

Failure should never feel punishing.

A wrong accusation should not delete the player's investigation.

Instead:

```text
WRONG ACCUSATION
      ↓
ENDING / FEEDBACK
      ↓
RETURN TO CASE
```

The player retains their evidence.

---

# 31. CASE SOLUTION STRUCTURE

The mystery must be internally represented using a fixed canonical solution.

Conceptually:

```text
CULPRIT
├── Motive
├── Opportunity
├── Method
├── Timeline
├── Supporting Evidence
└── Contradictions
```

The final accusation system compares the player's selections against this canonical structure.

---

# 32. ACCUSATION DESIGN

The final accusation should feel like a major moment.

Before confirming:

```text
YOU ARE ABOUT TO ACCUSE:

[Suspect]

MOTIVE:
[Selected motive]

METHOD:
[Selected method]

KEY EVIDENCE:
[Selected evidence]

CONFIRM ACCUSATION
```

The player should understand that they are submitting their final theory.

---

# 33. FINAL REVEAL

After the accusation, the game should explain the actual case.

The reveal should answer:

- What happened?
- Who did it?
- Why?
- How?
- What evidence proved it?
- Which statements were lies?
- What happened in the critical timeline?

The reveal is the player's reward for solving the mystery.

---

# 34. ENDING QUALITY

A correct ending should not merely say:

> "Correct!"

It should provide narrative closure.

The player should understand how the pieces fit together.

---

# 35. CASE PACING

The first case should have a deliberate rhythm.

### Opening

Mystery.

### Early Investigation

Curiosity.

### Middle

Confusion and competing theories.

### Discovery

Major contradiction.

### Late Investigation

Connections begin forming.

### Final Phase

The player becomes confident.

### Accusation

Commitment.

### Reveal

Resolution.

---

# 36. INFORMATION PACING

Do not reveal all important information immediately.

Use:

```text
QUESTION
↓
PARTIAL ANSWER
↓
NEW QUESTION
↓
EVIDENCE
↓
CONTRADICTION
↓
DEEPER QUESTION
↓
ANSWER
```

This creates investigative momentum.

---

# 37. RED HERRINGS

Red herrings are allowed but must follow rules.

A red herring should:

- Be believable
- Have an explanation
- Not require arbitrary guessing
- Eventually make sense

A red herring should create uncertainty, not frustration.

---

# 38. MYSTERY LOGIC

Before implementation, the case should be representable as a logic graph.

Conceptually:

```text
EVENT A
   ↓
EVENT B
   ↓
EVIDENCE C
   ↓
STATEMENT D
   ↓
CONTRADICTION E
   ↓
SUSPECT F
   ↓
MOTIVE G
```

Every major clue should have a reason for existing.

---

# 39. CLUE DESIGN RULE

Every important clue should answer:

> **Why does this clue exist?**

Possible answers:

- Establishes time.
- Establishes location.
- Establishes opportunity.
- Establishes motive.
- Contradicts a suspect.
- Eliminates a suspect.
- Connects two facts.
- Explains the method.
- Supports the final solution.

If a clue has no purpose, remove it.

---

# 40. NO RANDOM PUZZLES

Do not add puzzles simply because detective games "usually have puzzles."

Every puzzle must arise naturally from the investigation.

Good:

> Decode a password found in evidence because the player needs access to relevant information.

Bad:

> Solve a random color-matching puzzle to open a drawer.

---

# 41. GAME UI STRUCTURE

Primary navigation:

```text
CASE
├── INVESTIGATE
├── EVIDENCE
├── SUSPECTS
├── TIMELINE
├── CASE BOARD
└── NOTES
```

The player should always know where they are.

---

# 42. MOBILE UI PRIORITIES

Hierarchy:

1. Current information
2. Primary action
3. Navigation
4. Secondary information

Avoid putting every possible feature on screen simultaneously.

Use:

- Bottom navigation
- Cards
- Modals
- Tabs
- Expandable sections

where appropriate.

---

# 43. VISUAL LANGUAGE

The game should use a consistent visual language.

## Background

Dark neutral surfaces.

## Panels

Slightly lighter surfaces.

## Accent

One primary investigative accent color.

## Warning

Reserved for contradictions or danger.

## Success

Reserved for confirmed discoveries and solved states.

Do not use excessive colors.

---

# 44. TYPOGRAPHY

Typography should prioritize readability.

Use:

- Large headings
- Medium section titles
- Comfortable body text
- Clearly separated dialogue
- High contrast

Avoid overly decorative fonts for important information.

---

# 45. CHARACTER PRESENTATION

Suspects should have distinct portraits.

Portraits should communicate:

- Age range
- Personality
- Mood
- Relationship to the case

Portraits should remain visually consistent.

---

# 46. ATMOSPHERE

The game should create atmosphere through:

- Lighting
- Sound
- UI motion
- Environmental details
- Dialogue
- Pacing

It does not require expensive graphics.

Good atmosphere is more important than graphical complexity.

---

# 47. SOUND DESIGN

Use sound to communicate:

### Discovery

A subtle evidence sound.

### Important discovery

A stronger confirmation sound.

### Contradiction

A distinctive investigative cue.

### Menu

Minimal interface sound.

### Ending

Unique resolution music.

Sound must remain subtle.

---

# 48. HAPTICS

Optional haptic feedback can be used for:

- Evidence discovery
- Important confirmation
- Contradiction discovery
- Final accusation

Never make haptics mandatory.

---

# 49. NARRATIVE STYLE

The story should feel grounded.

Avoid:

- Excessive melodrama
- Cartoon villain dialogue
- Constant jokes
- Unrealistic detective monologues
- Characters explaining information they would naturally know

The player should discover information naturally through interaction.

---

# 50. PROTAGONIST

The player character should remain relatively understated.

The protagonist exists primarily to:

- Ask questions
- Investigate
- Observe
- Interpret

Avoid making the protagonist constantly explain the mystery to the player.

The player should perform the thinking.

---

# 51. CASE 001 NARRATIVE FRAMEWORK

The first case is:

**THE LAST CALL**

Core premise:

A man is found dead inside his apartment.

An unusual phone call shortly before the incident creates the first major mystery.

Several people connected to the victim have reasons to hide information.

The investigation gradually reveals that the obvious story does not match the actual timeline.

The exact identities, events, clues, dialogue, culprit, motive, and solution are defined in the dedicated case-content documents.

---

# 52. CASE 001 CHARACTER DESIGN REQUIREMENTS

There must be exactly four primary suspects.

Each suspect must be distinguishable through:

- Appearance
- Personality
- Speech
- Relationship
- Motivation
- Information

Avoid giving all suspects identical personalities.

---

# 53. CASE 001 CLUE DESIGN TARGET

Target:

**15–20 meaningful clues.**

Suggested distribution:

```text
Crime Scene:
5–7

Digital:
3–4

Suspect/Testimony:
4–5

Timeline:
2–3

Final confirmation:
2–3
```

These numbers are targets, not mandatory if the case logic requires adjustment.

---

# 54. CLUE DEPENDENCY

Clues may unlock other clues.

Example:

```text
Find phone
    ↓
Discover unknown number
    ↓
Question suspect
    ↓
Learn relationship
    ↓
Return to phone
    ↓
Discover relevant message
```

This creates a natural investigation loop.

---

# 55. BACKTRACKING

Backtracking should be purposeful.

Returning to a location should happen because:

- New information makes something relevant.
- A suspect mentioned something.
- The player wants to verify a theory.
- A previously inaccessible interaction becomes available.

Do not force unnecessary backtracking.

---

# 56. REPLAYABILITY

Case 001 is primarily designed for a first-time solve.

Replayability can come from:

- Alternate accusation outcomes
- Discovering missed clues
- Different investigation order
- Exploring all dialogue
- Trying alternative theories

However, replayability must not compromise clarity.

---

# 57. ACCESSIBLE INFORMATION

Critical information should not permanently disappear.

If the player discovered a clue, they can review it.

If a suspect provided an important statement, it should be recorded.

The game should respect the player's memory limitations.

---

# 58. INFORMATION JOURNAL

The journal should automatically record major discoveries.

Example:

```text
CASE NOTES

✓ Victim's phone contained a recent call.
✓ Suspect 02 claims to have left earlier.
✓ Window was unlocked.
✓ Neighbor heard a noise.
? Exact time of death remains uncertain.
```

The journal should help organize the investigation without solving it.

---

# 59. STATE MANAGEMENT

The game should track investigation state.

Conceptually:

```text
CASE_STARTED
BRIEFING_READ
CRIME_SCENE_ENTERED

EVIDENCE_001_FOUND
EVIDENCE_002_FOUND

SUSPECT_001_INTERVIEWED
SUSPECT_002_INTERVIEWED

CONTRADICTION_001_DISCOVERED

TIMELINE_EVENT_001_CONFIRMED

ACCUSATION_AVAILABLE
CASE_SOLVED
```

This allows content to react to player progress.

---

# 60. UNLOCK RULES

Unlocks should be based on meaningful investigation progress.

Good:

> Find the phone record → unlock question about the call.

Bad:

> Click 10 objects → unlock random dialogue.

---

# 61. PLAYER GUIDANCE

The game should provide gentle direction.

Possible mechanisms:

- Case objectives
- Journal updates
- Subtle UI markers
- Dialogue prompts
- Optional hints

The player should rarely ask:

> "What am I even supposed to do?"

But should often ask:

> "What does this clue mean?"

That distinction is important.

---

# 62. OBJECTIVES

Objectives should be concise.

Examples:

```text
CURRENT OBJECTIVE

Investigate the apartment.

OPTIONAL

Review the victim's phone.
```

Objectives should not tell the player the answer.

---

# 63. CASE COMPLETION

A case is complete when:

- The player makes an accusation.
- The game evaluates it.
- The narrative ending plays.
- The case status becomes Solved or Unsolved.
- The player can review the case.

---

# 64. CASE RESULT SCREEN

The result screen should display:

- Case outcome
- Accused suspect
- Correct/incorrect status
- Evidence quality
- Optional case score
- Continue/replay option

Avoid excessive statistics.

---

# 65. OPTIONAL CASE SCORE

If included, score should measure investigative quality rather than speed.

Possible factors:

- Evidence discovered
- Contradictions discovered
- Correct final reasoning
- Hint usage

Do not heavily punish slower players.

Detective work is not a race.

---

# 66. NO TIMER

Case 001 should not have a countdown timer.

Players should be able to think.

Pressure comes from the mystery, not from a clock.

---

# 67. NO ENERGY SYSTEM

Do not limit investigation attempts with:

- Hearts
- Energy
- Lives
- Tickets

The player should be free to investigate.

---

# 68. NO FORCED MONETIZATION

The first version should contain:

- No ads
- No purchases
- No premium currency
- No subscription
- No loot boxes

This is a complete standalone prototype/game.

---

# 69. SAVE PHILOSOPHY

Saving should be invisible and reliable.

The player should never need to think:

> "Did the game save?"

Auto-save after important progress.

---

# 70. BACK BUTTON BEHAVIOR

Android back navigation should be predictable.

For example:

```text
Evidence Details
↓ Back
Evidence List
↓ Back
Case Menu
↓ Back
Main Menu
```

Do not unexpectedly exit the application from an inner screen.

---

# 71. ERROR STATES

Errors should be understandable.

Bad:

> NullReferenceException.

Good:

> "This evidence could not be loaded. Please return to the case."

Developer errors should remain hidden from normal players.

---

# 72. DATA-DRIVEN CONTENT

Case content should be stored separately from presentation logic whenever practical.

Conceptually:

```text
CaseData
├── Case metadata
├── Suspects
├── Evidence
├── Dialogue
├── Timeline
├── Contradictions
├── Accusation options
└── Endings
```

This makes future cases easier to add.

---

# 73. FUTURE CASE ARCHITECTURE

Future cases should ideally be added as data/content rather than rewriting the entire game.

Example:

```text
CASE_001
CASE_002
CASE_003
```

Each case can define its own:

- Scene
- Suspects
- Evidence
- Dialogue
- Logic
- Ending

---

# 74. CONTENT AUTHORITY

The following are authoritative:

### PRD

Defines product requirements.

### Game Design Bible

Defines gameplay philosophy and system behavior.

### Case Bible

Defines exact story facts.

### Dialogue Bible

Defines exact dialogue.

### Evidence Bible

Defines exact evidence and relationships.

### Implementation Documentation

Defines technical implementation details.

The implementation AI must not override narrative facts.

---

# 75. DEVELOPMENT ORDER

Development should proceed in this order.

## STEP 1

Project foundation.

## STEP 2

Navigation.

## STEP 3

Case loading.

## STEP 4

Investigation scene.

## STEP 5

Evidence collection.

## STEP 6

Evidence inventory.

## STEP 7

Suspect system.

## STEP 8

Dialogue.

## STEP 9

Evidence presentation.

## STEP 10

Contradictions.

## STEP 11

Timeline.

## STEP 12

Case board.

## STEP 13

Accusation.

## STEP 14

Endings.

## STEP 15

Save/load.

## STEP 16

Polish.

---

# 76. DEVELOPMENT RULE: ONE WORKING SYSTEM AT A TIME

Never build five interconnected systems simultaneously without testing them.

After every major system:

1. Run the application.
2. Test the system.
3. Check navigation.
4. Check save state.
5. Check edge cases.
6. Fix errors.
7. Continue.

---

# 77. DEVELOPMENT RULE: PRESERVE WORKING FEATURES

When modifying the project:

- Do not rewrite unrelated systems.
- Do not replace working architecture unnecessarily.
- Do not remove functionality merely to simplify implementation.
- Do not change case facts.
- Do not create duplicate systems.

---

# 78. DEVELOPMENT RULE: NO INVENTED CONTENT

If the AI is implementing the game and encounters:

> "What should this suspect say?"

It must not invent dialogue if the authoritative dialogue document has not provided it.

If required information is missing:

```text
CONTENT REQUIRED:
Suspect 02 response to Evidence 07.
```

The developer should then receive a clear request for the missing content.

---

# 79. QUALITY BAR

The game should prioritize:

### Reliability

Above everything.

### Clarity

The player understands what they can do.

### Consistency

The mystery makes sense.

### Atmosphere

The game feels like a detective story.

### Polish

Small details feel intentional.

---

# 80. WHAT SUCCESS LOOKS LIKE

A successful player session should look like:

> "I entered the apartment."

> "I found something strange."

> "That doesn't match what the neighbor said."

> "Wait... if that call happened at 10:42, then their timeline doesn't make sense."

> "I need to question them again."

> "There it is."

> "I know what happened."

> "I'm accusing them."

That is the emotional experience the game should create.

---

# 81. CORE PLAYER EMOTIONS

The game should deliberately move the player through:

```text
CURIOUS
   ↓
SUSPICIOUS
   ↓
UNCERTAIN
   ↓
CONFUSED
   ↓
DISCOVERY
   ↓
UNDERSTANDING
   ↓
CONFIDENT
   ↓
SATISFIED
```

The game should not maintain constant tension.

Moments of clarity are important.

---

# 82. MYSTERY CURVE

The player's understanding should gradually increase.

Conceptually:

```text
UNDERSTANDING

100% |                         █████
     |                    █████
     |               █████
     |          █████
     |     █████
  0% |████
     +-----------------------------
       Beginning             Ending
```

The exact curve can fluctuate.

The player should sometimes become less certain after discovering new information.

That is part of solving a mystery.

---

# 83. SUSPICION CURVE

Suspicion should move between suspects.

The player may initially suspect:

```text
Suspect A ███████
Suspect B ███
Suspect C █████
Suspect D ██
```

Later:

```text
Suspect A ███
Suspect B ███████
Suspect C ██
Suspect D ████
```

The game should encourage changing theories as evidence changes.

---

# 84. FAIRNESS RULE

The final solution must be logically possible before the final accusation.

The player should never need information revealed only after choosing the culprit.

The reveal should confirm the player's reasoning, not retroactively justify it.

---

# 85. PLAYER KNOWLEDGE VS CHARACTER KNOWLEDGE

The game must distinguish:

### Player knows

Information discovered during gameplay.

### Character knows

Information known by the protagonist.

### Suspect knows

Information known by a suspect.

This distinction prevents accidental spoilers.

---

# 86. SPOILER CONTROL

UI should avoid accidentally revealing:

- Culprit
- Solution
- Correct motive
- Final method

unless the player reaches the appropriate stage.

---

# 87. REVEAL DESIGN

The final reveal should preferably replay the timeline.

Example structure:

```text
BEFORE THE INCIDENT
↓
THE ARGUMENT
↓
THE PHONE CALL
↓
THE CRITICAL EVENT
↓
THE COVER-UP
↓
THE DISCOVERY
```

This gives the player a satisfying reconstruction.

---

# 88. CASE REPLAY

After completing the case, the player should be able to replay it.

Replay should reset:

- Investigation progress
- Evidence collection
- Dialogue unlocks
- Contradictions
- Accusation state

Completed case status should remain recorded in the case selection screen.

---

# 89. FIRST VERSION CUT LIST

If development becomes too difficult, cut in this order:

1. Advanced case-board visuals.
2. Optional hints.
3. Optional scoring.
4. Complex animations.
5. Advanced environmental interactions.
6. Secondary dialogue.

Never cut:

- Evidence
- Suspects
- Contradictions
- Core dialogue
- Accusation
- Case solution
- Save system
- Complete case loop

---

# 90. MVP DEFINITION

The absolute minimum playable version is:

```text
MAIN MENU
    ↓
CASE BRIEFING
    ↓
CRIME SCENE
    ↓
EVIDENCE
    ↓
SUSPECT INTERVIEW
    ↓
CONTRADICTION
    ↓
ACCUSATION
    ↓
ENDING
```

If this works, the game exists.

Everything else improves it.

---

# 91. FINAL DESIGN PRINCIPLE

**Do not confuse complexity with depth.**

A detective game does not need:

- Huge maps
- Hundreds of clues
- Dozens of characters
- Complex combat
- Procedural generation
- Massive graphics

It needs:

**Good information.**

**Good characters.**

**Good contradictions.**

**Good logic.**

**Good pacing.**

And most importantly:

> **A solution that the player can actually figure out.**

---

# 92. MASTER RULE

Whenever a new feature is proposed, ask:

### Does this make the investigation more interesting?

If yes:

Consider it.

If no:

Do not add it merely because it sounds impressive.

The project succeeds by remaining focused.

---

# 93. FINAL GAME LOOP SUMMARY

```text
┌──────────────────────┐
│      CASE BRIEF      │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│   INVESTIGATE SCENE  │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│   COLLECT EVIDENCE   │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│   QUESTION SUSPECTS  │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│ PRESENT EVIDENCE     │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│ FIND CONTRADICTIONS  │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│ REBUILD TIMELINE     │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│ CONNECT THE EVIDENCE │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│ MAKE ACCUSATION      │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│ REVEAL THE TRUTH     │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│      CASE CLOSED     │
└──────────────────────┘
```

---

# 94. THE GOLDEN RULE OF THE LAST CALL

**The player should never be given the answer.**

They should be given:

- The scene.
- The people.
- The evidence.
- The statements.
- The contradictions.
- The timeline.

Then they solve it.

That is the game.