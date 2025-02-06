package jp.kuskyst.transporter_chan.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import jp.kuskyst.transporter_chan.ui.SettingScreen
import jp.kuskyst.transporter_chan.viewmodel.SettingViewModel

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {

    private val viewModel: SettingViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), PERMISSION_REQUEST_CODE)
        }

        setContent {
            SettingScreen(viewModel)
        }
    }
}
