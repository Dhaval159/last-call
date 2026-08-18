# THE LAST CALL
# EVIDENCE BIBLE

**Case ID:** CASE-001  
**Document Version:** 1.0  
**Status:** Canonical Evidence Specification  
**Purpose:** Defines every evidence item available in Case 001.

---

# 1. PURPOSE

This document is the authoritative source for all evidence in **The Last Call**.

The implementation must use these evidence definitions exactly.

Do not invent additional story-critical evidence.

Do not change the meaning of evidence.

Do not create contradictions that are not specified here.

Evidence may be discovered in different orders, but its underlying meaning must remain constant.

---

# 2. EVIDENCE SYSTEM

Every evidence item has:

- Unique ID
- Name
- Category
- Location
- Discovery condition
- Player-facing description
- Detailed investigation result
- Actual significance
- Related suspects
- Related timeline events
- Contradictions
- Importance level

---

# 3. EVIDENCE CATEGORIES

Evidence belongs to one of five categories:

### PHYSICAL

Objects or physical traces found at the scene.

### DIGITAL

Phones, messages, records, computers, or other digital information.

### DOCUMENTARY

Written records, files, notes, receipts, and documents.

### TESTIMONIAL

Statements made by suspects or witnesses.

### ENVIRONMENTAL

Observations about the crime scene.

---

# 4. IMPORTANCE LEVELS

### LEVEL 1 — CONTEXT

Helps establish the world.

### LEVEL 2 — RELEVANT

Helps narrow the investigation.

### LEVEL 3 — IMPORTANT

Helps establish a major theory.

### LEVEL 4 — CRITICAL

Necessary for proving the final solution.

---

# 5. COMPLETE EVIDENCE LIST

Case 001 contains **20 primary evidence items**.

```text
E001 — Victim's Phone
E002 — Last Outgoing Call
E003 — Phone Contact Metadata
E004 — Desk Document
E005 — Missing Financial File
E006 — Desk Fingerprint
E007 — Broken Glass
E008 — Heavy Paperweight
E009 — Apartment Door
E010 — Window
E011 — Maya's Statement
E012 — Victor's Statement
E013 — Nora's Statement
E014 — Daniel's Initial Statement
E015 — Victor's Dinner Record
E016 — Nora's Digital Activity
E017 — Maya's Transportation Record
E018 — Daniel's Return Evidence
E019 — Financial Investigation File
E020 — Daniel's Final Contradiction
```

---

# 6. E001 — VICTIM'S PHONE

## Category

Digital

## Importance

Level 2 — Relevant

## Location

Victim's desk.

## Discovery

Available during the initial investigation.

## Player Description

> Elias's phone is lying beside a stack of papers on the desk. The screen is locked, but the recent-call notification is visible.

## Detailed Investigation

The phone contains a visible recent outgoing call.

The exact recipient is not immediately clear.

## Significance

Establishes that Elias was actively using his phone shortly before his death.

It leads the player toward E002.

## Related

- E002
- E003
- Timeline event at 10:42 PM

---

# 7. E002 — LAST OUTGOING CALL

## Category

Digital

## Importance

Level 4 — Critical

## Location

Victim's phone.

## Discovery

Triggered by investigating E001.

## Player Description

> The last outgoing call was placed at 10:42 PM.

The recipient is listed using a number rather than an immediately recognizable name.

## Detailed Investigation

The call lasted long enough to establish that Elias intentionally contacted someone shortly before the critical event.

## Significance

Establishes that Elias was alive at 10:42 PM.

This is essential to reconstructing the timeline.

## Related

- E001
- E003
- Timeline event at 10:42 PM
- Daniel's timeline

---

# 8. E003 — PHONE CONTACT METADATA

## Category

Digital

## Importance

Level 3 — Important

## Location

Victim's phone.

## Discovery

Available after inspecting the call record.

## Player Description

> The phone number belongs to a contact stored under an abbreviated name.

The contact information links the number to a person connected to Elias's investigation.

## Significance

The call provides a connection between Elias's investigation and the events shortly before his death.

It should create suspicion without immediately revealing the murderer.

## Related

- E002
- E019
- Timeline

---

# 9. E004 — DESK DOCUMENT

## Category

Documentary

## Importance

Level 2 — Relevant

## Location

Victim's desk.

## Discovery

Initial investigation.

## Player Description

> Several printed pages cover Elias's desk. Most contain financial figures and company information.

## Detailed Investigation

The documents indicate that Elias was investigating irregular financial transactions.

Some sections are marked with handwritten notes.

## Significance

