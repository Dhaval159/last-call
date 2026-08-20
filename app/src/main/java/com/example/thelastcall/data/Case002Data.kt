package com.example.thelastcall.data

object Case002Data : CaseDefinition {
    const val CASE_ID = "CASE-002"
    const val CASE_TITLE = "The Last Round"
    const val VICTIM_NAME = "Elena Voss"
    const val VICTIM_AGE = 42
    const val VICTIM_OCCUPATION = "Owner & Head Mixologist, Meridian"
    const val CASE_LOCATION = "Meridian (Floors 29–30, Aldwyn Hotel)"
    const val INCIDENT_TIME = "Approximately 11:28 PM - 11:32 PM"

    override val id: String = CASE_ID
    override val title: String = CASE_TITLE
    override val subtitle: String = "Homicide Investigation"
    override val incidentDate: String = "October 24"
    override val incidentTime: String = INCIDENT_TIME
    override val location: String = CASE_LOCATION
    override val victimName: String = VICTIM_NAME
    override val victimAge: Int = VICTIM_AGE
    override val victimOccupation: String = VICTIM_OCCUPATION
    override val briefingSummary: String get() = BRIEFING_SUMMARY
    override val primaryObjectiveText: String = "Determine the true circumstances of Elena Voss's death, uncover the staged crime scene, and identify the person responsible."
    override val initialDialogueText: String = "Detective, we have a body in the 29th-floor wine cellar of Meridian. On the surface it looks like a botched robbery for a rare vintage, but the scene doesn't add up."

    override val isPlaceholder: Boolean = false
    override val isAvailable: Boolean = true

    override val introFacts: List<IntroFact> = listOf(
        IntroFact("Victim: Elena Voss", "42-year-old owner of Meridian, found deceased in the Floor 29 wine cellar.", "person", 0xFFD32F2F),
        IntroFact("Discovery Time: 12:10 AM", "Head bartender Theo Marsh discovered the body near a broken shelf and a missing 1990 Bordeaux.", "call", 0xFFF59E0B),
        IntroFact("Staged Scene: Loading Dock", "Loading dock door was left ajar, pointing toward an external break-in, but forensic details raise immediate questions.", "lock", 0xFF06B6D4)
    )

    override val motives: List<CaseMotiveOption> = listOf(
        CaseMotiveOption("MOTIVE_HEARTBREAK", "Heartbreak & Desperation over Elena's refusal to stay and plan to disappear forever"),
        CaseMotiveOption("MOTIVE_FINANCIAL_DEAL", "Preventing deal collapse and securing an independent payout from the Titan acquisition"),
        CaseMotiveOption("MOTIVE_THEFT_EXPOSURE", "Concealing ongoing register skimming and theft to cover medical bills"),
        CaseMotiveOption("MOTIVE_PAST_REVENGE", "Retribution for the embezzlement scheme ten years ago that imprisoned Marcus Webb")
    )

    override val weapons: List<CaseWeaponOption> = listOf(
        CaseWeaponOption("WEAPON_STONE_PLANTER", "Fatal Blunt Force Head Trauma Against Rooftop Stone Planter Ledge"),
        CaseWeaponOption("WEAPON_WINE_BOTTLE", "Blunt Force Trauma via Heavy Wine Bottle in Cellar"),
        CaseWeaponOption("WEAPON_LIGATURE", "Physical Strangulation / Ligature in Cellar"),
        CaseWeaponOption("WEAPON_FALL_CELLAR", "Accidental Fall onto Concrete Floor from Broken Shelving")
    )

    override val initialUnlockedTimelineIds: Set<String> = setOf("T001", "T007", "T021")

    val BRIEFING_SUMMARY = """
        On the night of a private 'farewell tasting' event, Elena Voss, the 42-year-old owner and head mixologist of acclaimed rooftop cocktail bar Meridian, was found dead in the venue's wine cellar on Floor 29.
        
        The scene initially suggests a robbery gone wrong: a broken shelf, scattered bottles, a locked reserve case forced open with a valuable 1990 Bordeaux missing, and the Floor 29 loading dock door left ajar.
        
        However, the physical evidence in the wine cellar does not match how Elena died, and building security logs suggest deep contradictions among those present that night.
    """.trimIndent()

