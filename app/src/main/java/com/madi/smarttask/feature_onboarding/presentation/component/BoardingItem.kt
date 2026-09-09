package com.madi.smarttask.feature_onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.core.presentation.ui.theme.SpaceSmall
import com.madi.smarttask.feature_onboarding.domain.model.Boarding

@Composable
fun BoardingItem(
    modifier: Modifier = Modifier,
    boarding: Boarding
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SpaceSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = boarding.image,
            contentDescription = boarding.title
        )
        Spacer(Modifier.height(SpaceMedium))
        Text(
            text = boarding.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(SpaceSmall))
        Text(
            text = boarding.description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}