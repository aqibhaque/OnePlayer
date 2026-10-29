
package com.example.oneplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class BookViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookList = BookList().apply {
        add(Book("The Great Gatsby", "F. Scott Fitzgerald"))
        add(Book("To Kill a Mockingbird", "Harper Lee"))
        add(Book("1984", "George Orwell"))
        add(Book("Pride and Prejudice", "Jane Austen"))
        add(Book("The Hobbit", "J.R.R. Tolkien"))
        add(Book("Moby Dick", "Herman Melville"))
        add(Book("The Catcher in the Rye", "J.D. Salinger"))
        add(Book("The Alchemist", "Paulo Coelho"))
        add(Book("Brave New World", "Aldous Huxley"))
        add(Book("The Lord of the Rings", "J.R.R. Tolkien"))
        add(Book("Jane Eyre", "Charlotte Bronte"))
        add(Book("Frankenstein", "Mary Shelley"))
    }

    val selectedBookIndex: LiveData<Int> =
        savedStateHandle.getLiveData("selectedBookIndex")

    fun selectBook(index: Int) {
        if (index >= 0 && index < bookList.size()) {
            savedStateHandle["selectedBookIndex"] = index
        }
    }

    fun getSelectedBook(): Book? {
        val index = selectedBookIndex.value ?: return null

        return if (index >= 0 && index < bookList.size()) {
            bookList.get(index)
        } else {
            null
        }
    }
}
