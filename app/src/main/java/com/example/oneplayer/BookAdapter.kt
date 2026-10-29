
package com.example.oneplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val bookList: BookList,
    private val onBookClicked: (Int) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView =
            itemView.findViewById(R.id.bookTitleTextView)

        val authorTextView: TextView =
            itemView.findViewById(R.id.bookAuthorTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_book, parent, false)

        return BookViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int
    ) {
        val book = bookList.get(position)

        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author

        holder.itemView.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                onBookClicked(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size()
    }
}
