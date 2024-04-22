package com.example.android.bookdiary.editingpage

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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
import com.example.android.bookdiary.databinding.FragmentEditingBinding
import getIndexSpinner


class EditingFragment : Fragment() {
    private lateinit var binding: FragmentEditingBinding
    private lateinit var viewModel: EditingViewModel
    private lateinit var spinner: Spinner
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_editing, container, false)
        val application = requireNotNull(this.activity).application
        val args = EditingFragmentArgs.fromBundle(requireArguments())
        val dao = BookDiaryDatabase.getInstance(application).getBookDiaryDatabaseDao()
        val viewModelFactory = EditingViewModelFactory(args.bookId, dao)
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
            .get(EditingViewModel::class.java)

        binding.addButton.setOnClickListener(){
            if(Integer.parseInt(binding.pageReadEditText.text.toString()) > Integer.parseInt(binding.pageCountEditText.text.toString())){
                Toast.makeText(requireContext(), "Неверно указано соотношение прочитанного к общему количеству страниц", Toast.LENGTH_SHORT).show()

            }else {
                viewModel.onSetBook(
                    binding.titleEditText.text.toString(),
                    binding.authorEditText.text.toString(),
                    binding.genreEditText.text.toString(),
                    binding.pageCountEditText.text.toString(),
                    binding.pageReadEditText.text.toString(),
                    binding.statusSpinner.selectedItem.toString(),
                    viewModel.coverLink.value!!,
                )

            }
        }

        binding.selectImageButton.setOnClickListener(){
            openGallery()
        }

        viewModel.currentBook.observe(viewLifecycleOwner, Observer { book ->
            binding.titleEditText.setText(book.name)
            binding.authorEditText.setText(book.author)
            binding.genreEditText.setText(book.genre)
            val bitmap = BitmapFactory.decodeByteArray(book.coverPath, 0, book.coverPath.size)
            binding.coverImageView.setImageBitmap(bitmap)
            binding.statusSpinner.setSelection(getIndexSpinner(book.status))
            binding.pageCountEditText.setText(book.pageNumber.toString())
            binding.pageReadEditText.setText(book.pageRead.toString())
        })

        viewModel.navigateToMain.observe(viewLifecycleOwner,  Observer { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    EditingFragmentDirections
                        .actionEditingFragmentToMainFragment())
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