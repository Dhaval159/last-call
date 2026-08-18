# THE LAST CALL
# GAME FLOW & PLAYER JOURNEY SPECIFICATION

**Case ID:** CASE-001  
**Version:** 1.0  
**Platform:** Android  
**Purpose:** Define the complete playable flow of the investigation from launch to final result.

---

# 1. PURPOSE

This document defines **what the player does, in what order, what becomes available, and what happens after each major action**.

It is the authoritative flow document for:

- Game startup
- Case introduction
- Crime-scene investigation
- Case-file usage
- Suspect interviews
- Evidence presentation
- Timeline reconstruction
- Deduction
- Accusation
- Ending
- Save/resume behavior

The implementation must not create arbitrary progression that conflicts with this flow.

---

# 2. CORE PLAYER LOOP

The entire game is built around:

```text
OBSERVE
   ↓
INVESTIGATE
   ↓
COLLECT
   ↓
QUESTION
   ↓
COMPARE
   ↓
CONNECT
   ↓
CONTRADICT
   ↓
DEDUCE
   ↓
ACCUSE
```

The player should repeatedly feel:

> "I found something. Now I need to understand what it means."

---

# 3. HIGH-LEVEL FLOW

```text
APP LAUNCH
   ↓
MAIN MENU
   ↓
NEW CASE
   ↓
CASE BRIEFING
   ↓
CRIME SCENE
   ↓
INITIAL INVESTIGATION
   ↓
CASE FILE UNLOCKED
   ↓
SUSPECT INVESTIGATION
   ↓
INTERVIEWS
   ↓
EVIDENCE COMPARISON
   ↓
TIMELINE RECONSTRUCTION
   ↓
FINANCIAL INVESTIGATION
   ↓
CONTRADICTION
   ↓
DEDUCTIONS
   ↓
ACCUSATION
   ↓
CASE RESULT
   ↓
CASE REVIEW
```

---

# 4. APP LAUNCH

## State

```text
APP_START
```

Show:

```text
THE LAST CALL
```

Then transition quickly to:

```text
MAIN_MENU
```

Do not create a long loading screen.

---

# 5. MAIN MENU

Primary options:

```text
CONTINUE
NEW CASE
SETTINGS
```

If no save exists:

```text
NEW CASE
SETTINGS
```

If a completed case exists:

```text
PLAY AGAIN
REVIEW CASE
SETTINGS
```

---

# 6. NEW CASE

When the player selects:

```text
NEW CASE
```

Show confirmation if an existing investigation exists:

```text
Start a new investigation?

Your current progress will be replaced.
```

Buttons:

```text
CANCEL
START NEW CASE
```

---

# 7. CASE BRIEFING

The briefing introduces:

```text
Victim: Elias Voss
Location: Elias's apartment
Situation: Elias has been found dead.
```

The player is told that Elias had been investigating suspicious financial activity.

Do not reveal:

```text
The culprit
The complete timeline
The motive
The final solution
```

---

# 8. BRIEFING OBJECTIVE

Initial objective:

```text
O001
Investigate the apartment.
```

Then enter:

```text
CRIME_SCENE
```

---

# 9. CRIME SCENE

The player enters the apartment.

The scene should contain interactive investigation points.

Important early objects include:

```text
Victim's phone
Desk / investigation notes
Broken glass
Paperweight
Door
Window
Drawers / documents
```

The player should not be forced to inspect them in one exact order unless necessary.

---

# 10. CRIME-SCENE DESIGN RULE

The player should have multiple investigation paths.

For example:

```text
PHONE
DESK
ROOM
DOOR
WINDOW
DOCUMENTS
```

Do not make the player repeatedly tap the same object hoping for a hidden trigger.

---

# 11. INITIAL EVIDENCE

Early investigation should establish:

```text
E001 Victim's Phone
E002 Last Outgoing Call
E003 Phone Call History
E004 Investigation Notes
E007 Broken Glass
E008 Heavy Paperweight
E009 Apartment Door
E010 Window
E013 Elias's Personal Note
```

