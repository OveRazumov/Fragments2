package com.overazumov.userlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.google.android.material.textfield.TextInputEditText
import com.overazumov.userlist.databinding.FragmentEditorBinding

class EditorFragment: Fragment() {
    private lateinit var binding: FragmentEditorBinding
    private lateinit var user: User
    private var viewIdWithFocus: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditorBinding.inflate(layoutInflater)

        if (savedInstanceState != null) {
            user = savedInstanceState.getParcelable(USER_BUNDLE_KEY)!!
            val name = savedInstanceState.getString(NAME_BUNDLE_KEY)!!
            val surname = savedInstanceState.getString(SURNAME_BUNDLE_KEY)!!
            val number = savedInstanceState.getString(PHONE_BUNDLE_KEY)!!
            initEditTexts(name, surname, number)
        } else initEditTexts(user.name, user.surname, user.phoneNumber.number)

        requireActivity().onBackPressedDispatcher.addCallback {
            navigateToListFragment()
        }

        with(binding) {
            confirmButton.setOnClickListener {
                if (isDataCorrect()) {
                    navigateToListFragment()
                }
            }

            cancelButton.setOnClickListener {
                if (!isDataSame()) {
                    setFragmentResult(
                        ListFragment.SET_USER_REQUEST_KEY,
                        bundleOf(USER_BUNDLE_KEY to user)
                    )
                }
                navigateToListFragment()
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(binding) {
            outState.putParcelable(USER_BUNDLE_KEY, user)
            outState.putString(NAME_BUNDLE_KEY, nameEditText.text.toString())
            outState.putString(SURNAME_BUNDLE_KEY, surnameEditText.text.toString())
            outState.putString(PHONE_BUNDLE_KEY, phoneEditText.text.toString())
        }
    }

    private fun navigateToListFragment() {
        parentFragmentManager.popBackStack(ListFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun initEditTexts(name: String, surname: String, number: String) {
        fillEditTexts(name, surname, number)
        setEditTextsBehavior()
        setCursors(name, surname, number)
        setFocus()
    }

    private fun fillEditTexts(name: String, surname: String, number: String) {
        with(binding) {
            nameEditText.setText(name)
            surnameEditText.setText(surname)
            phoneEditText.setText(number)
        }
    }

    private fun setEditTextsBehavior() {
        with(binding) {
            nameEditText.setBehavior(ListFragment.SET_NAME_REQUEST_KEY, NAME_BUNDLE_KEY)
            surnameEditText.setBehavior(ListFragment.SET_SURNAME_REQUEST_KEY, SURNAME_BUNDLE_KEY)
            phoneEditText.setBehavior(ListFragment.SET_PHONE_REQUEST_KEY, PHONE_BUNDLE_KEY)

            phoneEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    phoneEditText.clearFocus()
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(phoneEditText.windowToken, 0)
                } else false
            }
        }
    }

    private fun setCursors(name: String, surname: String, number: String) {
        with(binding) {
            nameEditText.setSelection(name.length)
            surnameEditText.setSelection(surname.length)
            phoneEditText.setSelection(number.length)
        }
    }

    private fun setFocus() {
        binding.root.findViewById<EditText>(viewIdWithFocus ?: return).requestFocus()
    }

    private fun TextInputEditText.setBehavior(requestKey: String, bundleKey: String) {
        addTextChangedListener {
            clearErrors()

            setFragmentResult(requestKey, bundleOf(
                ID_BUNDLE_KEY to user.id,
                bundleKey to text.toString()
            ))
        }
    }

    private fun clearErrors() {
        with(binding) {
            nameTextLayout.error = null
            surnameTextLayout.error = null
            phoneTextLayout.error = null
        }
    }

    private fun isDataSame(): Boolean {
        with(binding) {
            val nameMatch = nameEditText.text.toString() == user.name
            val surnameMatch = surnameEditText.text.toString() == user.surname
            val phoneMatch = phoneEditText.text.toString() == user.phoneNumber.number
            return nameMatch && surnameMatch && phoneMatch
        }
    }

    private fun isDataCorrect(): Boolean {
        var result = true
        with(binding) {
            if (nameEditText.text.toString().isEmpty()) {
                nameTextLayout.error = getString(R.string.enter_name)
                result = false
            }
            if (surnameEditText.text.toString().isEmpty()) {
                surnameTextLayout.error = getString(R.string.enter_surname)
                result = false
            }
            if (phoneEditText.text.toString().length != 10) {
                phoneTextLayout.error = getString(R.string.incorrect_phone_number)
                result = false
            }
            return result
        }
    }

    companion object {
        const val USER_BUNDLE_KEY = "USER"
        const val ID_BUNDLE_KEY = "ID"
        const val NAME_BUNDLE_KEY = "NAME"
        const val SURNAME_BUNDLE_KEY = "SURNAME"
        const val PHONE_BUNDLE_KEY = "PHONE"

        fun newInstance(user: User, viewIdWithFocus: Int? = null): Fragment {
            return EditorFragment().apply {
                this.user = user
                this.viewIdWithFocus = viewIdWithFocus
            }
        }
    }
}