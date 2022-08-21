package com.example.mygithubapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mygithubapp.R
import com.example.mygithubapp.Utils.Resource
import com.example.mygithubapp.VM.PullRequestVM
import com.example.mygithubapp.databinding.FragmentPullRequestsBinding
import kotlinx.android.synthetic.main.layout_error_empty_state.view.*

class PullRequestFragment : Fragment() {

    private lateinit var viewModel: PullRequestVM
    private lateinit var binding : FragmentPullRequestsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_pull_requests, container, false)
        viewModel = (activity as MainActivity).viewModel
        initTasks()
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = viewModel.pullRequestAdapter
    }

    private fun init() {
        initApiObservers()
        binding.swipeRefresh.setOnRefreshListener {
            initTasks()
        }
    }

    private fun initTasks() {
        viewModel.getPRDetails(viewModel.owner, viewModel.repo)
    }

    private fun initApiObservers() {
        viewModel.pullRequestDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    stopLoadingProgress()
                    response.data?.let {
                        if(it.isEmpty())
                            binding.noPrEmptyState.emptyView.visibility = View.VISIBLE
                        else
                            viewModel.initPRList(it)
                    }
                }

                is Resource.Error -> {
                    stopLoadingProgress()
                    binding.connectionErrorState.visibility = View.VISIBLE
                    binding.connectionErrorState.retryBtn.setOnClickListener {
                        initTasks()
                    }
                }

                is Resource.Loading -> {
                    binding.LoadingInProgress.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun stopLoadingProgress() {
        binding.LoadingInProgress.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    companion object {
        const val TAG = "PullRequestFragment"
        fun newInstance(): PullRequestFragment {
            return PullRequestFragment()
        }
    }
}