Not all need to be discovered immediately.

---

# 12. PHONE FLOW

Player inspects:

```text
Victim's Phone
```

Discover:

```text
E001
```

The phone can then reveal:

```text
E002
E003
```

This establishes that the phone is a meaningful investigation object.

---

# 13. LAST CALL

The player discovers:

```text
E002
```

Important information:

```text
Elias made a final outgoing call at 10:42 PM.
```

This becomes a major timeline anchor.

Add:

```text
T009
```

---

# 14. DESK FLOW

The desk provides:

```text
E004 Investigation Notes
E011 Elias's Research Notes
E012 Financial Transaction Record
```

Deeper investigation can reveal:

```text
E005 Missing Financial File
E019 Financial Investigation File
```

---

# 15. INITIAL CASE FILE UNLOCK

Once the player has enough initial evidence, unlock:

```text
CASE FILE
```

The player can now review:

```text
Evidence
Suspects
Timeline
Objectives
```

Deductions may unlock once enough relationships exist.

---

# 16. SUSPECT INTRODUCTION

The four primary suspects are:

```text
Maya Voss
Victor Hale
Nora Bennett
Daniel Mercer
```

The player can investigate them.

The order does not determine the solution.

---

# 17. SUSPECT FLOW

Generic:

```text
CASE FILE
   ↓
SUSPECTS
   ↓
SELECT SUSPECT
   ↓
SUSPECT PROFILE
   ↓
INTERVIEW
   ↓
QUESTIONS
   ↓
STATEMENTS
   ↓
PRESENT EVIDENCE
   ↓
REACTIONS
   ↓
NEW INFORMATION
```

---

# 18. MAYA FLOW

Initial information:

```text
Maya argued with Elias.
```

Player asks about:

```text
The argument
Her relationship with Elias
When she left
Whether she returned
```

Important result:

```text
Maya left around 9:05 PM.
```

Unlock:

```text
E017 Maya's Transportation Record
T002 Maya leaves
```

---

# 19. MAYA INVESTIGATION RESULT

Maya remains emotionally suspicious because:

```text
She argued with Elias.
```

But her timeline is supported.

The game should teach the player:

> Suspicious behavior does not automatically mean guilt.

Do not explicitly state this as a tutorial message unless necessary.

---

# 20. VICTOR FLOW

Victor is investigated for:

```text
Business relationship
Financial conflict
Where he was that evening
Relationship with Elias
```

Important evidence:

```text
E015 Victor's Dinner Record
T003
```

Victor's alibi should become reasonably supported.

---

# 21. NORA FLOW

Nora is investigated for:

```text
Research relationship
Access to Elias's work
Copied investigation material
Where she was
```

Important information:

```text
Nora copied some investigation material.
```

But:

```text
E016 supports her location at home.
```

This creates suspicion without making her the culprit.

---

# 22. DANIEL FLOW

Daniel is investigated for:

```text
Relationship with Elias
Financial investigation
His visit
Departure time
Whether he returned
Reason for meeting Elias
```

Initial statement:

```text
He left before 10 PM.
```

Important statement:

```text
He says he did not return.
```

This must be recorded.

---

# 23. DANIEL'S FIRST INTERVIEW

The player should not immediately be able to prove Daniel is lying.

Initial result:

```text
Daniel appears cooperative,
but his timeline is important.
```

Record:

```text
E020 Daniel's Timeline Statement
```

---

# 24. INVESTIGATION OPENS FURTHER

After multiple suspect interviews, the player should have enough context to return to:

```text
Crime Scene
Case File
Documents
Timeline
```

The game should encourage investigation without forcing one rigid route.

---

# 25. FINANCIAL INVESTIGATION

The player examines Elias's research.

Discover:

```text
E012 Financial Transaction Record
E019 Financial Investigation File
E014 Daniel's Connection to the Investigation
```

These establish:

```text
Elias was investigating financial misconduct.
Daniel had a reason to fear the investigation.
```

---

# 26. MOTIVE DEDUCTION

Once relevant evidence is discovered:

```text
M001
```

