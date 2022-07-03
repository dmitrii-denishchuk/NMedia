package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.text.Selection
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)
        binding.postRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.createButton.setOnClickListener {
            with(binding.message) {
                val message = text.toString()
                viewModel.clickedCreate(message)
                clearFocus()
                hideKeyboard()
            }
        }

        binding.applyButton.setOnClickListener {
            with(binding.message) {
                val message = text.toString()
                viewModel.clickedCreate(message)
                clearFocus()
                hideKeyboard()
            }
        }

        binding.cancelButton.setOnClickListener {
            with(binding.message) {
                binding.groupEditMessage.visibility = View.GONE
                binding.groupNewMessage.visibility = View.VISIBLE
                text = null
                clearFocus()
                hideKeyboard()
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.message) {
                val message = currentPost?.message
                setText(message)
                if (message != null) {
                    binding.groupEditMessage.visibility = View.VISIBLE
                    binding.groupNewMessage.visibility = View.INVISIBLE
                    Selection.setSelection(editableText, editableText.length)
                    requestFocus()
                    showKeyboard()
                } else {
                    binding.groupEditMessage.visibility = View.GONE
                    binding.groupNewMessage.visibility = View.VISIBLE
                    clearFocus()
                    hideKeyboard()
                }
            }
        }

        viewModel.shareEvent.observe(this) {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.message)
            }
            val shareIntent = Intent.createChooser(intent, "Share")
            startActivity(shareIntent)
        }

        val activityLauncher = registerForActivityResult(
            NewPostActivity.ResultContract
        ) { postMessage: String? ->
            postMessage?.let(viewModel::onCreateNewPost)
        }
        binding.ok.setOnClickListener {
            activityLauncher.launch(Unit)
        }
    }
}