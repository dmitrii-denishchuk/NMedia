package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewOrEditPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.clickListeners.PostsClickListeners
import ru.netology.nmedia.databinding.FragmentPostViewBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class PostViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostViewBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val viewHolder =
            PostsAdapter.ViewHolder(binding.postCardLayout, object : PostsClickListeners {
                override fun clickedLike(post: Post) {
                    viewModel.clickedLike(post)
                }

                override fun clickedShare(post: Post) {
                    viewModel.clickedShare(post)
                    viewModel.shareEvent.observe(viewLifecycleOwner) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, post.message)
                        }
                        val shareIntent = Intent.createChooser(intent, post.postHeader)
                        startActivity(shareIntent)
                    }
                }

                override fun clickedRemove(post: Post) {
                    viewModel.clickedRemove(post)
                    findNavController().navigateUp()
                }

                override fun clickedEdit(post: Post) {
                    viewModel.clickedEdit(post)
                    findNavController().navigate(R.id.action_postViewFragment_to_newOrEditPostFragment,
                        Bundle().apply { textArg = post.message })
                }

                override fun clickedPlay(post: Post) {
                    viewModel.clickedPlay(post)
                    viewModel.playEvent.observe(viewLifecycleOwner) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = post.video.toUri()
                            Uri.parse(post.video)
                        }
                        val playIntent = Intent.createChooser(intent, post.postHeader)
                        startActivity(playIntent)
                    }

                }

                override fun clickedPost(post: Post) {
                    binding.postCardLayout.previewPostButton.visibility = View.GONE
                }
            })

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.filter { post ->
                post.id == requireArguments().getInt("postId")
            }
        }

        viewHolder.bind(checkNotNull(viewModel.currentPost.value))

        return binding.root
    }
}