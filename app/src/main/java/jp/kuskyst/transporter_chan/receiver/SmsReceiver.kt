package jp.kuskyst.transporter_chan.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val pdus = intent.extras?.get("pdus") as Array<Any>?
            val messages = pdus?.mapNotNull { pdu ->
                val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                sms?.messageBody
            }

            messages?.forEach { message ->
                Log.d("SMSReceiver", "Received message: $message")

                // 送信者の情報も取得
                val sender = SmsMessage.createFromPdu(pdus[0] as ByteArray).originatingAddress
                sendToWebhook(context, sender, message)
            }
        }
    }

    private fun sendToWebhook(context: Context, sender: String?, message: String) {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val url = sharedPreferences.getString("webhook_url", "") ?: return

        if (url.isEmpty()) {
            Log.e("SMSReceiver", "Webhook URLが設定されていません")
            return
        }

        // JSONデータの作成
        val json = JSONObject()
        json.put("sender", sender)
        json.put("message", message)

        // HTTPリクエストの送信
        val client = OkHttpClient()
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SMSReceiver", "Webhookへの送信に失敗しました", e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("SMSReceiver", "Webhookへ送信成功: ${response.body?.string()}")
            }
        })
    }
}
