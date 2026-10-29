
package com.example.oneplayer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class BookFragment : Fragment(R.layout.fragment_book) {

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView =
            view.findViewById<TextView>(R.id.selectedBookTitleTextView)

        val authorTextView =
            view.findViewById<TextView>(R.id.selectedBookAuthorTextView)

        fun displaySelectedBook() {
            val book = bookViewModel.getSelectedBook()

            if (book != null) {
                titleTextView.text = book.title
                authorTextView.text = book.author
            } else {
                titleTextView.text = "Select a Book"
                authorTextView.text = ""
            }
        }

        displaySelectedBook()

        bookViewModel.selectedBookIndex.observe(viewLifecycleOwner) {
            displaySelectedBook()
        }
    }
}
