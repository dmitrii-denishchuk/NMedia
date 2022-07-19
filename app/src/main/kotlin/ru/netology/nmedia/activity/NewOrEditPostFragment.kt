package ru.netology.nmedia.activity

import android.os.Bundle
import android.text.Selection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.FragmentNewOrEditPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel


class NewOrEditPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrEditPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        arguments?.textArg.let(binding.edit::setText)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (viewModel.currentPost.value?.let { viewModel.data.value?.contains(it) } == true) {
                viewModel.currentPost.value = null
                binding.edit.setText("")
            } else viewModel.currentPost.value = Post(message = binding.edit.text.toString())
            binding.edit.hideKeyboard()
            findNavController().navigateUp()
        }

        binding.okButton.setOnClickListener {
            while (binding.edit.text.isNullOrBlank()) {
                Snackbar.make(binding.root, "Пустое сообщение", 1000).show()
                return@setOnClickListener
            }
            viewModel.clickedSave(binding.edit.text.toString())
            binding.edit.hideKeyboard()
            findNavController().navigateUp()
        }

        binding.cancelButton.setOnClickListener {
            viewModel.currentPost.value = null
            binding.edit.hideKeyboard()
            findNavController().navigateUp()
        }

        viewModel.currentPost.observe(viewLifecycleOwner) {
            with(binding.edit) {
                setText(viewModel.currentPost.value?.message)
                requestFocus()
                showKeyboard()
                Selection.setSelection(editableText, editableText.length)
            }
        }

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}