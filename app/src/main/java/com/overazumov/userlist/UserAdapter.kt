package com.overazumov.userlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.overazumov.userlist.databinding.UserItemBinding

class UserAdapter(
    private val userList: MutableList<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        with(holder.binding) {
            fullName.text = "${user.name} ${user.surname}"
            phoneNumber.text = user.phoneNumber.toString()
            image.setImageResource(user.imageRes)

            item.setOnClickListener {
                onItemClick(user.copy())
            }
        }
    }

    override fun getItemCount(): Int = userList.size

    fun setUser(user: User) {
        val position = getPosition(user.id)
        userList[position] = user
        notifyItemChanged(position)
    }

    fun setName(id: Int, name: String) {
        val position = getPosition(id)
        userList[position].name = name
        notifyItemChanged(position)
    }

    fun setSurname(id: Int, surname: String) {
        val position = getPosition(id)
        userList[position].surname = surname
        notifyItemChanged(position)
    }

    fun setPhoneNumber(id: Int, number: String) {
        val position = getPosition(id)
        userList[position].phoneNumber = User.PhoneNumber(number)
        notifyItemChanged(position)
    }

    fun getUserList(): List<User> = userList

    private fun getPosition(id: Int): Int = userList.indexOfFirst { it.id == id }
}