becomes available.

Meaning:

```text
Daniel had a motive connected to Elias's investigation.
```

Do not automatically accuse Daniel yet.

---

# 27. RETURN EVIDENCE

Further investigation reveals:

```text
E018 Daniel's Return Evidence
```

This establishes that Daniel returned to the apartment later.

Important timeline:

```text
T007
10:20 PM
Daniel returns.
```

---

# 28. CONTRADICTION TRIGGER

The contradiction requires:

```text
E018
+
E020
```

Specifically:

```text
Daniel said he left before 10 PM.
Daniel said he did not return.
Evidence places him back at the apartment.
```

Create:

```text
C001
```

---

# 29. CONTRADICTION PRESENTATION

When C001 is created:

Show a meaningful investigation moment.

Possible presentation:

```text
TIMELINE CONTRADICTION

Daniel's statement:
"I left before 10 PM."

New evidence:
Daniel was back at the apartment around 10:20 PM.

His timeline does not match the evidence.
```

This should feel important.

---

# 30. DEDUCTION FLOW

After C001 and motive evidence exist:

Create:

```text
D006
Daniel lied about returning.
```

Then:

```text
D007
Daniel had a motive connected to the investigation.
```

Then:

```text
D008
Daniel's timeline, motive, and opportunity form
the strongest case against him.
```

---

# 31. TIMELINE RECONSTRUCTION

The player can manually review the timeline.

Important events:

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

10:20 PM
Daniel returns.

10:35 PM
Confrontation.

10:42 PM
Elias makes final outgoing call.

10:45 PM
Daniel leaves.
```

---

# 32. TIMELINE UX

The player should not need to know all events immediately.

Events can appear as:

```text
LOCKED
DISCOVERED
SUPPORTED
CONFIRMED
```

The timeline should visually communicate which facts are established.

---

# 33. FREE INVESTIGATION PHASE

After the major systems unlock, allow the player to move freely among:

```text
Crime Scene
Case File
Suspects
Evidence
Timeline
Deductions
Objectives
```

Do not constantly interrupt with mandatory popups.

---

# 34. INVESTIGATION GUIDANCE

If the player appears stuck, the game may provide subtle guidance.

Examples:

```text
An unresolved question remains in the timeline.
```

or:

```text
You should review Daniel's statement against
the evidence you've collected.
```

Do not directly reveal:

```text
DANIEL IS THE KILLER
```

---

# 35. OBJECTIVE FLOW

Recommended sequence:

```text
O001
Investigate the apartment.
        ↓
O002
Identify the people connected to Elias.
        ↓
O003
Verify the suspects' statements.
        ↓
O004
Reconstruct the final timeline.
        ↓
O005
Investigate Elias's financial investigation.
        ↓
O006
Identify contradictions.
        ↓
O007
Establish motive and opportunity.
        ↓
O008
Make a final accusation.
```

Some objectives may overlap.

Do not force the player to complete every objective in a rigid order if the requirements are already satisfied.

---

# 36. OBJECTIVE COMPLETION

When an objective completes:

Show subtle feedback:

```text
OBJECTIVE COMPLETE
```

Then display the next meaningful objective.

Do not use excessive celebratory effects because this is a serious mystery.

---

# 37. CASE READINESS

The accusation becomes strongly available after:

```text
E002
E018
E019
E020
C001
M001
OP001
```

The player can then choose:

```text
MAKE FINAL ACCUSATION
```

---

# 38. ACCUSATION SCREEN

The player chooses:

```text
WHO IS RESPONSIBLE?
```

Suspects:

```text
Maya Voss
Victor Hale
Nora Bennett
Daniel Mercer
```

Then provide:

```text
Why?
Which evidence supports your accusation?
```

---

# 39. ACCUSATION VALIDATION

Correct:

```text
Daniel Mercer
```

Required core logic:

```text
C001
+
M001
+
OP001
```

Result:

```text
CORRECT
```

---

# 40. WRONG ACCUSATION

If the player chooses:

```text
Maya
Victor
Nora
```

show:

```text
THE CASE IS NOT ESTABLISHED

