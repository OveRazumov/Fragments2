package com.overazumov.userlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.overazumov.userlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportFragmentManager.findFragmentByTag(ListFragment.TAG) == null) {
            supportFragmentManager.commit {
                replace(R.id.listContainer, ListFragment.newInstance(), ListFragment.TAG)
            }
        }
    }
}