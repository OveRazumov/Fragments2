package com.overazumov.userlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import com.overazumov.userlist.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentListBinding.inflate(layoutInflater)

        val userList = if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList(USER_LIST_BUNDLE_KEY)
        } else User.Fabric.createUsers(fullNames)

        adapter = UserAdapter(userList!!.toMutableList()) { user -> onItemClick(user) }

        with(binding.recyclerView) {
            adapter = this@ListFragment.adapter
            addItemDecoration(RecyclerViewDecoration(context))
        }

        setFragmentResultListener(SET_USER_REQUEST_KEY) { _, bundle ->
            val user = bundle.getParcelable<User>(EditorFragment.USER_BUNDLE_KEY)!!
            adapter.setUser(user)
        }

        setFragmentResultListener(SET_NAME_REQUEST_KEY) { _, bundle ->
            val id = bundle.getInt(EditorFragment.ID_BUNDLE_KEY)
            val name = bundle.getString(EditorFragment.NAME_BUNDLE_KEY)!!
            adapter.setName(id, name)
        }

        setFragmentResultListener(SET_SURNAME_REQUEST_KEY) { _, bundle ->
            val id = bundle.getInt(EditorFragment.ID_BUNDLE_KEY)
            val surname = bundle.getString(EditorFragment.SURNAME_BUNDLE_KEY)!!
            adapter.setSurname(id, surname)
        }

        setFragmentResultListener(SET_PHONE_REQUEST_KEY) { _, bundle ->
            val id = bundle.getInt(EditorFragment.ID_BUNDLE_KEY)
            val number = bundle.getString(EditorFragment.PHONE_BUNDLE_KEY)!!
            adapter.setPhoneNumber(id, number)
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(USER_LIST_BUNDLE_KEY, ArrayList(adapter.getUserList()))
    }

    private fun onItemClick(user: User) {
        val viewIdWithFocus = requireActivity().window.currentFocus?.id
        parentFragmentManager.commit {
            replace(R.id.editorContainer, EditorFragment.newInstance(user, viewIdWithFocus), TAG)
            addToBackStack(TAG)
        }
    }

    companion object {
        const val TAG = "LIST_FRAGMENT"
        const val SET_USER_REQUEST_KEY = "SET_USER"
        const val SET_NAME_REQUEST_KEY = "SET_NAME"
        const val SET_SURNAME_REQUEST_KEY = "SET_SURNAME"
        const val SET_PHONE_REQUEST_KEY = "SET_PHONE"
        const val USER_LIST_BUNDLE_KEY = "USER_LIST"

        fun newInstance(): Fragment = ListFragment()
    }
}