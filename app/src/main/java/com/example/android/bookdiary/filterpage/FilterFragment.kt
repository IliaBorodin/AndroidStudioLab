package com.example.android.bookdiary.filterpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.bookdiary.database.BookDiaryDatabase
import com.example.android.bookdiary.databinding.FragmentFilterBinding


class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var viewModel: FilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, com.example.android.bookdiary.R.layout.fragment_filter, container, false)
        val application = requireNotNull(this.activity).application
        val dao = BookDiaryDatabase.getInstance(application).getBookDiaryDatabaseDao()
        val viewModelFactory = FilterViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FilterViewModel::class.java)
        var array:Array<String> = arrayOf("Любой")
        var mas:Array<String> = arrayOf("123")
       // val tempList = splitAuthors(viewModel.authorList.value!!)
       // val authorsArray = tempList.toTypedArray()
       // val spinner = binding.root.findViewById<Spinner>(R.id.authorSpinner)
       // val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("Значение 1", "Значение 2", "Значение 3"))
       // spinner.adapter = adapter
        viewModel.authorList.observe(viewLifecycleOwner, Observer { books ->
            if (books != null)
                mas = books.toTypedArray()
                val newmas = array + mas
                func(newmas)
        })









        return binding.root
    }
    fun func(array:Array<String>){
       // val authorsArray = arrayOf("Автор 1", "Автор 2", "Автор 3")
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            array

        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.authorSpinner.adapter = adapter
        binding.authorSpinner.setSelection(0)
    }

}