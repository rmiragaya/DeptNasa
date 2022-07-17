package com.rmiragaya.deptnasachallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rmiragaya.deptnasachallenge.R
import com.rmiragaya.deptnasachallenge.databinding.DatesListFragmentBinding
import com.rmiragaya.deptnasachallenge.models.DateResponseItem
import com.rmiragaya.deptnasachallenge.models.DownloadState
import com.rmiragaya.deptnasachallenge.models.PhotoList
import com.rmiragaya.deptnasachallenge.ui.activity.MainActivity
import com.rmiragaya.deptnasachallenge.ui.viewmodel.MainViewmodel
import com.rmiragaya.deptnasachallenge.utils.Constants.Companion.PHOTO_LIST
import com.rmiragaya.deptnasachallenge.utils.Resource

class DatesListFragment : Fragment() {

    private var _binding: DatesListFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var listViewmodel: MainViewmodel
    private val datesListAdapter by lazy { DatesListAdapter { date -> navigateToDatePhotos(date) }}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DatesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewmodel = (activity as MainActivity).viewmodel

        setUpObserver()

        setUpRecyclerView()
    }

    private fun setUpObserver() {
        listViewmodel.dateListResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar(false)
                    response.data?.let { listResponse ->
                        datesListAdapter.setData(listResponse)
                        getDatesPhotos(0)
                    }
                }
                is Resource.Error -> {
                    progressBar(false)
                    showSnackBar(response.message)
                }
                is Resource.Loading -> {
                    progressBar(true)
                }
            }
        }

        listViewmodel.dateLoading.observe(viewLifecycleOwner) {
            it?.date?.let { date ->
                Log.d("SEARCH", "search date ${date} result ${it.downloadState?.name}")
                updateDateList(it)
                if (it.downloadState == DownloadState.SUCCES) {
                    getDatesPhotos(datesListAdapter.getNextDate(date))
                }
            }
        }
    }

    private fun getDatesPhotos(indexDate: Int?) {
        indexDate?.let {
            Log.d("SEARCH", "go get index $indexDate")
            val fullList = listViewmodel.dateListResponse.value?.data
            val dateToSearch = fullList?.get(it)
            if (dateToSearch?.downloadState == DownloadState.IDLE || dateToSearch?.downloadState == null) {
                listViewmodel.getDatePhotos(dateToSearch?.date)
            }
        }
    }

    private fun progressBar(visible: Boolean) {
        with(binding.listFragmentPb) {
            visibility = if (visible) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    private fun showSnackBar(responseMessage: String?) {
        this.view?.let { it ->
            val message =
                if (!responseMessage.isNullOrEmpty()) responseMessage else getString(R.string.generic_error)
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecyclerView() {
        binding.listFragmentRv.apply {
            adapter = datesListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun navigateToDatePhotos(date: PhotoList) {
        date.photoList?.let {
            if (it.isEmpty()) {
                showSnackBar(getString(R.string.not_yet))
            } else {
                openPhotosFragment(date)
            }
        } ?: run {
            showSnackBar(getString(R.string.not_yet))
        }
    }

    private fun openPhotosFragment(item: PhotoList) {
        val bundle = Bundle().apply {
            putParcelable(PHOTO_LIST, item)
        }
        findNavController().navigate(
            R.id.action_dateListFragment_to_photosFragment,
            bundle
        )
    }

    private fun updateDateList(dateLoading: DateResponseItem) {
        val listaCompleta = listViewmodel.dateListResponse.value?.data
        listaCompleta?.let {
            val index = listaCompleta.withIndex().find { dateLoading.date == it.value.date }?.index
            index?.let {
                listaCompleta[index].apply {
                    downloadState = dateLoading.downloadState
                    datePhotos = dateLoading.datePhotos
                }
                datesListAdapter.setData(listaCompleta, index)
            }
        }
    }
}