    val EVIDENCE_LIST = listOf(
        EvidenceItem(
            id = "EVD-01",
            name = "Body Position & Scene State",
            category = EvidenceCategory.PHYSICAL,
            location = "Floor 29 — Wine Cellar",
            discoveryCondition = "Found during initial wine cellar examination",
            playerDescription = "Elena's body is positioned on her side near a broken shelf. There are no visible defensive wounds consistent with a struggle in this room.",
            detailedInvestigation = "The body position appears staged after the fact. Dust on the concrete floor is undisturbed except immediately around where she lies.",
            significanceText = "The initial discovery scene. Posture and dust patterns suggest the body was moved here postmortem.",
            relatedSuspects = listOf("SUS-02", "SUS-04"),
            relatedTimelineEvents = listOf("T021"),
            relatedEvidence = listOf("EVD-02", "EVD-05"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "glass",
            unlocksEvidenceOnInspect = listOf("EVD-05", "EVD-11")
        ),
        EvidenceItem(
            id = "EVD-02",
            name = "Broken Shelf & Scattered Bottles",
            category = EvidenceCategory.PHYSICAL,
            location = "Floor 29 — Wine Cellar",
            discoveryCondition = "Examining the broken shelving near the body",
            playerDescription = "A wooden display shelf is broken with wine bottles shattered across the floor. The damage is localized directly below the shelf.",
            detailedInvestigation = "The break appears clumsily staged by hand rather than resulting from a violent human impact.",
            significanceText = "Supports the hypothesis that the struggle in the wine cellar was fabricated after the fact.",
            relatedEvidence = listOf("EVD-01", "EVD-03"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "glass"
        ),
        EvidenceItem(
            id = "EVD-03",
            name = "Missing 1990 Bordeaux",
            category = EvidenceCategory.PHYSICAL,
            location = "Floor 29 — Wine Cellar",
            discoveryCondition = "Inspecting the locked reserve wine case",
            playerDescription = "A locked reserve wine case has been forced open. A single bottle of 1990 Chateau Margaux Bordeaux is missing.",
            detailedInvestigation = "The missing bottle creates the appearance of a high-value theft motive, but adjacent bottles of equal value were left untouched.",
            significanceText = "Deliberate misdirection intended to point investigators toward an external burglar.",
            relatedEvidence = listOf("EVD-01", "EVD-02", "EVD-04"),
            importance = EvidenceImportance.IMPORTANT,
            iconType = "vault"
        ),
        EvidenceItem(
            id = "EVD-04",
            name = "Dock Door Ajar",
            category = EvidenceCategory.ENVIRONMENTAL,
            location = "Floor 29 — Loading Dock",
            discoveryCondition = "Examining the Floor 29 loading dock exit",
            playerDescription = "The heavy exterior service door leading to the loading dock was found unlocked and slightly ajar.",
            detailedInvestigation = "Staff routinely prop this door open for late deliveries; the latch was not forced despite initial appearances.",
            significanceText = "A red herring suggesting external intruder entry; staff were aware it was routinely unalarmed.",
            relatedSuspects = listOf("SUS-04", "SUS-02"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "door"
        ),
        EvidenceItem(
            id = "EVD-05",
            name = "Coroner: COD & Lividity Mismatch",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Medical Examiner's Office",
            discoveryCondition = "Autopsy report received during Phase 3 breakthrough",
            playerDescription = "Elena died of blunt force cranial trauma. However, fixed posterior lividity proves she lay flat on her back for 20-30 minutes postmortem before being turned onto her side in the cellar.",
            detailedInvestigation = "Conclusively proves Elena died elsewhere, remained on her back, and was transported into the wine cellar postmortem.",
            significanceText = "The central forensic breakthrough proving the wine cellar is a staged crime scene.",
            relatedEvidence = listOf("EVD-01", "EVD-06", "EVD-08"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "document"
        ),
        EvidenceItem(
            id = "EVD-06",
            name = "Stone Residue in Wound",
            category = EvidenceCategory.PHYSICAL,
            location = "Forensic Lab",
            discoveryCondition = "Forensic lab microscopic analysis of the fatal head wound",
            playerDescription = "Microscopic analysis of the occipital laceration reveals crystalline limestone dust and exterior sealant, matching architectural stone planters.",
            detailedInvestigation = "The residue is completely incompatible with the wine cellar's smooth poured concrete floor. The impact occurred against outdoor stone masonry.",
            significanceText = "Physically links Elena's fatal head trauma to the rooftop stone planter on Floor 30.",
            relatedEvidence = listOf("EVD-05", "EVD-07", "EVD-08"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "weapon"
        ),
        EvidenceItem(
            id = "EVD-07",
            name = "Earring Stud in Roof Decking",
            category = EvidenceCategory.PHYSICAL,
            location = "Floor 30 — Rooftop Deck",
            discoveryCondition = "Examining the wood decking on the Floor 30 rooftop terrace",
            playerDescription = "A single silver and pearl earring stud is wedged tightly between the wooden planks near the stone planter. It matches the earring in Elena's left ear.",
            detailedInvestigation = "Places Elena directly at the rooftop planter during a sudden struggle or fall that dislodged the earring.",
            significanceText = "Direct physical evidence placing Elena on the rooftop deck at the moment of trauma.",
            relatedEvidence = listOf("EVD-01", "EVD-06", "EVD-08"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "physical"
        ),
        EvidenceItem(
            id = "EVD-08",
            name = "Cleaned Bloodstain (Luminol)",
            category = EvidenceCategory.PHYSICAL,
            location = "Floor 30 — Rooftop Deck",
            discoveryCondition = "Applying luminol to the stone planter ledge on Floor 30",
            playerDescription = "Luminol reveals a cleaned bloodstain on the lower corner of the stone planter ledge. DNA matches Elena Voss.",
            detailedInvestigation = "Confirms the rooftop terrace as the true scene of the fatal head impact. Someone wiped the ledge down after the fall.",
            significanceText = "Definitively establishes the rooftop as the true scene of death and proves an intentional cleanup.",
            relatedEvidence = listOf("EVD-05", "EVD-06", "EVD-07", "EVD-22"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "weapon",
            unlocksEvidenceOnInspect = listOf("EVD-06", "EVD-07")
        ),
        EvidenceItem(
            id = "EVD-09",
            name = "Nightcap w/ R. Calendar Entry",
            category = EvidenceCategory.DIGITAL,
            location = "Victim's Phone",
            discoveryCondition = "Inspecting Elena's smartphone calendar",
            playerDescription = "A calendar entry set for 11:30 PM: 'Last Round - Nightcap w/ R. on roof'.",
            detailedInvestigation = "Indicates Elena had a pre-arranged private meeting on the rooftop deck with someone whose name begins with 'R'.",
            significanceText = "Points to a late-night private meeting with Renata Cole during the critical window.",
            relatedSuspects = listOf("SUS-04"),
            relatedTimelineEvents = listOf("T018"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "phone",
            unlocksEvidenceOnInspect = listOf("EVD-10")
        ),
        EvidenceItem(
            id = "EVD-10",
            name = "11:27 PM Voice Memo",
            category = EvidenceCategory.DIGITAL,
            location = "Victim's Phone",
            discoveryCondition = "Recovering unsent audio recordings on Elena's phone",
            playerDescription = "A 32-second voice memo recorded at 11:27 PM. Elena speaks calmly alone on the roof: 'I'm telling Renata tonight. It's time to stop running. Marcus's daughter deserved the truth, and so does she.'",
            detailedInvestigation = "Proves Elena was alive, alone, and completely at peace at 11:27 PM after Priya's departure.",
            significanceText = "The master time anchor definitively clearing Priya and Iris and anchoring the fatal encounter between 11:28 PM and 11:32 PM.",
            relatedSuspects = listOf("SUS-03", "SUS-05", "SUS-04"),
            relatedTimelineEvents = listOf("T016", "T017", "T018"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "call"
        ),
        EvidenceItem(
            id = "EVD-11",
            name = "Napkin w/ Priya's Contact Info",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Victim's Jacket",
            discoveryCondition = "Searching Elena's jacket pocket",
            playerDescription = "A cocktail napkin with handwritten text: 'We need to talk about Marcus. Priya - 555-0192'.",
            detailedInvestigation = "Confirms Priya had a private conversation with Elena regarding Marcus Webb earlier in the evening.",
            significanceText = "Connects Priya to Elena's past identity and prompts deeper interrogation.",
            relatedSuspects = listOf("SUS-03"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "document"
        ),
        EvidenceItem(
            id = "EVD-12",
            name = "Old Records: Elena Kowalski",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Victim's Office / Safe",
            discoveryCondition = "Examining old corporate archives and public records",
            playerDescription = "Ten-year-old court records and news clippings showing 'Elena Kowalski' orchestrated an embezzlement scheme for which Marcus Webb took the full legal fall.",
            detailedInvestigation = "Elena Voss is Elena Kowalski. Marcus Webb served years in prison for her crime while she fled and rebuilt her life.",
            significanceText = "Reveals Priya's revenge motive as Marcus Webb's daughter and explains Elena's guilt.",
            relatedSuspects = listOf("SUS-03"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "folder"
        ),
        EvidenceItem(
            id = "EVD-13",
            name = "David's Badge Re-entry, 11:05 PM",
            category = EvidenceCategory.DIGITAL,
            location = "Main Building Security Logs",
            discoveryCondition = "Reviewing Floor 30 elevator badge access logs",
            playerDescription = "Electronic badge swipe records show David Ostrow used his keycard to re-enter Floor 30 at 11:05 PM.",
            detailedInvestigation = "Directly shatters David's claim that he left the building at 10:45 PM and never returned.",
            significanceText = "Catches David Ostrow in a direct contradiction regarding his departure time.",
            relatedSuspects = listOf("SUS-01"),
            relatedTimelineEvents = listOf("T009"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "access_card"
        ),
        EvidenceItem(
            id = "EVD-14",
            name = "Busboy Witness Statement",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Floor 30 Service Corridor",
            discoveryCondition = "Interviewing floor staff and busboy",
            playerDescription = "A busboy heard a shouting match on the rooftop deck between a man and a woman between 11:05 PM and 11:12 PM, with the man yelling 'You can't kill this deal!'.",
            detailedInvestigation = "Corroborates David's presence on the roof arguing with Elena about the Titan acquisition.",
            significanceText = "Places David in an angry confrontation with Elena on the roof until 11:12 PM.",
            relatedSuspects = listOf("SUS-01"),
            relatedTimelineEvents = listOf("T009"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "statement"
        ),
        EvidenceItem(
            id = "EVD-15",
            name = "Rideshare Pickup, 11:19 PM",
            category = EvidenceCategory.DIGITAL,
            location = "Rideshare Dispatch Records",
            discoveryCondition = "Verifying rideshare GPS records from the hotel entrance",
            playerDescription = "GPS dispatch confirms David Ostrow entered a rideshare vehicle outside the hotel at 11:19 PM and arrived home across town at 11:48 PM.",
            detailedInvestigation = "Conclusively proves David departed the Aldwyn Hotel before the estimated time of death (11:28–11:32 PM).",
            significanceText = "Conclusively clears David Ostrow of committing the murder.",
            relatedSuspects = listOf("SUS-01"),
            relatedTimelineEvents = listOf("T011"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "transit"
        ),
        EvidenceItem(
            id = "EVD-16",
            name = "David's 11:14 PM Text to Whit",
            category = EvidenceCategory.DIGITAL,
            location = "Phone Records",
            discoveryCondition = "Extracting message history between David and Whit",
            playerDescription = "Text from David to Whit at 11:14 PM: 'She knows about our arrangement. The deal is dead. I'm leaving the hotel now.'",
            detailedInvestigation = "Confirms David gave up and was leaving the building, demonstrating he did not commit violence.",
            significanceText = "Corroborates David's departure and exposes his secret side deal with Whit.",
            relatedSuspects = listOf("SUS-01", "SUS-06"),
            relatedTimelineEvents = listOf("T010"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "message"
        ),
        EvidenceItem(
            id = "EVD-17",
            name = "Hotel Key-Card Log, 10:47 PM",
            category = EvidenceCategory.DIGITAL,
            location = "Hotel System",
            discoveryCondition = "Analyzing Whit Sokol's hotel room door sensor data",
            playerDescription = "The hotel system shows Whit's keycard tapped his room door at 10:47 PM, but front desk logs show this was a key reactivation tap. Hallway cameras place him heading back toward Floor 30 at 11:18 PM.",
            detailedInvestigation = "Whit's claim that he was asleep by eleven is false; he returned to Floor 30 at 11:18 PM after receiving David's text, but retreated without going beyond the bar.",
            significanceText = "Dismantles Whit's alibi while confirming he never reached the rooftop deck.",
            relatedSuspects = listOf("SUS-06"),
            relatedTimelineEvents = listOf("T008", "T012"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "keycard"
        ),
        EvidenceItem(
            id = "EVD-18",
            name = "POS Scan Gap, 11:12–11:18 PM",
            category = EvidenceCategory.DIGITAL,
            location = "Kitchen Inventory Terminal",
            discoveryCondition = "Examining bar-code scan timestamps on the kitchen POS",
            playerDescription = "Barcode scanner logs show continuous bottle scanning by Theo from 11:00 PM to 11:45 PM, except for an unlogged 6-minute gap between 11:12 PM and 11:18 PM.",
            detailedInvestigation = "Theo stopped scanning inventory during this window, contradicting his claim of uninterrupted work.",
            significanceText = "Implicates Theo with an unexplained time gap during the closing hour.",
            relatedSuspects = listOf("SUS-02"),
            relatedTimelineEvents = listOf("T013"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "digital"
        ),
        EvidenceItem(
            id = "EVD-19",
            name = "Cooler Maintenance Log",
            category = EvidenceCategory.DIGITAL,
            location = "Kitchen Environmental System",
            discoveryCondition = "Downloading walk-in cooler sensor diagnostics",
            playerDescription = "Automated refrigeration log records a compressor pressure alert at 11:12 PM and a manual temperature reset inside the walk-in cooler at 11:17 PM.",
            detailedInvestigation = "Independently proves Theo was inside the kitchen walk-in cooler resolving the compressor alarm during his 6-minute scan gap.",
            significanceText = "Conclusively clears Theo Marsh of leaving the kitchen during the 11:12-11:18 PM window.",
            relatedSuspects = listOf("SUS-02"),
            relatedTimelineEvents = listOf("T013"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "digital"
        ),
        EvidenceItem(
            id = "EVD-20",
            name = "Register Shortfall Report",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Manager's Office Ledger",
            discoveryCondition = "Reviewing weekly cash register audit sheets",
            playerDescription = "Audit records show recurring weekly register discrepancies of approximately $400 over the past two months.",
            detailedInvestigation = "Elena had noticed the shortfall and confronted Theo at 10:50 PM. Theo was skimming cash for his mother's medical treatments.",
            significanceText = "Explains Theo's panic and lie about not speaking to Elena, proving the dispute was financial, not homicidal.",
            relatedSuspects = listOf("SUS-02"),
            importance = EvidenceImportance.RELEVANT,
            discoverableInitially = false,
            iconType = "receipt"
        ),
        EvidenceItem(
            id = "EVD-21",
            name = "Main Security Log (Roof, post-10:20)",
            category = EvidenceCategory.DIGITAL,
            location = "Security Console, Floor 28",
            discoveryCondition = "Exporting main building CCTV and door access logs",
            playerDescription = "The master security log indicates Renata Cole conducted rounds until 10:20 PM and logged zero rooftop access entries for the remainder of the night.",
            detailedInvestigation = "Renata edited this master database using administrative credentials to engineer a false clearance for herself.",
            significanceText = "The fabricated electronic alibi that seems to clear Renata on the surface.",
            relatedSuspects = listOf("SUS-04"),
            relatedTimelineEvents = listOf("T005"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "access_card",
            unlocksEvidenceOnInspect = listOf("EVD-13", "EVD-15", "EVD-17")
        ),
        EvidenceItem(
            id = "EVD-22",
            name = "Service Stairwell Keypad Log, 11:26 & 11:52 PM",
            category = EvidenceCategory.DIGITAL,
            location = "Interior Service Stairwell Subsystem",
            discoveryCondition = "Extracting legacy standalone keypad logs from Floor 29 service stairwell door",
            playerDescription = "Standalone subsystem logs record PIN code '4491' (assigned exclusively to Renata Cole) entered at the Floor 30 stairwell door at 11:26 PM and the Floor 29 wine cellar door at 11:52 PM.",
            detailedInvestigation = "The interior service stairwell has no cameras and runs on an independent legacy keypad controller that Renata forgot to scrub. Places Renata on Floor 30 at 11:26 PM and Floor 29 at 11:52 PM.",
            significanceText = "The decisive contradiction shattering Renata's entire alibi and proving she moved the body.",
            relatedSuspects = listOf("SUS-04"),
            relatedTimelineEvents = listOf("T017", "T020"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "access_card"
        ),
        EvidenceItem(
            id = "EVD-23",
            name = "Renata's Personal Ledger",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Renata's Office Safe, Floor 28",
            discoveryCondition = "Searching locked desk drawer in security office",
            playerDescription = "A private notebook detailing unauthorized after-hours private venue rentals and explicit notes on how to overwrite camera timestamps on the main security server.",
            detailedInvestigation = "Demonstrates Renata possessed both the motive to maintain secrets and the technical skill to edit the main security logs.",
            significanceText = "Explains how and why Renata was able to manipulate the building's main access logs.",
            relatedSuspects = listOf("SUS-04"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "vault"
        ),
        EvidenceItem(
            id = "EVD-24",
            name = "Behavioral Tell re: Planter",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Renata Cole Interrogation",
            discoveryCondition = "Observing Renata's reaction when presented with rooftop stone planter forensics",
            playerDescription = "Renata, who remained clinical and composed through every logistical question, turns pale, stammers, and grips the table when the rooftop stone planter is described.",
            detailedInvestigation = "A targeted psychological tell showing specific, intimate distress regarding the exact physical mechanism of Elena's fatal head trauma.",
            significanceText = "Emotional confirmation linking Renata directly to the rooftop planter accident.",
            relatedSuspects = listOf("SUS-04"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "statement"
        ),
        EvidenceItem(
            id = "EVD-25",
            name = "Iris & Theo Texts",
            category = EvidenceCategory.DIGITAL,
            location = "Iris and Theo's Phones",
            discoveryCondition = "Recovering deleted chat thread between Iris and Theo",
            playerDescription = "Chat messages confirming Iris Chen and Theo Marsh are secretly in a relationship and discussing their worries about Elena's planned sale of Meridian.",
            detailedInvestigation = "Explains Iris's presence in the back of house and reveals she is Elena's younger half-sister.",
            significanceText = "Clears Iris and Theo of conspiracy against Elena while revealing Iris's family ties.",
            relatedSuspects = listOf("SUS-05", "SUS-02"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "message"
        ),
        EvidenceItem(
            id = "EVD-26",
            name = "Dock Camera, Priya's Exit 11:27 PM",
            category = EvidenceCategory.DIGITAL,
            location = "Loading Dock Exterior Camera",
            discoveryCondition = "Reviewing external loading dock footage",
            playerDescription = "Surveillance footage captures Priya Nandan exiting through the Floor 29 loading dock door onto the street at precisely 11:27 PM.",
            detailedInvestigation = "Corroborates Elena's 11:27 PM voice memo, proving Priya departed before the fatal encounter occurred on the roof.",
            significanceText = "Definitively confirms Priya was off the premises during the time of Elena's death.",
            relatedSuspects = listOf("SUS-03"),
            relatedTimelineEvents = listOf("T016"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "surveillance"
        )
    )

    val SUSPECTS = listOf(
        Suspect(
            id = "SUS-01",
            name = "David Ostrow",
            age = 58,
            occupation = "Silent Business Partner / Investor",
            relationship = "Elena's Business Partner (8 years)",
            publicStory = "Believed to be Elena's closest professional ally and friend. Claims he left the event at 10:45 PM and went straight home.",
            hiddenTruth = "Has serious gambling debts and negotiated a secret side arrangement with Whit Sokol. Confronted Elena on the roof at 11:05 PM; left angry and took a rideshare at 11:19 PM.",
            personalityDescription = "Warm and charismatic in public, controlling in private, cornered and irritable under financial pressure.",
            initialAlibiSummary = "Claims he left the building at 10:45 PM and went straight home.",
            avatarColorHex = 0xFF5C6BC0,
            initials = "DO"
        ),
        Suspect(
            id = "SUS-02",
            name = "Theo Marsh",
            age = 29,
            occupation = "Head Bartender",
            relationship = "Elena's Protégé & Successor",
            publicStory = "Hardworking protégé. Claims he was doing inventory in the kitchen continuously from 11:00 PM to 11:45 PM, finding the body in the cellar at 12:10 AM.",
            hiddenTruth = "Was skimming $400/week from the register for mother's medical bills. Confronted by Elena at 10:50 PM. Was in walk-in cooler dealing with compressor alarm during the 11:12-11:18 PM scan gap.",
            personalityDescription = "Intense, meticulous, quietly ambitious, over-explains when nervous.",
            initialAlibiSummary = "Claims he was doing inventory in the kitchen continuously from 11:00 to 11:45 PM.",
            avatarColorHex = 0xFF78909C,
            initials = "TM"
        ),
        Suspect(
            id = "SUS-03",
            name = "Priya Nandan",
            age = 35,
            occupation = "Hospitality Journalist",
            relationship = "Meridian Regular & Investigative Reporter",
            publicStory = "Longtime regular and respected journalist. Claims she said goodbye at 10:30 PM and went straight home.",
            hiddenTruth = "Daughter of Marcus Webb, the man who served prison time for Elena's past embezzlement scheme. Re-entered at 11:15 PM, confronted Elena on the roof at 11:20 PM, received an apology and promise to clear her father, and left peacefully at 11:27 PM.",
            personalityDescription = "Sharp, controlled, observant, excellent at extracting information.",
            initialAlibiSummary = "Claims she left the tasting event at 10:30 PM and went home.",
            avatarColorHex = 0xFF8D6E63,
            initials = "PN"
        ),
        Suspect(
            id = "SUS-04",
            name = "Renata Cole",
            age = 46,
            occupation = "Building & Security Manager, Aldwyn Hotel",
            relationship = "Former Romantic Partner & Quiet Protector",
            publicStory = "Professional and reliable manager. Claims she did building rounds until 10:20 PM, then remained in her 28th-floor office the rest of the night.",
            hiddenTruth = "Runs an unauthorized after-hours side business by editing security logs. Met Elena on the roof at 11:28 PM for their planned 'last round'. When Elena revealed she intended to disappear and leave her behind, Renata grabbed her arm; Elena broke free, fell, struck her head on the stone planter, and died. Renata staged the wine cellar robbery to cover it up, unaware the service stairwell keypad kept separate logs.",
            personalityDescription = "Guarded, precise, logistical, deeply devoted, uncomfortable with emotional vulnerability.",
            initialAlibiSummary = "Claims she did building rounds until 10:20 PM, then stayed in her 28th-floor office all night.",
            avatarColorHex = 0xFFD32F2F,
            initials = "RC"
        ),
        Suspect(
            id = "SUS-05",
            name = "Iris Chen",
            age = 27,
            occupation = "Server & Bar-Back",
            relationship = "Elena's Secret Younger Half-Sister",
            publicStory = "Capable, quiet new hire. Claims she was doing closing cleanup in the kitchen from 11:00 PM to 11:45 PM and never left.",
            hiddenTruth = "Elena's half-sister and secretly dating Theo. Went to the roof at 11:20 PM to confront Elena about her plans to sell and abandon her again. Left after a brief heated exchange before Priya arrived.",
            personalityDescription = "Watchful, reserved, quick to deflect, loyal to those she trusts.",
            initialAlibiSummary = "Claims she was doing closing cleanup in the kitchen from 11:00 to 11:45 PM and never left.",
            avatarColorHex = 0xFF4DB6AC,
            initials = "IC"
        ),
        Suspect(
            id = "SUS-06",
            name = "Whit Sokol",
            age = 51,
            occupation = "Acquisitions Lead, Titan Hospitality",
            relationship = "Prospective Buyer & Business Counterpart",
            publicStory = "Smooth corporate dealmaker introduced only as an old friend. Claims he went to his hotel room at 10:47 PM and was asleep by eleven.",
            hiddenTruth = "Negotiated a secret side deal with David Ostrow. Came back down to Floor 30 at 11:18 PM after David texted that the deal was dead, saw Priya near the stairs, and retreated to the bar without going up. The acquisition contract required Elena alive, so her death ruins his deal.",
            personalityDescription = "Smooth, transactional, guarded, impatient with non-executives.",
            initialAlibiSummary = "Claims he went to his hotel room at 10:47 PM and was asleep by eleven.",
            avatarColorHex = 0xFF9E9D24,
            initials = "WS"
        )
    )

    val QUESTIONS = listOf(
        // SUS-01 David Ostrow
        InterviewQuestion("Q_DO_1", "SUS-01", "What was your schedule during the farewell tasting?", "I enjoyed the tasting, had a few drinks with guests, and left the building around 10:45 PM before things got quiet. I went straight home.", recordedStatementId = "ST_DO_1", statementSummary = "David claims he left at 10:45 PM and went straight home.", unlocksEvidenceIds = listOf("EVD-13")),
        InterviewQuestion("Q_DO_2", "SUS-01", "What was your business relationship with Elena?", "We were partners for eight years. I was the financial backer who believed in her vision when nobody else did. We were close friends.", statementSummary = "David describes his close professional relationship with Elena."),
        InterviewQuestion("Q_DO_3", "SUS-01", "Security logs show you swiped back in at 11:05 PM and argued on the roof. Explain that.", "Alright, fine! I went back up to Floor 30. Elena told me she was killing the Titan sale. I was furious—I just wanted her to reconsider! We argued, but I walked away and left in a cab!", recordedStatementId = "ST_DO_2", requiredEvidenceId = "EVD-13", statementSummary = "David admits returning to argue with Elena about the sale, but insists he left in a cab.", unlocksEvidenceIds = listOf("EVD-14", "EVD-15")),
        InterviewQuestion("Q_DO_4", "SUS-01", "Did you have a secret side arrangement with Whit Sokol?", "I... I have debts, Detective. Heavy gambling debts. Whit promised me a guaranteed payout regardless of Elena's decision. But I swear on my life, when Elena told me the deal was dead, I texted Whit at 11:14 PM and left!", requiredEvidenceId = "EVD-16", statementSummary = "David confesses his secret side deal with Whit and provides his 11:14 PM text.", unlocksEvidenceIds = listOf("EVD-16")),
        InterviewQuestion("Q_DO_5", "SUS-01", "What were Elena's exact words during your rooftop argument?", "She said: 'I know what you and Whit did, David. I'm not letting either of you use me to run again. The sale is off, and I'm settling my debts tonight.' Then she told me to get out.", requiredEvidenceId = "EVD-15", statementSummary = "David recounts Elena's resolved words before his departure."),

        // SUS-02 Theo Marsh
        InterviewQuestion("Q_TM_1", "SUS-02", "What were your duties and movements between 11:00 PM and midnight?", "I was in the kitchen doing bottle inventory from 11:00 PM until 11:45 PM continuously. I didn't leave the kitchen until I went to the cellar at 12:10 AM for the reserve Bordeaux and found Elena.", recordedStatementId = "ST_TM_1", statementSummary = "Theo claims continuous kitchen inventory from 11:00 to 11:45 PM.", unlocksEvidenceIds = listOf("EVD-18")),
        InterviewQuestion("Q_TM_2", "SUS-02", "When did you last speak with Elena Voss?", "I barely talked to her today. Not after 10:45 PM when the main floor cleared out.", statementSummary = "Theo denies speaking to Elena after 10:45 PM."),
        InterviewQuestion("Q_TM_3", "SUS-02", "POS terminal logs show a 6-minute scan gap between 11:12 PM and 11:18 PM. What happened?", "Look, I... I did see Elena briefly around 10:50 PM. She noticed a discrepancy in the cash register and snapped at me. I was shaken up, so during inventory I had to step away to deal with a cooler alarm.", recordedStatementId = "ST_TM_2", requiredEvidenceId = "EVD-18", statementSummary = "Theo admits an earlier register argument and explains the scan gap was a cooler alarm.", unlocksEvidenceIds = listOf("EVD-19", "EVD-20")),
        InterviewQuestion("Q_TM_4", "SUS-02", "Register reports show $400 weekly shortfalls. Were you skimming cash?", "Yes... God, yes, I took the money! My mother's medical bills were drowning me! Titan promised me a General Manager job if the sale went through. I wanted the sale to happen! I needed Elena alive!", requiredEvidenceId = "EVD-20", statementSummary = "Theo admits skimming money for medical bills and explains he needed the sale to succeed."),
        InterviewQuestion("Q_TM_5", "SUS-02", "Why were you recording audio snippets around the bar?", "I was terrified of being blindsided by new management or fired for the shortfall. I kept notes to protect myself. I swear I was fixing the walk-in compressor during those six minutes!", requiredEvidenceId = "EVD-19", statementSummary = "Theo explains his audio recordings were self-protective."),

        // SUS-03 Priya Nandan
        InterviewQuestion("Q_PN_1", "SUS-03", "What was your schedule after the tasting began?", "I stayed for the toasts, chatted with Elena and the regulars, said my goodbyes at 10:30 PM, and took a cab home. I haven't set foot in that building since.", recordedStatementId = "ST_PN_1", statementSummary = "Priya claims she left at 10:30 PM and never returned.", unlocksEvidenceIds = listOf("EVD-11")),
        InterviewQuestion("Q_PN_2", "SUS-03", "What was the subject of your investigation into Meridian?", "Just a standard piece on independent craft cocktail venues facing corporate buyout pressures from groups like Titan Hospitality.", statementSummary = "Priya claims she was writing a general industry article."),
        InterviewQuestion("Q_PN_3", "SUS-03", "We found a napkin in Elena's jacket asking to talk about 'Marcus'. Explain that.", "I... I spoke to Elena privately near the wine wall at 9:10 PM. I let her know that I knew who she really was.", requiredEvidenceId = "EVD-11", statementSummary = "Priya admits confronting Elena earlier about Marcus, but denies returning later.", unlocksEvidenceIds = listOf("EVD-12")),
        InterviewQuestion("Q_PN_4", "SUS-03", "You are Marcus Webb's daughter. Did you return to the building to seek revenge?", "My father went to prison for ten years for her crime! Elena Kowalski stole his life and built this bar! Yes, I came back at 11:15 PM and went to the roof. But I didn't kill her!", recordedStatementId = "ST_PN_2", requiredEvidenceId = "EVD-12", statementSummary = "Priya admits returning at 11:15 PM to confront Elena on the roof as Marcus Webb's daughter.", unlocksEvidenceIds = listOf("EVD-10", "EVD-26")),
        InterviewQuestion("Q_PN_5", "SUS-03", "What happened during your rooftop confrontation with Elena at 11:20 PM?", "She didn't fight me. She started crying. She apologized for everything, told me she had killed the Titan deal, and promised she would publicly clear my father's name tomorrow. I was stunned. I left her alone on the roof at 11:26 PM and walked out the dock door.", requiredEvidenceId = "EVD-10", statementSummary = "Priya recounts Elena's sincere apology and promise to clear Marcus Webb."),

        // SUS-04 Renata Cole
        InterviewQuestion("Q_RC_1", "SUS-04", "What were your duties and location as security manager tonight?", "I oversaw building logistics, did security rounds across Floors 28 to 30 until 10:20 PM, and then stayed in my 28th-floor office monitoring the console all night.", recordedStatementId = "ST_RC_1", statementSummary = "Renata claims rounds until 10:20 PM and office all night.", unlocksEvidenceIds = listOf("EVD-09", "EVD-21")),
        InterviewQuestion("Q_RC_2", "SUS-04", "Did you access the rooftop deck after locking it for the night?", "I haven't been up to the roof since I locked the public doors for the night at 10:20 PM. The master security system confirms that.", recordedStatementId = "ST_RC_2", statementSummary = "Renata insists she never accessed the roof after 10:20 PM."),
        InterviewQuestion("Q_RC_3", "SUS-04", "Elena's calendar has a 11:30 PM entry: 'Nightcap w/ R. on roof'. Was that you?", "Elena and I had talked about having a final drink together after the tasting, yes. But she canceled, so I never went up.", requiredEvidenceId = "EVD-09", statementSummary = "Renata admits the planned nightcap but claims Elena canceled.", unlocksEvidenceIds = listOf("EVD-23")),
        InterviewQuestion("Q_RC_4", "SUS-04", "Your office safe contains a ledger detailing unauthorized room rentals and log editing.", "That side business has nothing to do with Elena's death! Yes, I rent private rooms after hours and edit badge timestamps so hotel management doesn't notice. But I didn't touch Elena!", requiredEvidenceId = "EVD-23", statementSummary = "Renata admits her side business and ability to edit main security logs.", unlocksEvidenceIds = listOf("EVD-22")),
        InterviewQuestion("Q_RC_5", "SUS-04", "The service stairwell keypad logged your PIN at 11:26 PM and 11:52 PM. Explain that.", "The... the service stairwell? That old keypad isn't connected to the... I mean, that must be a system glitch...", requiredEvidenceId = "EVD-22", statementSummary = "Renata is deeply rattled by the service stairwell keypad logs.", unlocksEvidenceIds = listOf("EVD-24")),
        InterviewQuestion("Q_RC_6", "SUS-04", "Forensics found Elena's blood on the rooftop stone planter ledge and her earring wedged in the deck.", "I didn't mean for her to die! She told me she was going to disappear again, leave the city, leave me behind forever! I grabbed her arm to stop her—she pulled away, tripped on the decking, and hit the stone ledge! I panicked! I couldn't bear to see her legacy destroyed, so I moved her down to the cellar and staged the robbery!", requiredEvidenceId = "EVD-24", statementSummary = "Renata breaks down and confesses to the accidental fall and wine cellar staging."),

        // SUS-05 Iris Chen
        InterviewQuestion("Q_IC_1", "SUS-05", "What were your duties during the closing hours?", "I was doing closing cleanup and dishwashing in the kitchen from 11:00 PM to 11:45 PM. I never left the kitchen area.", recordedStatementId = "ST_IC_1", statementSummary = "Iris claims she never left the kitchen from 11:00 to 11:45 PM.", unlocksEvidenceIds = listOf("EVD-25")),
        InterviewQuestion("Q_IC_2", "SUS-05", "What was your relationship with Elena Voss?", "She was my boss. I've only worked here three months as a server and bar-back. We kept things strictly professional.", statementSummary = "Iris claims a purely professional relationship with Elena."),
        InterviewQuestion("Q_IC_3", "SUS-05", "A staff member spotted you near the Floor 30 service stairwell around 11:20 PM.", "I... I stepped out for some fresh air for a few minutes. That's all.", statementSummary = "Iris admits briefly stepping away from the kitchen."),
        InterviewQuestion("Q_IC_4", "SUS-05", "Recovered texts show you are Elena's half-sister and dating Theo Marsh.", "Elena is my older half-sister! I tracked her down after our mother died. She gave me this job so we could reconnect. But last week I found out she was planning to sell everything and disappear under another identity—abandoning me all over again! I went up to the roof at 11:20 PM to confront her, but we just argued and I walked back down.", recordedStatementId = "ST_IC_2", requiredEvidenceId = "EVD-25", statementSummary = "Iris reveals she is Elena's half-sister and explains her brief confrontation on the roof.", unlocksEvidenceIds = listOf("EVD-10")),
        InterviewQuestion("Q_IC_5", "SUS-05", "Elena's 11:27 PM voice memo shows she was calm and alone after you left. How did you leave things?", "She was sad, but gentle with me. She told me she loved me and promised she wouldn't abandon me again. I went back to the kitchen before Priya arrived on the roof.", requiredEvidenceId = "EVD-10", statementSummary = "Iris confirms her exchange ended peacefully before Priya arrived."),

        // SUS-06 Whit Sokol
        InterviewQuestion("Q_WS_1", "SUS-06", "What was your schedule after the farewell tasting?", "I went up to my hotel room at 10:47 PM, reviewed acquisition documents, and was asleep by eleven. I had no further contact with anyone.", recordedStatementId = "ST_WS_1", statementSummary = "Whit claims he went to his room at 10:47 PM and was asleep by 11:00 PM.", unlocksEvidenceIds = listOf("EVD-17")),
        InterviewQuestion("Q_WS_2", "SUS-06", "What was the nature of Titan Hospitality's acquisition of Meridian?", "Standard acquisition. We buy top-tier boutique properties and expand their brand equity across regional markets.", statementSummary = "Whit describes the acquisition as routine."),
        InterviewQuestion("Q_WS_3", "SUS-06", "Hotel records show your keycard tap was a refresh, and you were seen on Floor 30 at 11:18 PM.", "David texted me that Elena was killing the deal! I came back down to Floor 30 at 11:18 PM to try to save the contract. But I saw Priya heading toward the stairs, realized things were chaotic, and stayed at the bar before going back up!", recordedStatementId = "ST_WS_2", requiredEvidenceId = "EVD-17", statementSummary = "Whit admits returning to Floor 30 at 11:18 PM after David's text, but stayed at the bar.", unlocksEvidenceIds = listOf("EVD-16")),
        InterviewQuestion("Q_WS_4", "SUS-06", "Did you have a secret side arrangement with David Ostrow?", "David wanted a guaranteed golden parachute, so we drew up an addendum. But you have to understand: Titan's acquisition contract specifically voids if Elena Voss is not personally attached to the business for two years! Her death completely destroys the deal for me!", requiredEvidenceId = "EVD-16", statementSummary = "Whit reveals the contract voiding clause, proving Elena's death hurts his financial interest.")
    )

    val STATEMENTS = listOf(
        StatementItem(
            id = "ST_DO_1",
            suspectId = "SUS-01",
            statementText = "I enjoyed the tasting, had a few drinks with guests, and left the building around 10:45 PM before things got quiet. I went straight home.",
            summary = "David claims he left at 10:45 PM and went straight home.",
            sourceContext = "David Ostrow — Initial Statement",
            timestamp = "10:45 PM",
            relatedEvidenceIds = listOf("EVD-13", "EVD-14"),
            contradictionId = "C001_DAVID_DEPARTURE"
        ),
        StatementItem(
            id = "ST_DO_2",
            suspectId = "SUS-01",
            statementText = "Alright, fine! I went back up to Floor 30. Elena told me she was killing the Titan sale. I was furious—I just wanted her to reconsider! We argued, but I walked away and left in a cab!",
            summary = "David admits returning to Floor 30 to argue about the sale.",
            sourceContext = "David Ostrow — Re-interrogation",
            timestamp = "11:05 PM - 11:12 PM",
            relatedEvidenceIds = listOf("EVD-13", "EVD-14", "EVD-15")
        ),
        StatementItem(
            id = "ST_TM_1",
            suspectId = "SUS-02",
            statementText = "I was in the kitchen doing bottle inventory from 11:00 PM until 11:45 PM continuously. I didn't leave the kitchen until I went to the cellar at 12:10 AM for the reserve Bordeaux and found Elena.",
            summary = "Theo claims continuous kitchen inventory from 11:00 to 11:45 PM.",
            sourceContext = "Theo Marsh — Initial Statement",
            timestamp = "11:00 PM - 11:45 PM",
            relatedEvidenceIds = listOf("EVD-18", "EVD-19"),
            contradictionId = "C002_THEO_INVENTORY"
        ),
        StatementItem(
            id = "ST_TM_2",
            suspectId = "SUS-02",
            statementText = "Look, I... I did see Elena briefly around 10:50 PM. She noticed a discrepancy in the cash register and snapped at me. I was shaken up, so during inventory I had to step away to deal with a cooler alarm.",
            summary = "Theo admits an earlier register dispute and cooler alarm repair.",
            sourceContext = "Theo Marsh — Re-interrogation",
            timestamp = "10:50 PM & 11:12 PM",
            relatedEvidenceIds = listOf("EVD-18", "EVD-19", "EVD-20")
        ),
        StatementItem(
            id = "ST_PN_1",
            suspectId = "SUS-03",
            statementText = "I stayed for the toasts, chatted with Elena and the regulars, said my goodbyes at 10:30 PM, and took a cab home. I haven't set foot in that building since.",
            summary = "Priya claims she left at 10:30 PM and never returned.",
            sourceContext = "Priya Nandan — Initial Statement",
            timestamp = "10:30 PM",
            relatedEvidenceIds = listOf("EVD-11", "EVD-12", "EVD-26"),
            contradictionId = "C003_PRIYA_RETURN"
        ),
        StatementItem(
            id = "ST_PN_2",
            suspectId = "SUS-03",
            statementText = "My father went to prison for ten years for her crime! Elena Kowalski stole his life and built this bar! Yes, I came back at 11:15 PM and went to the roof. But I didn't kill her!",
            summary = "Priya admits returning at 11:15 PM to confront Elena as Marcus Webb's daughter.",
            sourceContext = "Priya Nandan — Re-interrogation",
            timestamp = "11:15 PM - 11:26 PM",
            relatedEvidenceIds = listOf("EVD-12", "EVD-10", "EVD-26")
        ),
        StatementItem(
            id = "ST_RC_1",
            suspectId = "SUS-04",
            statementText = "I oversaw building logistics, did security rounds across Floors 28 to 30 until 10:20 PM, and then stayed in my 28th-floor office monitoring the console all night.",
            summary = "Renata claims rounds until 10:20 PM and office all night.",
            sourceContext = "Renata Cole — Initial Statement",
            timestamp = "10:20 PM onwards",
            relatedEvidenceIds = listOf("EVD-21", "EVD-22"),
            contradictionId = "C005_RENATA_LOG_CONTRADICTION"
        ),
        StatementItem(
            id = "ST_RC_2",
            suspectId = "SUS-04",
            statementText = "I haven't been up to the roof since I locked the public doors for the night at 10:20 PM. The master security system confirms that.",
            summary = "Renata insists she never accessed the roof after 10:20 PM.",
            sourceContext = "Renata Cole — Security Alibi",
            timestamp = "After 10:20 PM",
            relatedEvidenceIds = listOf("EVD-21", "EVD-22"),
            contradictionId = "C005_RENATA_LOG_CONTRADICTION"
        ),
        StatementItem(
            id = "ST_IC_1",
            suspectId = "SUS-05",
            statementText = "I was doing closing cleanup and dishwashing in the kitchen from 11:00 PM to 11:45 PM. I never left the kitchen area.",
            summary = "Iris claims she never left the kitchen from 11:00 to 11:45 PM.",
            sourceContext = "Iris Chen — Initial Statement",
            timestamp = "11:00 PM - 11:45 PM",
            relatedEvidenceIds = listOf("EVD-25")
        ),
        StatementItem(
            id = "ST_IC_2",
            suspectId = "SUS-05",
            statementText = "Elena is my older half-sister! I tracked her down after our mother died. She gave me this job so we could reconnect. But last week I found out she was planning to sell everything and disappear under another identity—abandoning me all over again! I went up to the roof at 11:20 PM to confront her, but we just argued and I walked back down.",
            summary = "Iris reveals she is Elena's half-sister and explains her brief roof confrontation.",
            sourceContext = "Iris Chen — Re-interrogation",
            timestamp = "11:20 PM",
            relatedEvidenceIds = listOf("EVD-25", "EVD-10")
        ),
        StatementItem(
            id = "ST_WS_1",
            suspectId = "SUS-06",
            statementText = "I went up to my hotel room at 10:47 PM, reviewed acquisition documents, and was asleep by eleven. I had no further contact with anyone.",
            summary = "Whit claims he went to his room at 10:47 PM and was asleep by 11:00 PM.",
            sourceContext = "Whit Sokol — Initial Statement",
            timestamp = "10:47 PM onwards",
            relatedEvidenceIds = listOf("EVD-17"),
            contradictionId = "C004_WHIT_ALIBI"
        ),
        StatementItem(
            id = "ST_WS_2",
            suspectId = "SUS-06",
            statementText = "David texted me that Elena was killing the deal! I came back down to Floor 30 at 11:18 PM to try to save the contract. But I saw Priya heading toward the stairs, realized things were chaotic, and stayed at the bar before going back up!",
            summary = "Whit admits returning to Floor 30 at 11:18 PM after David's text.",
            sourceContext = "Whit Sokol — Re-interrogation",
            timestamp = "11:18 PM",
            relatedEvidenceIds = listOf("EVD-17", "EVD-16")
        )
    )

    val EVIDENCE_REACTIONS = listOf(
        // SUS-01 David Ostrow
        EvidenceReaction(
            suspectId = "SUS-01",
            evidenceId = "EVD-13",
            detectivePrompt = "Electronic badge logs show you swiped into Floor 30 at 11:05 PM, contradicting your claim that you left at 10:45 PM.",
            suspectResponse = "Alright, fine! I went back up to Floor 30. Elena told me she was killing the Titan sale and I was desperate to change her mind!",
            isContradiction = true,
            triggersContradictionId = "C001_DAVID_DEPARTURE",
            unlocksQuestionIds = listOf("Q_DO_3")
        ),
        EvidenceReaction(
            suspectId = "SUS-01",
            evidenceId = "EVD-15",
            detectivePrompt = "Rideshare records confirm you were picked up outside the hotel at 11:19 PM, minutes before Elena died.",
            suspectResponse = "Thank God... I was terrified you would think I hurt her. I argued with her, but I walked away and left.",
            clearsSuspectCriticalPeriod = true,
            unlocksQuestionIds = listOf("Q_DO_5")
        ),
        EvidenceReaction(
            suspectId = "SUS-01",
            evidenceId = "EVD-16",
            detectivePrompt = "Your text to Whit Sokol at 11:14 PM confirms you gave up on the deal and were leaving the building.",
            suspectResponse = "Yes! I sent that text as I took the elevator down to the lobby. The deal was dead, and I had no reason to stay.",
            unlocksQuestionIds = listOf("Q_DO_4")
        ),

        // SUS-02 Theo Marsh
        EvidenceReaction(
            suspectId = "SUS-02",
            evidenceId = "EVD-18",
            detectivePrompt = "POS scanner records show an unlogged 6-minute gap in your inventory between 11:12 PM and 11:18 PM.",
            suspectResponse = "I... I wasn't attacking Elena! The walk-in cooler alarm went off and I had to reset the compressor!",
            isContradiction = true,
            triggersContradictionId = "C002_THEO_INVENTORY",
            unlocksQuestionIds = listOf("Q_TM_3")
        ),
        EvidenceReaction(
            suspectId = "SUS-02",
            evidenceId = "EVD-19",
            detectivePrompt = "Refrigeration system diagnostics verify you were inside the walk-in cooler resetting the compressor from 11:12 to 11:17 PM.",
            suspectResponse = "See?! I told you I never left the kitchen! I was fixing the cooler the whole time!",
            clearsSuspectCriticalPeriod = true,
            unlocksQuestionIds = listOf("Q_TM_5")
        ),
        EvidenceReaction(
            suspectId = "SUS-02",
            evidenceId = "EVD-20",
            detectivePrompt = "Weekly audit reports show recurring $400 register shortfalls. Elena noticed and confronted you at 10:50 PM.",
            suspectResponse = "I took the cash for my mom's chemotherapy, but I didn't kill Elena! The Titan buyout promised me GM—I needed Elena alive!",
            unlocksQuestionIds = listOf("Q_TM_4")
        ),

        // SUS-03 Priya Nandan
        EvidenceReaction(
            suspectId = "SUS-03",
            evidenceId = "EVD-11",
            detectivePrompt = "A cocktail napkin in Elena's jacket shows you asked to speak privately about 'Marcus' earlier in the evening.",
            suspectResponse = "I spoke to her by the wine wall at 9:10 PM. I wanted her to know that her past hadn't stayed buried.",
            unlocksQuestionIds = listOf("Q_PN_3")
        ),
        EvidenceReaction(
            suspectId = "SUS-03",
            evidenceId = "EVD-12",
            detectivePrompt = "Court records prove Elena Kowalski framed your father Marcus Webb for embezzlement ten years ago.",
            suspectResponse = "She ruined my father's life! Yes, I went back up to the roof at 11:15 PM to demand answers, but I didn't lay a hand on her!",
            isContradiction = true,
            triggersContradictionId = "C003_PRIYA_RETURN",
            unlocksQuestionIds = listOf("Q_PN_4")
        ),
        EvidenceReaction(
            suspectId = "SUS-03",
            evidenceId = "EVD-26",
            detectivePrompt = "Loading dock cameras capture you exiting the building at 11:27 PM, corroborated by Elena's voice memo.",
            suspectResponse = "Elena apologized and promised to clear my father's name. I got what I came for and walked away.",
            clearsSuspectCriticalPeriod = true,
            unlocksQuestionIds = listOf("Q_PN_5")
        ),

        // SUS-04 Renata Cole
        EvidenceReaction(
            suspectId = "SUS-04",
            evidenceId = "EVD-09",
            detectivePrompt = "Elena's calendar scheduled an 11:30 PM 'Nightcap w/ R. on roof'. That was you, wasn't it?",
            suspectResponse = "We planned a drink, yes. But Elena canceled, so I stayed in my office all night.",
            unlocksQuestionIds = listOf("Q_RC_3")
        ),
        EvidenceReaction(
            suspectId = "SUS-04",
            evidenceId = "EVD-23",
            detectivePrompt = "Your personal ledger records unauthorized room rentals and shows how you overwrite camera timestamps.",
            suspectResponse = "That ledger is just my private business! Editing room timestamps has nothing to do with Elena!",
            triggersMotiveId = "MOTIVE_HEARTBREAK",
            unlocksQuestionIds = listOf("Q_RC_4")
        ),
        EvidenceReaction(
            suspectId = "SUS-04",
            evidenceId = "EVD-22",
            detectivePrompt = "The service stairwell keypad recorded your personal PIN at 11:26 PM and 11:52 PM, shattering your office alibi.",
            suspectResponse = "The service stairwell... you pulled the legacy subsystem logs?! I... I...",
            isContradiction = true,
            triggersContradictionId = "C005_RENATA_LOG_CONTRADICTION",
            unlocksQuestionIds = listOf("Q_RC_5")
        ),
        EvidenceReaction(
            suspectId = "SUS-04",
            evidenceId = "EVD-08",
            detectivePrompt = "Luminol revealed Elena's blood on the rooftop stone planter ledge, exactly where her earring was wedged.",
            suspectResponse = "The planter... oh God, the planter... it was an accident! She told me she was leaving forever and I tried to hold her back!",
            unlocksEvidenceIds = listOf("EVD-24"),
            unlocksQuestionIds = listOf("Q_RC_6")
        ),

        // SUS-05 Iris Chen
        EvidenceReaction(
            suspectId = "SUS-05",
            evidenceId = "EVD-25",
            detectivePrompt = "Recovered chat messages confirm you are Elena's half-sister and secretly dating Theo Marsh.",
            suspectResponse = "Elena is my sister. I went to the roof at 11:20 PM because I felt abandoned by her planned sale, but we didn't fight violently.",
            unlocksQuestionIds = listOf("Q_IC_4")
        ),
        EvidenceReaction(
            suspectId = "SUS-05",
            evidenceId = "EVD-10",
            detectivePrompt = "Elena's 11:27 PM voice memo confirms your exchange was brief, peaceful, and ended well before the fatal incident.",
            suspectResponse = "She told me she loved me and promised she wouldn't leave me behind. I went straight back down to the kitchen.",
            clearsSuspectCriticalPeriod = true,
            unlocksQuestionIds = listOf("Q_IC_5")
        ),

        // SUS-06 Whit Sokol
        EvidenceReaction(
            suspectId = "SUS-06",
            evidenceId = "EVD-17",
            detectivePrompt = "Hotel sensor logs show your 10:47 PM key tap was just a keycard refresh, and you were seen on Floor 30 at 11:18 PM.",
            suspectResponse = "I came down at 11:18 PM after David texted me, but I never went past the bar area!",
            isContradiction = true,
            triggersContradictionId = "C004_WHIT_ALIBI",
            clearsSuspectCriticalPeriod = true,
            unlocksQuestionIds = listOf("Q_WS_3", "Q_WS_4")
        )
    )

    val TIMELINE_EVENTS = listOf(
        TimelineEvent("T001", "8:00 PM", "Farewell Tasting Begins", "Private farewell tasting begins at Meridian on Floor 30. Guests include David, Priya, Whit, and regulars.", sourceEvidenceId = "EVD-09"),
        TimelineEvent("T002", "8:45 PM", "Elena's Toast", "Elena toasts the room and hints at 'big news' regarding the venue's future.", sourceEvidenceId = "EVD-09"),
        TimelineEvent("T003", "9:10 PM", "Priya's Private Confrontation", "Priya confronts Elena near the wine wall, revealing she knows Elena's former identity as Elena Kowalski.", sourceEvidenceId = "EVD-11", relatedSuspectId = "SUS-03"),
        TimelineEvent("T004", "9:45 PM", "David & Whit Hushed Discussion", "David Ostrow and Whit Sokol discuss their secret side deal. Renata overhears a fragment in passing.", sourceEvidenceId = "EVD-16", relatedSuspectId = "SUS-01"),
        TimelineEvent("T005", "10:20 PM", "Renata Logs Building Round", "Renata logs a security round, then manipulates the master console to appear 'in office' for the rest of the night.", sourceEvidenceId = "EVD-21", relatedSuspectId = "SUS-04"),
        TimelineEvent("T006", "10:30 PM", "Priya's Claimed Exit", "Priya publicly says goodbye and leaves the venue through the main entrance.", sourceEvidenceId = "EVD-11", relatedSuspectId = "SUS-03"),
        TimelineEvent("T007", "10:45 PM", "Whit Heads to Room / David Claimed Departure", "Whit heads toward the elevators; David claims he departed the building.", sourceEvidenceId = "EVD-13", relatedSuspectId = "SUS-01"),
        TimelineEvent("T008", "10:47 PM", "Whit's Keycard Reactivation", "Whit Sokol reactivates his keycard at his hotel room door.", sourceEvidenceId = "EVD-17", relatedSuspectId = "SUS-06", requiredEvidenceForUnlock = listOf("EVD-17")),
        TimelineEvent("T009", "11:05 PM", "David's Rooftop Confrontation", "David Ostrow swipes back onto Floor 30 and argues furiously with Elena on the rooftop deck.", sourceEvidenceId = "EVD-13", relatedSuspectId = "SUS-01", requiredEvidenceForUnlock = listOf("EVD-13", "EVD-14")),
        TimelineEvent("T010", "11:14 PM", "David Texts Whit", "David texts Whit that the deal is dead and that he is leaving the building.", sourceEvidenceId = "EVD-16", relatedSuspectId = "SUS-01", requiredEvidenceForUnlock = listOf("EVD-16")),
        TimelineEvent("T011", "11:19 PM", "David Leaves in Rideshare", "David Ostrow gets into a verified rideshare vehicle outside the hotel lobby.", sourceEvidenceId = "EVD-15", relatedSuspectId = "SUS-01", requiredEvidenceForUnlock = listOf("EVD-15")),
        TimelineEvent("T012", "11:18 PM", "Whit Returns to Bar", "Whit Sokol comes back down to Floor 30 to salvage the deal, but retreats upon seeing Priya.", sourceEvidenceId = "EVD-17", relatedSuspectId = "SUS-06", requiredEvidenceForUnlock = listOf("EVD-17")),
        TimelineEvent("T013", "11:12 PM", "Theo's Cooler Repair", "Theo Marsh steps into the kitchen walk-in cooler to reset a tripped compressor alarm.", sourceEvidenceId = "EVD-19", relatedSuspectId = "SUS-02", requiredEvidenceForUnlock = listOf("EVD-18", "EVD-19")),
        TimelineEvent("T014", "11:20 PM", "Iris Confronts Elena", "Iris Chen slips up to the rooftop deck to confront Elena about abandonment; exchange ends quietly.", sourceEvidenceId = "EVD-25", relatedSuspectId = "SUS-05", requiredEvidenceForUnlock = listOf("EVD-25")),
        TimelineEvent("T015", "11:22 PM", "Priya's Rooftop Reconciliation", "Priya confronts Elena about Marcus Webb; Elena tearfully apologizes and promises to clear his name.", sourceEvidenceId = "EVD-12", relatedSuspectId = "SUS-03", requiredEvidenceForUnlock = listOf("EVD-12")),
        TimelineEvent("T016", "11:27 PM", "Priya Exits / Elena's Voice Memo", "Priya exits via the dock camera; Elena records a peaceful voice memo reflecting on the reconciliation.", sourceEvidenceId = "EVD-10", relatedSuspectId = "SUS-03", requiredEvidenceForUnlock = listOf("EVD-10", "EVD-26")),
        TimelineEvent("T017", "11:28 PM", "Renata Arrives on Roof", "Renata Cole enters the service stairwell with PIN 4491 and arrives on the rooftop deck.", sourceEvidenceId = "EVD-22", relatedSuspectId = "SUS-04", requiredEvidenceForUnlock = listOf("EVD-22")),
        TimelineEvent("T018", "11:32 PM", "Fatal Struggle at Stone Planter", "Renata grabs Elena's arm to stop her from leaving; Elena breaks free, falls, and strikes the stone planter ledge.", sourceEvidenceId = "EVD-08", relatedSuspectId = "SUS-04", requiredEvidenceForUnlock = listOf("EVD-06", "EVD-07", "EVD-08")),
        TimelineEvent("T019", "11:35 PM", "Body Transport & Staging", "Renata moves Elena's body down the service stairwell to the wine cellar and stages the robbery.", sourceEvidenceId = "EVD-05", relatedSuspectId = "SUS-04", requiredEvidenceForUnlock = listOf("EVD-05")),
        TimelineEvent("T020", "11:52 PM", "Renata Exits Service Stairwell", "Renata keys out of the Floor 29 wine cellar service stairwell using PIN 4491.", sourceEvidenceId = "EVD-22", relatedSuspectId = "SUS-04", requiredEvidenceForUnlock = listOf("EVD-22")),
        TimelineEvent("T021", "12:10 AM", "Body Discovered by Theo", "Theo Marsh goes down to the wine cellar for the reserve Bordeaux and discovers Elena's body.", sourceEvidenceId = "EVD-01", relatedSuspectId = "SUS-02")
    )

    val OBJECTIVES = listOf(
        Objective("O001", "Inspect the Discovery Scene", "Search the Floor 29 wine cellar and loading dock for clues regarding the reported robbery.", condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-01", "EVD-02", "EVD-03", "EVD-04")), leadActionLabel = "Inspect Cellar", leadTarget = Screen.CRIME_SCENE, focusTab = CaseFileTab.EVIDENCE),
        Objective("O002", "Conduct Initial Interrogations", "Interview all key persons of interest present at Meridian on the night of the tasting.", condition = ObjectiveCondition.InterviewSuspects(listOf("SUS-01", "SUS-02", "SUS-03", "SUS-04", "SUS-05", "SUS-06")), leadActionLabel = "Review Suspects", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.SUSPECTS),
        Objective("O003", "Analyze Forensic Autopsy Report", "Review the medical examiner's findings regarding cause of death and lividity patterns.", condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-05", "EVD-06")), leadActionLabel = "Review Forensics", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.EVIDENCE),
        Objective("O004", "Investigate Rooftop True Crime Scene", "Search the Floor 30 rooftop deck for physical traces of the true fatal impact.", condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-07", "EVD-08")), leadActionLabel = "Examine Rooftop", leadTarget = Screen.CRIME_SCENE, focusTab = CaseFileTab.EVIDENCE),
        Objective("O005", "Expose False Alibis & Contradictions", "Confront suspects with digital access logs and timeline evidence.", condition = ObjectiveCondition.UnlockContradictions(listOf("C001_DAVID_DEPARTURE", "C002_THEO_INVENTORY", "C003_PRIYA_RETURN")), leadActionLabel = "Expose Lies", leadTarget = Screen.DETECTIVE_BOARD, focusTab = CaseFileTab.DEDUCTIONS),
        Objective("O006", "Uncover Secondary Keypad Subsystem", "Extract legacy service stairwell access logs to break the false security clearance.", condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-21", "EVD-22", "EVD-23")), leadActionLabel = "Access Subsystem", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.EVIDENCE),
        Objective("O007", "Establish Motive & Opportunity", "Form deductions establishing Renata Cole's presence and motive on the roof.", condition = ObjectiveCondition.DiscoveredMotiveAndOpportunity(), leadActionLabel = "Build Theory", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.THEORY),
        Objective("O008", "Submit Final Accusation", "Formulate the complete case against the true culprit with supporting evidence.", condition = ObjectiveCondition.CaseSolved, leadActionLabel = "Indict Culprit", leadTarget = Screen.FINAL_CASE_REVIEW, focusTab = CaseFileTab.THEORY)
    )

    val LEADS = listOf(
        InvestigationLead(
            id = "LEAD_002_01_CELLAR_SCENE",
            title = "THE SCENE IN THE CELLAR",
            subtitle = "Staged Robbery & Postmortem Discrepancies",
            shortDescription = "Examine the Floor 29 discovery scene, uncover the postmortem transfer, and question Theo Marsh.",
            briefing = "At 12:10 AM, head bartender Theo Marsh reported discovering Elena Voss deceased in the Floor 29 wine cellar. The scene presents initial signs of a burglary: shattered wine shelves, a forced reserve vintage case, and an open loading dock door. Search the scene, document the evidence, inspect the body for trauma patterns, question the discoverer, and establish the initial forensic reality.",
            centralQuestion = "Did Elena die in the wine cellar, or was the discovery scene staged?",
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_01_SEARCH_CELLAR",
                    title = "Search the Floor 29 Wine Cellar",
                    description = "Examine the crime scene to secure the body position, broken shelf, and missing vintage bottle.",
                    actionLabel = "Search Cellar",
                    target = LeadActionTarget.CrimeScene("hotspot_body"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-01", "EVD-02", "EVD-03"))
                ),
                LeadObjective(
                    id = "OBJ_002_01_EXAMINE_BODY",
                    title = "Examine Victim & Forensic Discrepancies",
                    description = "Inspect the body in detail to review the medical examiner's findings on cause of death and lividity.",
                    actionLabel = "Examine Body",
                    target = LeadActionTarget.Evidence("EVD-01"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-05"))
                ),
                LeadObjective(
                    id = "OBJ_002_01_INTERVIEW_THEO",
                    title = "Interview Discoverer Theo Marsh",
                    description = "Question Theo Marsh about his arrival in the cellar and the missing 1990 Bordeaux.",
                    actionLabel = "Interview Theo",
                    target = LeadActionTarget.Suspect("SUS-02", "Q_TM_1"),
                    condition = ObjectiveCondition.AskQuestions(listOf("Q_TM_1"))
                ),
                LeadObjective(
                    id = "OBJ_002_01_CHECK_DOCK",
                    title = "Inspect Loading Dock Service Exit",
                    description = "Check the Floor 29 loading dock door to verify the reported burglar's escape route.",
                    actionLabel = "Check Dock Door",
                    target = LeadActionTarget.CrimeScene("hotspot_dock_door"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-04"))
                ),
                LeadObjective(
                    id = "OBJ_002_01_STAGED_SCENE",
                    title = "Establish Staged Crime Scene Deduction",
                    description = "Form the deduction establishing that Elena died elsewhere and was moved into the cellar postmortem.",
                    actionLabel = "Review Reasoning",
                    target = LeadActionTarget.Reasoning(CaseFileTab.DEDUCTIONS),
                    condition = ObjectiveCondition.FormDeductions(listOf("D001_STAGED_SCENE"))
                )
            ),
            completionSummary = "Forensic evidence establishes that Elena Voss did not die in the wine cellar. Fixed posterior lividity proves she lay flat on her back for 20-30 minutes postmortem before being positioned on her side beside the broken shelving. The wine cellar struggle and robbery were staged.",
            nextLeadId = "LEAD_002_02_TIMELINES_AND_DEPARTURES",
            associatedEvidenceIds = listOf("EVD-01", "EVD-02", "EVD-03", "EVD-04", "EVD-05"),
            associatedSuspectIds = listOf("SUS-02"),
            associatedLocation = "Floor 29 — Wine Cellar",
            orderIndex = 1
        ),
        InvestigationLead(
            id = "LEAD_002_02_TIMELINES_AND_DEPARTURES",
            title = "THE 11:05 PM RE-ENTRY & FALSE ALIBIS",
            subtitle = "Electronic Access Logs & Initial Contradictions",
            shortDescription = "Compare suspect statements against Floor 30 electronic badge logs and uncover initial timeline conflicts.",
            briefing = "Now that we know the cellar was staged, we must reconstruct who was truly present on Floor 30 during the critical late-night window. Security badge records and suspect testimonies must be cross-examined to expose false departures.",
            centralQuestion = "Who re-entered Floor 30 around 11:00 PM and what were they hiding?",
            unlockLeadIds = listOf("LEAD_002_01_CELLAR_SCENE"),
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_02_DAVID_BADGE",
                    title = "Review Floor 30 Security Badge Logs",
                    description = "Inspect electronic elevator access records for suspect movements around 11:00 PM.",
                    actionLabel = "Review Logs",
                    target = LeadActionTarget.CrimeScene("hotspot_security_console"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-13"))
                ),
                LeadObjective(
                    id = "OBJ_002_02_CONFRONT_DAVID",
                    title = "Confront David Ostrow with Badge Re-entry",
                    description = "Expose David's false 10:45 PM departure using his 11:05 PM elevator badge swipe.",
                    actionLabel = "Confront David",
                    target = LeadActionTarget.Suspect("SUS-01"),
                    condition = ObjectiveCondition.UnlockContradictions(listOf("C001_DAVID_DEPARTURE"))
                )
            ),
            completionSummary = "David Ostrow's claimed 10:45 PM departure was disproven. He swiped back onto Floor 30 at 11:05 PM and argued furiously with Elena over the acquisition deal before departing by rideshare at 11:19 PM.",
            nextLeadId = "LEAD_002_03_ROOFTOP_FORENSICS",
            associatedEvidenceIds = listOf("EVD-13", "EVD-14", "EVD-15"),
            associatedSuspectIds = listOf("SUS-01"),
            associatedLocation = "Floor 28 — Security Office",
            orderIndex = 2
        ),
        InvestigationLead(
            id = "LEAD_002_03_ROOFTOP_FORENSICS",
            title = "THE ROOFTOP CRIME SCENE",
            subtitle = "Forensic Traces on Floor 30",
            shortDescription = "Investigate the rooftop terrace and locate the true scene of the fatal trauma.",
            briefing = "Microscopic wound analysis revealed limestone residue completely foreign to the concrete cellar. Search the Floor 30 rooftop deck to identify where Elena suffered the fatal impact.",
            centralQuestion = "Where did Elena actually die, and what physical traces remain on the rooftop?",
            unlockLeadIds = listOf("LEAD_002_02_TIMELINES_AND_DEPARTURES"),
            isMajorBreakthrough = true,
            breakthroughTitle = "TRUE CRIME SCENE IDENTIFIED: FLOOR 30 ROOFTOP",
            breakthroughDescription = "Luminol forensics and physical jewelry confirm Elena Voss died at the Floor 30 stone planter ledge, not in the cellar.",
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_03_EXAMINE_ROOF",
                    title = "Search the Floor 30 Rooftop Planter",
                    description = "Search the outdoor terrace to recover stone residue and Elena's lost earring.",
                    actionLabel = "Search Rooftop",
                    target = LeadActionTarget.CrimeScene("hotspot_planter"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-06", "EVD-07", "EVD-08"))
                ),
                LeadObjective(
                    id = "OBJ_002_03_ROOF_DEDUCTION",
                    title = "Establish Rooftop True Scene Deduction",
                    description = "Form the deduction proving Floor 30 was the true scene of death.",
                    actionLabel = "Form Deduction",
                    target = LeadActionTarget.Reasoning(CaseFileTab.DEDUCTIONS),
                    condition = ObjectiveCondition.FormDeductions(listOf("D002_ROOFTOP_TRUE_SCENE"))
                )
            ),
            completionSummary = "Luminol testing on the Floor 30 architectural planter ledge confirmed Elena's blood, and her pearl earring stud was found wedged into the adjacent teak decking. The rooftop terrace is the true scene of the fatal head impact.",
            nextLeadId = "LEAD_002_04_COMMUNICATIONS_AND_ALIBIS",
            associatedEvidenceIds = listOf("EVD-06", "EVD-07", "EVD-08"),
            associatedLocation = "Floor 30 — Rooftop Deck",
            orderIndex = 3
        ),
        InvestigationLead(
            id = "LEAD_002_04_COMMUNICATIONS_AND_ALIBIS",
            title = "DIGITAL THREADS & TIME ANCHORS",
            subtitle = "Reconstructing the Final Minutes",
            shortDescription = "Recover communication records and Elena's final voice memo to establish alibis.",
            briefing = "Extract Elena's encrypted smartphone recordings and verify suspect communication threads to isolate the exact minutes of the murder.",
            centralQuestion = "Who was on the roof during the critical murder window between 11:28 and 11:32 PM?",
            unlockLeadIds = listOf("LEAD_002_03_ROOFTOP_FORENSICS"),
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_04_VOICE_MEMO",
                    title = "Recover 11:27 PM Voice Memo",
                    description = "Analyze Elena's phone audio proving she was peaceful and alone at 11:27 PM.",
                    actionLabel = "Review Audio",
                    target = LeadActionTarget.Evidence("EVD-10"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-09", "EVD-10"))
                ),
                LeadObjective(
                    id = "OBJ_002_04_CLEAR_ALIBIS",
                    title = "Clear Innocent Suspects",
                    description = "Form deductions establishing that David, Theo, Priya, and Iris are cleared.",
                    actionLabel = "Review Alibis",
                    target = LeadActionTarget.Reasoning(CaseFileTab.DEDUCTIONS),
                    condition = ObjectiveCondition.FormDeductions(listOf("D003_ALIBIS_CLEARED"))
                )
            ),
            completionSummary = "Elena's 11:27 PM voice memo and independent GPS logs conclusively clear David, Theo, Priya, and Iris, narrowing the fatal encounter to 11:28 PM - 11:32 PM.",
            nextLeadId = "LEAD_002_05_THE_SERVICE_STAIRWELL",
            associatedEvidenceIds = listOf("EVD-09", "EVD-10", "EVD-15", "EVD-19", "EVD-26"),
            orderIndex = 4
        ),
        InvestigationLead(
            id = "LEAD_002_05_THE_SERVICE_STAIRWELL",
            title = "THE UNMONITORED STAIRWELL",
            subtitle = "The Standalone Keypad Subsystem",
            shortDescription = "Examine the unmonitored service stairwell logs to shatter Renata Cole's security alibi.",
            briefing = "Renata Cole claimed she never visited the roof and edited master CCTV logs. But the camera-free service stairwell runs on an independent legacy keypad controller. Extract the unscrubbed logs.",
            centralQuestion = "Who bypassed the building's master security cameras to transport the victim?",
            unlockLeadIds = listOf("LEAD_002_04_COMMUNICATIONS_AND_ALIBIS"),
            isMajorBreakthrough = true,
            breakthroughTitle = "DECISIVE CONTRADICTION: KEYPAD PIN 4491",
            breakthroughDescription = "Renata Cole's personal PIN was recorded entering the roof at 11:26 PM and exiting the cellar at 11:52 PM.",
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_05_KEYPAD_LOGS",
                    title = "Extract Service Stairwell Keypad Logs",
                    description = "Recover access timestamps from the legacy stairwell access controller.",
                    actionLabel = "Inspect Keypad",
                    target = LeadActionTarget.CrimeScene("hotspot_stairwell_keypad"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-22"))
                ),
                LeadObjective(
                    id = "OBJ_002_05_RENATA_CONTRADICTION",
                    title = "Confront Renata Cole with Dual Keypad Logs",
                    description = "Establish the decisive contradiction shattering Renata's master console alibi.",
                    actionLabel = "Confront Renata",
                    target = LeadActionTarget.Suspect("SUS-04"),
                    condition = ObjectiveCondition.UnlockContradictions(listOf("C005_RENATA_LOG_CONTRADICTION"))
                )
            ),
            completionSummary = "Renata Cole's exclusive PIN code 4491 places her on the rooftop deck at 11:26 PM immediately prior to Elena's fall, and in the Floor 29 wine cellar at 11:52 PM staging the body.",
            nextLeadId = "LEAD_002_06_FINAL_PROSECUTION",
            associatedEvidenceIds = listOf("EVD-21", "EVD-22", "EVD-23"),
            associatedSuspectIds = listOf("SUS-04"),
            associatedLocation = "Floor 29/30 — Service Stairwell",
            orderIndex = 5
        ),
        InvestigationLead(
            id = "LEAD_002_06_FINAL_PROSECUTION",
            title = "THE FINAL CHARGE",
            subtitle = "Synthesizing the Prosecution Dossier",
            shortDescription = "Assemble all forensic pillars, motive, weapon, and time anchors to indict the culprit.",
            briefing = "With all innocent parties cleared, the rooftop true crime scene established, and Renata's dual-system contradiction locked in, finalize the indictment.",
            centralQuestion = "Can we establish the complete motive, weapon, and time anchors to convict the culprit?",
            unlockLeadIds = listOf("LEAD_002_05_THE_SERVICE_STAIRWELL"),
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_06_BUILD_THEORY",
                    title = "Establish Complete Case Theory",
                    description = "Designate Renata Cole, heartbreak motive, and stone planter weapon in your Theory.",
                    actionLabel = "Open Theory",
                    target = LeadActionTarget.Reasoning(CaseFileTab.THEORY),
                    condition = ObjectiveCondition.DiscoveredMotiveAndOpportunity()
                ),
                LeadObjective(
                    id = "OBJ_002_06_INDICT",
                    title = "Submit Final Indictment",
                    description = "Formulate the accusation and secure a conviction.",
                    actionLabel = "Indict Culprit",
                    target = LeadActionTarget.CaseReview,
                    condition = ObjectiveCondition.CaseSolved
                )
            ),
            completionSummary = "Renata Cole was formally convicted of the homicide of Elena Voss.",
            associatedSuspectIds = listOf("SUS-04"),
            orderIndex = 6
        ),
        InvestigationLead(
            id = "LEAD_002_OPTIONAL_TITAN_DEAL",
            title = "TITAN CAPITAL & THE MERIDIAN BUYOUT",
            subtitle = "Corporate Maneuvers & Side Deals",
            shortDescription = "Investigate the financial motives and background behind the impending corporate buyout.",
            briefing = "Examine the secret communications and agreements between Whit Sokol and David Ostrow to understand the full financial pressure surrounding Elena's planned departure.",
            centralQuestion = "What were the financial ramifications of Elena killing the Titan acquisition?",
            unlockEvidenceIds = listOf("EVD-16"),
            isOptional = true,
            objectives = listOf(
                LeadObjective(
                    id = "OBJ_002_OPT_ANALYZE_DEAL",
                    title = "Analyze Titan Deal Voiding Clause",
                    description = "Review Whit Sokol's acquisition documents to understand why Elena's death hurts Titan.",
                    actionLabel = "Review Documents",
                    target = LeadActionTarget.Evidence("EVD-16"),
                    condition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-16", "EVD-17")),
                    isOptional = true
                ),
                LeadObjective(
                    id = "OBJ_002_OPT_SIDE_DEAL",
                    title = "Establish Side Deal Deduction",
                    description = "Deduce how David and Whit were secretly scheming behind Elena's back.",
                    actionLabel = "Form Deduction",
                    target = LeadActionTarget.Reasoning(CaseFileTab.DEDUCTIONS),
                    condition = ObjectiveCondition.FormDeductions(listOf("D004_DAVID_WHIT_SIDE_DEAL")),
                    isOptional = true
                )
            ),
            completionSummary = "Titan Hospitality's contract strictly voided without Elena Voss, proving Whit Sokol had every reason to keep her alive.",
            associatedEvidenceIds = listOf("EVD-13", "EVD-16", "EVD-17"),
            associatedSuspectIds = listOf("SUS-01", "SUS-06"),
            orderIndex = 7
        )
    )

    val INVESTIGATION_MOMENTS = listOf(
        InvestigationMoment(
            id = "MOMENT_002_01_CELLAR_DISCREPANCY",
            title = "FORENSIC DISCREPANCY",
            subtitle = "Autopsy vs. Discovery Scene",
            narrativeText = "The coroner's preliminary report establishes fixed posterior lividity. Elena lay flat on her back for 20 to 30 minutes before being placed on her side in the wine cellar. This cellar was not the true scene of death.",
            type = InvestigationMomentType.NEW_DEVELOPMENT,
            triggerCondition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-05")),
            associatedEvidenceId = "EVD-05",
            associatedLeadId = "LEAD_002_01_CELLAR_SCENE",
            actionLabel = "EXAMINE AUTOPSY",
            actionTarget = LeadActionTarget.Evidence("EVD-05"),
            priority = 10
        ),
        InvestigationMoment(
            id = "MOMENT_002_02_DAVID_BADGE",
            title = "STATEMENT CONFLICT",
            subtitle = "David Ostrow's Alibi Collapses",
            narrativeText = "Floor 30 elevator badge records prove David Ostrow swiped back in at 11:05 PM. This directly conflicts with his initial statement claiming he departed at 10:45 PM.",
            type = InvestigationMomentType.STATEMENT_UPDATE,
            triggerCondition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-13")),
            associatedEvidenceId = "EVD-13",
            associatedSuspectId = "SUS-01",
            associatedLeadId = "LEAD_002_02_TIMELINES_AND_DEPARTURES",
            actionLabel = "RE-INTERVIEW DAVID",
            actionTarget = LeadActionTarget.Suspect("SUS-01"),
            priority = 20
        ),
        InvestigationMoment(
            id = "MOMENT_002_03_ROOFTOP_CONNECTION",
            title = "FORENSIC CONNECTION",
            subtitle = "Wound Matches Rooftop Stone Planter",
            narrativeText = "Microscopic limestone dust recovered from Elena's fatal head trauma corresponds precisely with the architectural stone planter on the Floor 30 rooftop deck. Elena died on the rooftop terrace.",
            type = InvestigationMomentType.EVIDENCE_CONNECTION,
            triggerCondition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-06", "EVD-08")),
            associatedEvidenceId = "EVD-08",
            associatedLeadId = "LEAD_002_03_ROOFTOP_FORENSICS",
            actionLabel = "EXAMINE ROOFTOP TRACES",
            actionTarget = LeadActionTarget.CrimeScene("hotspot_planter"),
            isMajorBreakthrough = true,
            priority = 30
        ),
        InvestigationMoment(
            id = "MOMENT_002_04_VOICE_MEMO",
            title = "CRITICAL TIME ANCHOR",
            subtitle = "Elena's 11:27 PM Voice Memo Recovered",
            narrativeText = "Elena recorded an unsent voice memo alone on the rooftop deck at 11:27 PM, completely peaceful. This single recording clears Priya Nandan and Iris Chen, narrowing the fatal encounter to 11:28 PM – 11:32 PM.",
            type = InvestigationMomentType.COMMUNICATION_RECOVERED,
            triggerCondition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-10")),
            associatedEvidenceId = "EVD-10",
            associatedLeadId = "LEAD_002_04_COMMUNICATIONS_AND_ALIBIS",
            actionLabel = "LISTEN TO MEMO",
            actionTarget = LeadActionTarget.Evidence("EVD-10"),
            priority = 40
        ),
        InvestigationMoment(
            id = "MOMENT_002_05_STAIRWELL_KEYPAD",
            title = "DECISIVE CONTRADICTION",
            subtitle = "Legacy Keypad PIN 4491 Logged",
            narrativeText = "While the master CCTV console shows zero rooftop activity, the independent service stairwell keypad recorded PIN 4491—assigned exclusively to Renata Cole—entering the roof at 11:26 PM and exiting the cellar at 11:52 PM.",
            type = InvestigationMomentType.BREAKTHROUGH,
            triggerCondition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-22")),
            associatedEvidenceId = "EVD-22",
            associatedSuspectId = "SUS-04",
            associatedLeadId = "LEAD_002_05_THE_SERVICE_STAIRWELL",
            actionLabel = "CONFRONT RENATA",
            actionTarget = LeadActionTarget.Suspect("SUS-04"),
            isMajorBreakthrough = true,
            priority = 50
        ),
        InvestigationMoment(
            id = "MOMENT_002_06_OPTIONAL_TITAN",
            title = "BEHIND THE ACQUISITION",
            subtitle = "Titan Capital's Key-Person Clause",
            narrativeText = "Titan Hospitality's acquisition agreement contained an explicit voiding clause: the buyout terminates if Elena Voss is not attached to the business for two years. Her death destroys Whit Sokol's deal.",
            type = InvestigationMomentType.NEW_DEVELOPMENT,
            triggerCondition = ObjectiveCondition.DiscoverEvidence(listOf("EVD-16", "EVD-17")),
            associatedEvidenceId = "EVD-16",
            associatedLeadId = "LEAD_002_OPTIONAL_TITAN_DEAL",
            actionLabel = "REVIEW DEAL RECORDS",
            actionTarget = LeadActionTarget.CaseFile(CaseFileTab.EVIDENCE),
            priority = 15
        )
    )

    val CONTRADICTIONS = listOf(
        Contradiction(
            id = "C001_DAVID_DEPARTURE",
            title = "David Ostrow's Fabricated 10:45 PM Departure",
            suspectId = "SUS-01",
            statementIds = listOf("ST_DO_1"),
            evidenceIds = listOf("EVD-13", "EVD-14"),
            conflictSummary = "David claimed he left at 10:45 PM. Security badge logs and a witness prove he re-entered Floor 30 at 11:05 PM and had a heated shouting match on the roof.",
            fullExplanation = "David Ostrow swore he left the building for good at 10:45 PM. However, elevator access card records (EVD-13) and busboy testimony (EVD-14) place him back on Floor 30 arguing furiously with Elena on the rooftop deck between 11:05 PM and 11:12 PM."
        ),
        Contradiction(
            id = "C002_THEO_INVENTORY",
            title = "Theo Marsh's Continuous Inventory Claim",
            suspectId = "SUS-02",
            statementIds = listOf("ST_TM_1"),
            evidenceIds = listOf("EVD-18"),
            conflictSummary = "Theo claimed he scanned inventory continuously from 11:00 to 11:45 PM. POS records show an unlogged 6-minute scan gap from 11:12 to 11:18 PM.",
            fullExplanation = "Theo Marsh insisted he never paused or left his inventory station in the kitchen. However, terminal logs (EVD-18) record a total scanning hiatus between 11:12 PM and 11:18 PM."
        ),
        Contradiction(
            id = "C003_PRIYA_RETURN",
            title = "Priya Nandan's Denied Return to Meridian",
            suspectId = "SUS-03",
            statementIds = listOf("ST_PN_1"),
            evidenceIds = listOf("EVD-11", "EVD-12"),
            conflictSummary = "Priya claimed she left at 10:30 PM and never set foot in the building again. Archival records reveal her father's wrongful conviction, and she returned at 11:15 PM.",
            fullExplanation = "Priya Nandan claimed she went straight home at 10:30 PM. However, the discovery of her family history tying her to Marcus Webb (EVD-12) and her napkin note (EVD-11) led to the revelation that she re-entered at 11:15 PM to confront Elena."
        ),
        Contradiction(
            id = "C004_WHIT_ALIBI",
            title = "Whit Sokol's False Room Alibi",
            suspectId = "SUS-06",
            statementIds = listOf("ST_WS_1"),
            evidenceIds = listOf("EVD-17"),
            conflictSummary = "Whit claimed he was in his hotel room asleep by 11:00 PM. Sensor records reveal the 10:47 PM tap was a keycard refresh and he returned to Floor 30 at 11:18 PM.",
            fullExplanation = "Whit Sokol claimed he remained in his room after 10:47 PM. Sensor diagnostics prove the keycard was merely reactivated at the door, and corridor cameras record him returning to Floor 30 at 11:18 PM after receiving David's text."
        ),
        Contradiction(
            id = "C005_RENATA_LOG_CONTRADICTION",
            title = "Renata Cole's Dual Logging System Contradiction",
            suspectId = "SUS-04",
            statementIds = listOf("ST_RC_1", "ST_RC_2"),
            evidenceIds = listOf("EVD-21", "EVD-22"),
            conflictSummary = "Renata claimed she remained in her office all night, supported by her edited main security logs. However, the standalone service stairwell keypad recorded her PIN at 11:26 PM and 11:52 PM.",
            fullExplanation = "Renata Cole engineered a false clearance on the main CCTV security server (EVD-21) to show zero rooftop activity after 10:20 PM. However, she was unaware that the camera-free interior service stairwell ran on an independent legacy keypad subsystem (EVD-22), which recorded her exclusive PIN entered at 11:26 PM (entering Floor 30 roof) and 11:52 PM (exiting Floor 29 cellar after staging the body)."
        )
    )

    val CONTRADICTION_CHALLENGES = listOf(
        ContradictionChallenge(
            id = "CHAL_C001",
            suspectId = "SUS-01",
            evidenceId = "EVD-13",
            contradictionId = "C001_DAVID_DEPARTURE",
            prompt = "David claims he left the building at 10:45 PM and never returned. What do the security badge records prove?",
            options = listOf(
                ChallengeOption(
                    key = "A",
                    text = "David used his keycard to swipe back into Floor 30 at 11:05 PM.",
                    isCorrect = true,
                    feedback = "Correct. Electronic badge records place David back on Floor 30 at 11:05 PM, directly disproving his claimed 10:45 PM departure."
                ),
                ChallengeOption(
                    key = "B",
                    text = "David called a rideshare vehicle at 10:45 PM.",
                    isCorrect = false,
                    feedback = "Rideshare records show a pickup at 11:19 PM, not 10:45 PM."
                ),
                ChallengeOption(
                    key = "C",
                    text = "David was seen drinking at the main bar all night.",
                    isCorrect = false,
                    feedback = "Witnesses place David arguing on the roof, not relaxing at the bar."
                ),
                ChallengeOption(
                    key = "D",
                    text = "David stole the reserve 1990 Bordeaux bottle.",
                    isCorrect = false,
                    feedback = "There is no evidence linking David to the cellar wine cage."
                )
            ),
            successFeedback = "David's alibi collapses. Badge records prove he swiped back into Floor 30 at 11:05 PM.",
            failurePrompt = "Focus on what the electronic badge records specifically prove about David's movements."
        ),
        ContradictionChallenge(
            id = "CHAL_C002",
            suspectId = "SUS-02",
            evidenceId = "EVD-18",
            contradictionId = "C002_THEO_INVENTORY",
            prompt = "Theo claims he was scanning inventory continuously from 11:00 PM to 11:45 PM. What does the POS terminal data reveal?",
            options = listOf(
                ChallengeOption(
                    key = "A",
                    text = "The POS terminal logged a continuous, unbroken sequence of scans.",
                    isCorrect = false,
                    feedback = "The data shows an unlogged gap, not continuous scanning."
                ),
                ChallengeOption(
                    key = "B",
                    text = "Theo completely stopped scanning inventory for 6 minutes between 11:12 PM and 11:18 PM.",
                    isCorrect = true,
                    feedback = "Correct. The 6-minute scan gap proves Theo was not scanning inventory continuously as claimed."
                ),
                ChallengeOption(
                    key = "C",
                    text = "Theo logged into the wine cellar inventory from Floor 30.",
                    isCorrect = false,
                    feedback = "The scan station was located in the kitchen on Floor 29."
                ),
                ChallengeOption(
                    key = "D",
                    text = "The POS terminal recorded Elena's badge swipe in the kitchen.",
                    isCorrect = false,
                    feedback = "Elena's badge was not recorded on the kitchen POS."
                )
            ),
            successFeedback = "Theo is forced to admit the 6-minute gap during his inventory work.",
            failurePrompt = "Look at the specific timestamp timestamps recorded by the inventory terminal."
        ),
        ContradictionChallenge(
            id = "CHAL_C003",
            suspectId = "SUS-03",
            evidenceId = "EVD-12",
            contradictionId = "C003_PRIYA_RETURN",
            prompt = "Priya insists she had no personal stake in Elena Voss and went straight home at 10:30 PM. What do the archival records prove?",
            options = listOf(
                ChallengeOption(
                    key = "A",
                    text = "Priya is the daughter of Marcus Webb, the man convicted for Elena's past embezzlement scheme.",
                    isCorrect = true,
                    feedback = "Correct. The records establish Priya's deep personal motive and explain why she returned to confront Elena."
                ),
                ChallengeOption(
                    key = "B",
                    text = "Priya was secretly employed as a mixologist at Meridian.",
                    isCorrect = false,
                    feedback = "Priya is a hospitality journalist, not a mixologist."
                ),
                ChallengeOption(
                    key = "C",
                    text = "Priya negotiated the Titan Hospitality acquisition contract.",
                    isCorrect = false,
                    feedback = "Whit Sokol negotiated the deal, not Priya."
                ),
                ChallengeOption(
                    key = "D",
                    text = "Priya stole the 1990 Bordeaux from the wine cellar.",
                    isCorrect = false,
                    feedback = "The missing bottle was part of the staged robbery."
                )
            ),
            successFeedback = "Priya's composure breaks as her true identity and motive are exposed.",
            failurePrompt = "Consider Priya's relationship to the historical embezzlement case."
        ),
        ContradictionChallenge(
            id = "CHAL_C004",
            suspectId = "SUS-06",
            evidenceId = "EVD-17",
            contradictionId = "C004_WHIT_ALIBI",
            prompt = "Whit claims he went to his room at 10:47 PM and was asleep by eleven. What does the hotel keycard analysis establish?",
            options = listOf(
                ChallengeOption(
                    key = "A",
                    text = "Whit's keycard was used to enter the Floor 29 wine cellar.",
                    isCorrect = false,
                    feedback = "Whit's keycard had no access permissions for the wine cellar."
                ),
                ChallengeOption(
                    key = "B",
                    text = "The 10:47 PM tap was only a keycard reactivation, and Whit returned to Floor 30 at 11:18 PM.",
                    isCorrect = true,
                    feedback = "Correct. Whit did not remain in his room; he returned to Floor 30 at 11:18 PM after David texted him."
                ),
                ChallengeOption(
                    key = "C",
                    text = "Whit never checked into the Aldwyn Hotel.",
                    isCorrect = false,
                    feedback = "Whit was staying as a guest in the hotel."
                ),
                ChallengeOption(
                    key = "D",
                    text = "Whit was seen arguing on the rooftop deck at 11:30 PM.",
                    isCorrect = false,
                    feedback = "Cameras show Whit remained near the bar and never went up to the roof."
                )
            ),
            successFeedback = "Whit admits returning to Floor 30 at 11:18 PM after receiving David's warning text.",
            failurePrompt = "Examine what the hotel room door log specifically recorded."
        ),
        ContradictionChallenge(
            id = "CHAL_C005",
            suspectId = "SUS-04",
            evidenceId = "EVD-22",
            contradictionId = "C005_RENATA_LOG_CONTRADICTION",
            prompt = "Renata claims she remained in her office all night and the main security logs show no roof access. What do the service stairwell subsystem logs prove?",
            options = listOf(
                ChallengeOption(
                    key = "A",
                    text = "The service stairwell was locked and unpowered all evening.",
                    isCorrect = false,
                    feedback = "The subsystem was active and recording code entries."
                ),
                ChallengeOption(
                    key = "B",
                    text = "Renata's exclusive PIN code '4491' was entered at the Floor 30 stairwell door at 11:26 PM and the Floor 29 cellar door at 11:52 PM.",
                    isCorrect = true,
                    feedback = "Correct! The unscrubbed standalone keypad recorded Renata entering the roof right before Elena died and exiting the wine cellar after staging the scene."
                ),
                ChallengeOption(
                    key = "C",
                    text = "David Ostrow used the service stairwell at 11:30 PM.",
                    isCorrect = false,
                    feedback = "David's PIN was not recorded; David had already departed by rideshare at 11:19 PM."
                ),
                ChallengeOption(
                    key = "D",
                    text = "Theo Marsh swiped his badge at the loading dock at 11:52 PM.",
                    isCorrect = false,
                    feedback = "The entry was logged on the service stairwell keypad by PIN 4491, belonging to Renata."
                )
            ),
            successFeedback = "The decisive contradiction is established! Renata's engineered security alibi completely shatters.",
            failurePrompt = "Examine the specific PIN codes and timestamps logged by the legacy keypad subsystem."
        )
    )

    val COMMUNICATION_THREADS = listOf(
        CommunicationThread(
            id = "thread_david_whit",
            suspectId = "SUS-01",
            title = "David Ostrow & Whit Sokol",
            contactInitials = "DO",
            contactColorHex = 0xFF5C6BC0,
            channelLabel = "Encrypted SMS / Signal",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_dw_1",
                    sender = "David Ostrow",
                    timestamp = "9:48 PM",
                    text = "Whit, make sure the side addendum guarantees my $1.2M equity regardless of Elena's operational transition clause.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_dw_2",
                    sender = "Whit Sokol",
                    timestamp = "9:52 PM",
                    text = "It's in the draft. Just make sure Elena signs the main acquisition agreement tonight.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_dw_3",
                    sender = "David Ostrow",
                    timestamp = "11:14 PM",
                    text = "She knows about our arrangement. The deal is dead. I'm leaving the hotel now.",
                    isFromVictim = false,
                    attachmentEvidenceId = "EVD-16"
                ),
                CommunicationMessage(
                    id = "msg_dw_4",
                    sender = "Whit Sokol",
                    timestamp = "11:16 PM",
                    text = "Are you insane?! I'm coming back down right now.",
                    isFromVictim = false
                )
            )
        ),
        CommunicationThread(
            id = "thread_iris_theo",
            suspectId = "SUS-05",
            title = "Iris Chen & Theo Marsh",
            contactInitials = "IC",
            contactColorHex = 0xFF4DB6AC,
            channelLabel = "Private Messaging",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_it_1",
                    sender = "Iris Chen",
                    timestamp = "10:35 PM",
                    text = "Theo, Elena is planning to sell and disappear again. She's going to leave me behind just like before.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_it_2",
                    sender = "Theo Marsh",
                    timestamp = "10:40 PM",
                    text = "Iris, don't do anything reckless. Titan promised me the GM spot. If she sells, we both have a future.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_it_3",
                    sender = "Iris Chen",
                    timestamp = "11:18 PM",
                    text = "I'm going up to the roof to talk to her face to face.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_it_4",
                    sender = "Iris Chen",
                    timestamp = "11:23 PM",
                    text = "She promised she wouldn't leave me. I'm heading back down to the kitchen now.",
                    isFromVictim = false,
                    attachmentEvidenceId = "EVD-25"
                )
            )
        ),
        CommunicationThread(
            id = "thread_elena_renata",
            suspectId = "SUS-04",
            title = "Elena Voss & Renata Cole",
            contactInitials = "RC",
            contactColorHex = 0xFFD32F2F,
            channelLabel = "Cellular Messages",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_er_1",
                    sender = "Elena Voss",
                    timestamp = "10:02 PM",
                    text = "Renata, meet me on the roof after everyone leaves. Last round, just the two of us. We need to talk about what comes next.",
                    isFromVictim = true
                ),
                CommunicationMessage(
                    id = "msg_er_2",
                    sender = "Renata Cole",
                    timestamp = "10:05 PM",
                    text = "I'll be there as soon as I finish locking the perimeter.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_er_3",
                    sender = "Elena Voss",
                    timestamp = "11:00 PM",
                    text = "I'm up on the deck with a bottle whenever you're ready.",
                    isFromVictim = true,
                    attachmentEvidenceId = "EVD-09"
                )
            )
        ),
        CommunicationThread(
            id = "thread_voice_memo",
            suspectId = null,
            title = "Elena's Phone Audio Memo",
            contactInitials = "EV",
            contactColorHex = 0xFFF59E0B,
            channelLabel = "Encrypted Voice Recording",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_vm_1",
                    sender = "Elena Voss",
                    timestamp = "11:27 PM",
                    text = "Voice Memo [00:32]: 'I'm telling Renata tonight. It's time to stop running. Marcus's daughter deserved the truth, and so does she. No more reinventions. I'm staying and making things right.'",
                    isFromVictim = true,
                    attachmentEvidenceId = "EVD-10"
                )
            )
        )
    )

    val CALL_LOGS: List<CallLogEntry> = listOf(
        CallLogEntry(
            id = "call_001",
            contactName = "David Ostrow -> Whit Sokol",
            direction = CallDirection.OUTGOING,
            timestamp = "11:14 PM",
            durationLabel = "38s",
            linkedEvidenceId = "EVD-16",
            isCritical = true
        ),
        CallLogEntry(
            id = "call_002",
            contactName = "Walk-in Cooler Alarm Line",
            direction = CallDirection.INCOMING,
            timestamp = "11:12 PM",
            durationLabel = "System Alert",
            linkedEvidenceId = "EVD-19",
            isCritical = false
        ),
        CallLogEntry(
            id = "call_003",
            contactName = "Hotel Security Console",
            direction = CallDirection.OUTGOING,
            timestamp = "10:20 PM",
            durationLabel = "Routine Log",
            linkedEvidenceId = "EVD-21",
            isCritical = false
        ),
        CallLogEntry(
            id = "call_004",
            contactName = "Elena Voss Voice Recording",
            direction = CallDirection.OUTGOING,
            timestamp = "11:27 PM",
            durationLabel = "32s",
            linkedEvidenceId = "EVD-10",
            isCritical = true
        )
    )

    val DEDUCTIONS = listOf(
        Deduction(
            id = "D001_STAGED_SCENE",
            title = "Wine Cellar is a Staged Crime Scene",
            reasoning = "Coroner's posterior lividity and stone residue in the head wound prove Elena did not die in the concrete cellar; the body was moved postmortem.",
            supportingEvidenceIds = listOf("EVD-01", "EVD-02", "EVD-05", "EVD-06"),
            requiredEvidence = listOf("EVD-01", "EVD-05")
        ),
        Deduction(
            id = "D002_ROOFTOP_TRUE_SCENE",
            title = "Rooftop Deck is the True Scene of Death",
            reasoning = "Luminol blood traces on the stone planter ledge and Elena's lost earring in the deck planks confirm Floor 30 as the scene of the fatal impact.",
            supportingEvidenceIds = listOf("EVD-06", "EVD-07", "EVD-08"),
            requiredEvidence = listOf("EVD-07", "EVD-08")
        ),
        Deduction(
            id = "D003_ALIBIS_CLEARED",
            title = "Alibis Clear David, Theo, Priya, Iris & Whit",
            reasoning = "GPS rideshare logs, refrigeration diagnostics, dock surveillance, and Elena's calm 11:27 PM voice memo clear all five suspects from the fatal window.",
            supportingEvidenceIds = listOf("EVD-10", "EVD-15", "EVD-19", "EVD-26"),
            requiredEvidence = listOf("EVD-10", "EVD-15", "EVD-19", "EVD-26")
        ),
        Deduction(
            id = "D004_DAVID_WHIT_SIDE_DEAL",
            title = "David and Whit's Secret Side Deal",
            reasoning = "Texts and badge logs reveal David and Whit were secretly negotiating an independent payout, explaining their lies and panic.",
            supportingEvidenceIds = listOf("EVD-13", "EVD-16", "EVD-17"),
            requiredEvidence = listOf("EVD-13", "EVD-16")
        ),
        Deduction(
            id = "D005_THEO_THEFT_EXPLAINED",
            title = "Theo's Discrepancy Was Unrelated Theft",
            reasoning = "Theo's missing minutes and panic were caused by register skimming for medical bills and fixing a cooler alarm, not violence.",
            supportingEvidenceIds = listOf("EVD-18", "EVD-19", "EVD-20"),
            requiredEvidence = listOf("EVD-19", "EVD-20")
        ),
        Deduction(
            id = "D006_PRIYA_RECONCILIATION",
            title = "Priya's Peaceful Reconciliation",
            reasoning = "Elena's 11:27 PM voice memo and dock exit camera prove Priya's confrontation ended peacefully with an apology before Elena died.",
            supportingEvidenceIds = listOf("EVD-10", "EVD-12", "EVD-26"),
            requiredEvidence = listOf("EVD-10", "EVD-26")
        ),
        Deduction(
            id = "D007_DUAL_SECURITY_SYSTEMS",
            title = "Manipulated Master Log vs. Keypad Subsystem",
            reasoning = "Renata edited the main security console to create a false alibi, but forgot that the camera-free service stairwell runs on a separate keypad subsystem.",
            supportingEvidenceIds = listOf("EVD-21", "EVD-22", "EVD-23"),
            requiredEvidence = listOf("EVD-21", "EVD-22", "EVD-23"),
            requiredContradictions = listOf("C005_RENATA_LOG_CONTRADICTION")
        ),
        Deduction(
            id = "D008_RENATA_SOLE_CULPRIT",
            title = "Renata Cole is Conclusively Responsible",
            reasoning = "With the rooftop true scene established, all other suspects cleared, dual keypad logs placing her at the death and transport windows, and her emotional tell, Renata Cole is the sole culprit.",
            supportingEvidenceIds = listOf("EVD-06", "EVD-07", "EVD-08", "EVD-21", "EVD-22", "EVD-23", "EVD-24"),
            requiredEvidence = listOf("EVD-08", "EVD-21", "EVD-22", "EVD-23"),
            requiredContradictions = listOf("C005_RENATA_LOG_CONTRADICTION")
        )
    )

    val CRIME_SCENE_HOTSPOTS = listOf(
        CrimeSceneHotspot(
            id = "hotspot_body",
            name = "Victim's Body & Broken Shelf",
            locationLabel = "Floor 29 — Wine Cellar",
            description = "Elena's body positioned on her side beside snapped display shelving.",
            xPercent = 0.45f,
            yPercent = 0.55f,
            primaryEvidenceId = "EVD-01",
            secondaryEvidenceId = "EVD-02"
        ),
        CrimeSceneHotspot(
            id = "hotspot_wine_case",
            name = "Reserve Wine Cage",
            locationLabel = "Floor 29 — Wine Cellar",
            description = "A locked vintage wine cage with a broken padlock and missing bottle.",
            xPercent = 0.70f,
            yPercent = 0.40f,
            primaryEvidenceId = "EVD-03"
        ),
        CrimeSceneHotspot(
            id = "hotspot_dock_door",
            name = "Loading Dock Service Door",
            locationLabel = "Floor 29 — Loading Dock",
            description = "Heavy metal service door leading outside, found unlatched and ajar.",
            xPercent = 0.20f,
            yPercent = 0.60f,
            primaryEvidenceId = "EVD-04",
            secondaryEvidenceId = "EVD-26"
        ),
        CrimeSceneHotspot(
            id = "hotspot_pos_terminal",
            name = "Kitchen POS & Inventory Station",
            locationLabel = "Floor 29 — Kitchen",
            description = "POS terminal and barcode scanning station with bottle inventory logs.",
            xPercent = 0.35f,
            yPercent = 0.35f,
            primaryEvidenceId = "EVD-18",
            secondaryEvidenceId = "EVD-20"
        ),
        CrimeSceneHotspot(
            id = "hotspot_cooler",
            name = "Walk-in Cooler Diagnostics",
            locationLabel = "Floor 29 — Kitchen",
            description = "Environmental sensor panel on the kitchen walk-in refrigeration unit.",
            xPercent = 0.80f,
            yPercent = 0.30f,
            primaryEvidenceId = "EVD-19"
        ),
        CrimeSceneHotspot(
            id = "hotspot_planter",
            name = "Rooftop Stone Planter Ledge",
            locationLabel = "Floor 30 — Rooftop Deck",
            description = "Architectural limestone planter with sharp lower ledge overlooking the city.",
            xPercent = 0.50f,
            yPercent = 0.45f,
            primaryEvidenceId = "EVD-08",
            secondaryEvidenceId = "EVD-06"
        ),
        CrimeSceneHotspot(
            id = "hotspot_decking",
            name = "Rooftop Wood Decking",
            locationLabel = "Floor 30 — Rooftop Deck",
            description = "Teak plank flooring adjacent to the stone planter ledge.",
            xPercent = 0.55f,
            yPercent = 0.65f,
            primaryEvidenceId = "EVD-07"
        ),
        CrimeSceneHotspot(
            id = "hotspot_nightcap_table",
            name = "Rooftop Table & Phone",
            locationLabel = "Floor 30 — Rooftop Deck",
            description = "A private corner cocktail table with an open wine bottle and smartphone.",
            xPercent = 0.25f,
            yPercent = 0.50f,
            primaryEvidenceId = "EVD-09",
            secondaryEvidenceId = "EVD-10"
        ),
        CrimeSceneHotspot(
            id = "hotspot_security_console",
            name = "Master Security Console",
            locationLabel = "Floor 28 — Security Office",
            description = "Multi-monitor CCTV console and building badge database server.",
            xPercent = 0.60f,
            yPercent = 0.35f,
            primaryEvidenceId = "EVD-21",
            secondaryEvidenceId = "EVD-13"
        ),
        CrimeSceneHotspot(
            id = "hotspot_renata_desk",
            name = "Manager's Desk & Safe",
            locationLabel = "Floor 28 — Security Office",
            description = "A locked administrative desk and personal floor safe.",
            xPercent = 0.40f,
            yPercent = 0.65f,
            primaryEvidenceId = "EVD-23",
            secondaryEvidenceId = "EVD-12"
        ),
        CrimeSceneHotspot(
            id = "hotspot_stairwell_keypad",
            name = "Service Stairwell Keypad Subsystem",
            locationLabel = "Floor 29/30 — Service Stairwell",
            description = "Standalone legacy access controller mounted beside the unmonitored stairwell door.",
            xPercent = 0.15f,
            yPercent = 0.45f,
            primaryEvidenceId = "EVD-22"
        )
    )

    override val evidenceList: List<EvidenceItem> get() = EVIDENCE_LIST
    override val suspects: List<Suspect> get() = SUSPECTS
    override val questions: List<InterviewQuestion> get() = QUESTIONS
    override val statements: List<StatementItem> get() = STATEMENTS
    override val reactions: List<EvidenceReaction> get() = EVIDENCE_REACTIONS
    override val timelineEvents: List<TimelineEvent> get() = TIMELINE_EVENTS
    override val objectives: List<Objective> get() = OBJECTIVES
    override val leads: List<InvestigationLead> get() = LEADS
    override val investigationMoments: List<InvestigationMoment> get() = INVESTIGATION_MOMENTS
    override val centralQuestion: String get() = "Investigate Elena Voss's death, uncover the staged cellar scene, and identify the true culprit on Floor 30."
    override val contradictions: List<Contradiction> get() = CONTRADICTIONS
    override val contradictionChallenges: List<ContradictionChallenge> get() = CONTRADICTION_CHALLENGES
    override val communicationThreads: List<CommunicationThread> get() = COMMUNICATION_THREADS
    override val callLogs: List<CallLogEntry> get() = CALL_LOGS
    override val deductions: List<Deduction> get() = DEDUCTIONS
    override val crimeSceneHotspots: List<CrimeSceneHotspot> get() = CRIME_SCENE_HOTSPOTS

    override val customDeductionMessages: Map<Pair<String, String>, String> = mapOf(
        Pair("EVD-21", "EVD-22") to "The main security log claims Renata never accessed the roof, but the standalone service stairwell keypad recorded her personal PIN twice."
    )

    override val culpritSolution: CulpritSolution = CulpritSolution(
        culpritSuspectId = "SUS-04",
        correctMotiveKey = "MOTIVE_HEARTBREAK",
        correctWeaponKey = "WEAPON_STONE_PLANTER",
        requiredContradictionIds = listOf("C005_RENATA_LOG_CONTRADICTION"),
        requiredMotiveEvidenceIds = listOf("EVD-23"),
        requiredTimeAnchorEvidenceIds = listOf("EVD-10", "EVD-22"),
        criticalEvidenceIds = listOf("EVD-06", "EVD-07", "EVD-08", "EVD-10", "EVD-21", "EVD-22", "EVD-23", "EVD-24"),
        clearedSuspectIdsForPerfect = listOf("SUS-01", "SUS-02", "SUS-03", "SUS-05", "SUS-06"),
        minEvidenceCountForPerfect = 20,
        prematureFeedbackTitle = "Suspicion is Not Proof",
        prematureFeedbackMessage = "You suspect the right person, but have not assembled the decisive dual-log contradiction, time anchor, and true rooftop crime scene evidence required for prosecution.",
        wrongSuspectFeedbackTemplate = "The established evidence does not support %s as the perpetrator. The investigation remains open.",
        solvedTitle = "Case Solved",
        perfectTitle = "Perfect Investigation",
        solvedFeedbackMessage = "Renata Cole's false alibi collapsed under the weight of the legacy service stairwell keypad logs, rooftop luminol forensics, and her intimate relationship with the victim.",
        culpritSummaryHeader = "RENATA COLE",
        culpritSummaryDetails = "Motive: Desperation and heartbreak over Elena's planned disappearance.\nFatal Weapon: Fatal blunt force cranial trauma against the rooftop stone planter ledge.",
        decisiveContradictionSummary = "Renata Cole claimed she remained in her 28th-floor office all night and edited the master security console to show zero rooftop activity. However, she was unaware that the camera-free interior service stairwell ran on an independent legacy keypad subsystem, which logged her exclusive PIN (4491) entered at 11:26 PM (arriving on the roof before the fall) and 11:52 PM (exiting the cellar after staging the body).",
        chronologicalReconstructionSteps = listOf(
            ChronologicalStep("8:00 PM", "Farewell tasting event begins at Meridian on Floor 30 with regulars and business counterparts."),
            ChronologicalStep("8:45 PM", "Elena toasts the room, hinting at major changes and seeding the sale rumor."),
            ChronologicalStep("9:10 PM", "Priya privately confronts Elena near the wine wall, revealing she knows Elena's former name (Elena Kowalski)."),
            ChronologicalStep("9:45 PM", "David and Whit discuss their secret side deal in a quiet corner. Renata overhears a fragment in passing."),
            ChronologicalStep("10:00 PM", "Elena tells Renata she's killing the sale and seriously considering disappearing again. Renata is devastated."),
            ChronologicalStep("10:20 PM", "Renata logs a security round, then manipulates the master console to appear 'in office' for the rest of the night."),
            ChronologicalStep("10:30 PM", "Priya publicly says goodbye and exits the venue."),
            ChronologicalStep("10:45 PM", "Whit heads to the elevators; David claims to leave."),
            ChronologicalStep("10:50 PM", "Elena confronts Theo about weekly cash register shortfalls; Theo deflects."),
            ChronologicalStep("11:00 PM", "Elena goes up to the rooftop deck with a bottle for her planned 'last round' with Renata."),
            ChronologicalStep("11:05 PM", "David swipes back onto Floor 30 and argues with Elena on the roof over the collapsing deal; Elena refuses to budge."),
            ChronologicalStep("11:14 PM", "David texts Whit that the deal is dead and exits the building, taking a rideshare at 11:19 PM."),
            ChronologicalStep("11:15 PM", "Priya re-enters through a side door and heads toward the roof."),
            ChronologicalStep("11:18 PM", "Whit comes back down to Floor 30, spots Priya near the stairs, and retreats to the bar."),
            ChronologicalStep("11:20 PM", "Iris slips up to the roof to confront Elena about abandonment; Elena comforts her and Iris returns to the kitchen."),
            ChronologicalStep("11:22 PM", "Priya reaches the roof; Elena tearfully apologizes and promises to publicly clear Marcus Webb's name."),
            ChronologicalStep("11:27 PM", "Priya exits via the dock camera; Elena records a peaceful voice memo reflecting on the reconciliation."),
            ChronologicalStep("11:28 PM", "Renata enters the service stairwell with PIN 4491 and arrives on the rooftop deck for their planned drink."),
            ChronologicalStep("11:31 PM", "Elena reveals she still intends to leave everything behind. Renata grabs her arm to stop her; Elena wrenches free, trips, and strikes her head on the stone planter ledge."),
            ChronologicalStep("11:32 PM", "Elena dies from the cranial trauma within minutes. Renata is alone on the roof in total panic."),
            ChronologicalStep("11:35 PM", "Renata carries the body down the camera-free service stairwell to Floor 29, breaks a shelf, and removes a 1990 Bordeaux to stage a robbery."),
            ChronologicalStep("11:52 PM", "Renata uses PIN 4491 to exit the service stairwell onto Floor 29, unaware the legacy controller logged her code."),
            ChronologicalStep("12:05 AM", "Renata returns to her office to edit the master CCTV and badge logs for the rooftop doors."),
            ChronologicalStep("12:10 AM", "Theo goes down to the wine cellar for the reserve Bordeaux and discovers Elena's body.")
        )
    )

    override fun getEvidence(id: String): EvidenceItem? = EVIDENCE_LIST.find { it.id == id }
    override fun getSuspect(id: String): Suspect? = SUSPECTS.find { it.id == id }
    override fun getQuestion(id: String): InterviewQuestion? = QUESTIONS.find { it.id == id }
    override fun getStatement(id: String): StatementItem? = STATEMENTS.find { it.id == id }
    override fun getTimelineEvent(id: String): TimelineEvent? = TIMELINE_EVENTS.find { it.id == id }
    fun getObjective(id: String): Objective? = OBJECTIVES.find { it.id == id }
    override fun getContradiction(id: String): Contradiction? = CONTRADICTIONS.find { it.id == id }
    override fun getDeduction(id: String): Deduction? = DEDUCTIONS.find { it.id == id }

    override fun checkContradictionPair(source1Id: String, source2Id: String): Contradiction? {
        val ids = setOf(source1Id.trim(), source2Id.trim())
        // C001: David (ST_DO_1 + EVD-13 or EVD-14)
        if (ids.contains("ST_DO_1") && (ids.contains("EVD-13") || ids.contains("EVD-14"))) {
            return CONTRADICTIONS.find { it.id == "C001_DAVID_DEPARTURE" }
        }
        // C002: Theo (ST_TM_1 + EVD-18)
        if (ids.contains("ST_TM_1") && ids.contains("EVD-18")) {
            return CONTRADICTIONS.find { it.id == "C002_THEO_INVENTORY" }
        }
        // C003: Priya (ST_PN_1 + EVD-11 or EVD-12)
        if (ids.contains("ST_PN_1") && (ids.contains("EVD-11") || ids.contains("EVD-12"))) {
            return CONTRADICTIONS.find { it.id == "C003_PRIYA_RETURN" }
        }
        // C004: Whit (ST_WS_1 + EVD-17)
        if (ids.contains("ST_WS_1") && ids.contains("EVD-17")) {
            return CONTRADICTIONS.find { it.id == "C004_WHIT_ALIBI" }
        }
        // C005: Renata (ST_RC_1 or ST_RC_2 or EVD-21 + EVD-22)
        if ((ids.contains("ST_RC_1") || ids.contains("ST_RC_2") || ids.contains("EVD-21")) && ids.contains("EVD-22")) {
            return CONTRADICTIONS.find { it.id == "C005_RENATA_LOG_CONTRADICTION" }
        }
        return null
    }

    override fun checkDeductionPair(
        source1Id: String,
        source2Id: String,
        relationship: ReasoningRelationship
    ): Pair<Deduction?, String?> {
        val ids = setOf(source1Id.trim(), source2Id.trim())

        // D001: Staged Wine Cellar (EVD-05 + EVD-06 or EVD-01 + EVD-05 with ESTABLISHES/SUPPORTS/CONTRADICTS)
        if ((ids.contains("EVD-05") && ids.contains("EVD-06")) || (ids.contains("EVD-01") && ids.contains("EVD-05"))) {
            return Pair(getDeduction("D001_STAGED_SCENE"), "Posterior lividity and stone residue in the cranial wound prove the victim was moved to the cellar postmortem.")
        }

        // D002: Rooftop True Scene (EVD-07 + EVD-08 with ESTABLISHES/SUPPORTS/CONNECTS)
        if (ids.contains("EVD-07") && ids.contains("EVD-08")) {
            return Pair(getDeduction("D002_ROOFTOP_TRUE_SCENE"), "Luminol bloodstains and Elena's lost earring confirm the rooftop stone planter as the true scene of the fatal impact.")
        }

        // D003: Alibis Cleared (EVD-10 + EVD-15 or EVD-19 or EVD-26 with ESTABLISHES/SUPPORTS/DISPROVES)
        if (ids.contains("EVD-10") && (ids.contains("EVD-15") || ids.contains("EVD-19") || ids.contains("EVD-26"))) {
            return Pair(getDeduction("D003_ALIBIS_CLEARED"), "Independent GPS logs, cooler diagnostics, and Elena's 11:27 PM voice memo clear David, Theo, Priya, and Iris.")
        }

        // D004: David & Whit Side Deal (EVD-13 + EVD-16 with CONNECTS/ESTABLISHES)
        if (ids.contains("EVD-13") && ids.contains("EVD-16")) {
            return Pair(getDeduction("D004_DAVID_WHIT_SIDE_DEAL"), "Texts and badge logs reveal David and Whit were secretly negotiating an independent payout.")
        }

        // D005: Theo's Discrepancy Was Unrelated Theft (EVD-19 + EVD-20 with CONNECTS/ESTABLISHES)
        if (ids.contains("EVD-19") && ids.contains("EVD-20")) {
            return Pair(getDeduction("D005_THEO_THEFT_EXPLAINED"), "Register shortfalls and cooler diagnostics explain Theo's panic as an unrelated theft rather than violence.")
        }

        // D006: Priya's Peaceful Reconciliation (EVD-10 + EVD-26 with ESTABLISHES/SUPPORTS)
        if (ids.contains("EVD-10") && ids.contains("EVD-26")) {
            return Pair(getDeduction("D006_PRIYA_RECONCILIATION"), "Elena's 11:27 PM voice memo and loading dock surveillance prove Priya departed peacefully before the fatal window.")
        }

        // D007: Dual Security Systems (EVD-21 + EVD-22 or EVD-22 + EVD-23 with CONTRADICTS/ESTABLISHES)
        if ((ids.contains("EVD-21") && ids.contains("EVD-22")) || (ids.contains("EVD-22") && ids.contains("EVD-23"))) {
            return Pair(getDeduction("D007_DUAL_SECURITY_SYSTEMS"), "The edited master security log is directly contradicted by the standalone service stairwell keypad subsystem.")
        }

        // D008: Renata Sole Culprit (EVD-22 + EVD-08 or EVD-22 + EVD-24 with ESTABLISHES/SUPPORTS)
        if (ids.contains("EVD-22") && (ids.contains("EVD-08") || ids.contains("EVD-24") || ids.contains("SUS-04"))) {
            return Pair(getDeduction("D008_RENATA_SOLE_CULPRIT"), "With all others cleared, dual keypad logs placing her at the roof and cellar, and her composure breaking, Renata Cole is conclusively responsible.")
        }

        // Custom connection messages
        val custom = customDeductionMessages[Pair(source1Id.trim(), source2Id.trim())]
            ?: customDeductionMessages[Pair(source2Id.trim(), source1Id.trim())]
        return Pair(null, custom)
    }
}
