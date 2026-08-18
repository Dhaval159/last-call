# THE LAST CALL
# UI / UX BIBLE

**Case ID:** CASE-001  
**Document Version:** 1.0  
**Status:** Canonical UI/UX Specification  
**Platform:** Android  
**Environment:** Google AI Studio Web App Generation  
**Orientation:** Landscape preferred  
**Purpose:** Defines every major screen, navigation pattern, interaction pattern, layout rule, and mobile UX requirement.

---

# 1. PURPOSE

This document defines how **The Last Call** should look and behave from the player's perspective.

The implementation AI must use this document when creating:

- Main menu
- Case briefing
- Crime scene
- Investigation interactions
- Evidence viewer
- Case file
- Suspect list
- Interview screen
- Evidence presentation
- Timeline
- Deduction board
- Final accusation
- Case result
- Pause/settings
- Mobile navigation

This document defines UI/UX.

It does not replace:

- PRD
- Game Design Bible
- Dialogue Bible
- Timeline Bible
- Gameplay Systems Bible
- Evidence Bible
- Suspects Bible

---

# 2. CORE UX PHILOSOPHY

The interface should feel like a professional detective investigation tool.

It should be:

- Clean
- Dark
- Serious
- Minimal
- Readable
- Responsive
- Touch-friendly
- Information-focused

Avoid the appearance of:

- A generic AI-generated dashboard
- A mobile banking app
- A fantasy RPG menu
- A social media application
- A cluttered visual novel

---

# 3. VISUAL MOOD

Recommended visual direction:

```text
Dark investigation room
Muted surfaces
Warm off-white text
Subtle red investigation accents
Soft shadows
Thin borders
Minimal animations
```

The interface should feel slightly cinematic without becoming difficult to use.

---

# 4. COLOR SYSTEM

Use a restrained palette.

## Background

Very dark charcoal.

## Surface

Slightly lighter charcoal.

## Elevated Surface

Used for cards and dialogs.

## Primary Text

Warm white.

## Secondary Text

Muted gray.

## Accent

Muted investigative red.

## Success

Muted green.

## Warning

Muted amber.

Do not use extremely saturated neon colors.

---

# 5. TYPOGRAPHY

Use a clean readable sans-serif font.

Recommended hierarchy:

```text
GAME TITLE
Large

SCREEN TITLE
Medium/Large

SECTION TITLE
Medium

BODY
Regular

SECONDARY INFORMATION
Small

METADATA
Small / muted
```

Avoid decorative fonts for body text.

---

# 6. MOBILE-FIRST RULE

The game must be comfortable to play on a phone.

All important controls must be easy to tap.

Avoid tiny text.

Avoid tiny icons without labels.

Avoid buttons placed directly against screen edges.

---

# 7. SAFE AREA

Respect:

- Android status bar
- Navigation area
- Camera cutouts
- Rounded display corners

Content should have reasonable padding.

---

# 8. GLOBAL NAVIGATION

Primary navigation should be contextual.

During investigation:

```text
CASE FILE
```

opens the investigation hub.

The player should not need a permanent five-button navigation bar covering the bottom of the screen.

---

# 9. MAIN MENU

Screen:

```text
--------------------------------------------

              THE LAST CALL

          A Detective Investigation

              [ NEW CASE ]

              [ CONTINUE ]

              [ SETTINGS ]

--------------------------------------------
```

The title should have strong visual presence.

The menu should remain minimal.

---

# 10. MAIN MENU BACKGROUND

Recommended:

A dark, slightly blurred apartment interior.

Possible visual elements:

- Desk lamp
- Files
- Phone
- Window
- Rain outside
- Police tape-like visual texture

Do not make the background too busy.

---

# 11. NEW CASE BUTTON

Primary button.

Label:

**NEW CASE**

Tap:

Open new-case confirmation if a save exists.

---

# 12. CONTINUE BUTTON

If no save:

```text
CONTINUE
```

appears disabled.

If save exists:

```text
CONTINUE
```

is active.

---

# 13. SETTINGS BUTTON

Opens:

```text
SETTINGS

Music        [ ON ]
Sound        [ ON ]
Text Speed   [ NORMAL ]
Hints        [ ON ]

[ RESET CASE ]

[ BACK ]
```

---

# 14. CASE BRIEFING SCREEN

Layout:

```text
CASE 001

THE LAST CALL

VICTIM
Elias Voss

LOCATION
Private apartment

STATUS
Homicide investigation

--------------------------------

CASE SUMMARY

[briefing text]

--------------------------------

OBJECTIVE

Investigate the apartment
and establish what happened.

              [ ENTER SCENE ]
```

