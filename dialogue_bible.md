# THE LAST CALL
# DIALOGUE BIBLE

**Case ID:** CASE-001  
**Document Version:** 1.0  
**Status:** Canonical Dialogue Specification  
**Purpose:** Defines the dialogue, interrogation logic, evidence reactions, and conversational progression for Case 001.

---

# 1. PURPOSE

This document defines the actual dialogue content for **The Last Call**.

The implementation AI must use this document as the canonical source for:

- Suspect conversations
- Interview questions
- Answers
- Evidence presentation
- Contradictions
- Character reactions
- Unlock conditions
- Dialogue progression
- Investigation tone

The implementation AI must not invent major dialogue that changes the case facts.

Minor connective text may be generated only when it does not contradict this document.

---

# 2. DIALOGUE DESIGN PRINCIPLES

The dialogue system should feel like an investigation rather than a visual novel.

The player should:

1. Ask questions.
2. Listen to answers.
3. Notice inconsistencies.
4. Inspect evidence.
5. Present evidence.
6. Unlock new information.
7. Reconstruct the timeline.

The player should not simply click through dialogue until the game tells them who the murderer is.

---

# 3. DIALOGUE UI

Each conversation should display:

```text
SUSPECT NAME
Character portrait

Dialogue text

[Question]
[Question]
[Present Evidence]
[End Interview]
```

When evidence is presented:

```text
PRESENT EVIDENCE

Evidence Name
Short description

[Present]
[Back]
```

---

# 4. INTERVIEW STATES

Each suspect interview can have these states:

```text
NOT_STARTED
INITIAL_INTERVIEW
QUESTIONS_UNLOCKED
EVIDENCE_REACTION
CONTRADICTION_DISCOVERED
INTERVIEW_COMPLETED
```

The player may revisit suspects.

Previously discovered information should remain remembered.

---

# 5. GENERAL QUESTION RULES

Questions should not permanently disappear after being asked.

However, questions that have already been answered may appear visually marked as:

**ASKED**

New questions should unlock when:

- The player discovers relevant evidence.
- A contradiction is established.
- Another suspect provides relevant information.
- A new case objective becomes active.

---

# 6. INTRODUCTION — DETECTIVE BRIEFING

## Scene

Elias's apartment.

## Opening Narration

> "Elias Voss had spent years asking questions other people didn't want answered."

> "Tonight, someone made sure he wouldn't ask any more."

The player enters the apartment.

The investigator receives the initial objective:

**OBJECTIVE: Examine the apartment and establish what happened.**

---

# 7. INITIAL INVESTIGATION DIALOGUE

After discovering the phone:

> "His phone is still here."

After discovering the last call:

> "10:42 PM."

> "That puts him alive much later than expected."

After examining the apartment door:

> "No obvious forced entry."

After discovering the broken glass:

> "Something happened here."

The player receives:

**OBJECTIVE UPDATED: Identify everyone who had contact with Elias tonight.**

---

# 8. SUSPECT ORDER

The intended first interview order is:

1. Maya
2. Victor
3. Nora
4. Daniel

The player may later revisit them.

---

# 9. MAYA VOSS — INITIAL INTERVIEW

## Opening

**Detective:**

> "Maya Voss?"

**Maya:**

> "Yes."

**Detective:**

> "I'm investigating your brother's death."

**Maya:**

> "I know."

**Detective:**

> "I need you to tell me what happened tonight."

**Maya:**

> "We argued."

---

# 10. MAYA — QUESTION 01

### Question

**"What were you arguing about?"**

### Answer

> "Elias's investigation."

> "He wouldn't stop."

> "I told him he was putting himself in danger."

---

# 11. MAYA — QUESTION 02

### Question

**"Were you angry with him?"**

### Answer

> "Yes."

> "Of course I was."

> "That doesn't mean I wanted him dead."

---

# 12. MAYA — QUESTION 03

### Question

**"What time did you leave?"**

### Answer

> "A little after nine."

> "I don't remember the exact minute."

---

# 13. MAYA — QUESTION 04

### Question

**"Did you come back?"**

### Answer

> "No."

> "I went home."

---

# 14. MAYA — QUESTION 05

