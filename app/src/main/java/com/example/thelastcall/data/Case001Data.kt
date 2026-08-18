package com.example.thelastcall.data

object Case001Data : CaseDefinition {
    const val CASE_ID = "CASE-001"
    const val CASE_TITLE = "Last Call"
    const val VICTIM_NAME = "Elias Voss"
    const val VICTIM_AGE = 42
    const val VICTIM_OCCUPATION = "Investigative Journalist"
    const val CASE_LOCATION = "Apartment 7B, Bellweather Heights"
    const val INCIDENT_TIME = "Approximately 10:50 PM - 11:05 PM"

    override val id: String = CASE_ID
    override val title: String = CASE_TITLE
    override val subtitle: String = "Homicide Investigation"
    override val incidentDate: String = "October 14"
    override val incidentTime: String = INCIDENT_TIME
    override val location: String = CASE_LOCATION
    override val victimName: String = VICTIM_NAME
    override val victimAge: Int = VICTIM_AGE
    override val victimOccupation: String = VICTIM_OCCUPATION
    override val briefingSummary: String get() = BRIEFING_SUMMARY
    override val primaryObjectiveText: String = "Identify the person responsible for Elias Voss's death and substantiate the charge with decisive evidence."
    override val initialDialogueText: String = "Detective, we have a clean scene on the surface, but Elias Voss made a phone call minutes before he was struck. That call is our anchor."

    override val introFacts: List<IntroFact> = listOf(
        IntroFact("Victim: Elias Voss", "42-year-old investigative journalist discovered deceased inside penthouse study.", "person", 0xFFD32F2F),
        IntroFact("Time Anchor: 10:42 PM", "Victim placed a verified outgoing call, establishing he was alive minutes before the incident.", "call", 0xFFF59E0B),
        IntroFact("Scene: Undisturbed Entry", "No signs of forced entry. Victim admitted the visitor voluntarily.", "lock", 0xFF06B6D4)
    )

    override val motives: List<CaseMotiveOption> = listOf(
        CaseMotiveOption("MOTIVE_FINANCIAL", "Prevent exposure of multi-million dollar embezzlement & fraud"),
        CaseMotiveOption("MOTIVE_FAMILY", "Bitter inheritance and personal estrangement dispute"),
        CaseMotiveOption("MOTIVE_CORPORATE", "Silencing corporate exposé threatening pending company acquisition"),
        CaseMotiveOption("MOTIVE_BLACKMAIL", "Retaliation over leaked personal research dossier")
    )

    override val weapons: List<CaseWeaponOption> = listOf(
        CaseWeaponOption("WEAPON_PAPERWEIGHT", "Heavy Cast-Iron Desk Paperweight (Blunt Force Trauma)"),
        CaseWeaponOption("WEAPON_GLASS", "Shattered Whiskey Glass Fragment (Laceration)"),
        CaseWeaponOption("WEAPON_STRANGULATION", "Telephone Cord / Physical Ligature"),
        CaseWeaponOption("WEAPON_FALL", "Accidental Fall During Altercation")
    )

    override val initialUnlockedTimelineIds: Set<String> = setOf("T001", "T005", "T006")

    val BRIEFING_SUMMARY = """
        At approximately 11:05 PM, Elias Voss, a 42-year-old investigative journalist, was found dead inside his apartment. 
        There are no signs of forced entry. Elias was known for pursuing aggressive exposés into financial corruption.
        
        Four people connected to him were seen or in contact with him this evening. Each has given a statement, but one made sure Elias would never publish again.
        
        Shortly before his death, Elias placed a final phone call at 10:42 PM. That call anchors the timeline.
    """.trimIndent()