Establishes that Elias was actively working on a financial investigation.

## Related

- E005
- E019
- Daniel
- Victor

---

# 10. E005 — MISSING FINANCIAL FILE

## Category

Documentary

## Importance

Level 3 — Important

## Location

Victim's desk.

## Discovery

After examining E004.

## Player Description

> A section of the investigation appears incomplete. One expected file is missing from the collection.

## Detailed Investigation

The missing material appears to have contained information connecting people involved in the financial investigation.

## Significance

Establishes that someone may have deliberately removed evidence after the confrontation.

## Related

- E004
- E019
- Daniel

---

# 11. E006 — DESK FINGERPRINT

## Category

Physical

## Importance

Level 4 — Critical

## Location

Victim's desk.

## Discovery

After examining the desk carefully.

## Player Description

> A partial fingerprint is visible on the polished surface of the desk.

## Detailed Investigation

The print belongs to Daniel.

This does not prove murder by itself because Daniel admits he was at the apartment earlier.

## Significance

Initially weak evidence.

Later becomes important when combined with the timeline.

## Related

- Daniel
- E014
- E018
- E020

---

# 12. E007 — BROKEN GLASS

## Category

Physical

## Importance

Level 2 — Relevant

## Location

Living room.

## Discovery

Initial investigation.

## Player Description

> A drinking glass lies broken near the edge of the living room.

## Significance

Suggests a physical confrontation occurred.

It does not identify the attacker.

## Related

- E008
- Timeline
- Daniel

---

# 13. E008 — HEAVY PAPERWEIGHT

## Category

Physical

## Importance

Level 3 — Important

## Location

Near the desk.

## Discovery

After examining the area around E007.

## Player Description

> A heavy metal paperweight lies on the floor near the desk.

## Detailed Investigation

The object appears to have been recently moved.

The exact forensic significance is not immediately available to the player.

## Significance

This is the object used by Daniel during the confrontation.

It becomes important during the final reconstruction.

## Related

- E007
- Daniel
- Final solution

---

# 14. E009 — APARTMENT DOOR

## Category

Environmental

## Importance

Level 2 — Relevant

## Location

Entrance.

## Discovery

Initial investigation.

## Player Description

> There are no obvious signs that the apartment door was forced open.

## Significance

Suggests the attacker was likely someone Elias knew or willingly allowed inside.

This narrows the suspect pool.

## Related

- All suspects
- Timeline

---

# 15. E010 — WINDOW

## Category

Environmental

## Importance

Level 1 — Context

## Location

Living room.

## Discovery

Initial investigation.

## Player Description

> The window is unlocked.

## Significance

Creates an early possibility that someone entered or exited through the window.

However, there is no evidence that this actually happened.

This is a mild red herring.

## Important Rule

The window must never become a false solution.

---

# 16. E011 — MAYA'S STATEMENT

## Category

Testimonial

## Importance

Level 3 — Important

## Source

Maya Voss.

## Initial Statement

Maya admits arguing with Elias earlier.

She claims she left at approximately 9:05 PM.

She denies returning.

## Significance

Initially makes Maya suspicious.

Later supported by E017.

## Related

- Maya
- E017
- Timeline

---

# 17. E012 — VICTOR'S STATEMENT

## Category

Testimonial

## Importance

Level 2 — Relevant

## Source

Victor Hale.

## Initial Statement

Victor admits Elias was investigating his company.

He confirms they had conflicts.

He denies visiting Elias's apartment that evening.

## Significance

Creates the strongest obvious motive.

Later weakened by E015.

---

# 18. E013 — NORA'S STATEMENT

## Category

Testimonial

## Importance

Level 2 — Relevant

## Source

Nora Bennett.

## Initial Statement

Nora says she worked with Elias earlier but left before the evening.

She denies returning.

She also initially claims she knows little about Elias's current investigation.

## Significance

Her nervous behavior makes her suspicious.

Later evidence reveals she secretly copied investigation files.

This is unrelated to the murder.

---

# 19. E014 — DANIEL'S INITIAL STATEMENT

## Category

Testimonial

## Importance

Level 4 — Critical

## Source

Daniel Mercer.

## Initial Statement

Daniel says:

> He argued with Elias and left before 10 PM.

He claims he went home afterward.

He denies returning to the apartment.

## Significance

This statement is the foundation of Daniel's false alibi.

The player's job is to prove that it is false.

---

# 20. E015 — VICTOR'S DINNER RECORD

## Category

Documentary

## Importance

Level 3 — Important

## Location

External investigation record.

