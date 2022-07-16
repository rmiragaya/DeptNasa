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
import com.rmiragaya.deptnasachallenge.models.DateResponse
import com.rmiragaya.deptnasachallenge.ui.activity.MainActivity
import com.rmiragaya.deptnasachallenge.ui.viewmodel.MainViewmodel
import com.rmiragaya.deptnasachallenge.utils.Constants.Companion.OBJECT_KEY
import com.rmiragaya.deptnasachallenge.utils.Resource

class DateListFragment : Fragment() {

    private var _binding: DatesListFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var listViewmodel: MainViewmodel
    private val listDatesAdapter by lazy {
        ListDatesAdapter(onClick = { date -> navigateToDatePhotos(date) })
    }

    private fun navigateToDatePhotos(date: DateResponse?) {
        date?.let {
            if (date.listOfDates.isEmpty()) {
                // todo not data yet
                Log.d("Detail", "not data yet")
            } else {
                Log.d("Detail", "navegar a la siguiente pantalla")
                openPhotosFragment(date)
            }
        } ?: run {
        // todo not data yet
            Log.d("Detail", "not data yet")
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
                        listDatesAdapter.differ.submitList(listResponse)
                        listDatesAdapter.differ.
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
            Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun setUpRecyclerView() {
        binding.listFragmentRv.apply {
            adapter = listDatesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun openPhotosFragment(item: DateResponse) {
        val bundle = Bundle().apply {
            putParcelable(OBJECT_KEY, item)
        }
        findNavController().navigate(
            R.id.action_dateListFragment_to_photosFragment,
            bundle
        )
    }


}

