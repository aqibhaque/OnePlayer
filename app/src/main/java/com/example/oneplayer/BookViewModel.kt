
package com.example.oneplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class BookViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookList = BookList()

    private val _booksChanged = MutableLiveData(0)
    val booksChanged: LiveData<Int> = _booksChanged

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

    fun replaceBooks(newBooks: List<Book>) {
        bookList.replaceBooks(newBooks)

        savedStateHandle["selectedBookIndex"] = -1

        _booksChanged.value =
            (_booksChanged.value ?: 0) + 1
    }
}