## Discovery

Unlocked after questioning Victor.

## Player Description

> A reservation and payment record places Victor at a private dinner during the critical period.

## Significance

Strongly supports Victor's alibi.

It eliminates the most obvious suspect.

---

# 21. E016 — NORA'S DIGITAL ACTIVITY

## Category

Digital

## Importance

Level 3 — Important

## Location

Nora's digital records.

## Discovery

After questioning Nora about her evening.

## Player Description

> Activity on Nora's personal device places her at home during the relevant period.

## Significance

Supports Nora's innocence.

It also establishes that she was not physically present at the apartment.

---

# 22. E017 — MAYA'S TRANSPORTATION RECORD

## Category

Digital / Documentary

## Importance

Level 3 — Important

## Location

Investigation records.

## Discovery

After questioning Maya about when she left.

## Player Description

> A transportation record confirms Maya's departure from the area shortly after her argument with Elias.

## Significance

Supports Maya's timeline.

It eliminates her from the critical period.

---

# 23. E018 — DANIEL'S RETURN EVIDENCE

## Category

Physical / Documentary

## Importance

Level 4 — Critical

## Location

Investigation record derived from apartment access and physical evidence.

## Discovery

Requires the player to connect multiple earlier findings.

## Player Description

> Evidence indicates that Daniel was present at the apartment after the time he claimed to have left.

## Detailed Investigation

The evidence does not merely establish that Daniel had previously been inside.

It establishes later presence.

This is the critical breakthrough.

## Significance

Directly contradicts Daniel's statement.

## Related

- E006
- E014
- E019
- E020

---

# 24. E019 — FINANCIAL INVESTIGATION FILE

## Category

Documentary

## Importance

Level 4 — Critical

## Location

Hidden among Elias's investigation materials.

## Discovery

Requires sufficient investigation progress.

## Player Description

> The documents reveal a connection between Daniel's professional network and the financial irregularities Elias was investigating.

## Detailed Investigation

The documents establish that Daniel had a serious reason to prevent Elias from publishing the investigation.

## Significance

Establishes Daniel's motive.

## Related

- E004
- E005
- Daniel
- Final accusation

---

# 25. E020 — DANIEL'S FINAL CONTRADICTION

## Category

Testimonial / Deduction

## Importance

Level 4 — Critical

## Trigger

The player has:

- E002
- E006
- E018
- E019
- Daniel's statement E014

## Player Understanding

Daniel claimed:

> He left before 10 PM and never returned.

The player can establish:

1. Elias was alive at 10:42 PM.
2. Daniel had a reason to return.
3. Daniel's presence is established after his claimed departure.
4. Daniel's physical evidence is present.
5. Daniel's missing timeline corresponds to the critical period.

## Result

**Daniel's story is no longer credible.**

This is the final major contradiction.

---

# 26. EVIDENCE RELATIONSHIP MAP

```text id="p9q36e"
E001 PHONE
   ↓
E002 LAST CALL
   ↓
ELIAS ALIVE 10:42 PM
   ↓
TIMELINE

E004 DESK DOCUMENT
   ↓
E005 MISSING FILE
   ↓
E019 FINANCIAL FILE
   ↓
DANIEL'S MOTIVE

E006 FINGERPRINT
   ↓
DANIEL'S PRESENCE

E007 BROKEN GLASS
   ↓
E008 PAPERWEIGHT
   ↓
PHYSICAL CONFRONTATION

E014 DANIEL'S STATEMENT
   ↓
"LEFT BEFORE 10 PM"
   ↓
E018 RETURN EVIDENCE
   ↓
E020 CONTRADICTION
   ↓
DANIEL
```

---

# 27. SUSPECT ELIMINATION MAP

## MAYA

```text
Suspicion:
Argument with Elias

↓
E011 Statement

↓
E017 Transportation Record

↓
Maya cleared from critical period
```

---

## VICTOR

```text
Suspicion:
Strong motive

↓

E012 Statement

↓

E015 Dinner Record

↓

Victor cleared from critical period
```

---

## NORA

```text
Suspicion:
Hidden investigation files

↓

E013 Statement

↓

E016 Digital Activity

↓

Nora cleared from critical period
```

---

## DANIEL

```text
Suspicion:
Financial motive

↓

E014 Initial Statement

↓

E006 Fingerprint

↓

E018 Return Evidence

↓

E019 Financial File

↓

E020 Final Contradiction

↓

DANIEL CONFIRMED
```

---

# 28. EVIDENCE DISCOVERY ORDER

The player does not have to follow a perfectly linear sequence.