---

# 15. CASE BRIEFING UX

Do not dump the entire mystery on the player.

The briefing should establish:

- Who died
- Where
- Basic circumstances
- What the player needs to do

The player should still have questions.

---

# 16. CRIME SCENE SCREEN

This is the main exploration screen.

Recommended layout:

```text
------------------------------------------------
| CASE FILE                         OBJECTIVE  |
|                                              |
|                                              |
|              APARTMENT                      |
|                                              |
|        [Desk]        [Phone]                |
|                                              |
|                    [Body Area]              |
|                                              |
| [Door]                         [Window]      |
|                                              |
------------------------------------------------
```

---

# 17. CRIME SCENE TOP BAR

Top-left:

```text
CASE FILE
```

Top-right:

```text
OBJECTIVE
```

The objective should be compact.

Example:

```text
OBJECTIVE
Investigate the apartment
```

---

# 18. INTERACTABLE OBJECTS

Objects should provide subtle visual feedback when tapped or hovered.

On touch:

- Slight highlight
- Small interaction marker
- Open inspection panel

Do not use large glowing outlines around every object at all times.

---

# 19. INTERACTION PANEL

When the player taps an object:

```text
--------------------------------
DESK

An old wooden desk covered
with investigation notes.

[EXAMINE]
[BACK]
--------------------------------
```

If the object has evidence:

```text
EVIDENCE FOUND

Investigation notes added
to the case file.

[VIEW EVIDENCE]
[CONTINUE]
```

---

# 20. EVIDENCE DISCOVERY NOTIFICATION

Use a compact modal.

```text
EVIDENCE DISCOVERED

E002
Last Outgoing Call

Elias's phone records show
an outgoing call at 10:42 PM.

[ADD TO CASE]
```

---

# 21. CASE FILE BUTTON

The case file is available from investigation scenes.

When opened:

```text
CASE FILE
--------------------------------
EVIDENCE
SUSPECTS
TIMELINE
DEDUCTIONS
OBJECTIVES
--------------------------------
```

Use tabs or a segmented navigation system.

---

# 22. CASE FILE — EVIDENCE TAB

Layout:

```text
EVIDENCE

[ Search evidence ]

18 ITEMS

--------------------------------
PHONE
Last Outgoing Call
DIGITAL
--------------------------------

PAPERWEIGHT
Heavy Paperweight
PHYSICAL
--------------------------------

FINANCIAL FILE
Investigation Material
DOCUMENT
--------------------------------
```

---

# 23. EVIDENCE CARD DESIGN

Each card should show:

```text
ICON

NAME
CATEGORY

One-line description

STATUS
```

Example:

```text
PHONE

Last Outgoing Call
DIGITAL

Elias's final outgoing call.

INSPECTED
```

---

# 24. EVIDENCE DETAIL SCREEN

Layout:

```text
< BACK

LAST OUTGOING CALL

DIGITAL EVIDENCE

--------------------------------

Elias's phone contains
an outgoing call recorded
at 10:42 PM.

--------------------------------

SIGNIFICANCE

Establishes that Elias
was alive at 10:42 PM.

--------------------------------

RELATED

Timeline
Daniel Mercer
```

---

# 25. EVIDENCE RELATIONSHIP DISPLAY

When evidence is connected to something:

```text
RELATED TO

Daniel Mercer
10:42 PM Timeline Event
Return Evidence
```

Each related item should be tappable.

---

# 26. SUSPECTS TAB

Layout:

```text
SUSPECTS

--------------------------------
MAYA VOSS
Family
INTERVIEWED
--------------------------------

VICTOR HALE
Business associate
INTERVIEWED
--------------------------------

NORA BENNETT
Research associate
INTERVIEWED
--------------------------------

DANIEL MERCER
Professional contact
INTERVIEWED
--------------------------------
```

Do not label anyone:

**KILLER**

or:

**GUILTY**

---

# 27. SUSPECT CARD

Each suspect card can display:

```text
PORTRAIT

NAME
RELATIONSHIP

INTERVIEW STATUS

[VIEW FILE]
```

Keep the card compact.

---

# 28. SUSPECT DETAIL SCREEN

Example:

```text
DANIEL MERCER

Professional contact

--------------------------------

INTERVIEW
Completed

ALIBI
Questioned

EVIDENCE LINKS
3

CONTRADICTIONS
1

--------------------------------

[INTERVIEW]
[CASE CONNECTIONS]
```