### Question

**"Did Elias have enemies?"**

### Answer

> "He had people who hated what he was doing."

> "Victor Hale, for one."

> "And Daniel Mercer."

---

# 15. MAYA — QUESTION 06

### Question

**"Why didn't you mention Daniel immediately?"**

### Answer

> "Because Daniel wasn't the problem I was arguing with Elias about."

> "I didn't think it mattered."

---

# 16. MAYA — QUESTION 07

### Question

**"Did you know what Elias was investigating?"**

### Answer

> "Not everything."

> "I knew it involved money."

> "I knew he thought someone was hiding something."

---

# 17. MAYA — QUESTION 08

### Question

**"Why did you want him to stop?"**

### Answer

> "Because I was scared."

> "He didn't know when to stop."

---

# 18. MAYA — PRESENT EVIDENCE: E017

## Evidence

**Maya's Transportation Record**

### Detective

> "This record places you away from the apartment shortly after you left."

### Maya

> "Yes."

> "That's what I told you."

### Result

Maya's timeline becomes more credible.

Unlock:

**MAYA CLEARED FROM CRITICAL PERIOD**

---

# 19. MAYA — PRESENT EVIDENCE: E019

## Evidence

**Financial Investigation File**

### Detective

> "Elias was investigating financial irregularities."

### Maya

> "I knew."

### Detective

> "You knew this could affect your work."

### Maya

> "I was afraid it would."

### Detective

> "So you had another reason to want him to stop."

### Maya

> "Yes."

### Maya

> "But I didn't kill him."

---

# 20. MAYA — END INTERVIEW

**Maya:**

> "If you find whoever did this..."

> "Don't let them turn Elias into the villain."

Interview ends.

---

# 21. VICTOR HALE — INITIAL INTERVIEW

## Opening

**Detective:**

> "Victor Hale?"

**Victor:**

> "That's me."

**Detective:**

> "You knew Elias Voss."

**Victor:**

> "Unfortunately."

---

# 22. VICTOR — QUESTION 01

### Question

**"What was your relationship with Elias?"**

### Answer

> "He was investigating my company."

> "He believed there was something illegal happening."

---

# 23. VICTOR — QUESTION 02

### Question

**"Was there something illegal happening?"**

### Answer

> "Not that I'm aware of."

---

# 24. VICTOR — QUESTION 03

### Question

**"Did you threaten Elias?"**

### Answer

> "I told him I would take legal action."

> "That's not a death threat."

---

# 25. VICTOR — QUESTION 04

### Question

**"Did you visit his apartment tonight?"**

### Answer

> "No."

---

# 26. VICTOR — QUESTION 05

### Question

**"Where were you?"**

### Answer

> "At a private dinner."

---

# 27. VICTOR — QUESTION 06

### Question

**"Who can confirm that?"**

### Answer

> "The reservation and payment records can."

---

# 28. VICTOR — QUESTION 07

### Question

**"Did you want Elias to stop investigating you?"**

### Answer

> "Obviously."

---

# 29. VICTOR — QUESTION 08

### Question

**"Would you have killed him to stop the investigation?"**

### Answer

> "No."

> "Elias was a nuisance."

> "Dead men don't stop investigations. Evidence does."

---

# 30. VICTOR — PRESENT EVIDENCE: E015

## Evidence

**Victor's Dinner Record**

### Detective

> "Your dinner record places you elsewhere during the critical period."

### Victor

> "As I said."

### Detective

> "You understand why I needed to verify it."

### Victor

> "Yes."

### Result

Victor's alibi is strengthened.

Unlock:

**VICTOR CLEARED FROM CRITICAL PERIOD**

---

# 31. VICTOR — PRESENT EVIDENCE: E019

### Detective

> "Elias had evidence concerning financial irregularities."

### Victor

> "He had been collecting material for months."

### Detective

> "Did he have evidence against you?"

### Victor

> "He had suspicions."

### Detective

> "That's not what I asked."

### Victor

> "Then ask your question properly."

---

# 32. VICTOR — OPTIONAL INFORMATION

If the player asks about Daniel:

### Detective

> "What can you tell me about Daniel Mercer?"

