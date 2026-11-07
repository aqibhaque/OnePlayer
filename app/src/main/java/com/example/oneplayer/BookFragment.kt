
package com.example.oneplayer

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide

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

        val coverImageView =
            view.findViewById<ImageView>(R.id.selectedBookCoverImageView)

        fun displaySelectedBook() {
            val book = bookViewModel.getSelectedBook()

            if (book != null) {
                titleTextView.text = book.title
                authorTextView.text = book.author

                if (book.coverURI.isNotEmpty()) {
                    coverImageView.visibility = View.VISIBLE

                    Glide.with(this)
                        .load(book.coverURI)
                        .into(coverImageView)
                } else {
                    Glide.with(this).clear(coverImageView)
                    coverImageView.visibility = View.GONE
                }
            } else {
                titleTextView.text = "Select a Book"
                authorTextView.text = ""

                Glide.with(this).clear(coverImageView)
                coverImageView.visibility = View.GONE
            }
        }

        displaySelectedBook()

        bookViewModel.selectedBookIndex.observe(viewLifecycleOwner) {
            displaySelectedBook()
        }
    }
}
