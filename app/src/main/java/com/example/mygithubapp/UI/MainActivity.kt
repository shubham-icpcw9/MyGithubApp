package com.example.mygithubapp.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubapp.R
import com.example.mygithubapp.Repo.PullRequestRepository
import com.example.mygithubapp.VM.PullRequestVM
import com.example.mygithubapp.VM.PullRequestVMProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : PullRequestVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pullRequestRepository = PullRequestRepository()
        val viewModelProviderFactory = PullRequestVMProviderFactory(pullRequestRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[PullRequestVM::class.java]
        supportFragmentManager.beginTransaction().apply {
            add(R.id.hostFragContainer, PullRequestFragment.newInstance(), PullRequestFragment.TAG)
            commitAllowingStateLoss()
        }
    }
}