    val EVIDENCE_LIST = listOf(
        EvidenceItem(
            id = "E001",
            name = "Victim's Phone",
            category = EvidenceCategory.DIGITAL,
            location = "Victim's Desk",
            discoveryCondition = "Found during initial desk examination",
            playerDescription = "Elias's smartphone is lying beside a stack of research papers on the desk. The screen is locked, but a recent outgoing call notification is visible.",
            detailedInvestigation = "The phone was active shortly before Elias's death. Examining the call log reveals a critical timestamp.",
            significanceText = "Leads to discovering the exact time Elias placed his last outgoing call.",
            relatedSuspects = listOf("S004"),
            relatedTimelineEvents = listOf("T009"),
            relatedEvidence = listOf("E002", "E003"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "phone",
            unlocksEvidenceOnInspect = listOf("E002", "E003")
        ),
        EvidenceItem(
            id = "E002",
            name = "Last Outgoing Call",
            category = EvidenceCategory.DIGITAL,
            location = "Victim's Phone",
            discoveryCondition = "Discovered by inspecting Elias's phone",
            playerDescription = "The phone records show a final outgoing call placed at 10:42 PM. The call connected and lasted long enough to establish direct communication.",
            detailedInvestigation = "This call proves conclusively that Elias Voss was alive and conscious at 10:42 PM, much later than initial estimates.",
            significanceText = "Anchors the entire timeline. Any suspect claiming they left before 10:00 PM and that Elias died right after is contradicted by this time anchor.",
            relatedSuspects = listOf("S004", "S001", "S002", "S003"),
            relatedTimelineEvents = listOf("T009"),
            relatedEvidence = listOf("E001", "E014", "E018", "E020"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            discoverableInitially = false,
            iconType = "call"
        ),
        EvidenceItem(
            id = "E003",
            name = "Phone Contact Metadata",
            category = EvidenceCategory.DIGITAL,
            location = "Victim's Phone",
            discoveryCondition = "Inspecting phone call details",
            playerDescription = "The 10:42 PM call was placed to a secure research contact line Elias used when verifying high-stakes financial data.",
            detailedInvestigation = "Metadata confirms Elias was attempting to deliver urgent confirmation about his financial investigation.",
            significanceText = "Shows Elias was in the midst of finalizing his exposé just minutes before the confrontation.",
            relatedSuspects = listOf("S004", "S002"),
            relatedTimelineEvents = listOf("T009"),
            relatedEvidence = listOf("E002", "E019"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "digital"
        ),
        EvidenceItem(
            id = "E004",
            name = "Desk Document",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Victim's Desk",
            discoveryCondition = "Found on Elias's desk",
            playerDescription = "Several printed financial sheets and company profiles cover the desk, with handwritten annotations and margin notes by Elias.",
            detailedInvestigation = "The records examine suspicious capital movements and hidden consulting kickbacks.",
            significanceText = "Establishes that Elias was actively working on an exposé targeting financial fraud.",
            relatedSuspects = listOf("S004", "S002", "S003"),
            relatedEvidence = listOf("E005", "E011", "E019"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "document"
        ),
        EvidenceItem(
            id = "E005",
            name = "Missing Financial File",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Victim's Desk Organizer",
            discoveryCondition = "Inspecting the organized file folders on the desk",
            playerDescription = "A labeled folder tab marked 'Mercer & Associated Accounts' is empty. The primary evidentiary dossier has been removed.",
            detailedInvestigation = "Someone deliberately took the specific file during or immediately following the confrontation.",
            significanceText = "Points directly to an assailant who needed to conceal incriminating financial records.",
            relatedSuspects = listOf("S004"),
            relatedEvidence = listOf("E004", "E019"),
            importance = EvidenceImportance.IMPORTANT,
            discoverableInitially = false,
            iconType = "folder"
        ),
        EvidenceItem(
            id = "E006",
            name = "Desk Fingerprint",
            category = EvidenceCategory.PHYSICAL,
            location = "Polished Edge of Desk",
            discoveryCondition = "Inspecting the corner of the victim's desk",
            playerDescription = "A clear partial fingerprint lifted from the mahogany desk surface matches Daniel Mercer.",
            detailedInvestigation = "Daniel admits he visited earlier that night, so the fingerprint alone does not prove he stayed. However, its position near the struggle area becomes crucial when combined with timeline evidence.",
            significanceText = "Physically links Daniel to the desk area where the confrontation took place.",
            relatedSuspects = listOf("S004"),
            relatedTimelineEvents = listOf("T005", "T007"),
            relatedEvidence = listOf("E014", "E018", "E020"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            iconType = "fingerprint"
        ),
        EvidenceItem(
            id = "E007",
            name = "Broken Glass",
            category = EvidenceCategory.PHYSICAL,
            location = "Living Room Rug",
            discoveryCondition = "Examining the floor near the center table",
            playerDescription = "A heavy drinking glass lies shattered across the floor. Liquid residue indicates it was knocked over during a sudden, violent movement.",
            detailedInvestigation = "The shatter pattern indicates a physical struggle erupted near the desk and spilled into the living area.",
            significanceText = "Establishes a violent physical confrontation took place.",
            relatedTimelineEvents = listOf("T008"),
            relatedEvidence = listOf("E008"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "glass"
        ),
        EvidenceItem(
            id = "E008",
            name = "Heavy Paperweight",
            category = EvidenceCategory.PHYSICAL,
            location = "Living Room Floor",
            discoveryCondition = "Inspecting the area beside the shattered glass",
            playerDescription = "A solid cast-iron decorative paperweight lies on the carpet. Traces of impact and microscopic fibers match the trauma found on Elias.",
            detailedInvestigation = "This object was taken from Elias's desk and used as an improvised weapon during the altercation.",
            significanceText = "Identified as the weapon used to incapacitate Elias.",
            relatedTimelineEvents = listOf("T008"),
            relatedEvidence = listOf("E006", "E007"),
            importance = EvidenceImportance.IMPORTANT,
            iconType = "weapon"
        ),
        EvidenceItem(
            id = "E009",
            name = "Apartment Door",
            category = EvidenceCategory.ENVIRONMENTAL,
            location = "Main Entrance",
            discoveryCondition = "Examining the front door and lock mechanism",
            playerDescription = "The deadbolt and frame show zero signs of tampering, picking, or forced entry. The door was unlocked from the inside.",
            detailedInvestigation = "Elias opened the door willingly to someone he recognized and felt comfortable letting inside.",
            significanceText = "Narrows the perpetrator to someone Elias knew personally.",
            relatedSuspects = listOf("S001", "S002", "S003", "S004"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "door"
        ),
        EvidenceItem(
            id = "E010",
            name = "Window",
            category = EvidenceCategory.ENVIRONMENTAL,
            location = "Living Room Window",
            discoveryCondition = "Inspecting the exterior window",
            playerDescription = "The window is latched loosely. The fire escape outside shows undisturbed dust and rain patterns from earlier in the evening.",
            detailedInvestigation = "No one entered or escaped via the window. It served purely as ventilation.",
            significanceText = "Contextual observation ruling out external break-ins.",
            importance = EvidenceImportance.CONTEXT,
            iconType = "window"
        ),
        EvidenceItem(
            id = "E011",
            name = "Maya's Statement",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Maya Voss Interview",
            discoveryCondition = "Recorded during Maya's interview",
            playerDescription = "Maya admits she argued loudly with Elias about abandoning his dangerous investigation, but insists she left around 9:05 PM and never returned.",
            detailedInvestigation = "Her emotional argument makes her seem suspicious initially, but her departure time must be tested against independent evidence.",
            significanceText = "Establishes Maya's claimed timeline (left ~9:05 PM).",
            relatedSuspects = listOf("S001"),
            relatedTimelineEvents = listOf("T001", "T002"),
            relatedEvidence = listOf("E017"),
            importance = EvidenceImportance.IMPORTANT,
            iconType = "statement"
        ),
        EvidenceItem(
            id = "E012",
            name = "Victor's Statement",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Victor Hale Interview",
            discoveryCondition = "Recorded during Victor's interview",
            playerDescription = "Victor admits he was furious about Elias investigating his development firm and threatened legal action, but denies ever visiting the apartment.",
            detailedInvestigation = "Victor had a clear motive, but claims an alibi during the crucial hours.",
            significanceText = "Establishes Victor's business motive and claimed absence.",
            relatedSuspects = listOf("S002"),
            relatedTimelineEvents = listOf("T003"),
            relatedEvidence = listOf("E015"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "statement"
        ),
        EvidenceItem(
            id = "E013",
            name = "Nora's Statement",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Nora Bennett Interview",
            discoveryCondition = "Recorded during Nora's interview",
            playerDescription = "Nora states she assisted Elias with filing research earlier in the afternoon, left before evening, and stayed at home all night.",
            detailedInvestigation = "Her nervous demeanor hints at a secret, but her physical presence at the apartment is unconfirmed.",
            significanceText = "Establishes Nora's assistant role and alibi.",
            relatedSuspects = listOf("S003"),
            relatedTimelineEvents = listOf("T004"),
            relatedEvidence = listOf("E016"),
            importance = EvidenceImportance.RELEVANT,
            iconType = "statement"
        ),
        EvidenceItem(
            id = "E014",
            name = "Daniel's Initial Statement",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Daniel Mercer Interview",
            discoveryCondition = "Recorded during Daniel's initial interview",
            playerDescription = "Daniel admits he visited Elias around 9:40 PM to discuss the investigation. He firmly claims he left before 10:00 PM, went home, and NEVER returned.",
            detailedInvestigation = "This strict statement is the core of Daniel's defense. Proving he returned later will shatter his entire alibi.",
            significanceText = "The critical lie. Daniel commits to having left before 10:00 PM and never returning.",
            relatedSuspects = listOf("S004"),
            relatedTimelineEvents = listOf("T005", "T006"),
            relatedEvidence = listOf("E002", "E006", "E018", "E020"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            iconType = "statement"
        ),
        EvidenceItem(
            id = "E015",
            name = "Victor's Dinner Record",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Restaurant Billing Archive",
            discoveryCondition = "Unlocked after questioning Victor and verifying his alibi",
            playerDescription = "Official digital reservation timestamp and credit card billing records place Victor Hale at a private business dinner from 8:45 PM until 11:15 PM.",
            detailedInvestigation = "Independent restaurant staff and timestamped receipts verify Victor never left the establishment during the critical window.",
            significanceText = "Conclusively clears Victor Hale of physical presence at the crime scene.",
            relatedSuspects = listOf("S002"),
            relatedTimelineEvents = listOf("T003"),
            importance = EvidenceImportance.IMPORTANT,
            iconType = "receipt"
        ),
        EvidenceItem(
            id = "E016",
            name = "Nora's Digital Activity",
            category = EvidenceCategory.DIGITAL,
            location = "ISP Activity Log",
            discoveryCondition = "Unlocked after questioning Nora about her evening",
            playerDescription = "Continuous cloud workspace activity, document edits, and home Wi-Fi session logs place Nora Bennett at her personal residence throughout 9:00 PM to 11:30 PM.",
            detailedInvestigation = "While Nora admits she secretly duplicated research files out of fear, she never traveled to Elias's apartment.",
            significanceText = "Conclusively clears Nora Bennett of committing the murder.",
            relatedSuspects = listOf("S003"),
            relatedTimelineEvents = listOf("T004"),
            importance = EvidenceImportance.IMPORTANT,
            iconType = "laptop"
        ),
        EvidenceItem(
            id = "E017",
            name = "Maya's Transportation Record",
            category = EvidenceCategory.DIGITAL,
            location = "Transit / Rideshare Records",
            discoveryCondition = "Unlocked after questioning Maya regarding her departure",
            playerDescription = "A rideshare receipt with verified GPS pickup at 9:07 PM and drop-off at Maya's residence at 9:34 PM confirms her departure.",
            detailedInvestigation = "Maya did not return to the apartment area that night.",
            significanceText = "Conclusively clears Maya Voss from the murder timeframe.",
            relatedSuspects = listOf("S001"),
            relatedTimelineEvents = listOf("T002"),
            importance = EvidenceImportance.IMPORTANT,
            iconType = "transit"
        ),
        EvidenceItem(
            id = "E018",
            name = "Daniel's Return Evidence",
            category = EvidenceCategory.PHYSICAL,
            location = "Building Access Log & Foyer Camera",
            discoveryCondition = "Discovered by cross-referencing building keyfob logs and apartment surveillance",
            playerDescription = "Building entry logs and hallway motion sensors record Daniel Mercer swiping back into the apartment corridor at 10:20 PM and departing hastily at 10:45 PM.",
            detailedInvestigation = "This irrefutable evidence places Daniel inside Elias's apartment between 10:20 PM and 10:45 PM, directly spanning Elias's 10:42 PM call and the confrontation.",
            significanceText = "Directly shatters Daniel's claim that he left before 10:00 PM and never returned. The key to the case.",
            relatedSuspects = listOf("S004"),
            relatedTimelineEvents = listOf("T007", "T008", "T010"),
            relatedEvidence = listOf("E014", "E002", "E006", "E020"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            iconType = "access_card"
        ),
        EvidenceItem(
            id = "E019",
            name = "Financial Investigation File",
            category = EvidenceCategory.DOCUMENTARY,
            location = "Elias's Hidden Research Vault",
            discoveryCondition = "Discovered by examining Elias's locked drawer research notes",
            playerDescription = "Uncovered banking transactions and off-shore wire transfers prove Daniel Mercer personally orchestrated the embezzlement scheme Elias was about to publish.",
            detailedInvestigation = "Daniel faced complete professional destruction, criminal indictment, and millions in liabilities if Elias went public.",
            significanceText = "Establishes Daniel's motive: silencing Elias to protect himself from criminal exposure.",
            relatedSuspects = listOf("S004"),
            relatedEvidence = listOf("E004", "E005", "E018"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            iconType = "vault"
        ),
        EvidenceItem(
            id = "E020",
            name = "Daniel's Final Contradiction",
            category = EvidenceCategory.TESTIMONIAL,
            location = "Case Deduction Board",
            discoveryCondition = "Formed by presenting Daniel's Return Evidence against his initial timeline statement",
            playerDescription = "Daniel claimed: 'I left before 10 PM and never returned.' Evidence proves: Elias was alive at 10:42 PM, and Daniel was back inside from 10:20 PM to 10:45 PM with motive to destroy the file.",
            detailedInvestigation = "Daniel's alibi is an outright fabrication. He had motive, opportunity, and weapon access during the fatal minutes.",
            significanceText = "The definitive proof solving Case 001.",
            relatedSuspects = listOf("S004"),
            relatedTimelineEvents = listOf("T007", "T008", "T009", "T010"),
            relatedEvidence = listOf("E002", "E006", "E014", "E018", "E019"),
            importance = EvidenceImportance.CRITICAL,
            isCritical = true,
            iconType = "verdict"
        )
    )

    val SUSPECTS = listOf(
        Suspect(
            id = "S001",
            name = "Maya Voss",
            age = 37,
            occupation = "Architect",
            relationship = "Victim's Sister",
            publicStory = "Visited Elias to convince him to stop his dangerous investigation. Admits they had a heated argument, but says she left shortly after 9:00 PM.",
            hiddenTruth = "She was worried Elias's exposé might implicate contacts at her architectural firm, which made her plead with him. But she went straight home after 9:05 PM.",
            personalityDescription = "Intelligent, emotional, protective, and direct. Genuinely grieving her brother despite their past disagreements.",
            initialAlibiSummary = "Claims she left Elias's apartment around 9:05 PM and took a rideshare home.",
            avatarColorHex = 0xFF5C6BC0,
            initials = "MV"
        ),
        Suspect(
            id = "S002",
            name = "Victor Hale",
            age = 51,
            occupation = "Property Developer",
            relationship = "Subject of Investigation",
            publicStory = "Acknowledges Elias was harassing his development company with groundless corruption claims, but denies visiting the apartment.",
            hiddenTruth = "Threatened Elias with aggressive litigation earlier that week, but was dining at an upscale restaurant throughout the entire night of the incident.",
            personalityDescription = "Authoritative, wealthy, polished, and impatient. Accustomed to commanding rooms and using lawyers rather than violence.",
            initialAlibiSummary = "Claims he was dining at an executive private dinner all evening.",
            avatarColorHex = 0xFF78909C,
            initials = "VH"
        ),
        Suspect(
            id = "S003",
            name = "Nora Bennett",
            age = 29,
            occupation = "Research Assistant",
            relationship = "Professional Assistant",
            publicStory = "Worked with Elias on organizing paperwork during the afternoon and left before evening. Claims she knew little about his final conclusions.",
            hiddenTruth = "Secretly made duplicates of Elias's research files out of fear the investigation would collapse, but stayed at home working on her laptop all night.",
            personalityDescription = "Detail-oriented, observant, nervous, and quick-spoken under pressure. Terrified of being entangled in the scandal.",
            initialAlibiSummary = "Claims she stayed home working on digital research all evening.",
            avatarColorHex = 0xFF8D6E63,
            initials = "NB"
        ),
        Suspect(
            id = "S004",
            name = "Daniel Mercer",
            age = 39,
            occupation = "Financial Consultant",
            relationship = "Former Associate",
            publicStory = "Met Elias earlier that evening around 9:40 PM to discuss the financial files. Claims they argued and he left before 10:00 PM, never returning.",
            hiddenTruth = "Realized Elias had uncovered his multi-million embezzlement. Returned at 10:20 PM to force Elias to hand over the dossiers, struck him with the paperweight when Elias refused, stole the main file, and fled at 10:45 PM.",
            personalityDescription = "Calm, calculated, persuasive, and image-conscious. Speaks with measured precision and deflects scrutiny with professional poise.",
            initialAlibiSummary = "Insists he left before 10:00 PM and went straight home.",
            avatarColorHex = 0xFFD32F2F,
            initials = "DM"
        )
    )

    val QUESTIONS = listOf(
        // Maya Questions
        InterviewQuestion("Q_MAYA_1", "S001", "What were you and Elias arguing about tonight?", "Elias was obsessed with this investigation. He wouldn't stop, no matter who warned him. I told him he was putting himself in genuine danger and pleaded with him to drop it.", recordedStatementId = "ST001", statementSummary = "Maya argued with Elias over his dangerous investigation.", unlocksEvidenceIds = listOf("E011")),
        InterviewQuestion("Q_MAYA_2", "S001", "Were you angry enough to hurt him?", "Angry? Yes, of course I was angry! He was risking everything. But he was my brother. I wanted to protect him, not hurt him.", statementSummary = "Maya denies wishing any physical harm on her brother."),
        InterviewQuestion("Q_MAYA_3", "S001", "What exact time did you leave the apartment?", "It was a little after nine. Around 9:05 PM. I remember glancing at my phone as I hailed a ride on the street outside.", statementSummary = "Maya states she left the apartment at approximately 9:05 PM.", unlocksEvidenceIds = listOf("E017")),
        InterviewQuestion("Q_MAYA_4", "S001", "Did you return to the apartment at any point?", "No! I went straight home, locked my door, and tried to calm down. I never went back.", recordedStatementId = "ST002", statementSummary = "Maya insists she never returned to the apartment."),
        InterviewQuestion("Q_MAYA_5", "S001", "Did Elias mention who he considered his greatest threat?", "He had people who despised his work. Victor Hale, for one. And Daniel Mercer. Daniel was constantly checking in on how much Elias had discovered.", statementSummary = "Maya points to Victor Hale and Daniel Mercer as people who feared Elias's work."),
        InterviewQuestion("Q_MAYA_6", "S001", "Did you know what financial misconduct Elias was uncovering?", "I knew it involved dirty money moving through real estate networks. I was terrified people associated with my architectural clients might be involved, which is why I panicked.", requiredEvidenceId = "E019", statementSummary = "Maya admits she feared her professional network was connected to the fraud."),

        // Victor Questions
        InterviewQuestion("Q_VIC_1", "S002", "What was your relationship with Elias Voss?", "He was an insufferable nuisance. He spent months digging through my company's transactions, convinced he would find a grand conspiracy where there was only standard corporate restructuring.", recordedStatementId = "ST003", statementSummary = "Victor acknowledges Elias was investigating his company.", unlocksEvidenceIds = listOf("E012")),
        InterviewQuestion("Q_VIC_2", "S002", "Did you threaten Elias before tonight?", "I informed him through my legal counsel that slanderous publications would be met with aggressive lawsuits. That is called protecting a business, Detective, not a murder threat.", statementSummary = "Victor admits threatening legal action against Elias."),
        InterviewQuestion("Q_VIC_3", "S002", "Did you visit Elias's apartment tonight?", "Certainly not. I have far better uses of my time than visiting rundown apartments in Bellweather Heights.", statementSummary = "Victor denies visiting Elias's apartment."),
        InterviewQuestion("Q_VIC_4", "S002", "Where were you between 9:00 PM and 11:00 PM?", "I was hosting an executive dinner at Le Petit Sommet with four corporate partners and the restaurant manager. My reservation and credit card records will confirm that immediately.", statementSummary = "Victor claims an airtight dinner alibi.", unlocksEvidenceIds = listOf("E015")),
        InterviewQuestion("Q_VIC_5", "S002", "What can you tell me about Daniel Mercer?", "Daniel was Elias's financial advisor on several audits. He understood Elias's investigative methods better than anyone. If anyone had access to Elias's findings, it was Daniel.", statementSummary = "Victor notes that Daniel Mercer had intimate knowledge of Elias's work."),

        // Nora Questions
        InterviewQuestion("Q_NORA_1", "S003", "What was your role assisting Elias?", "Research and archiving. I cross-referenced banking records, organized physical document binders, and checked public filings.", recordedStatementId = "ST004", statementSummary = "Nora describes her research assistant duties.", unlocksEvidenceIds = listOf("E013")),
        InterviewQuestion("Q_NORA_2", "S003", "When did you last see Elias in person?", "Earlier this afternoon, around 4:30 PM. I left before evening set in and went straight to my flat.", statementSummary = "Nora says she left before evening."),
        InterviewQuestion("Q_NORA_3", "S003", "Did you return to the apartment tonight?", "No, not at all! I stayed at my desk working on digital research until late.", statementSummary = "Nora denies returning to the apartment.", unlocksEvidenceIds = listOf("E016")),
        InterviewQuestion("Q_NORA_4", "S003", "Did you take or copy any of Elias's investigation dossiers?", "I... I made digital backups of some files. I was terrified that if someone came after Elias, all our work would vanish or I'd be blamed. But I never went back to his apartment tonight!", recordedStatementId = "ST005", requiredEvidenceId = "E004", statementSummary = "Nora admits copying files for security, but denies returning."),
        InterviewQuestion("Q_NORA_5", "S003", "Who understood the financial records best?", "Daniel Mercer. Elias showed Daniel everything because Daniel was helping him interpret the offshore transaction sheets.", statementSummary = "Nora confirms Daniel had full access to the financial findings."),

        // Daniel Questions
        InterviewQuestion("Q_DAN_1", "S004", "When did you last see Elias Voss?", "Earlier tonight. I went over to his apartment around 9:40 PM because he wanted to discuss some figures from the investigation.", recordedStatementId = "ST008", statementSummary = "Daniel admits visiting Elias at 9:40 PM.", unlocksEvidenceIds = listOf("E014")),
        InterviewQuestion("Q_DAN_2", "S004", "Did you and Elias have an argument?", "We had a disagreement, yes. Elias had become paranoid. He started making reckless accusations that people in my consulting circle were complicit in fraud.", statementSummary = "Daniel admits they argued over fraud accusations."),
        InterviewQuestion("Q_DAN_3", "S004", "What time did you leave Elias's apartment?", "I left before 10:00 PM. Around 9:50 PM. I didn't see the point in yelling in circles with an unreasonable man.", recordedStatementId = "ST006", statementSummary = "Daniel claims he left before 10:00 PM.", unlocksEvidenceIds = listOf("E018")),
        InterviewQuestion("Q_DAN_4", "S004", "Did you return to the apartment after 10:00 PM?", "No. Absolutely not. I went straight home and stayed there. I had no further contact with Elias.", recordedStatementId = "ST007", statementSummary = "Daniel insists he never returned.", unlocksEvidenceIds = listOf("E018")),
        InterviewQuestion("Q_DAN_5", "S004", "Why was the 'Mercer Accounts' file missing from Elias's desk?", "I have no idea what Elias kept in his organizers. If a file is missing, you should ask whoever ransacked his desk.", requiredEvidenceId = "E005", statementSummary = "Daniel denies knowing about the missing file."),
        InterviewQuestion("Q_DAN_6", "S004", "Surveillance shows you returned to the building at 10:20 PM. Explain that.", "That's... that's impossible. Your camera footage must be timestamped incorrectly or capturing someone else.", requiredEvidenceId = "E018", statementSummary = "Daniel is visibly shaken when confronted with return footage."),
        InterviewQuestion("Q_DAN_7", "S004", "Elias made a call at 10:42 PM while you were inside. What happened?", "...", requiredEvidenceId = "E020", statementSummary = "Daniel falls silent as his timeline collapses.")
    )

    val EVIDENCE_REACTIONS = listOf(
        // Maya reactions
        EvidenceReaction(
            suspectId = "S001",
            evidenceId = "E017",
            detectivePrompt = "This rideshare transit log confirms you were picked up at 9:07 PM and arrived home by 9:34 PM.",
            suspectResponse = "Yes! That is exactly what I told you. I left right after our argument and never came back.",
            clearsSuspectCriticalPeriod = true,
            unlocksEvidenceIds = listOf("E017")
        ),
        EvidenceReaction(
            suspectId = "S001",
            evidenceId = "E019",
            detectivePrompt = "Elias's files show he was investigating massive financial fraud involving consulting firms.",
            suspectResponse = "I knew it was dangerous. I begged him to stop before someone desperate decided to silence him.",
            unlocksQuestionIds = listOf("Q_MAYA_6")
        ),

        // Victor reactions
        EvidenceReaction(
            suspectId = "S002",
            evidenceId = "E015",
            detectivePrompt = "The restaurant receipts and manager statements verify your presence at dinner from 8:45 to 11:15 PM.",
            suspectResponse = "As I stated from the outset. I am a businessman, Detective. When I have conflicts, I retain attorneys. I don't prowl apartment hallways.",
            clearsSuspectCriticalPeriod = true,
            unlocksEvidenceIds = listOf("E015")
        ),
        EvidenceReaction(
            suspectId = "S002",
            evidenceId = "E019",
            detectivePrompt = "Elias's dossier uncovered systemic corruption, but the primary culprit was a consulting intermediary.",
            suspectResponse = "Daniel Mercer handled those third-party audits. If Elias was murdered over those books, look at the consultant who had everything to lose."
        ),

        // Nora reactions
        EvidenceReaction(
            suspectId = "S003",
            evidenceId = "E016",
            detectivePrompt = "Your digital activity logs confirm you were logged in from your home network all evening.",
            suspectResponse = "Thank goodness... I was terrified you wouldn't believe me. I really did stay home all night.",
            clearsSuspectCriticalPeriod = true,
            unlocksEvidenceIds = listOf("E016")
        ),
        EvidenceReaction(
            suspectId = "S003",
            evidenceId = "E004",
            detectivePrompt = "I found research documents on Elias's desk matching copies you organized.",
            suspectResponse = "I... I admit I kept duplicate copies of the spreadsheets because Elias was getting paranoid. But I swear I didn't steal anything from his desk tonight.",
            unlocksQuestionIds = listOf("Q_NORA_4")
        ),

        // Daniel reactions
        EvidenceReaction(
            suspectId = "S004",
            evidenceId = "E006",
            detectivePrompt = "Your fingerprint was identified on the edge of Elias's desk near the struggle area.",
            suspectResponse = "I already told you, Detective—I was there at 9:40 PM discussing paperwork. Of course my print is on the desk. That proves nothing about what happened later."
        ),
        EvidenceReaction(
            suspectId = "S004",
            evidenceId = "E002",
            detectivePrompt = "Elias placed his last outgoing phone call at 10:42 PM, proving he was alive long after you claimed he was alone.",
            suspectResponse = "Then whoever called or visited him at 10:42 PM is your killer. I was already miles away at home."
        ),
        EvidenceReaction(
            suspectId = "S004",
            evidenceId = "E019",
            detectivePrompt = "Elias's hidden files reveal you embezzled millions through the shell companies he was about to expose.",
            suspectResponse = "Those are unproven allegations and accounting discrepancies! Elias had no right to publish that slander!",
            triggersMotiveId = "MOTIVE_FINANCIAL"
        ),
        EvidenceReaction(
            suspectId = "S004",
            evidenceId = "E018",
            detectivePrompt = "Building keycard logs and corridor surveillance place you entering at 10:20 PM and fleeing at 10:45 PM.",
            suspectResponse = "What?! That's impossible... I... there must be a mistake with the system clock!",
            isContradiction = true,
            triggersContradictionId = "C001",
            unlocksEvidenceIds = listOf("E020"),
            unlocksQuestionIds = listOf("Q_DAN_6", "Q_DAN_7")
        )
    )

    val TIMELINE_EVENTS = listOf(
        TimelineEvent("T001", "8:40 PM", "Maya Argues with Elias", "Maya Voss visits Elias and confronts him about stopping his investigation.", sourceEvidenceId = "E011", relatedSuspectId = "S001"),
        TimelineEvent("T002", "9:05 PM", "Maya Departs", "Maya leaves the apartment. Verified by transit records.", sourceEvidenceId = "E017", relatedSuspectId = "S001", requiredEvidenceForUnlock = listOf("E017")),
        TimelineEvent("T003", "9:25 PM", "Victor at Dinner", "Victor Hale attends executive dinner across town. Verified by billing.", sourceEvidenceId = "E015", relatedSuspectId = "S002", requiredEvidenceForUnlock = listOf("E015")),
        TimelineEvent("T004", "9:30 PM", "Nora at Home", "Nora Bennett is active on her home network. Verified by ISP logs.", sourceEvidenceId = "E016", relatedSuspectId = "S003", requiredEvidenceForUnlock = listOf("E016")),
        TimelineEvent("T005", "9:40 PM", "Daniel's First Arrival", "Daniel Mercer arrives at Elias's apartment for initial discussion.", sourceEvidenceId = "E006", relatedSuspectId = "S004"),
        TimelineEvent("T006", "9:50 PM", "Daniel's Claimed Departure", "Daniel claims he left the apartment permanently before 10:00 PM.", sourceEvidenceId = "E014", relatedSuspectId = "S004"),
        TimelineEvent("T007", "10:20 PM", "Daniel Returns to Apartment", "Daniel Mercer returns to the building foyer and enters Elias's apartment.", sourceEvidenceId = "E018", relatedSuspectId = "S004", requiredEvidenceForUnlock = listOf("E018")),
        TimelineEvent("T008", "10:35 PM", "Physical Confrontation", "A violent altercation breaks out; glass shatters and paperweight is used.", sourceEvidenceId = "E007", requiredEvidenceForUnlock = listOf("E007", "E008")),
        TimelineEvent("T009", "10:42 PM", "Elias's Last Outgoing Call", "Elias places his final confirmed call from his smartphone.", sourceEvidenceId = "E002", requiredEvidenceForUnlock = listOf("E002")),
        TimelineEvent("T010", "10:45 PM", "Daniel Flees with Dossier", "Daniel Mercer leaves the building carrying the stolen financial dossier.", sourceEvidenceId = "E018", relatedSuspectId = "S004", requiredEvidenceForUnlock = listOf("E018"))
    )

    val OBJECTIVES = listOf(
        Objective("O001", "Investigate the Apartment", "Search the crime scene for physical clues, documents, and Elias's phone.", condition = ObjectiveCondition.DiscoverEvidence(listOf("E001", "E004", "E007", "E008")), leadActionLabel = "Investigate Scene", leadTarget = Screen.CRIME_SCENE, focusTab = CaseFileTab.EVIDENCE),
        Objective("O002", "Identify Connected Suspects", "Identify the four key individuals who interacted with Elias Voss tonight.", condition = ObjectiveCondition.InterviewSuspects(listOf("S001", "S002", "S003", "S004")), leadActionLabel = "Review People", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.SUSPECTS),
        Objective("O003", "Verify Suspect Statements", "Interrogate the suspects and examine their alibis and explanations.", condition = ObjectiveCondition.AskQuestions(listOf("Q_MAYA_1", "Q_VIC_1", "Q_NORA_1", "Q_DAN_1"))),
        Objective("O004", "Reconstruct the Timeline", "Establish when each person arrived, departed, and where they were during the critical window.", condition = ObjectiveCondition.DiscoverEvidence(listOf("E002", "E015", "E016", "E017", "E018")), leadActionLabel = "Open Timeline", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.TIMELINE),
        Objective("O005", "Uncover Financial Investigation", "Discover the hidden financial evidence Elias was preparing to expose.", condition = ObjectiveCondition.DiscoverEvidence(listOf("E004", "E005", "E019")), leadActionLabel = "Review Evidence", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.EVIDENCE),
        Objective("O006", "Identify Critical Contradictions", "Expose the false statement in Daniel Mercer's timeline using hard evidence.", condition = ObjectiveCondition.UnlockContradictions(listOf("C001")), leadActionLabel = "Open Board", leadTarget = Screen.DETECTIVE_BOARD, focusTab = CaseFileTab.DEDUCTIONS),
        Objective("O007", "Establish Motive & Opportunity", "Connect the financial fraud motive and physical presence to the culprit.", condition = ObjectiveCondition.DiscoveredMotiveAndOpportunity(), leadActionLabel = "Build Theory", leadTarget = Screen.CASE_FILE, focusTab = CaseFileTab.THEORY),
        Objective("O008", "Make the Final Accusation", "Formulate the accusation against the true culprit with supporting proof.", condition = ObjectiveCondition.CaseSolved, leadActionLabel = "Submit Accusation", leadTarget = Screen.FINAL_CASE_REVIEW, focusTab = CaseFileTab.THEORY)
    )

    val STATEMENTS = listOf(
        StatementItem(
            id = "ST001",
            suspectId = "S001",
            statementText = "Elias was obsessed with this investigation. He wouldn't stop, no matter who warned him. I told him he was putting himself in genuine danger and pleaded with him to drop it.",
            summary = "Maya argued with Elias over his dangerous investigation.",
            sourceContext = "Maya Voss — Initial Questioning",
            timestamp = "8:40 PM - 9:05 PM",
            relatedEvidenceIds = listOf("E011", "E019")
        ),
        StatementItem(
            id = "ST002",
            suspectId = "S001",
            statementText = "No! I went straight home, locked my door, and tried to calm down. I never went back.",
            summary = "Maya insists she never returned to the apartment.",
            sourceContext = "Maya Voss — Alibi Statement",
            timestamp = "9:05 PM",
            relatedEvidenceIds = listOf("E017")
        ),
        StatementItem(
            id = "ST003",
            suspectId = "S002",
            statementText = "He was an insufferable nuisance. He spent months digging through my company's transactions, convinced he would find a grand conspiracy where there was only standard corporate restructuring.",
            summary = "Victor acknowledges Elias was investigating his company.",
            sourceContext = "Victor Hale — Deposition",
            timestamp = "9:25 PM",
            relatedEvidenceIds = listOf("E012", "E015")
        ),
        StatementItem(
            id = "ST004",
            suspectId = "S003",
            statementText = "Research and archiving. I cross-referenced banking records, organized physical document binders, and checked public filings.",
            summary = "Nora describes her research assistant duties.",
            sourceContext = "Nora Bennett — Interview",
            timestamp = "4:30 PM",
            relatedEvidenceIds = listOf("E004", "E013")
        ),
        StatementItem(
            id = "ST005",
            suspectId = "S003",
            statementText = "I... I made digital backups of some files. I was terrified that if someone came after Elias, all our work would vanish or I'd be blamed. But I never went back to his apartment tonight!",
            summary = "Nora admits copying files for security, but denies returning.",
            sourceContext = "Nora Bennett — Document Inquiry",
            timestamp = "9:30 PM",
            relatedEvidenceIds = listOf("E004", "E016")
        ),
        StatementItem(
            id = "ST006",
            suspectId = "S004",
            statementText = "I left before 10:00 PM. Around 9:50 PM. I didn't see the point in yelling in circles with an unreasonable man.",
            summary = "Daniel claims he left before 10:00 PM.",
            sourceContext = "Daniel Mercer — Timeline Statement",
            timestamp = "9:50 PM",
            relatedEvidenceIds = listOf("E014", "E018", "E002"),
            contradictionId = "C001"
        ),
        StatementItem(
            id = "ST007",
            suspectId = "S004",
            statementText = "No. Absolutely not. I went straight home and stayed there. I had no further contact with Elias.",
            summary = "Daniel insists he never returned to the apartment.",
            sourceContext = "Daniel Mercer — Alibi Statement",
            timestamp = "After 10:00 PM",
            relatedEvidenceIds = listOf("E014", "E018", "E002"),
            contradictionId = "C001"
        ),
        StatementItem(
            id = "ST008",
            suspectId = "S004",
            statementText = "Earlier tonight. I went over to his apartment around 9:40 PM because he wanted to discuss some figures from the investigation.",
            summary = "Daniel admits visiting Elias at 9:40 PM.",
            sourceContext = "Daniel Mercer — Initial Inquiry",
            timestamp = "9:40 PM",
            relatedEvidenceIds = listOf("E006", "E014")
        )
    )

    val CONTRADICTIONS = listOf(
        Contradiction(
            id = "C001",
            title = "Daniel Mercer's Fabricated Departure",
            suspectId = "S004",
            statementIds = listOf("ST006", "ST007"),
            evidenceIds = listOf("E018", "E002"),
            conflictSummary = "Daniel stated he left before 10:00 PM and never returned. Building access logs prove he returned at 10:20 PM and stayed until 10:45 PM while Elias was alive.",
            fullExplanation = "Daniel Mercer insisted under questioning that he left Elias's apartment at 9:50 PM and had no further contact. However, building keyfob logs and corridor surveillance (E018) place him inside the apartment from 10:20 PM to 10:45 PM. Elias's outgoing call at 10:42 PM (E002) confirms Elias was alive during Daniel's unacknowledged second visit."
        )
    )

    val CONTRADICTION_CHALLENGES = listOf(
        ContradictionChallenge(
            id = "CHAL_C001",
            suspectId = "S004",
            evidenceId = "E018",
            contradictionId = "C001",
            prompt = "Daniel insists he left before 10:00 PM and never came back. What do the keycard logs and corridor surveillance establish?",
            options = listOf(
                ChallengeOption(
                    key = "A",
                    text = "Daniel re-entered the apartment at 10:20 PM — after he claimed to have left for good.",
                    isCorrect = true,
                    feedback = "Correct. Building access logs place Daniel inside the apartment from 10:20 PM to 10:45 PM, directly contradicting his claim that he left before 10:00 PM and never returned."
                ),
                ChallengeOption(
                    key = "B",
                    text = "Daniel first arrived at the apartment at 9:40 PM.",
                    isCorrect = false,
                    feedback = "True — but Daniel admitted to that first visit. It does not contradict his claim that he left before 10:00 PM and stayed away."
                ),
                ChallengeOption(
                    key = "C",
                    text = "Elias placed his final outgoing call at 10:42 PM.",
                    isCorrect = false,
                    feedback = "Elias being alive at 10:42 PM is significant, but on its own it does not disprove Daniel's claim about leaving before 10:00 PM."
                ),
                ChallengeOption(
                    key = "D",
                    text = "Maya Voss departed the building at 9:05 PM.",
                    isCorrect = false,
                    feedback = "Maya's departure is verified by transit records — but it is unrelated to Daniel's claimed timeline."
                )
            ),
            successFeedback = "Daniel's claim collapses. Keycard logs place him inside the apartment at 10:20 PM — after he swore he had left for the night.",
            failurePrompt = "That detail does not contradict what Daniel told you. Focus on what the access records specifically prove about his timeline."
        )
    )

    val COMMUNICATION_THREADS = listOf(
        CommunicationThread(
            id = "thread_daniel",
            suspectId = "S004",
            title = "Daniel Mercer",
            contactInitials = "DM",
            contactColorHex = 0xFFD32F2F,
            channelLabel = "Encrypted SMS / Signal",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_d_1",
                    sender = "Daniel Mercer",
                    timestamp = "9:15 PM",
                    text = "Elias, we need to talk about the audit draft before you send anything to the editorial board. I'm heading over.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_d_2",
                    sender = "Elias Voss",
                    timestamp = "9:22 PM",
                    text = "The numbers speak for themselves, Daniel. If you have an explanation for the offshore routing, bring documentation.",
                    isFromVictim = true
                ),
                CommunicationMessage(
                    id = "msg_d_3",
                    sender = "Daniel Mercer",
                    timestamp = "9:38 PM",
                    text = "I'm downstairs at the foyer now. Buzz me up.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_d_4",
                    sender = "Daniel Mercer",
                    timestamp = "10:14 PM",
                    text = "You're making a catastrophic mistake, Elias. Think about what you're destroying.",
                    isFromVictim = false
                )
            )
        ),
        CommunicationThread(
            id = "thread_maya",
            suspectId = "S001",
            title = "Maya Voss",
            contactInitials = "MV",
            contactColorHex = 0xFF5C6BC0,
            channelLabel = "Cellular SMS",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_m_1",
                    sender = "Maya Voss",
                    timestamp = "8:15 PM",
                    text = "Elias, please answer my call. People are talking about your investigation and it's dangerous. I'm coming over.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_m_2",
                    sender = "Maya Voss",
                    timestamp = "9:12 PM",
                    text = "I can't watch you throw your life away for a headline. I took a cab home. Lock your door.",
                    isFromVictim = false
                ),
                CommunicationMessage(
                    id = "msg_m_3",
                    sender = "Maya Voss",
                    timestamp = "9:35 PM",
                    text = "I'm home now. Please just sleep on this and call me tomorrow morning.",
                    isFromVictim = false
                )
            )
        ),
        CommunicationThread(
            id = "thread_research",
            suspectId = null,
            title = "Secure Research Contact",
            contactInitials = "RC",
            contactColorHex = 0xFF38BDF8,
            channelLabel = "Encrypted Anchor Link",
            messages = listOf(
                CommunicationMessage(
                    id = "msg_r_1",
                    sender = "Elias Voss",
                    timestamp = "10:42 PM",
                    text = "Outgoing call connected (1m 48s) — Transmitting verified embezzlement spreadsheets and account ledgers.",
                    isFromVictim = true,
                    attachmentEvidenceId = "E002"
                )
            )
        )
    )

    val CALL_LOGS: List<CallLogEntry> = listOf(
        CallLogEntry(
            id = "call_001",
            contactName = "Secure Research Contact",
            direction = CallDirection.OUTGOING,
            timestamp = "10:42 PM",
            durationLabel = "1m 48s",
            linkedEvidenceId = "E002",
            isCritical = true
        ),
        CallLogEntry(
            id = "call_002",
            contactName = "Daniel Mercer",
            direction = CallDirection.INCOMING,
            timestamp = "9:38 PM",
            durationLabel = "42s",
            linkedEvidenceId = "E001",
            isCritical = false
        ),
        CallLogEntry(
            id = "call_003",
            contactName = "Maya Voss",
            direction = CallDirection.MISSED,
            timestamp = "8:32 PM",
            durationLabel = null,
            linkedEvidenceId = "E001",
            isCritical = false
        ),
        CallLogEntry(
            id = "call_004",
            contactName = "Nora Bennett",
            direction = CallDirection.OUTGOING,
            timestamp = "4:28 PM",
            durationLabel = "3m 12s",
            linkedEvidenceId = "E001",
            isCritical = false
        )
    )

    val DEDUCTIONS = listOf(
        Deduction("D001", "Elias Was Alive at 10:42 PM", "Phone records establish Elias placed a call at 10:42 PM, placing the murder window between 10:42 PM and 10:55 PM.", listOf("E001", "E002"), requiredEvidence = listOf("E002")),
        Deduction("D002", "Alibis Clear Maya, Victor & Nora", "Transit records, dinner billing, and ISP activity eliminate Maya, Victor, and Nora from the critical period.", listOf("E015", "E016", "E017"), requiredEvidence = listOf("E015", "E016", "E017")),
        Deduction("D003", "No Forced Entry", "Elias opened the door willingly, proving the attacker was someone he recognized and trusted enough to let inside.", listOf("E009"), requiredEvidence = listOf("E009")),
        Deduction("D004", "Daniel Mercer Lied About Departure", "Daniel claims he left before 10 PM, but access evidence places him back at 10:20 PM during the murder window.", listOf("E014", "E018"), requiredEvidence = listOf("E014", "E018"), requiredContradictions = listOf("C001")),
        Deduction("D005", "Financial Embezzlement Motive", "Daniel Mercer was directly implicated in the fraud scheme Elias was finalizing, giving him an urgent motive to silence Elias.", listOf("E005", "E019"), requiredEvidence = listOf("E019")),
        Deduction("D006", "Physical Struggle & Weapon", "The shattered glass and heavy paperweight indicate an escalated altercation where Daniel struck Elias with a desk object.", listOf("E007", "E008", "E006"), requiredEvidence = listOf("E007", "E008")),
        Deduction("D007", "The Missing File Was Stolen", "Daniel took the 'Mercer Accounts' dossier from the desk after the assault to destroy evidence connecting him to the crime.", listOf("E005", "E019"), requiredEvidence = listOf("E005", "E019")),
        Deduction("D008", "Daniel Mercer is the Sole Culprit", "With motive, opportunity, weapon access, and a collapsed false alibi, Daniel Mercer is conclusively responsible for Elias Voss's death.", listOf("E002", "E006", "E018", "E019", "E020"), requiredEvidence = listOf("E002", "E018", "E019"), requiredContradictions = listOf("C001"))
    )

    val CRIME_SCENE_HOTSPOTS = listOf(
        CrimeSceneHotspot(
            id = "hotspot_phone",
            name = "Victim's Smartphone",
            locationLabel = "Desk Edge",
            description = "Elias's smartphone rests on the desk with screen notifications.",
            xPercent = 0.62f,
            yPercent = 0.38f,
            primaryEvidenceId = "E001",
            secondaryEvidenceId = "E002"
        ),
        CrimeSceneHotspot(
            id = "hotspot_desk",
            name = "Investigation Desk & Papers",
            locationLabel = "Main Study Desk",
            description = "A mahogany desk scattered with financial spreadsheets and investigative notes.",
            xPercent = 0.45f,
            yPercent = 0.42f,
            primaryEvidenceId = "E004",
            secondaryEvidenceId = "E006"
        ),
        CrimeSceneHotspot(
            id = "hotspot_organizer",
            name = "File Cabinet & Drawers",
            locationLabel = "Beside Desk",
            description = "An open metal file organizer with categorized dossier tabs.",
            xPercent = 0.30f,
            yPercent = 0.35f,
            primaryEvidenceId = "E005",
            secondaryEvidenceId = "E019",
            requiredEvidenceForSecondary = "E004"
        ),
        CrimeSceneHotspot(
            id = "hotspot_glass",
            name = "Shattered Glass",
            locationLabel = "Living Room Carpet",
            description = "A heavy tumbler shattered across the floor amid liquid spills.",
            xPercent = 0.70f,
            yPercent = 0.65f,
            primaryEvidenceId = "E007"
        ),
        CrimeSceneHotspot(
            id = "hotspot_paperweight",
            name = "Cast-Iron Paperweight",
            locationLabel = "Floor Near Desk",
            description = "A heavy decorative metal paperweight resting unnaturally on the carpet.",
            xPercent = 0.52f,
            yPercent = 0.60f,
            primaryEvidenceId = "E008"
        ),
        CrimeSceneHotspot(
            id = "hotspot_door",
            name = "Apartment Entrance Door",
            locationLabel = "Foyer",
            description = "The main wooden door with an undisturbed deadbolt lock.",
            xPercent = 0.15f,
            yPercent = 0.55f,
            primaryEvidenceId = "E009"
        ),
        CrimeSceneHotspot(
            id = "hotspot_window",
            name = "Living Room Window",
            locationLabel = "Rear Wall",
            description = "An unlocked window looking out toward the city alleyway.",
            xPercent = 0.85f,
            yPercent = 0.30f,
            primaryEvidenceId = "E010"
        )
    )

    override val evidenceList: List<EvidenceItem> get() = EVIDENCE_LIST
    override val suspects: List<Suspect> get() = SUSPECTS
    override val questions: List<InterviewQuestion> get() = QUESTIONS
    override val statements: List<StatementItem> get() = STATEMENTS
    override val reactions: List<EvidenceReaction> get() = EVIDENCE_REACTIONS
    override val timelineEvents: List<TimelineEvent> get() = TIMELINE_EVENTS
    override val objectives: List<Objective> get() = OBJECTIVES
    override val contradictions: List<Contradiction> get() = CONTRADICTIONS
    override val contradictionChallenges: List<ContradictionChallenge> get() = CONTRADICTION_CHALLENGES
    override val communicationThreads: List<CommunicationThread> get() = COMMUNICATION_THREADS
    override val callLogs: List<CallLogEntry> get() = CALL_LOGS
    override val deductions: List<Deduction> get() = DEDUCTIONS
    override val crimeSceneHotspots: List<CrimeSceneHotspot> get() = CRIME_SCENE_HOTSPOTS

    override val customDeductionMessages: Map<Pair<String, String>, String> = mapOf(
        Pair("E006", "E018") to "Daniel's desk fingerprint physically connects his presence to the crime scene during his unacknowledged return."
    )

    override val culpritSolution: CulpritSolution = CulpritSolution(
        culpritSuspectId = "S004",
        correctMotiveKey = "MOTIVE_FINANCIAL",
        correctWeaponKey = "WEAPON_PAPERWEIGHT",
        requiredContradictionIds = listOf("C001"),
        requiredMotiveEvidenceIds = listOf("E019"),
        requiredTimeAnchorEvidenceIds = listOf("E002", "E018"),
        criticalEvidenceIds = listOf("E002", "E006", "E014", "E018", "E019", "E020"),
        clearedSuspectIdsForPerfect = listOf("S001", "S002", "S003"),
        minEvidenceCountForPerfect = 16,
        prematureFeedbackTitle = "Suspicion is Not Proof",
        prematureFeedbackMessage = "You suspect the right person, but have not assembled the decisive contradiction, time anchor, and motive required for prosecution.",
        wrongSuspectFeedbackTemplate = "The established evidence does not support %s as the perpetrator. The investigation remains open.",
        solvedTitle = "Case Solved",
        perfectTitle = "Perfect Investigation",
        solvedFeedbackMessage = "Daniel Mercer's false alibi collapsed under the weight of keycard logs, timestamps, and financial fraud dossiers.",
        culpritSummaryHeader = "DANIEL MERCER",
        culpritSummaryDetails = "Motive: Prevent exposure of multi-million embezzlement and offshore shell accounts.\nFatal Weapon: Heavy cast-iron desk paperweight.",
        decisiveContradictionSummary = "Daniel Mercer claimed he departed for good at 9:50 PM and went straight to sleep. However, building keycard logs and security camera footage proved he returned at 10:20 PM. Elias placed his desperate outgoing call at 10:42 PM during their struggle, placing Daniel squarely at the crime scene at the moment of death.",
        chronologicalReconstructionSteps = listOf(
            ChronologicalStep("9:05 PM", "Maya Voss departs after arguing with Elias over his dangerous exposé. Takes rideshare home."),
            ChronologicalStep("9:25 PM", "Victor Hale attends an executive dinner across town, confirmed by restaurant POS records."),
            ChronologicalStep("9:30 PM", "Nora Bennett stays home working on legal dossiers, confirmed by ISP access logs."),
            ChronologicalStep("9:40 PM", "Daniel Mercer arrives at Elias's apartment to assess what financial records Elias uncovered."),
            ChronologicalStep("9:50 PM", "Daniel exits the apartment after a heated dispute—the only true portion of his initial claim."),
            ChronologicalStep("10:20 PM", "Daniel returns and swipes his keycard to force Elias into surrendering the audit files."),
            ChronologicalStep("10:35 PM", "A physical altercation erupts; glass breaks and Daniel strikes Elias with the paperweight."),
            ChronologicalStep("10:40 PM", "Daniel panics, searching the study and stealing the 'Mercer Accounts' financial dossier."),
            ChronologicalStep("10:42 PM", "Elias places his final outgoing call, creating the decisive timestamp anchor."),
            ChronologicalStep("10:45 PM", "Daniel flees into the rain, attempting to fabricate his false departure alibi.")
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
        val ids = setOf(source1Id, source2Id)
        // C001: Daniel statement (ST006/ST007/E014) vs Access Evidence (E018) or Call Evidence (E002)
        if ((ids.contains("ST006") || ids.contains("ST007") || ids.contains("E014")) && (ids.contains("E018") || ids.contains("E002"))) {
            return CONTRADICTIONS.find { it.id == "C001" }
        }
        return null
    }

    override fun checkDeductionPair(source1Id: String, source2Id: String, relationship: ReasoningRelationship): Pair<Deduction?, String?> {
        val ids = setOf(source1Id, source2Id)

        // D001: Elias Was Alive at 10:42 PM (E001 + E002 with ESTABLISHES/SUPPORTS)
        if (ids.contains("E001") && ids.contains("E002") && (relationship == ReasoningRelationship.ESTABLISHES || relationship == ReasoningRelationship.SUPPORTS)) {
            return Pair(getDeduction("D001"), "Phone logs confirm Elias placed an outgoing call at 10:42 PM, anchoring the murder window after that moment.")
        }

        // D002: Alibis Clear Maya, Victor & Nora (E015 + E016 or E017 with ESTABLISHES/SUPPORTS/DISPROVES)
        if ((ids.contains("E015") || ids.contains("E016") || ids.contains("E017")) && (ids.contains("S001") || ids.contains("S002") || ids.contains("S003") || ids.contains("E015") || ids.contains("E016") || ids.contains("E017"))) {
            if (relationship == ReasoningRelationship.ESTABLISHES || relationship == ReasoningRelationship.SUPPORTS || relationship == ReasoningRelationship.DISPROVES) {
                return Pair(getDeduction("D002"), "Independent transit, dining, and network records confirm the alibis of Maya, Victor, and Nora.")
            }
        }

        // D003: No Forced Entry (E009 + E010 with ESTABLISHES/SUPPORTS)
        if (ids.contains("E009") && (relationship == ReasoningRelationship.ESTABLISHES || relationship == ReasoningRelationship.SUPPORTS)) {
            return Pair(getDeduction("D003"), "Unbroken deadbolt lock and undisturbed windows establish Elias admitted someone he recognized.")
        }

        // D004: Daniel Mercer Lied About Departure (ST006/ST007/E014 + E018 with CONTRADICTS)
        if ((ids.contains("ST006") || ids.contains("ST007") || ids.contains("E014")) && ids.contains("E018") && relationship == ReasoningRelationship.CONTRADICTS) {
            return Pair(getDeduction("D004"), "Daniel Mercer claimed he left before 10:00 PM and never returned, directly contradicted by building access footage at 10:20 PM.")
        }

        // D005: Financial Embezzlement Motive (E005 + E019 or E004 + E019 with ESTABLISHES/SUPPORTS)
        if (ids.contains("E019") && (ids.contains("E005") || ids.contains("E004") || ids.contains("S004")) && (relationship == ReasoningRelationship.ESTABLISHES || relationship == ReasoningRelationship.SUPPORTS)) {
            return Pair(getDeduction("D005"), "Elias's hidden financial dossiers prove Daniel Mercer was embezzling millions and faced complete ruin if published.")
        }

        // D006: Physical Struggle & Weapon (E007 + E008 with ESTABLISHES/CONNECTS)
        if (ids.contains("E007") && ids.contains("E008") && (relationship == ReasoningRelationship.ESTABLISHES || relationship == ReasoningRelationship.CONNECTS)) {
            return Pair(getDeduction("D006"), "Shattered glass and cast-iron paperweight residue establish a sudden violent confrontation at the desk.")
        }

        // D007: The Missing File Was Stolen (E005 + E019 with CONNECTS/ESTABLISHES)
        if (ids.contains("E005") && ids.contains("E019") && (relationship == ReasoningRelationship.CONNECTS || relationship == ReasoningRelationship.ESTABLISHES)) {
            return Pair(getDeduction("D007"), "The empty 'Mercer Accounts' folder indicates the assailant removed incriminating records after the assault.")
        }

        // D008: Daniel Mercer is the Sole Culprit (E018 + E019 or E020 + S004 with ESTABLISHES)
        if ((ids.contains("E018") || ids.contains("E020")) && ids.contains("E019") && (relationship == ReasoningRelationship.ESTABLISHES || relationship == ReasoningRelationship.SUPPORTS)) {
            return Pair(getDeduction("D008"), "With motive, opportunity during the fatal window, and a shattered alibi, Daniel Mercer is conclusively responsible.")
        }

        // Generic meaningful connections
        if (ids.contains("E006") && ids.contains("E018") && relationship == ReasoningRelationship.CONNECTS) {
            return Pair(null, "Daniel's desk fingerprint physically connects his presence to the crime scene during his unacknowledged return.")
        }

        return Pair(null, null)
    }
}
