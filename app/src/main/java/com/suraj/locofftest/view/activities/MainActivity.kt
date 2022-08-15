package com.suraj.locofftest.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.suraj.locofftest.R
import com.suraj.locofftest.data.response.ApiResponse
import com.suraj.locofftest.databinding.ActivityMainBinding
import com.suraj.locofftest.model.Users
import com.suraj.locofftest.utils.CheckValidation
import com.suraj.locofftest.utils.MessageLog
import com.suraj.locofftest.utils.PaginationScrollListener
import com.suraj.locofftest.view.adapters.UserAdapter
import com.suraj.locofftest.viewmodel.HomeViewModel
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mAdapter: UserAdapter

    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(HomeViewModel::class.java)

        initMyOrderRecyclerView()
        observerDataRequest()
    }

    private fun initMyOrderRecyclerView() {
        //attach adapter to  recycler
        mAdapter = UserAdapter(this@MainActivity)
        binding.userAdapter = mAdapter
        binding.viewRecycler.setHasFixedSize(true)
        binding.viewRecycler.itemAnimator = DefaultItemAnimator()

        loadFirstPage()

        binding.viewRecycler.addOnScrollListener(object : PaginationScrollListener(binding.viewRecycler.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                Handler(Looper.myLooper()!!).postDelayed({
                    loadNextPage()
                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
    }

    private fun loadFirstPage() {
        hideErrorView()
        if (CheckValidation.isConnected(this)) {
            homeViewModel.requestFirstPageUsers(currentPage)
        }else{
            showErrorView(null)
        }
    }

    fun loadNextPage() {
        if (CheckValidation.isConnected(this)) {
            homeViewModel.requestFirstNextPageUsers(currentPage)
        }else{
            mAdapter.showRetry(true, fetchErrorMessage(null))
        }
    }

    private fun observerDataRequest(){
        homeViewModel.usersFirstPageResponse.observe(this) {
            if (it is ApiResponse) {
                hideErrorView()
                val results: MutableList<Users> = fetchResults(it) as MutableList<Users>
                binding.mainProgress.visibility = View.GONE
                mAdapter.addAll(results)
                totalPages = it.total_pages!!

                if (currentPage <= totalPages) mAdapter.addLoadingFooter()
                else isLastPage = true
            }else if (it is Throwable){
                showErrorView(it)
            }else{
                MessageLog.setLogCat("TAG_TEST" , "Error First Page")
            }
        }

        homeViewModel.usersNextPageResponse.observe(this) {
            if (it is ApiResponse) {

                val results = fetchResults(it) as MutableList<Users>
                mAdapter.removeLoadingFooter()
                isLoading = false
                mAdapter.addAll(results)

                if (currentPage != totalPages) mAdapter.addLoadingFooter()
                else isLastPage = true

            }else if (it is Throwable){
                mAdapter.showRetry(true, fetchErrorMessage(it))
            }else{
                MessageLog.setLogCat("TAG_TEST" , "Error First Page")
            }

        }
    }

    private fun fetchResults(moviesTopModel: ApiResponse): List<Users>? {
        return moviesTopModel.data
    }

    private fun hideErrorView() {
        if (binding.lyError.errorLayout.visibility == View.VISIBLE) {
            binding.lyError.errorLayout.visibility = View.GONE
            binding.mainProgress.visibility = View.VISIBLE
        }
    }

    private fun showErrorView(throwable: Throwable?) {
        if (binding.lyError.errorLayout.visibility == View.GONE) {
            binding.lyError.errorLayout.visibility = View.VISIBLE
            binding.mainProgress.visibility = View.GONE

            if (!CheckValidation.isConnected(this)) {
                binding.lyError.errorTxtCause.setText(R.string.error_msg_no_internet)
            } else {
                if (throwable is TimeoutException) {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_timeout)
                } else {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_unknown)
                }
            }
        }
    }

    private fun fetchErrorMessage(throwable: Throwable?): String {
        var errorMsg: String = resources.getString(R.string.error_msg_unknown)

        if (!CheckValidation.isConnected(this)) {
            errorMsg = resources.getString(R.string.error_msg_no_internet)
        } else if (throwable is TimeoutException) {
            errorMsg = resources.getString(R.string.error_msg_timeout)
        }

        return errorMsg
    }
}