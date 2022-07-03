package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.edit.text?.toString())
        }
    }

    private fun onOkButtonClicked(postMessage: String?) {
        intent = Intent()
        if (postMessage.isNullOrBlank()) {
            setResult(Activity.RESULT_CANCELED, intent)
        } else {
            intent.putExtra(POST_MESSAGE_EXTRA_KEY, postMessage)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    private companion object {
        const val POST_MESSAGE_EXTRA_KEY = "postMessage"
    }

    object ResultContract : ActivityResultContract<Unit, String?>() {

        override fun createIntent(context: Context, input: Unit) =
            Intent(context, NewPostActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_MESSAGE_EXTRA_KEY)
        }
    }
}