---

# 29. INTERVIEW SCREEN

Recommended layout:

```text
------------------------------------------------
| DANIEL MERCER                    [CASE FILE] |
|                                              |
|             CHARACTER PORTRAIT               |
|                                              |
| Daniel dialogue appears here.                |
|                                              |
|----------------------------------------------|
| [Ask about Elias]                            |
| [Ask about the argument]                     |
| [Ask about timeline]                         |
| [Ask about investigation]                    |
| [Present Evidence]                           |
| [End Interview]                              |
------------------------------------------------
```

---

# 30. DIALOGUE PANEL

Dialogue should have strong readability.

Character name:

```text
DANIEL MERCER
```

Dialogue:

```text
"I already told you what happened."
```

Question options appear below.

---

# 31. DIALOGUE ANIMATION

Optional:

- Fade in
- Small typewriter effect
- Character expression change

Avoid excessively slow text animations.

Text should be quickly readable.

---

# 32. TEXT SPEED

Settings:

```text
SLOW
NORMAL
FAST
INSTANT
```

Default:

**NORMAL**

---

# 33. QUESTION BUTTONS

Question buttons should be clear.

Example:

```text
[ What were you discussing? ]

[ When did you leave? ]

[ Did you return? ]

[ Where did you go afterward? ]
```

Avoid vague buttons such as:

```text
Tell me more
Continue
Question him
```

unless the dialogue context requires them.

---

# 34. ASKED QUESTIONS

After a question has been answered:

```text
✓ When did you leave?
```

It remains accessible but visually subdued.

---

# 35. LOCKED QUESTIONS

If a question requires evidence:

```text
🔒 Ask about the missing file
```

Optional small explanation:

```text
Requires additional evidence.
```

Do not reveal the exact clue unless intended.

---

# 36. PRESENT EVIDENCE SCREEN

When selecting:

**PRESENT EVIDENCE**

open a modal:

```text
PRESENT EVIDENCE

Choose an item to show.

[ Last Outgoing Call ]
[ Financial Investigation ]
[ Return Evidence ]
[ Desk Fingerprint ]

[ CANCEL ]
```

---

# 37. EVIDENCE FILTER

Optional filters:

```text
ALL
DIGITAL
DOCUMENT
PHYSICAL
FINANCIAL
TIMELINE
```

Useful when the evidence inventory becomes larger.

---

# 38. PRESENTED EVIDENCE CONFIRMATION

Before presenting:

```text
PRESENT THIS EVIDENCE?

Return Evidence

This may change the conversation.

[ PRESENT ]
[ CANCEL ]
```

---

# 39. CONTRADICTION UI

When a contradiction is established:

```text
--------------------------------
CONTRADICTION FOUND

Daniel said he never returned.

Evidence places him at the
apartment later that night.

--------------------------------

[ADD TO DEDUCTIONS]
[CONTINUE]
--------------------------------
```

The presentation should feel important but not like a game-over screen.

---

# 40. TIMELINE SCREEN

Layout:

```text
CASE TIMELINE

8:40 PM
Maya argues with Elias
✓ CONFIRMED

9:05 PM
Maya leaves
✓ CONFIRMED

9:40 PM
Daniel arrives
✓ CONFIRMED

9:50 PM
Daniel leaves
✓ STATEMENT

10:20 PM
?????????????
UNKNOWN

10:42 PM
Elias's final call
✓ CONFIRMED

10:45 PM
?????????????
UNKNOWN
```

---

# 41. TIMELINE EVENT CARD

Each event should show:

```text
TIME

EVENT

STATUS

SUPPORTING EVIDENCE
```

Tap to expand.

---

# 42. TIMELINE STATUS

Use text and icons.

```text
? UNKNOWN
~ SUSPECTED
○ SUPPORTED
✓ CONFIRMED
```

Do not rely on color alone.

---

# 43. TIMELINE ADD EVENT

When appropriate:

```text
ADD TIMELINE EVENT

TIME
[ 10:20 PM ]

EVENT
[ Daniel returned to the apartment ]

SUPPORTING EVIDENCE
[ Return Evidence ]

[ CONFIRM ]
[ CANCEL ]
```

---

# 44. DEDUCTION BOARD

The deduction board is the player's reasoning space.

It should resemble a clean detective wall rather than a generic flowchart.

Possible layout:

```text
------------------------------------------------
| DEDUCTIONS                                   |
|                                              |
| [Last Call] ---- [Elias Alive]              |
|                       |                      |
|                       ↓                      |
|                [Daniel Timeline]             |
|                       |                      |
|                       ↓                      |
|                [Return Evidence]             |
|                       |                      |
|                       ↓                      |
|                 [False Alibi]                |
|                                              |
------------------------------------------------
```

---

# 45. DEDUCTION NODE

Each node contains:

```text
TITLE
TYPE
SHORT DESCRIPTION
```

Example:

```text
RETURN EVIDENCE
TIMELINE

Daniel was present after
his claimed departure.
```

---

# 46. DEDUCTION CONNECTION

When a valid connection is created:

- Draw a line
- Brief animation
- Display confirmation
- Update deduction count

Example:

```text
CONNECTION ESTABLISHED
```

---

# 47. INVALID CONNECTION

If incorrect:

```text
No meaningful connection established.

Review the evidence.
```

Do not remove evidence.

---

# 48. OBJECTIVES SCREEN

Layout:

```text
OBJECTIVES

CURRENT

○ Reconstruct the final timeline

COMPLETED

✓ Investigate the apartment
✓ Identify the suspects
✓ Verify the initial alibis
✓ Investigate the financial material
```

Keep it simple.

---

# 49. CASE PROGRESS

Optional small indicator:

```text
CASE PROGRESS
████████░░ 80%
```

Do not reveal whether the player is close to the correct culprit.

Progress should represent investigation completeness, not "distance to killer."

---

# 50. FINAL ACCUSATION SCREEN

The final accusation should feel serious.

```text
FINAL ACCUSATION

Who killed Elias Voss?

--------------------------------

[ Maya Voss ]
[ Victor Hale ]
[ Nora Bennett ]
[ Daniel Mercer ]

--------------------------------

MOTIVE

[ Select motive ]

--------------------------------

KEY EVIDENCE

[ Select evidence ]

--------------------------------

[ REVIEW CASE ]
[ MAKE ACCUSATION ]
```

---

# 51. ACCUSATION CONFIRMATION

Before submission:

```text
FINALIZE ACCUSATION?

Suspect:
Daniel Mercer

Motive:
Prevent exposure of financial misconduct

Evidence:
Return Evidence
Last Outgoing Call
Financial Investigation

[ SUBMIT ]
[ REVIEW ]
```

---

# 52. WRONG ACCUSATION SCREEN

Do not say:

> "Wrong! The killer is Daniel."

Instead:

```text
ACCUSATION NOT SUPPORTED

Your conclusion conflicts with
the established evidence.

The investigation remains open.

[RETURN TO CASE FILE]
```

---

# 53. PARTIAL ACCUSATION

If Daniel is selected too early:

```text
STRONG SUSPICION

Daniel Mercer remains suspicious.

However, the evidence is not
strong enough to support a final accusation.

Continue investigating.
```

---

# 54. CASE SOLVED SCREEN

```text
CASE SOLVED

THE LAST CALL

--------------------------------

CULPRIT

DANIEL MERCER

--------------------------------

MOTIVE

Prevent exposure of financial
misconduct.

--------------------------------

KEY DISCOVERY

Daniel returned after claiming
he had already left.

--------------------------------

[VIEW RECONSTRUCTION]
[VIEW CASE FILE]
[MAIN MENU]
```

---

# 55. FINAL RECONSTRUCTION SCREEN

The game should visually reconstruct the evening.

Example:

```text
THE NIGHT OF THE MURDER

9:05 PM
Maya leaves.

9:40 PM
Daniel arrives.

9:50 PM
Daniel leaves.

10:20 PM
Daniel returns.

10:37 PM
The confrontation becomes violent.

10:42 PM
Elias makes his final call.

10:45 PM
Daniel leaves.
```

Use subtle transitions between events.

---

# 56. PAUSE MENU

During exploration:

```text
PAUSED

[ CASE FILE ]

[ SETTINGS ]

[ SAVE ]

[ RETURN TO TITLE ]
```

The game should pause any active timers or animations.

There should be no timed gameplay, so pause should be safe at all times.

---

# 57. BACK NAVIGATION

Android back behavior:

### During dialogue

Return to question list or close dialogue only after confirmation if needed.

### In evidence viewer

Return to evidence list.

### In case file

Return to previous gameplay scene.

### In settings

Return to previous menu.

Never unexpectedly exit the entire case.

---

# 58. MODAL RULES

Use modals only for:

- Confirmation
- Important evidence discovery
- Contradictions
- Final accusation
- Critical case result