Your evidence does not sufficiently support
this accusation.

The investigation remains open.
```

Return to:

```text
CASE FILE
```

Do not erase progress.

---

# 41. PREMATURE ACCUSATION

If Daniel is selected before the necessary evidence exists:

```text
SUSPICION IS NOT PROOF

You have a strong suspicion,
but the evidence is not yet sufficient
for a final accusation.
```

Return to investigation.

---

# 42. FINAL RECONSTRUCTION

After a correct accusation, show a concise reconstruction.

Recommended sequence:

```text
Elias investigates financial misconduct.
        ↓
Daniel realizes Elias may expose him.
        ↓
Daniel visits Elias.
        ↓
Daniel leaves.
        ↓
Daniel returns later.
        ↓
Elias makes his final call.
        ↓
A confrontation occurs.
        ↓
Daniel leaves again.
        ↓
Daniel lies about returning.
        ↓
The evidence exposes the lie.
```

Do not make this excessively long.

---

# 43. CASE RESULT

Result states:

```text
PERFECT INVESTIGATION
CASE SOLVED
CASE SOLVED — INCOMPLETE
INVESTIGATION CONTINUES
```

---

# 44. PERFECT INVESTIGATION

Conditions:

```text
Correct accusation
+
All critical evidence
+
Major suspect alibis verified
+
Timeline substantially reconstructed
+
Critical contradiction
+
Motive deduction
+
Opportunity deduction
```

Show:

```text
PERFECT INVESTIGATION
```

---

# 45. SOLVED BUT INCOMPLETE

Correct accusation with missing optional information:

```text
CASE SOLVED
```

Then indicate:

```text
Some evidence and deductions were left undiscovered.
```

---

# 46. CASE REVIEW

After solving, allow:

```text
Review Evidence
Review Suspects
Review Timeline
Review Deductions
Review Objectives
Review Final Reconstruction
```

---

# 47. REPLAY

Player can select:

```text
PLAY AGAIN
```

This resets:

```text
CaseState
```

but preserves:

```text
Settings
```

---

# 48. SAVE FLOW

Autosave at:

```text
Case started
Evidence discovered
Interview completed
Statement recorded
Evidence presented
Contradiction created
Deduction created
Objective completed
Accusation submitted
Case solved
```

---

# 49. RESUME FLOW

If the player closes the app during investigation:

```text
Launch
 ↓
Continue
 ↓
Restore CaseState
 ↓
Return to last meaningful screen
```

The game should not reset the case.

---

# 50. RETURN-TO-INVESTIGATION FLOW

When returning from:

```text
Evidence detail
Interview
Timeline
Deduction
Objective
```

return to the most logical previous screen.

Do not always send the player to the main menu.

---

# 51. BACK BUTTON FLOW

Example:

```text
Evidence Detail
    ↓ Back
Evidence List

Evidence List
    ↓ Back
Case File

Case File
    ↓ Back
Investigation

Investigation
    ↓ Back
Briefing/Menu depending on context
```

---

# 52. NO SOFT LOCKS

The player must never reach a state where:

```text
No clue can be found
AND
No interview can progress
AND
No objective can advance
```

If this somehow occurs, provide a recovery route.

---

# 53. OPTIONAL INVESTIGATION ORDER

The player may:

```text
Interview Maya first
Interview Daniel first
Investigate documents first
Build timeline first
Review phone first
```

The game should adapt naturally.

Critical facts should remain available when their requirements are satisfied.

---

# 54. INFORMATION ORDER

The intended narrative order is:

```text
VICTIM
 ↓
CRIME SCENE
 ↓
FINAL CALL
 ↓
SUSPECTS
 ↓
ALIBIS
 ↓
FINANCIAL INVESTIGATION
 ↓
DANIEL'S TIMELINE
 ↓
RETURN EVIDENCE
 ↓
CONTRADICTION
 ↓
MOTIVE
 ↓
