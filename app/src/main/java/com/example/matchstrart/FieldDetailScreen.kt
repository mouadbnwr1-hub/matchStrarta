package com.example.matchstrart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.matchstrart.viewmodel.FieldViewModel
import com.example.matchstrart.ui.theme.StatusAvailable
import com.example.matchstrart.ui.theme.StatusOccupied


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldDetailScreen(
    navController: NavController,
    viewModel: FieldViewModel,
    fieldId: Int
) {
    val field = viewModel.getFieldById(fieldId)
    var showBookingDialog by remember { mutableStateOf(false) }

    if (field == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Terrain non trouvé")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails du terrain") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("edit_field/${field.id}")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // En-tête avec icône
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = field.sportType.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = field.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = field.sportType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Statut de disponibilité
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (field.isAvailable)
                            StatusAvailable.copy(alpha = 0.1f)
                        else
                            StatusOccupied.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    if (field.isAvailable) StatusAvailable else StatusOccupied
                                )
                        )
                        Text(
                            text = if (field.isAvailable) "Disponible maintenant" else "Actuellement occupé",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (field.isAvailable) StatusAvailable else StatusOccupied
                        )
                    }
                }

                // Informations principales
                Text(
                    text = "Informations",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                DetailInfoCard(
                    icon = Icons.Default.LocationOn,
                    title = "Localisation",
                    value = field.location
                )

                DetailInfoCard(
                    icon = Icons.Default.AttachMoney,
                    title = "Prix par heure",
                    value = "${field.pricePerHour} DH"
                )

                DetailInfoCard(
                    icon = Icons.Default.People,
                    title = "Capacité",
                    value = "${field.capacity} joueurs"
                )

                DetailInfoCard(
                    icon = Icons.Default.Star,
                    title = "Note moyenne",
                    value = if (field.rating > 0) "${field.rating}/5" else "Pas encore noté"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bouton de réservation
                if (field.isAvailable) {
                    Button(
                        onClick = { showBookingDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Réserver maintenant",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = false,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Block, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Non disponible")
                    }
                }
            }
        }
    }

    // Dialog de confirmation de réservation
    if (showBookingDialog) {
        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
            title = { Text("Confirmer la réservation") },
            text = {
                Column {
                    Text("Voulez-vous réserver ce terrain ?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Terrain: ${field.name}",
                        fontWeight = FontWeight.Bold
                    )
                    Text("Prix: ${field.pricePerHour} DH/h")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.bookField(field.id)
                        showBookingDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
fun DetailInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}