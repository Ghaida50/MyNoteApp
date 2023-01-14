package com.example.mynotes.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynotes.NoteApplication
import com.example.mynotes.R
import com.example.mynotes.database.Note
import com.example.mynotes.databinding.FragmentAddNoteBinding
import com.example.mynotes.viewmodels.NoteViewModel
import com.example.mynotes.viewmodels.NoteViewModelFactory

class AddNoteFragment : Fragment() {
    private val navigationArgs: UpdateNoteFragmentArgs by navArgs()

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database
                .noteDao()
        )
    }

    lateinit var note: Note

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                note = selectedItem
                bind(note)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.etNoteTitle.text.toString(),
            binding.etNoteBody.text.toString()

        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.etNoteTitle.text.toString(),
                binding.etNoteBody.text.toString()
            )
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
    private fun bind(note: Note) {
        binding.apply {
            etNoteTitle.setText(note.noteTitle, TextView.BufferType.SPANNABLE)
            etNoteBody.setText(note.noteBody, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.etNoteTitle.text.toString(),
                this.binding.etNoteBody.text.toString()

            )
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}