ACCUSATION
```

This is the preferred experience, but not a rigid click sequence.

---

# 55. PLAYER KNOWLEDGE VS SYSTEM KNOWLEDGE

The system knows:

```text
Daniel is the culprit.
```

The player should only know what has been discovered.

Never expose hidden state through:

- Debug text
- Incorrect UI labels
- Premature objective descriptions
- Asset filenames
- Dialogue
- Tooltips

---

# 56. ANTI-SPOILER UI

Do not write:

```text
Find evidence proving Daniel is guilty.
```

Instead:

```text
Verify the suspects' statements.
```

Do not write:

```text
Find Daniel's murder weapon.
```

Instead:

```text
Investigate the scene further.
```

---

# 57. GUIDANCE RULE

Hints should answer:

```text
WHAT CAN I INVESTIGATE NEXT?
```

not:

```text
WHO IS THE KILLER?
```

---

# 58. PLAYER AGENCY

The player should feel responsible for solving the case.

Avoid:

```text
automatic deduction every 20 seconds
```

Prefer:

```text
Evidence collected
→ Player notices relationship
→ System confirms a valid deduction
```

---

# 59. DEDUCTION UX

When the player creates or unlocks a deduction:

Show:

```text
NEW DEDUCTION
```

Then:

```text
Title
Reasoning
Supporting evidence
```

The player can revisit it later.

---

# 60. CONTRADICTION UX

Contradictions should feel different from ordinary clues.

Use:

```text
CONTRADICTION FOUND
```

with:

```text
Statement
Evidence
Why they conflict
```

---

# 61. CASE FILE INFORMATION ARCHITECTURE

Recommended:

```text
CASE FILE
│
├── Evidence
│   ├── All
│   ├── Critical
│   └── Supporting
│
├── Suspects
│   ├── Maya
│   ├── Victor
│   ├── Nora
│   └── Daniel
│
├── Timeline
│
├── Deductions
│
└── Objectives
```

---

# 62. COMPLETION FEEDBACK

Completed items should be marked clearly.

Examples:

```text
✓ Investigated apartment
✓ Interviewed Maya
✓ Verified Victor's alibi
✓ Found contradiction
```

Avoid huge banners covering the screen.

---

# 63. GAME FLOW ERROR RECOVERY

If an expected state is missing:

Example:

```text
Player has E018
but C001 does not exist.
```

The system should recalculate contradiction rules.

Do not require the player to rediscover the clue.

---

# 64. RE-ENTERING A SCENE

When returning to the crime scene:

Previously discovered evidence should remain marked.

Example:

```text
Phone
✓ Investigated
```

Do not force repeated discovery.

---

# 65. RE-READING DIALOGUE

Completed dialogue can be revisited through the suspect profile if desired.

The player should not lose previously recorded statements.

---

# 66. REVISITING SUSPECTS

Suspects can be revisited.

New questions appear when conditions are satisfied.

Previously answered questions remain recorded.

---

# 67. QUESTION UNLOCKS

Example:

```text
Daniel initial interview
    ↓
E020 recorded
    ↓
E018 discovered
    ↓
Return to Daniel
    ↓
New question about his return
```

This creates meaningful investigation progression.

---

# 68. NO RANDOM DIALOGUE

Critical suspect statements must be deterministic.

Do not randomly alter:

```text
Alibis
Times
Locations
Evidence reactions
Culprit identity
```

---

# 69. END-TO-END IDEAL PLAYTHROUGH

```text
1. Launch game.

2. Select NEW CASE.

3. Read briefing.

4. Enter apartment.

5. Inspect phone.

6. Discover final call at 10:42 PM.

7. Inspect investigation notes.

8. Learn Elias was investigating financial activity.

9. Examine scene.

10. Open Case File.

11. Review suspect list.

12. Interview Maya.

13. Verify Maya left around 9:05 PM.

14. Interview Victor.

15. Verify dinner alibi.

16. Interview Nora.

17. Learn she copied investigation material but remained home.

18. Interview Daniel.

19. Record his claim that he left before 10 PM
    and never returned.

20. Continue investigating Elias's financial files.

21. Discover Daniel's connection.