### Victor

> "Daniel knew how Elias worked."

> "He understood the investigation better than most people."

### Detective

> "Did Elias trust him?"

### Victor

> "He used to."

---

# 33. VICTOR — END INTERVIEW

**Victor:**

> "If you're looking for someone who had access to Elias's work..."

> "Start with the people he trusted."

Interview ends.

---

# 34. NORA BENNETT — INITIAL INTERVIEW

## Opening

**Detective:**

> "Nora Bennett?"

**Nora:**

> "Yes."

**Detective:**

> "You worked with Elias."

**Nora:**

> "I did."

---

# 35. NORA — QUESTION 01

### Question

**"What was your job?"**

### Answer

> "Research."

> "I organized documents, verified information, and kept track of sources."

---

# 36. NORA — QUESTION 02

### Question

**"When did you last see Elias?"**

### Answer

> "Earlier that day."

> "Before evening."

---

# 37. NORA — QUESTION 03

### Question

**"Did you return to the apartment?"**

### Answer

> "No."

---

# 38. NORA — QUESTION 04

### Question

**"What was Elias working on?"**

### Answer

> "Financial records."

> "Company transactions."

> "A lot of numbers."

---

# 39. NORA — QUESTION 05

### Question

**"Did you know what he had discovered?"**

### Answer

> "Not exactly."

This answer is deliberately incomplete.

---

# 40. NORA — QUESTION 06

### Question

**"Did you copy any of his files?"**

### Initial Answer

> "No."

This is a lie about a minor secret.

---

# 41. NORA — PRESENT EVIDENCE: E016

### Evidence

**Nora's Digital Activity**

### Detective

> "This places you at home during the relevant period."

### Nora

> "Yes."

### Detective

> "So you didn't return."

### Nora

> "No."

### Result

Nora's physical alibi is strengthened.

---

# 42. NORA — FOLLOW-UP

### Detective

> "But you weren't completely honest about the files."

### Nora

> "What do you mean?"

### Detective

> "You copied them."

### Nora

> "..."

### Nora

> "Yes."

---

# 43. NORA — FILE CONFESSION

### Detective

> "Why?"

### Nora

> "Because I was scared."

### Detective

> "Of Elias?"

### Nora

> "No."

> "Of what would happen if his investigation got bigger."

---

# 44. NORA — QUESTION

### Detective

> "Did Elias know you copied them?"

### Nora

> "No."

---

# 45. NORA — QUESTION

### Detective

> "Could those files have hurt someone?"

### Nora

> "They could have hurt a lot of people."

---

# 46. NORA — PRESENT EVIDENCE: E019

### Detective

> "This is what Elias was investigating."

### Nora

> "Yes."

### Detective

> "You knew this was serious."

### Nora

> "I knew."

### Detective

> "Did Daniel know?"

### Nora

> "Daniel knew more about the financial side than I did."

---

# 47. NORA — END INTERVIEW

**Nora:**

> "Elias trusted people too easily."

> "That's what made him good at finding things."

> "And that's what made him vulnerable."

Interview ends.

---

# 48. DANIEL MERCER — INITIAL INTERVIEW

## Opening

**Detective:**

> "Daniel Mercer?"

**Daniel:**

> "Yes."

**Detective:**

> "You knew Elias."

**Daniel:**

> "Professionally."

---

# 49. DANIEL — QUESTION 01

### Question

**"When did you last see Elias?"**

### Answer

> "Earlier that evening."

---

# 50. DANIEL — QUESTION 02

### Question

**"Why were you there?"**

### Answer

> "He wanted to discuss his investigation."

---

# 51. DANIEL — QUESTION 03

### Question

**"Did you argue?"**

### Answer

> "Yes."

---

# 52. DANIEL — QUESTION 04

### Question

**"What was the argument about?"**

### Answer

> "He thought I knew more about the financial records than I was telling him."

---

# 53. DANIEL — QUESTION 05

### Question

**"Did you?"**

### Answer

> "No."

---

# 54. DANIEL — QUESTION 06

### Question

**"When did you leave?"**

### Answer

> "Before 10 PM."

---

# 55. DANIEL — QUESTION 07

