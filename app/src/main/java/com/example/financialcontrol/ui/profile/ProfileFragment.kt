package com.example.financialcontrol.ui.profile

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.financialcontrol.DatabaseHelper
import com.example.financialcontrol.R
import com.example.financialcontrol.User
import com.example.financialcontrol.databinding.FragmentProfileBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var profileViewModel: ProfileViewModel? = null
    private var profilePhoto: ImageView? = null
    private var editTextName: EditText? = null
    private var editTextGender: EditText? = null
    private var editTextBirthday: EditText? = null
    private var editTextEmail: EditText? = null
    private var buttonUpdate: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profilePhoto = binding.profilePhoto
        editTextName = binding.editTextName
        editTextGender = binding.editTextGender
        editTextBirthday = binding.editTextBirthday
        editTextEmail = binding.editTextEmail
        buttonUpdate = binding.buttonUpdate
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        updateProfile()

        buttonUpdate!!.setOnClickListener {
            if(editTextName!!.text.isNotEmpty() && editTextGender!!.text.isNotEmpty() &&
                editTextBirthday!!.text.isNotEmpty() && editTextEmail!!.text.isNotEmpty())
            {
                val gender: Int
                val birthday: LocalDate

                val db = activity?.applicationContext?.let { DatabaseHelper(it, null) }

                val cursor = db?.selectIdInGendersByGender(editTextGender!!.text.toString())
                if(cursor == null || !cursor.moveToFirst())
                {
                    val toast: Toast = Toast.makeText(activity?.applicationContext,
                        "Incorrect gender!", Toast.LENGTH_LONG)
                    toast.show()
                    return@setOnClickListener
                }
                gender = cursor.getInt(0)

                try
                {
                    birthday = LocalDate.parse(editTextBirthday!!.text,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                }
                catch (ex: DateTimeParseException)
                {
                    val toast: Toast = Toast.makeText(activity?.applicationContext,
                        "Incorrect date format!", Toast.LENGTH_LONG)
                    toast.show()
                    return@setOnClickListener
                }

                if(profileViewModel!!.getUser().value == null)
                {
                    db.addUser(
                        name = editTextName!!.text.toString(),
                        email = editTextEmail!!.text.toString(),
                        gender = gender,
                        birthday = birthday
                    )
                }
                else
                {
                    db.updateUser(
                        id = profileViewModel!!.getUser().value!!.id,
                        name = editTextName!!.text.toString(),
                        email = editTextEmail!!.text.toString(),
                        gender = gender,
                        birthday = birthday
                    )
                }

                db.close()
                val toast: Toast = Toast.makeText(activity?.applicationContext,
                    "The data is recorded!", Toast.LENGTH_LONG)
                toast.show()
            }
            else
            {
                val toast: Toast = Toast.makeText(activity?.applicationContext,
                    "Fill in all the fields!", Toast.LENGTH_LONG)
                toast.show()
            }

            updateProfile()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        updateProfile()
    }

    private fun updateProfile()
    {
        val db = activity?.applicationContext?.let { DatabaseHelper(it, null) }
        val cursor = db?.getUsers()
        if (cursor != null && cursor.moveToFirst())
        {
            val user: User = User(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                gender = cursor.getInt(2),
                birthday = LocalDate.parse(cursor.getString(3)),
                email = cursor.getString(4)
            )
            profileViewModel!!.setUser(user)
        }

        profileViewModel!!.getUser().observe(viewLifecycleOwner)
        {
            editTextName?.setText(it.name)
            val gender = db?.selectGenderInGendersById(it.gender)
            if(gender == "Woman")
                profilePhoto!!.setImageResource(R.drawable.ic_default_photo_woman)
            else
                profilePhoto!!.setImageResource(R.drawable.ic_default_photo_man)
            editTextGender?.setText(gender)
            editTextBirthday?.setText(it.birthday.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            editTextEmail?.setText(it.email)
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}