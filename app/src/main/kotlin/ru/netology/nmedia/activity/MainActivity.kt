package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Selection
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.ActivityNewOrEditPostBinding
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        val newOrEditPostActivity = ActivityNewOrEditPostBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)
        mainActivityBinding.postRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.message)
            }
            val shareIntent = Intent.createChooser(intent, post.postHeader)
            startActivity(shareIntent)
        }

        viewModel.playEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = post.video.toUri()
                Uri.parse(post.video)
            }
            val playIntent = Intent.createChooser(intent, post.postHeader)
            startActivity(playIntent)
        }

        val activityLauncher = registerForActivityResult(
            NewOrEditPostActivity.ResultContract
        ) { postMessage: String? ->
            postMessage?.let(viewModel::clickedCreate)
        }

        mainActivityBinding.addPostButton.setOnClickListener {
            activityLauncher.launch(null)
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(newOrEditPostActivity.edit) {
                val message = currentPost?.message
                if (message != null) {
                    activityLauncher.launch(message)
                    Selection.setSelection(editableText, editableText.length)
                    requestFocus()
                    showKeyboard()
                } else {
                    clearFocus()
                    hideKeyboard()
                }
            }
        }
    }
}