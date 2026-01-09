package com.example.matchstrart.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.matchstrart.model.Field
import com.example.matchstrart.model.SportType
import com.example.matchstrart.viewmodel.FieldViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFieldScreen(
    navController: NavController,
    viewModel: FieldViewModel,
    fieldId: Int?
) {
    val existingField = fieldId?.let { viewModel.getFieldById(it) }
    val isEditMode = existingField != null

    var name by remember { mutableStateOf(existingField?.name ?: "") }
    var location by remember { mutableStateOf(existingField?.location ?: "") }
    var sportType by remember { mutableStateOf(existingField?.sportType ?: SportType.FOOTBALL) }
    var price by remember { mutableStateOf(existingField?.pricePerHour?.toString() ?: "") }
    var isAvailable by remember { mutableStateOf(existingField?.isAvailable ?: true) }
    var capacity by remember { mutableStateOf(existingField?.capacity?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var capacityError by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Modifier le terrain" else "Ajouter un terrain") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nom du terrain
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text("Nom du terrain *") },
                placeholder = { Text("Ex: Terrain Municipal") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError,
                supportingText = {
                    if (nameError) Text("Le nom est requis")
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Localisation
            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                    locationError = false
                },
                label = { Text("Localisation *") },
                placeholder = { Text("Ex: Centre-ville, Safi") },
                modifier = Modifier.fillMaxWidth(),
                isError = locationError,
                supportingText = {
                    if (locationError) Text("La localisation est requise")
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Type de sport (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = sportType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type de sport *") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    SportType.values().forEach { sport ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(sport.icon, contentDescription = null)
                                    Text(sport.displayName)
                                }
                            },
                            onClick = {
                                sportType = sport
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Prix
            OutlinedTextField(
                value = price,
                onValueChange = {
                    if (it.isEmpty() || it.toDoubleOrNull() != null) {
                        price = it
                        priceError = false
                    }
                },
                label = { Text("Prix (DH/heure) *") },
                placeholder = { Text("Ex: 150") },
                modifier = Modifier.fillMaxWidth(),
                isError = priceError,
                supportingText = {
                    if (priceError) Text("Le prix doit être un nombre valide")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Capacité
            OutlinedTextField(
                value = capacity,
                onValueChange = {
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        capacity = it
                        capacityError = false
                    }
                },
                label = { Text("Capacité (joueurs) *") },
                placeholder = { Text("Ex: 22") },
                modifier = Modifier.fillMaxWidth(),
                isError = capacityError,
                supportingText = {
                    if (capacityError) Text("La capacité doit être un nombre valide")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Statut de disponibilité
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Statut",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (isAvailable) "Disponible" else "Occupé",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Boutons d'action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Annuler")
                }

                Button(
                    onClick = {
                        // Validation
                        nameError = name.isBlank()
                        locationError = location.isBlank()
                        priceError = price.isBlank() || price.toDoubleOrNull() == null
                        capacityError = capacity.isBlank() || capacity.toIntOrNull() == null

                        if (!nameError && !locationError && !priceError && !capacityError) {
                            val field = Field(
                                id = existingField?.id ?: 0,
                                name = name,
                                location = location,
                                sportType = sportType,
                                pricePerHour = price.toDouble(),
                                isAvailable = isAvailable,
                                rating = existingField?.rating ?: 0f,
                                capacity = capacity.toInt()
                            )

                            if (isEditMode) {
                                viewModel.updateField(field)
                            } else {
                                viewModel.addField(field)
                            }

                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isEditMode) "Modifier" else "Ajouter")
                }
            }
        }
    }

    // Dialog de succès
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Succès") },
            text = {
                Text(
                    if (isEditMode)
                        "Le terrain a été modifié avec succès"
                    else
                        "Le terrain a été ajouté avec succès"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}