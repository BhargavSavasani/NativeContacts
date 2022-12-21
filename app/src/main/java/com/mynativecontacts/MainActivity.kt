package com.mynativecontacts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mynativecontacts.databinding.ActivityMainBinding
import com.nativecontacts.ContactProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mContactListAdapter: ContactListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerViewContacts()
        ContactProvider.getNativeContacts(this) {
            mContactListAdapter?.addAll(it)
            binding.rvContacts.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun setRecyclerViewContacts() {
        binding.rvContacts.apply {
            setHasFixedSize(true)
            mContactListAdapter = ContactListAdapter()
            adapter = mContactListAdapter
        }

    }
}