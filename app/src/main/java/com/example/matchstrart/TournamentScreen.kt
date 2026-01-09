package com.example.matchstrart.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.matchstrart.model.SportType
import com.example.matchstrart.model.Tournament

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentScreen(navController: NavController) {
    var showRegistrationDialog by remember { mutableStateOf<Tournament?>(null) }
    var playerName by remember { mutableStateOf("") }
    var participantNumber by remember { mutableStateOf("") }

    // Données de tournois d'exemple
    val tournaments = remember {
        listOf(
            Tournament(
                id = 1,
                name = "Coupe de Safi 2026",
                sportType = SportType.FOOTBALL,
                registrationFee = 500.0,
                participants = 12,
                maxParticipants = 16,
                startDate = "15 Janvier 2026",
                location = "Stade Municipal",
                description = "Tournoi de football amateur ouvert à toutes les équipes locales"
            ),
            Tournament(
                id = 2,
                name = "Challenge Basketball",
                sportType = SportType.BASKETBALL,
                registrationFee = 300.0,
                participants = 6,
                maxParticipants = 8,
                startDate = "20 Janvier 2026",
                location = "Complexe Sportif",
                description = "Compétition de basketball 3x3"
            ),
            Tournament(
                id = 3,
                name = "Open de Tennis",
                sportType = SportType.TENNIS,
                registrationFee = 200.0,
                participants = 18,
                maxParticipants = 32,
                startDate = "25 Janvier 2026",
                location = "Club Tennis Royal",
                description = "Tournoi de tennis en simple et double"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournois disponibles") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Column {
                            Text(
                                text = "Participez aux compétitions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Inscrivez-vous aux tournois locaux",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            items(tournaments) { tournament ->
                TournamentCard(
                    tournament = tournament,
                    onRegister = { showRegistrationDialog = it }
                )
            }
        }
    }

    // Dialog d'inscription
    showRegistrationDialog?.let { tournament ->
        AlertDialog(
            onDismissRequest = {
                showRegistrationDialog = null
                playerName = ""
                participantNumber = ""
            },
            icon = { Icon(Icons.Default.AppRegistration, contentDescription = null) },
            title = { Text("Inscription au tournoi") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = tournament.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Divider()

                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        label = { Text("Nom du joueur / équipe") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = participantNumber,
                        onValueChange = { participantNumber = it },
                        label = { Text("Numéro de participant (optionnel)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Frais d'inscription: ${tournament.registrationFee} DH",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRegistrationDialog = null
                        playerName = ""
                        participantNumber = ""
                    },
                    enabled = playerName.isNotBlank()
                ) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRegistrationDialog = null
                        playerName = ""
                        participantNumber = ""
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
fun TournamentCard(
    tournament: Tournament,
    onRegister: (Tournament) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = tournament.sportType.icon,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = tournament.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tournament.sportType.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Divider()

            // Informations du tournoi
            TournamentInfo(
                icon = Icons.Default.CalendarToday,
                label = "Date de début",
                value = tournament.startDate
            )

            TournamentInfo(
                icon = Icons.Default.LocationOn,
                label = "Lieu",
                value = tournament.location
            )

            TournamentInfo(
                icon = Icons.Default.People,
                label = "Participants",
                value = "${tournament.participants}/${tournament.maxParticipants}"
            )

            TournamentInfo(
                icon = Icons.Default.AttachMoney,
                label = "Frais d'inscription",
                value = "${tournament.registrationFee} DH"
            )

            // Description
            Text(
                text = tournament.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Barre de progression
            val progress = tournament.participants.toFloat() / tournament.maxParticipants.toFloat()
            Column {
                Text(
                    text = "Places restantes: ${tournament.maxParticipants - tournament.participants}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            // Bouton d'inscription
            Button(
                onClick = { onRegister(tournament) },
                modifier = Modifier.fillMaxWidth(),
                enabled = tournament.participants < tournament.maxParticipants
            ) {
                Icon(Icons.Default.AppRegistration, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (tournament.participants < tournament.maxParticipants)
                        "S'inscrire maintenant"
                    else
                        "Complet"
                )
            }
        }
    }
}

@Composable
fun TournamentInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}