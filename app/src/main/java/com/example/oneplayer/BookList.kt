
package com.example.oneplayer

class BookList {

    private val books = ArrayList<Book>()

    fun add(book: Book) {
        books.add(book)
    }

    fun get(index: Int): Book {
        return books[index]
    }

    fun size(): Int {
        return books.size
    }
}
