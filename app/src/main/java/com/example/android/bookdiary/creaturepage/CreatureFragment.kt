package com.example.android.bookdiary.creaturepage

import UriToByteArrayConverter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.bookdiary.R
import com.example.android.bookdiary.database.BookDiaryDatabase
import com.example.android.bookdiary.databinding.FragmentCreatureBinding


class CreatureFragment : Fragment() {
    private lateinit var binding: FragmentCreatureBinding
    private lateinit var viewModel: CreatureViewModel
    private lateinit var spinner:Spinner

    //----------------------------------------------------------------

    //----------------------------------------------------------------


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_creature, container, false)
        val application = requireNotNull(this.activity).application

        val dao = BookDiaryDatabase.getInstance(application).getBookDiaryDatabaseDao()
        val viewModelFactory = CreatureViewModelFactory( dao)
        spinner = binding.statusSpinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedStatus = parentView?.getItemAtPosition(position).toString()
                val pageReadEditText = binding.pageReadEditText
                when(selectedStatus) {
                    "Read", "Will read" ->{
                        pageReadEditText.visibility = View.GONE
                        binding.pageReadEditText.setText("0")
                    }
                    else -> pageReadEditText.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

         viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreatureViewModel::class.java)

        binding.addButton.setOnClickListener(){
            if(Integer.parseInt(binding.pageReadEditText.text.toString()) > Integer.parseInt(binding.pageCountEditText.text.toString())){
                Toast.makeText(requireContext(), "Неверно указано соотношение прочитанного к общему количеству страниц", Toast.LENGTH_SHORT).show()

            }
            else {
                (viewModel.coverLink.value ?: ByteArray(0))?.let { it ->
                    viewModel.onSetBook(
                        binding.titleEditText.text.toString(),
                        binding.authorEditText.text.toString(),
                        binding.genreEditText.text.toString(),
                        binding.pageCountEditText.text.toString(),
                        binding.pageReadEditText.text.toString(),
                        binding.statusSpinner.selectedItem.toString(),
                        it,
                    )
                }
            }

        }

        binding.selectImageButton.setOnClickListener(){
            openGallery()
        }

        viewModel.navigateToMain.observe(viewLifecycleOwner,  Observer { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(CreatureFragmentDirections
                    .actionCreatureFragmentToMainFragment())
                viewModel.doneNavigating()
            }
        })


        return binding.root
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            binding.coverImageView.setImageURI(selectedImageUri)
            val byteArray = UriToByteArrayConverter.uriToByteArray(requireContext(), selectedImageUri!!)
            viewModel.changeLink(byteArray!!)

        }
    }




}