22. Discover evidence placing Daniel back at the apartment.

23. Reconstruct the timeline.

24. Trigger C001.

25. Establish that Daniel lied.

26. Establish motive.

27. Establish opportunity.

28. Open final accusation.

29. Select Daniel Mercer.

30. Support accusation with critical evidence.

31. Submit.

32. View final reconstruction.

33. Receive CASE SOLVED.

34. Review case.

35. Optionally replay.
```

---

# 70. ALTERNATIVE PLAYTHROUGH ORDER

A valid player may instead:

```text
Crime scene
→ Daniel
→ Documents
→ Maya
→ Timeline
→ Victor
→ Nora
→ Return to Daniel
→ Contradiction
→ Accusation
```

This should still work.

---

# 71. MINIMUM SOLVABLE PATH

A player should be able to solve the case without discovering every optional clue.

Minimum core path:

```text
E002
E018
E019
E020
C001
M001
OP001
```

plus sufficient suspect context.

---

# 72. OPTIONAL CLUES

Optional evidence exists to:

```text
Increase confidence
Improve immersion
Clear innocent suspects
Improve final score
Make the world feel believable
```

Optional clues must never make the case impossible if missed.

---

# 73. CRITICAL CLUES

Critical clues:

```text
E002
E018
E019
E020
```

The implementation must ensure these are obtainable.

---

# 74. CRITICAL FLOW GUARANTEE

The game must never permanently hide a critical clue behind:

```text
A random event
An irreversible choice
A missable one-time dialogue
A timer
A hidden arbitrary condition
```

---

# 75. NO TIME LIMIT

The core case should not have a real-time countdown.

The player can investigate at their own pace.

---

# 76. NO FAILURE FROM EXPLORATION

Inspecting the wrong object does not cause:

```text
Game over
Lost clue
Permanent failure
```

---

# 77. NO COMBAT

The game is investigation-focused.

There is no:

```text
Combat
Weapon system
Enemy system
Action gameplay
```

---

# 78. NO RPG GRIND

Do not add:

```text
XP
Levels
Skill trees
Loot
Currency
```

The player's progression is knowledge.

---

# 79. KNOWLEDGE IS PROGRESSION

The intended progression is:

```text
"I don't know what happened."
        ↓
"I know the important people."
        ↓
"I know their timelines."
        ↓
"I found inconsistencies."
        ↓
"I know what Elias was investigating."
        ↓
"I know Daniel lied."
        ↓
"I can prove Daniel had motive and opportunity."
        ↓
"I can make the accusation."
```

---

# 80. FINAL DESIGN PRINCIPLE

The game should never feel like:

```text
Click everything until the game tells me the answer.
```

It should feel like:

```text
I collected information,
compared what people said,
noticed a contradiction,
connected the evidence,
and reached the conclusion myself.
```

---

# 81. FINAL FLOW CHECKLIST

Before release:

```text
[ ] App launches
[ ] New Case works
[ ] Continue works
[ ] Briefing works
[ ] Crime scene works
[ ] Evidence discovery works
[ ] Evidence persists
[ ] Case File works
[ ] Suspects work
[ ] Interviews work
[ ] Statements persist
[ ] Evidence presentation works
[ ] Timeline works
[ ] Deductions work
[ ] Contradiction works
[ ] Objectives work
[ ] Accusation works
[ ] Wrong accusation works
[ ] Premature accusation works
[ ] Correct accusation works
[ ] Result screen works
[ ] Case review works
[ ] Restart works
[ ] Save/load works
[ ] Android Back works
[ ] Offline mode works
[ ] No critical clue is missable
[ ] No critical path can soft-lock
```

---

# 82. CANONICAL END

The intended emotional progression is:

```text
CURIOSITY
   ↓
UNCERTAINTY
   ↓
SUSPICION
   ↓
INVESTIGATION
   ↓
CONTRADICTION
   ↓
CLARITY
   ↓
CONFIDENCE
   ↓
ACCUSATION
   ↓
RESOLUTION
```

That progression is the heart of **The Last Call**.
