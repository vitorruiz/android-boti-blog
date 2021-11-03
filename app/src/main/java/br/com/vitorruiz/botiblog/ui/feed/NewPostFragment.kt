package br.com.vitorruiz.botiblog.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.vitorruiz.botiblog.R
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostEntity
import br.com.vitorruiz.botiblog.databinding.FragmentNewPostBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewPostFragment(
    private val postToEdit: PostEntity? = null,
    private val onPostListener: (postId: Long?, postText: String) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postToEdit?.let {
            _binding.edtPostText.setText(it.text)
            _binding.tvTitle.setText(R.string.new_post_title_edit_post)
        }

        _binding.btPost.setOnClickListener { sendPost() }
    }

    private fun sendPost() {
        onPostListener(postToEdit?.id, _binding.edtPostText.text.toString())
        _binding.edtPostText.setText(null)
        dialog?.dismiss()
    }
}