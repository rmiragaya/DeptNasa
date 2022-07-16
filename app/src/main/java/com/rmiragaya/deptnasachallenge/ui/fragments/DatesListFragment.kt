package com.rmiragaya.deptnasachallenge.ui.fragments

import android.os.Bundle
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
import com.rmiragaya.deptnasachallenge.models.DownloadState.SUCCES
import com.rmiragaya.deptnasachallenge.models.PhotoList
import com.rmiragaya.deptnasachallenge.ui.activity.MainActivity
import com.rmiragaya.deptnasachallenge.ui.viewmodel.MainViewmodel
import com.rmiragaya.deptnasachallenge.utils.Constants.Companion.OBJECT_KEY
import com.rmiragaya.deptnasachallenge.utils.Resource

class DatesListFragment : Fragment() {

    private var _binding: DatesListFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var listViewmodel: MainViewmodel
    private val datesListAdapter by lazy {
        DatesListAdapter(
            onClick = { date -> navigateToDatePhotos(date) },
            onScreen = { date -> listViewmodel.getDatePhotos(date) }
        )
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

        setUpRecyclerView()

        setUpObserver()
    }

    private fun setUpObserver() {
        listViewmodel.dateListResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar(false)
                    response.data?.let { listResponse ->
                        datesListAdapter.setData(listResponse)
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
            getDatesPhotos()

        }

        listViewmodel.dateLoading.observe(viewLifecycleOwner) {
            it?.let { dateLoading ->
                updateDateList(dateLoading)
            }
        }
    }

    private fun getDatesPhotos() {
        val fullList = listViewmodel.dateListResponse.value?.data
        val date = fullList?.firstOrNull { it.downloadState == null }?.date
        date?.let {
            if (listViewmodel.dateListResponse.value?.data?.find { date == it.date }?.downloadState == null) {
                listViewmodel.getDatePhotos(date)
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

    private fun openPhotosFragment(item: PhotoList) {
        val bundle = Bundle().apply {
            putParcelable(OBJECT_KEY, item)
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

//                if (dateLoading.downloadState == SUCCES) {
////                    getDatesPhotos(datesListAdapter.getNextDate(it))
//                    getDatesPhotos()
//                }
            }
        }
    }
}

