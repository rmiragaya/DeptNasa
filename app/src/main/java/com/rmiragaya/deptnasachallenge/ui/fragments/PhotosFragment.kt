package com.rmiragaya.deptnasachallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rmiragaya.deptnasachallenge.databinding.PhotosFragmentBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.rmiragaya.deptnasachallenge.models.DatePhotosItem
import com.rmiragaya.deptnasachallenge.models.PhotoList


class PhotosFragment : Fragment() {

    private var _binding: PhotosFragmentBinding? = null
    private val binding get() = _binding!!

    private var photoList : PhotoList? = null
    private val args: PhotosFragmentArgs by navArgs()

    private val photoGridAdapter by lazy { PhotoGridAdapter { photo -> openPhoto(photo) } }

    private fun openPhoto(photo: DatePhotosItem) {
        Log.d("open photo", "openPhoto: ${photo.identifier}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        bindViews()
    }

    private fun setUpRecyclerView() {
        binding.photoListFragmentRv.apply {
            adapter = photoGridAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }


    private fun bindViews() {
        photoList = args.objectKey
        photoList.let {photoGridAdapter.differ.submitList(it?.photoList) }
    }

}