### Question

**"Did you return?"**

### Answer

> "No."

---

# 56. DANIEL — QUESTION 08

### Question

**"Did you contact Elias afterward?"**

### Answer

> "No."

---

# 57. DANIEL — QUESTION 09

### Question

**"Where did you go?"**

### Answer

> "Home."

---

# 58. DANIEL — QUESTION 10

### Question

**"Can anyone confirm that?"**

### Answer

> "I don't see why that's relevant."

---

# 59. DANIEL — PRESENT EVIDENCE: E006

## Evidence

**Desk Fingerprint**

### Detective

> "Your fingerprint was found on Elias's desk."

### Daniel

> "I was there earlier."

### Detective

> "So the fingerprint proves nothing?"

### Daniel

> "Correct."

### Result

No contradiction yet.

---

# 60. DANIEL — PRESENT EVIDENCE: E002

## Evidence

**Last Outgoing Call**

### Detective

> "Elias made a call at 10:42 PM."

### Daniel

> "Then he was alive at 10:42."

### Detective

> "Yes."

### Daniel

> "I don't see how that concerns me."

---

# 61. DANIEL — PRESENT EVIDENCE: E008

## Evidence

**Heavy Paperweight**

### Detective

> "Do you recognize this?"

### Daniel

> "It was on Elias's desk."

### Detective

> "You remember that?"

### Daniel

> "I had been there."

---

# 62. DANIEL — PRESENT EVIDENCE: E019

## Evidence

**Financial Investigation File**

### Detective

> "Elias had evidence connecting your professional network to financial irregularities."

### Daniel

> "That's an accusation."

### Detective

> "It's a document."

### Daniel

> "A document can be wrong."

---

# 63. DANIEL — PRESENT EVIDENCE: E018

## Evidence

**Return Evidence**

### Detective

> "This evidence places you at the apartment after the time you said you left."

### Daniel

> "That's not possible."

### Detective

> "Why?"

### Daniel

> "Because I wasn't there."

---

# 64. DANIEL — CONTRADICTION SEQUENCE

The player must connect the timeline.

### Detective

> "You said you left before 10."

### Daniel

> "Correct."

### Detective

> "Elias was alive at 10:42."

### Daniel

> "Apparently."

### Detective

> "And evidence places you at the apartment after your stated departure."

### Daniel

> "I told you. I wasn't there."

### Detective

> "Then explain why the evidence says otherwise."

### Daniel

> "..."

This is the moment Daniel's composure begins to fail.

---

# 65. DANIEL — FINAL QUESTIONS

## Question

**"Why did you return?"**

### Response

> "I didn't."

This is still a denial.

---

## Question

**"Why was the investigation material missing?"**

### Response

> "I don't know."

---

## Question

**"Why were you afraid of what Elias had found?"**

### Response

> "I wasn't."

---

# 66. FINAL CONTRADICTION

When the player has the required evidence:

- E002
- E018
- E019
- E006

The game unlocks:

**FINAL CONTRADICTION**

### Detective

> "You had a reason to stop Elias."

### Daniel

> "So did Victor."

### Detective

> "Victor was somewhere else."

### Daniel

> "You can't prove I was there."

### Detective

> "We already did."

Pause.

### Detective

> "You left once."

> "Then you came back."

Daniel says nothing.

---

# 67. DANIEL — FINAL REACTION

The game should not force an exaggerated confession.

Daniel's silence is more effective.

After a pause:

### Daniel

> "You don't know what Elias was going to publish."

### Detective

> "Then tell me."

### Daniel

> "It's too late."

Dialogue ends.

The final case reconstruction begins.

---

# 68. FINAL CASE RECONSTRUCTION

Narration:

> "Daniel had told the truth about one part of the evening."

> "He really had argued with Elias."

> "He really had left."

> "But he came back."

---

Narration:

> "By then, Elias had discovered enough to threaten Daniel's future."

---

Narration:

> "The second confrontation became violent."

---

Narration:

> "Daniel struck Elias with the paperweight."

---

Narration:

> "Then he removed the evidence that could expose him."

---

Narration:

> "And he lied about the missing part of the timeline."

