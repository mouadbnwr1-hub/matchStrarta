package com.example.matchstrart.viewmodel



import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.matchstrart.model.*

enum class SortOrder {
    NAME, PRICE_ASC, PRICE_DESC
}

/**
 * ViewModel pour gérer l'état de l'application
 */
class FieldViewModel : ViewModel() {

    // État des terrains
    private val _fields = mutableStateListOf<Field>()
    val fields: List<Field> = _fields

    // État du filtre de recherche
    var searchQuery by mutableStateOf("")
        private set

    // État du filtre par sport
    var selectedSportFilter by mutableStateOf<SportType?>(null)
        private set

    // État du tri
    var sortOrder by mutableStateOf(SortOrder.NAME)
        private set

    // Terrains filtrés et triés
    val filteredFields: List<Field>
        get() {
            var result = _fields.filter { field ->
                val matchesSearch = field.name.contains(searchQuery, ignoreCase = true) ||
                        field.location.contains(searchQuery, ignoreCase = true)
                val matchesSport = selectedSportFilter?.let { field.sportType == it } ?: true
                matchesSearch && matchesSport
            }

            result = when (sortOrder) {
                SortOrder.NAME -> result.sortedBy { it.name }
                SortOrder.PRICE_ASC -> result.sortedBy { it.pricePerHour }
                SortOrder.PRICE_DESC -> result.sortedByDescending { it.pricePerHour }
            }

            return result
        }

    init {
        loadSampleData()
    }

    // Charger des données d'exemple
    private fun loadSampleData() {
        _fields.addAll(
            listOf(
                Field(1, "Terrain Municipal", "Centre-ville, Safi", SportType.FOOTBALL, 150.0, true, 4.5f, 22),
                Field(2, "Complexe Sportif Atlas", "Hay Salam, Safi", SportType.BASKETBALL, 100.0, true, 4.2f, 10),
                Field(3, "Club Tennis Royal", "Bab Chaâba, Safi", SportType.TENNIS, 200.0, false, 4.8f, 4),
                Field(4, "Stade El Massira", "Route d'Essaouira", SportType.FOOTBALL, 180.0, true, 4.6f, 22),
                Field(5, "Salle Omnisport", "Hay Mohammadi", SportType.VOLLEYBALL, 120.0, true, 4.3f, 12),
                Field(6, "Terrain de Proximité", "Quartier Industriel", SportType.FOOTBALL, 80.0, true, 3.9f, 14),
                Field(7, "Basketball Arena", "Hay Dakla", SportType.BASKETBALL, 130.0, false, 4.4f, 10),
                Field(8, "Tennis Club Premium", "Marina de Safi", SportType.TENNIS, 250.0, true, 4.9f, 2)
            )
        )
    }

    // Mettre à jour la recherche
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    // Mettre à jour le filtre sport
    fun updateSportFilter(sport: SportType?) {
        selectedSportFilter = sport
    }

    // Mettre à jour l'ordre de tri
    fun updateSortOrder(order: SortOrder) {
        sortOrder = order
    }

    // Ajouter un terrain
    fun addField(field: Field) {
        val newField = field.copy(id = (_fields.maxOfOrNull { it.id } ?: 0) + 1)
        _fields.add(newField)
    }

    // Modifier un terrain
    fun updateField(field: Field) {
        val index = _fields.indexOfFirst { it.id == field.id }
        if (index != -1) {
            _fields[index] = field
        }
    }

    // Supprimer un terrain
    fun deleteField(field: Field) {
        _fields.remove(field)
    }

    // Obtenir un terrain par ID
    fun getFieldById(id: Int): Field? {
        return _fields.find { it.id == id }
    }

    // Réserver un terrain
    fun bookField(fieldId: Int) {
        val index = _fields.indexOfFirst { it.id == fieldId }
        if (index != -1) {
            _fields[index] = _fields[index].copy(isAvailable = false)
        }
    }
}