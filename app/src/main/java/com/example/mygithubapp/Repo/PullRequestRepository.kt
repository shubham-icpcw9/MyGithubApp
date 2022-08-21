package com.example.mygithubapp.Repo

import com.example.mygithubapp.Data.RetrofitService

class PullRequestRepository {

    suspend fun fetchPRDetails(owner: String, repo: String) =
        RetrofitService.retrofitApi.getGithubPrDetails(owner, repo, "closed")
}