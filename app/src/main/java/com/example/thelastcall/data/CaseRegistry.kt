package com.example.thelastcall.data

object CaseRegistry {
    private val cases = mutableMapOf<String, CaseDefinition>()
    private var defaultCaseId: String = "CASE-001"

    init {
        registerCase(Case001Data)
        registerCase(Case002Data)
    }

    fun registerCase(caseDefinition: CaseDefinition) {
        cases[caseDefinition.id] = caseDefinition
    }

    fun getCase(caseId: String): CaseDefinition? {
        return cases[caseId]
    }

    fun getDefaultCase(): CaseDefinition {
        return cases[defaultCaseId] ?: Case001Data
    }

    fun getAllCases(): List<CaseDefinition> {
        return cases.values.toList()
    }

    fun getAvailableCases(): List<CaseDefinition> {
        return cases.values.filter { it.isAvailable }.toList()
    }

    fun hasCase(caseId: String): Boolean {
        return cases.containsKey(caseId)
    }

    fun setDefaultCase(caseId: String) {
        if (cases.containsKey(caseId)) {
            defaultCaseId = caseId
        }
    }

    fun resetRegistry() {
        cases.clear()
        registerCase(Case001Data)
        registerCase(Case002Data)
        defaultCaseId = "CASE-001"
    }
}
