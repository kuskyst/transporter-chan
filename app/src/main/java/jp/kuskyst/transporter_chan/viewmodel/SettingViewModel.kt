package jp.kuskyst.transporter_chan.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    private val _webhookUrl = MutableStateFlow(sharedPreferences.getString("webhook_url", "") ?: "")
    val webhookUrl: StateFlow<String> get() = _webhookUrl

    fun saveWebhookUrl(newUrl: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("webhook_url", newUrl).apply()
            _webhookUrl.value = newUrl
        }
    }
}
