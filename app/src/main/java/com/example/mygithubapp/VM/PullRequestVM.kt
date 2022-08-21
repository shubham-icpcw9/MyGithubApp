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

    var pageNo = 0
    val owner = "square"
    val repo = "retrofit"
    var listEndReached = false
    private var currDataList = arrayListOf<PullRequestResponse>()

    val pullRequestDetails : MutableLiveData<Resource<List<PullRequestResponse>>> = MutableLiveData()
    val pullRequestAdapter = PullRequestAdapter{
        //load next page .... triggered only when reaching end of page
        if(!listEndReached && currDataList.isNotEmpty()){
            getPRDetails(owner, repo, ++pageNo)
        }
    }

    fun getPRDetails(owner: String, repo: String, pageNo : Int) = viewModelScope.launch {
        pullRequestDetails.postValue(Resource.Loading())
        val response = pullRequestRepository.fetchPRDetails(owner, repo, pageNo)
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

    fun isFirstPage() = pageNo == 0

    fun initPRList(data : List<PullRequestResponse>?) {
        currDataList.clear()
        addPRList(data)
    }

    fun addPRList(data : List<PullRequestResponse>?){
        currDataList.addAll(data ?: arrayListOf())
        if(data.isNullOrEmpty()) listEndReached = true
        if(isFirstPage())
            pullRequestAdapter.setData(data)
        else
            pullRequestAdapter.addPRData(data)
    }

}