---

# 69. FINAL CASE STATEMENT

The investigator concludes:

> "Daniel Mercer killed Elias Voss."

> "Not because he hated him."

> "Because Elias had found something Daniel couldn't afford to let him publish."

---

# 70. OPTIONAL FINAL DANIEL LINE

If the game includes one final character shot:

### Daniel

> "I only needed a little more time."

Then fade out.

This line is optional.

If omitted, the ending remains fully valid.

---

# 71. INTERVIEW REVISIT RULES

When returning to a suspect after new evidence:

The suspect should acknowledge previously discussed information.

Example:

### Maya revisit

> "You've already asked me about that."

### Victor revisit

> "What did you find?"

### Nora revisit

> "Is this about the files?"

### Daniel revisit

> "I already told you what happened."

---

# 72. UNLOCKED QUESTIONS

## Maya

Unlock E017-related questions after transportation evidence is found.

Unlock investigation questions after E019.

---

## Victor

Unlock dinner questions after discovering E015.

Unlock Daniel-related questions after establishing Daniel's connection to Elias.

---

## Nora

Unlock copied-file questions after discovering evidence related to the missing investigation material.

---

## Daniel

Unlock return-related questions only after E018.

Unlock motive questions after E019.

Unlock final contradiction after all required evidence is connected.

---

# 73. EVIDENCE PRESENTATION RULE

The player can present evidence in an arbitrary order.

However:

- A weak clue should not create a dramatic contradiction.
- A relevant clue should create a meaningful reaction.
- A critical clue should unlock new information.
- Evidence must never cause a suspect to reveal information they logically could not know.

---

# 74. WRONG EVIDENCE RESPONSE

If the player presents unrelated evidence:

### Example

Presenting Victor's dinner record to Nora.

Nora:

> "I don't understand what that has to do with me."

The player can return to the evidence list.

There is no penalty.

---

# 75. WRONG ACCUSATION DIALOGUE

If the player accuses Maya:

> "The evidence doesn't establish that Maya was present during the critical period."

If the player accuses Victor:

> "Victor had a motive, but his timeline does not fit."

If the player accuses Nora:

> "Nora hid information, but the evidence places her elsewhere."

If the player accuses Daniel without sufficient evidence:

> "Daniel may be suspicious, but suspicion is not enough."

---

# 76. PERFECT ACCUSATION DIALOGUE

If the player correctly identifies Daniel and provides the required reasoning:

### Detective

> "Daniel Mercer."

### System

**CULPRIT IDENTIFIED**

### Detective

> "The timeline doesn't lie."

---

# 77. DIALOGUE TONE

The overall tone should be:

- Serious
- Grounded
- Suspenseful
- Intelligent
- Conversational

Avoid:

- Excessive melodrama
- Cartoon villain dialogue
- Constant sarcasm
- Overly poetic speeches
- Long exposition dumps
- Obvious "I'm secretly evil" dialogue

---

# 78. DIALOGUE LENGTH

Most individual responses should be:

**1–3 sentences.**

Important revelations may use:

**3–5 sentences.**

The player should spend more time making deductions than reading giant blocks of text.

---

# 79. PLAYER AGENCY

The player should feel like they are conducting the interview.

Do not automatically ask every question.

The player chooses:

- What to ask
- When to present evidence
- Which suspect to revisit
- Which clues to connect

---

# 80. NO FAKE CHOICES

Do not include choices that are visually different but produce exactly the same result unless they are intentionally cosmetic.

Important questions should have meaningful consequences such as:

- Unlocking information
- Changing suspect reactions
- Revealing contradictions
- Adding evidence relationships

---

# 81. DIALOGUE STATE PERSISTENCE

The game must remember:

- Questions asked
- Evidence presented
- Secrets revealed
- Contradictions discovered
- Suspect clearance status
- Final accusation eligibility

Saving and loading must preserve these states.

---

# 82. FINAL DIALOGUE PRINCIPLE

The mystery should not be solved because a suspect says:

> "I did it."

The mystery should be solved because the player's evidence chain makes the suspect's story impossible.

The dialogue exists to give the player information.

The **deduction belongs to the player**.
