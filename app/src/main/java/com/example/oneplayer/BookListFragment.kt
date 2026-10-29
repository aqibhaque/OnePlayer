
package com.example.oneplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookListFragment : Fragment(R.layout.fragment_book_list) {

    private val bookViewModel: BookViewModel by activityViewModels()

    interface OnBookSelectedListener {
        fun onBookSelected()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.bookRecyclerView)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter = BookAdapter(
            bookViewModel.bookList
        ) { position ->

            bookViewModel.selectBook(position)

            (activity as? OnBookSelectedListener)
                ?.onBookSelected()
        }
    }
}
