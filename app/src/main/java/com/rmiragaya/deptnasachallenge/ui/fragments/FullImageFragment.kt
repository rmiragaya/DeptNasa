package com.rmiragaya.deptnasachallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rmiragaya.deptnasachallenge.R
import com.rmiragaya.deptnasachallenge.databinding.FullImageFragmentBinding
import com.rmiragaya.deptnasachallenge.models.DatePhotosItem
import com.rmiragaya.deptnasachallenge.ui.activity.MainActivity


class FullImageFragment : Fragment() {

    private var _binding: FullImageFragmentBinding? = null
    private val binding get() = _binding!!

    private var photoItem : DatePhotosItem? = null
    private val args: FullImageFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FullImageFragmentBinding.inflate(inflater, container, false)
        setToolbar()
        return binding.root
    }

    private fun setToolbar() {
        with(requireActivity() as MainActivity){
            this.setSupportActionBar(this.binding.toolbar)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.full_image_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.showinfo -> {
                sgowDialogWithInfo()
                Log.d("MENU", "Will post the photo to server")
                true
            }
            else -> true
        }
    }

    private fun sgowDialogWithInfo() {
        MaterialAlertDialogBuilder(requireActivity(),
        com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setMessage(photoItem.toString())
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        photoItem = args.photoItem
        val dateFormat = photoItem?.date?.replace("-", "/")

        photoItem.let {
            Glide.with(this)
                .load("https://epic.gsfc.nasa.gov/archive/enhanced/${dateFormat?.split(" ")?.first()}/png/${photoItem?.image}.png")
//                .transform(CenterCrop(), RoundedCorners(25))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.photo)
        }
    }
}