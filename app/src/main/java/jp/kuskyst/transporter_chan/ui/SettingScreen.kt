package jp.kuskyst.transporter_chan.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.kuskyst.transporter_chan.viewmodel.SettingViewModel

@Composable
fun SettingScreen(viewModel: SettingViewModel = hiltViewModel()) {
    val webhookUrl by viewModel.webhookUrl.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("設定: Webhook URL", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))

        var inputText by remember { mutableStateOf(webhookUrl) }

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Webhook URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveWebhookUrl(inputText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("保存")
        }
    }
}
