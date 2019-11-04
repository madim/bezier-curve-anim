package com.example.aviasalesanim.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.aviasalesanim.R
import com.example.aviasalesanim.ui.model.EventObserver
import com.example.aviasalesanim.ui.model.Nav
import com.example.aviasalesanim.ui.search.SearchCityFragment
import com.example.aviasalesanim.ui.search.SearchCityViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SelectCitiesFragment : Fragment() {

    private val viewModel: SearchCityViewModel by sharedViewModel()

    private lateinit var departureCity: TextView
    private lateinit var destinationCity: TextView
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.select_cities_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        departureCity = view.findViewById(R.id.departure)
        departureCity.setOnClickListener {
            viewModel.navigateTo(Nav.Departure)
        }

        destinationCity = view.findViewById(R.id.destination)
        destinationCity.setOnClickListener {
            viewModel.navigateTo(Nav.Destination)
        }

        searchButton = view.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            viewModel.navigateTo(Nav.Map)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.departureCity.observe(viewLifecycleOwner, Observer { city ->
            departureCity.text = city.fullname
        })
        viewModel.destinationCity.observe(viewLifecycleOwner, Observer { city ->
            destinationCity.text = city.fullname
        })
        viewModel.nav.observe(viewLifecycleOwner, EventObserver { navigation ->
            when (navigation) {
                Nav.Departure -> navigate(SearchCityFragment.newInstance(getString(R.string.departure)))
                Nav.Destination -> navigate(SearchCityFragment.newInstance(getString(R.string.destination)))
                Nav.Map -> navigate(MapFragment())
            }
        })
    }

    private fun navigate(fragment: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}
