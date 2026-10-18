package com.example.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.RequestStatus
import com.example.ui.theme.AccentAmberLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.DeepNavy

@Composable
fun RequestStatusPill(
    status: RequestStatus,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (color, bg, key) = when (status) {
        RequestStatus.SUBMITTED -> Triple(Color(0xFF1E40AF), Color(0xFFDBEAFE), "req_submitted")
        RequestStatus.SENT_TO_WORKER -> Triple(Color(0xFF0369A1), Color(0xFFE0F2FE), "req_sent_worker")
        RequestStatus.ACCEPTED -> Triple(Color(0xFF065F46), Color(0xFFD1FAE5), "req_accepted")
        RequestStatus.DECLINED -> Triple(AccentRed, AccentRedLight, "req_declined")
        RequestStatus.SCHEDULED -> Triple(DeepNavy, Color(0xFFE2E8F0), "req_scheduled")
        RequestStatus.IN_PROGRESS -> Triple(Color(0xFF92400E), AccentAmberLight, "req_in_progress")
        RequestStatus.COMPLETED -> Triple(Color(0xFF047857), Color(0xFFD1FAE5), "req_completed")
        RequestStatus.CANCELLED -> Triple(Color.Gray, Color(0xFFF1F5F9), "req_cancelled")
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bg,
        modifier = modifier
    ) {
        Text(
            text = LocalizedStrings.get(key, language),
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
