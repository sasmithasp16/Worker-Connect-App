package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.RequestUrgency
import com.example.ui.BookingDialogState
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBottomSheet(
    state: BookingDialogState,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onDescChange: (String) -> Unit,
    onLocalityChange: (String) -> Unit,
    onPinChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onSlotChange: (String) -> Unit,
    onUrgencyChange: (RequestUrgency) -> Unit,
    onSubmit: () -> Unit
) {
    if (!state.isOpen || state.worker == null) return

    val worker = state.worker

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("booking_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Handyman,
                    contentDescription = null,
                    tint = PrimaryTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = LocalizedStrings.get("modal_book_title", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "To: ${worker.displayName} (${worker.city})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Transparent Pricing Card Reminder
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Standard Inspection Fee: ₹${worker.visitCharge}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Base labour starts at ₹${worker.minLabourCharge}. Final quote given at site.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Problem Description Field
            OutlinedTextField(
                value = state.description,
                onValueChange = onDescChange,
                label = { Text("Problem Description *") },
                placeholder = { Text(LocalizedStrings.get("booking_desc_hint", language)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_desc_input"),
                minLines = 3,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Locality & PIN Code Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.locality,
                    onValueChange = onLocalityChange,
                    label = { Text("Locality / Street") },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("booking_locality_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.pinCode,
                    onValueChange = onPinChange,
                    label = { Text("PIN Code") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("booking_pin_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Urgency Selector
            Text(
                text = LocalizedStrings.get("booking_urgency", language),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RequestUrgency.values().forEach { urgency ->
                    val isSelected = state.urgency == urgency
                    FilterChip(
                        selected = isSelected,
                        onClick = { onUrgencyChange(urgency) },
                        label = {
                            Text(
                                text = when (urgency) {
                                    RequestUrgency.NORMAL -> "Standard"
                                    RequestUrgency.SAME_DAY -> "Same Day"
                                    RequestUrgency.EMERGENCY -> "Urgent / Emergency"
                                },
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (urgency == RequestUrgency.EMERGENCY) AccentAmberLight else TealContainer,
                            selectedLabelColor = if (urgency == RequestUrgency.EMERGENCY) Color(0xFF92400E) else TealDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preferred Date & Slot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.preferredDate,
                    onValueChange = onDateChange,
                    label = { Text("Preferred Date") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.preferredTimeSlot,
                    onValueChange = onSlotChange,
                    label = { Text("Time Slot") },
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_enquiry_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = LocalizedStrings.get("btn_submit_request", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
