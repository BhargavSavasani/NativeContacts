package com.mynativecontacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mynativecontacts.databinding.ItemContactListBinding
import com.nativecontacts.Contact

class ContactListAdapter : RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {

    private var mContactList = ArrayList<Contact>()

    fun addAll(contactList: ArrayList<Contact>) {
        mContactList = contactList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContactListViewHolder(
            ItemContactListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.binding.apply {
            val contact = mContactList[position]
            Glide
                .with(ivProfile)
                .load(contact.imageURL)
                .placeholder(R.drawable.ic_placeholder_user)
                .error(R.drawable.ic_placeholder_user)
                .into(ivProfile)
            tvName.text = contact.displayName
            tvPhoneNumber.text = contact.phoneNumberList.firstOrNull()?.number ?: ""
        }
    }

    override fun getItemCount() = mContactList.size

    inner class ContactListViewHolder(val binding: ItemContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}