package com.example.mygithubapp.VM

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubapp.Data.PullRequestResponse
import com.example.mygithubapp.UI.PullRequestAdapter
import com.example.mygithubapp.Repo.PullRequestRepository
import com.example.mygithubapp.Utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PullRequestVM(private val pullRequestRepository: PullRequestRepository) : ViewModel() {

    val pullRequestDetails : MutableLiveData<Resource<List<PullRequestResponse>>> = MutableLiveData()
    val pullRequestAdapter = PullRequestAdapter()
    val owner = "square"
    val repo = "retrofit"

    fun getPRDetails(owner: String, repo: String) = viewModelScope.launch {
        pullRequestDetails.postValue(Resource.Loading())
        val response = pullRequestRepository.fetchPRDetails(owner, repo)
        pullRequestDetails.postValue(handleContextualCardsResponse(response))
    }

    private fun handleContextualCardsResponse(response: Response<List<PullRequestResponse>>) :
            Resource<List<PullRequestResponse>>? {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    fun initPRList(data : List<PullRequestResponse>?) {
        pullRequestAdapter.setData(data)
    }

}