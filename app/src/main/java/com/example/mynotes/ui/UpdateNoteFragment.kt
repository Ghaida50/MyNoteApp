package com.example.mynotes.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynotes.NoteApplication
import com.example.mynotes.R
import com.example.mynotes.database.Note
import com.example.mynotes.databinding.FragmentUpdateNoteBinding
import com.example.mynotes.viewmodels.NoteViewModel
import com.example.mynotes.viewmodels.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class UpdateNoteFragment : Fragment() {
    private val navigationArgs: UpdateNoteFragmentArgs by navArgs()

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    lateinit var note: Note

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            note = selectedItem
            bind(note)
            setHasOptionsMenu(true)
        }
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        viewModel.deleteItem(note)
        findNavController().navigateUp()
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(note: Note) {
        binding.apply {
            title.text = note.noteTitle
            body.text = note.noteBody

            deleteItem.setOnClickListener { showConfirmationDialog() }
            editItem.setOnClickListener { editItem() }
        }
    }
    private fun editItem() {
        val action = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToAddNoteFragment(
            getString(R.string.edit_fragment_title),
            note.id
        )
        this.findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item?.itemId){
            R.id.share_menu ->{var shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, note.noteTitle)
                this.putExtra(Intent.EXTRA_TEXT, note.noteBody)
                this.type = "text/plain"
            }
            context?.startActivity(shareIntent)
            true }

        else ->{return super.onOptionsItemSelected(item)}}


        }

    }