Do not use modals for every interaction.

---

# 59. SCROLLING

Use vertical scrolling for:

- Long dialogue
- Evidence descriptions
- Case notes
- Suspect information

Keep critical buttons accessible.

If a screen has long content:

```text
content scrolls
bottom action remains visible
```

where practical.

---

# 60. BUTTON DESIGN

Primary button:

```text
[ ENTER SCENE ]
```

Secondary:

```text
[ BACK ]
```

Danger/destructive:

```text
[ RESET CASE ]
```

Important action:

```text
[ MAKE ACCUSATION ]
```

Avoid using the same visual emphasis for every button.

---

# 61. TOUCH TARGETS

Interactive elements should be comfortably tappable.

Avoid:

- tiny text links
- tiny icons
- tightly packed buttons
- buttons that require precision

If multiple buttons are stacked vertically, provide enough spacing.

---

# 62. ICONS

Use simple recognizable icons.

Examples:

```text
Evidence       document icon
Suspects       people icon
Timeline       clock icon
Deductions     connection icon
Objectives     checklist icon
Settings       gear icon
Back           arrow
```

Icons should support text, not replace it.

---

# 63. LOADING SCREEN

If loading is required:

```text
THE LAST CALL

Loading investigation...
```

Keep it brief.

Do not show fake progress percentages unless actual progress is available.

---

# 64. ERROR SCREEN

If an unrecoverable application error occurs:

```text
Something went wrong.

Your investigation has been preserved.

[RETURN TO CASE]
[RELOAD]
```

Never expose technical stack traces to the player.

---

# 65. EMPTY STATES

Evidence:

```text
No evidence discovered yet.
```

Suspects:

```text
No suspect information available.
```

Timeline:

```text
No confirmed timeline events yet.
```

Deductions:

```text
No deductions established.
```

Avoid blank screens.

---

# 66. FIRST-TIME PLAYER GUIDANCE

During the first crime-scene visit, show a small hint:

```text
TIP

Tap objects around the apartment
to investigate them.

You can review everything later
from the Case File.
```

Allow dismissal.

---

# 67. TUTORIAL

The tutorial should be embedded naturally into gameplay.

Do not create a long tutorial level.

Teach:

1. Tap object
2. Inspect evidence
3. Open case file
4. Interview suspect
5. Present evidence
6. Connect clues

---

# 68. FIRST EVIDENCE MOMENT

When the player finds the first evidence, explain:

```text
EVIDENCE

Important discoveries are saved
automatically to your Case File.

You can review them at any time.
```

Then continue.

---

# 69. FIRST INTERVIEW

Before the first suspect interview:

```text
INTERVIEW TIP

Ask questions carefully.

A suspect's statement can later
be compared with evidence.
```

---

# 70. FIRST CONTRADICTION

When the player discovers the first contradiction:

```text
CONTRADICTION

A statement does not match
the evidence.

Contradictions can help reconstruct
what really happened.
```

---

# 71. DEDUCTION TUTORIAL

When opening the deduction board for the first time:

```text
DEDUCTIONS

Connect facts that support
the same conclusion.

You are building the case yourself.
```

---

# 72. TIMELINE TUTORIAL

First opening:

```text
TIMELINE

Use evidence to establish
when events happened.

Unknown events can be reconstructed
as new information is discovered.
```

---

# 73. UX RULE: NEVER HIDE INFORMATION

Once the player discovers an important fact, it must remain accessible.

The player should be able to review:

- Evidence
- Dialogue
- Statements
- Timeline
- Contradictions
- Deductions

without replaying the entire game.

---

# 74. CASE MEMORY

Every important suspect statement should be recorded automatically.

Example:

```text
DANIEL MERCER

Statement:
"I left before 10 PM."

Source:
Interview

Status:
Unverified
```

This allows the player to compare statements later.

---

# 75. STATEMENT LOG

Inside each suspect profile:

```text
STATEMENTS

"When did you leave?"
Before 10 PM.

"Did you return?"
No.

"Why were you at Elias's apartment?"
To discuss the investigation.
```

Tap a statement to view the original question.

---

# 76. CONTRADICTION LOG

Inside the case file:

```text
CONTRADICTIONS

01
Daniel's departure claim conflicts
with return evidence.

[VIEW]
```

This prevents the player from needing to remember every detail.

---

# 77. EVIDENCE SOURCE LABEL

Every evidence item should display how it was discovered.

Examples:

```text
SOURCE
Elias's Apartment

SOURCE
Phone Records

SOURCE
Transportation Record

SOURCE
Digital Activity
```

This makes the case feel more grounded.

---

# 78. NO INVENTED PLAYER ACTIONS

The UI must not claim:

> "You searched the bedroom."

unless the player actually performed the relevant interaction.

The game should record real player actions.

---

# 79. UI STATE PERSISTENCE

If the player leaves a screen and returns:

- Selected tabs may be remembered.
- Scroll position may optionally be remembered.
- Evidence state must remain.
- Dialogue state must remain.
- Timeline state must remain.

---

# 80. RESPONSIVE LAYOUT RULE

For larger tablets:

Use additional horizontal space.

Example:

```text
LEFT:
Evidence list

CENTER:
Evidence detail

RIGHT:
Related clues
```

For phones:

Use stacked layout.

```text
Evidence list
↓
Evidence detail
↓
Related clues
```

---

# 81. PERFORMANCE UX

Transitions should be quick.

Avoid:

- Long fades
- Heavy particle effects
- Excessive screen movement
- Large animated backgrounds

The interface should feel responsive.

---

# 82. ANIMATION RULES

Recommended animation duration:

```text
Micro interaction:
100–200 ms

Panel:
200–300 ms

Major transition:
300–500 ms
```

Avoid animation delays before important interactions.

---

# 83. SOUND FEEDBACK

Examples:

Evidence discovered:

**soft click / paper sound**

Contradiction:

**subtle low pulse**

Correct deduction:

**quiet confirmation tone**

Case solved:

**restrained resolution cue**

Avoid arcade-style sounds.

---

# 84. VISUAL HIERARCHY

Every screen should answer:

1. Where am I?
2. What am I looking at?
3. What can I do?
4. What changed?
5. How do I go back?

If a screen cannot answer these clearly, simplify it.

---

# 85. INFORMATION DENSITY

The game contains a lot of information.

Do not display everything simultaneously.

Use:

- Tabs
- Expandable cards
- Detail screens
- Filters
- Related evidence
- Collapsible sections

This keeps the interface manageable.

---

# 86. CORE NAVIGATION MAP

```text
MAIN MENU
   |
   +-- NEW CASE
   |      |
   |      +-- BRIEFING
   |             |
   |             +-- CRIME SCENE
   |
   +-- CONTINUE
          |
          +-- CURRENT CASE
                 |
                 +-- CASE FILE
                 |      |
                 |      +-- EVIDENCE
                 |      +-- SUSPECTS
                 |      +-- TIMELINE
                 |      +-- DEDUCTIONS
                 |      +-- OBJECTIVES
                 |
                 +-- INTERVIEWS
                 |
                 +-- FINAL ACCUSATION
                 |
                 +-- CASE RESULT
```

---

# 87. CORE UX FLOW

```text
START
 ↓
BRIEFING
 ↓
SCENE
 ↓
INSPECT
 ↓
EVIDENCE
 ↓
CASE FILE
 ↓
SUSPECT
 ↓
QUESTION
 ↓
PRESENT EVIDENCE
 ↓
CONTRADICTION
 ↓
TIMELINE
 ↓
DEDUCTION
 ↓
ACCUSATION
 ↓
RECONSTRUCTION
 ↓
RESULT
```

---

# 88. UX FAILURE CONDITIONS

The player should never feel lost because of UI.

If the player has no obvious next action:

Show:

```text
OBJECTIVE

Review the suspect timelines.
```

If they need evidence:

```text
A clue from the case file may help.
```

Hints should guide without revealing the answer.

---

# 89. FINAL UX PRINCIPLE

The interface should disappear into the investigation.

The player should focus on:

```text
People
Statements
Evidence
Time
Contradictions
```

not on managing complicated menus.

---

# 90. IMPLEMENTATION PRIORITY

Build UI in this order:

```text
1. Main Menu
2. Briefing
3. Crime Scene
4. Interaction Panel
5. Evidence Viewer
6. Case File
7. Suspect List
8. Interview Screen
9. Evidence Presentation
10. Timeline
11. Deduction Board
12. Final Accusation
13. Case Result
14. Settings
15. Polish
```

---

# 91. FINAL UI/UX REQUIREMENT

The finished application should feel like a **small polished detective game**, not an AI-generated prototype.

Prioritize:

- Consistency
- Readability
- Fast interactions
- Clear navigation
- Strong atmosphere
- Mobile usability
- Evidence visibility
- Player reasoning

The UI should support the mystery rather than compete with it.
