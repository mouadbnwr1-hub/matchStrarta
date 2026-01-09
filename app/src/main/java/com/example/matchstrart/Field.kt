package com.example.matchstrart.model


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Modèle de données pour un terrain sportif
 */
data class Field(
    val id: Int,
    val name: String,
    val location: String,
    val sportType: SportType,
    val pricePerHour: Double,
    val isAvailable: Boolean = true,
    val rating: Float = 0f,
    val capacity: Int = 0
)

/**
 * Types de sports disponibles
 */
enum class SportType(val displayName: String, val icon: ImageVector) {
    FOOTBALL("Football", Icons.Default.SportsSoccer),
    BASKETBALL("Basketball", Icons.Default.SportsBasketball),
    TENNIS("Tennis", Icons.Default.SportsTennis),
    VOLLEYBALL("Volleyball", Icons.Default.SportsVolleyball)
}

/**
 * Niveaux d'équipe pour le matchmaking
 */
enum class TeamLevel(val displayName: String) {
    BEGINNER("Débutant"),
    INTERMEDIATE("Intermédiaire"),
    ADVANCED("Avancé")
}

/**
 * Modèle pour un tournoi
 */
data class Tournament(
    val id: Int,
    val name: String,
    val sportType: SportType,
    val registrationFee: Double,
    val participants: Int,
    val maxParticipants: Int,
    val startDate: String,
    val location: String,
    val description: String
)