However, the intended discovery progression is:

```text
E001
↓
E002
↓
E004
↓
E007
↓
E008
↓
E009
↓
E010
↓
Suspect interviews
↓
E011
E012
E013
E014
↓
E015
E016
E017
↓
Return investigation
↓
E005
↓
E019
↓
E006
↓
E018
↓
E020
↓
ACCUSATION
```

The implementation may permit alternate orders where logical.

---

# 29. CRITICAL EVIDENCE CHAIN

The final solution must be supported by this chain:

```text id="l6fdte"
E002
LAST CALL
        ↓
Elias alive at 10:42 PM
        ↓
E014
Daniel says he left before 10 PM
        ↓
E018
Daniel was present later
        ↓
E019
Daniel had motive
        ↓
E006
Daniel's physical presence is supported
        ↓
E020
His story collapses
        ↓
DANIEL IS CULPRIT
```

---

# 30. EVIDENCE THAT SHOULD NOT DIRECTLY PROVE GUILT

The following must NOT individually identify Daniel:

### E006 — Fingerprint

Daniel admits being there earlier.

### E008 — Paperweight

Does not contain enough information by itself.

### E019 — Financial file

Provides motive, not proof of murder.

### E002 — Last call

Establishes time, not culprit.

The solution comes from **combining evidence**.

---

# 31. RED HERRING EVIDENCE

## E010 — Window

Suggests possible unauthorized entry.

It is ultimately irrelevant.

## Maya's argument

Strong emotional suspicion but innocent.

## Victor's motive

Strong motive but reliable alibi.

## Nora's stolen files

Suspicious behavior but unrelated to murder.

These red herrings are intentionally designed to demonstrate:

> **Suspicion is not proof.**

---

# 32. EVIDENCE PRESENTATION RULES

When presenting evidence to a suspect:

### Relevant evidence

Should produce a meaningful response.

### Strong contradiction

Should unlock a new reaction or dialogue.

### Irrelevant evidence

Should produce a believable dismissal.

The player should never permanently lose because they presented the wrong evidence.

---

# 33. EVIDENCE DISCOVERY RULES

Evidence must not:

- Randomly disappear.
- Change meaning.
- Be duplicated.
- Contradict another canonical clue.
- Require impossible knowledge.
- Require pixel-perfect tapping.

Once discovered, critical evidence remains available.

---

# 34. EVIDENCE INVENTORY DISPLAY

Each evidence card should show:

```text id="r0gk8b"
[ICON]

NAME
Category

Short description

Related:
Suspect / Timeline / Evidence
```

Selecting the card opens the full investigation result.

---

# 35. EVIDENCE STATUS

Each item can have:

```text id="7fntoe"
NOT DISCOVERED
DISCOVERED
INSPECTED
CONNECTED
```

The implementation may use these states internally.

---

# 36. CASE-SOLVING REQUIREMENT

The player should be able to reach the correct accusation without discovering every minor clue.

However, the game should require enough critical information to prevent blind guessing.

Recommended minimum:

- E002
- E014
- E018
- E019

At least these core facts must be available before the correct full accusation is accepted.

---

# 37. FINAL ACCUSATION REQUIREMENTS

For a perfect solve:

### Culprit

Daniel Mercer.

### Motive

Prevent exposure of financial misconduct.

### Method

Strike Elias with the heavy paperweight.

### Key Evidence

Evidence establishing Daniel's later presence and contradicting his alibi.

---

# 38. IMPORTANT IMPLEMENTATION RULE

The game must not allow the player to solve the case by selecting Daniel merely because he has the highest suspicion score.

There is no hidden "culprit probability."

The final solution is based on evidence relationships.

---

# 39. CANONICAL TRUTH

The complete truth is:

Daniel Mercer was connected to financial misconduct that Elias was investigating.

Daniel visited Elias earlier and argued with him.

Daniel initially left.

Elias continued working.

Elias later discovered information that increased the danger to Daniel.

Daniel returned.

The two confronted each other.

Daniel attacked Elias with the heavy paperweight.

Daniel panicked and removed an important financial document.

Daniel left and lied about returning.

The investigation reconstructs the missing portion of the timeline.

The contradiction exposes Daniel.

---

# 40. FINAL DESIGN PRINCIPLE

No single clue solves the mystery.

The player solves the mystery by **connecting clues**.

The intended experience is:

> "That clue seemed unimportant earlier."

followed by:

> "Wait... it proves he was there."

and finally:

> "Everything fits."

That feeling is the core of the evidence system in **The Last Call**.