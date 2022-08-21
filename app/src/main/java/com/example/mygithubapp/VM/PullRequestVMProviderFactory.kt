package com.example.mygithubapp.VM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubapp.Repo.PullRequestRepository

class PullRequestVMProviderFactory(private val pullRequestRepository: PullRequestRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PullRequestVM(pullRequestRepository) as T
    }
}
