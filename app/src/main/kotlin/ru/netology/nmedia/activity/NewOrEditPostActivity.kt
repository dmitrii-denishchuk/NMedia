package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewOrEditPostBinding
import ru.netology.nmedia.utils.hideKeyboard

class NewOrEditPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewOrEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.requestFocus()

        binding.okButton.setOnClickListener {
            onOkButtonClicked(binding.edit.text?.toString())
        }

        binding.cancelButton.setOnClickListener {
            binding.edit.clearFocus()
            binding.edit.hideKeyboard()
            onCancelButtonClicked()
        }

        binding.edit.setText(intent.getStringExtra(POST_MESSAGE_EXTRA_KEY))
    }

    private companion object {
        const val POST_MESSAGE_EXTRA_KEY = "postMessage"
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

    private fun onCancelButtonClicked() {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    object ResultContract : ActivityResultContract<String?, String?>() {

        override fun createIntent(context: Context, input: String?): Intent {
            val intent = Intent(context, NewOrEditPostActivity::class.java)
            intent.putExtra(POST_MESSAGE_EXTRA_KEY, input)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null
            return intent.getStringExtra(POST_MESSAGE_EXTRA_KEY)
        }
    }
}