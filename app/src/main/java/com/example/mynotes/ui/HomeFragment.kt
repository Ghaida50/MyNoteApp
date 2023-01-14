package com.example.mynotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.NoteApplication
import com.example.mynotes.R
import com.example.mynotes.adapter.NoteAdapter
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.viewmodels.NoteViewModel
import com.example.mynotes.viewmodels.NoteViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database
                .noteDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NoteAdapter{
            val action =
                HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(it.id)
            this.findNavController().navigate(action)}

        binding.recyclerView.adapter = adapter
        viewModel.allItems.observe(this.viewLifecycleOwner){
                notes->notes.let {
            adapter.submitList(it)
        }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.fabAddNote.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }


    }
}