package com.example.android.bookdiary.mainpage

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.bookdiary.R
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabase
import com.example.android.bookdiary.databinding.FragmentMainBinding
import splitAuthors


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // registerPermission()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)
        val application = requireNotNull(this.activity).application
        val dao = BookDiaryDatabase.getInstance(application).getBookDiaryDatabaseDao()
        val viewModelFactory = MainViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        val adapter = BookDiaryAdapter(object : BookDiaryAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Long) {
                viewModel.onDeleteById(position)
            }

            override fun onEditClick(position: Long) {
                viewModel.onEdit(position)
            }
        })
        binding.bookList.adapter = adapter
        checkPermission()

        viewModel.books.observe(viewLifecycleOwner, Observer { books ->
            viewModel.setCurrList()
           // if (books != null)
               // adapter.data = viewModel.currentList.value!!
        })

        viewModel.currentList.observe(viewLifecycleOwner, Observer{ books->
            if(books!=null)
                adapter.data = books
        })

        binding.addBtn.setOnClickListener {
            viewModel.onAdding()
        }

        binding.filterBtn.setOnClickListener{
            viewModel.onFilter(binding.statusSpinner.selectedItem.toString(),
                binding.authorSpinner.selectedItem.toString())

           // adapter.data = viewModel.books.value!!
        }





      //  binding.bookList.


        viewModel.navigationToCreate.observe(viewLifecycleOwner, Observer { book ->
            if (book!!) {
                this.findNavController().navigate(
                    MainFragmentDirections
                        .actionMainFragmentToCreatureFragment())
                            viewModel.doneNavigating()
            }
        })


        viewModel.navigateToEditBook.observe(viewLifecycleOwner, Observer{ book ->
            if (book != null) {
                this.findNavController().navigate(
                    MainFragmentDirections
                        .actionMainFragmentToEditingFragment(book.bookId))
                            viewModel.doneNavigatingTwo()
            }
        })

        viewModel.authors.observe(viewLifecycleOwner, Observer { authors ->
            if (authors != null)
                getContentToSpinner(authors.toTypedArray())



        })


        return binding.root
    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_GRANTED->{

            }

            else->{
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)


            }
        }

    }


    private fun getContentToSpinner(array:Array<String>){
        // val authorsArray = arrayOf("Автор 1", "Автор 2", "Автор 3"
        val _array = splitAuthors(array.toList())
        val result = arrayOf("All") + _array
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            result

        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.authorSpinner.adapter = adapter
        binding.authorSpinner.setSelection(0)
    }







}