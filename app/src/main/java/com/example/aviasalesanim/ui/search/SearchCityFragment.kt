package com.example.aviasalesanim.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.aviasalesanim.R
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchCityFragment : Fragment() {

    private val viewModel: SearchCityViewModel by sharedViewModel()
    private val adapter = CityAdapter { city ->
        viewModel.selectCity(city)
        fragmentManager?.popBackStack()
    }

    private lateinit var clearQuery: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.search_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(TITLE_KEY)
        val query: EditText = view.findViewById(R.id.query)
        query.hint = title
        query.addTextChangedListener(queryWatcher)

        clearQuery = view.findViewById(R.id.clear_query)
        clearQuery.setOnClickListener { query.setText("") }

        val results: RecyclerView = view.findViewById(R.id.results)
        results.adapter = adapter

        viewModel.cities.observe(viewLifecycleOwner, Observer { cities ->
            adapter.submitList(cities)
        })
    }

    private val queryWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.onQueryChanged(query = s.toString())
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            clearQuery.isVisible = s.isNotEmpty()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }

    companion object {

        const val TITLE_KEY = "title"

        fun newInstance(title: String): Fragment {
            return SearchCityFragment().apply {
                arguments = bundleOf(TITLE_KEY to title)
            }
        }
    }
}
