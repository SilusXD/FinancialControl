package com.example.financialcontrol.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.financialcontrol.DatabaseHelper
import com.example.financialcontrol.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var profileViewModel: ProfileViewModel? = null
    private var profileName: TextView? = null
    private var profileEmail: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileName = binding.textViewFio
        profileEmail = binding.textViewEmail
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        val db = activity?.applicationContext?.let { DatabaseHelper(it, null) }
        val cursor = db?.getUsers()
        if (cursor != null && cursor.moveToFirst())
        {
            cursor.moveToNext()
            profileViewModel!!.setName(cursor.getString(1))
            profileViewModel!!.setEmail(cursor.getString(2))
        }



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel!!.getName().observe(viewLifecycleOwner)
        {
            profileName?.text = it
        }
        profileViewModel!!.getEmail().observe(viewLifecycleOwner)
        {
            profileEmail?.text = it
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}