
package com.example.oneplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import org.json.JSONArray

class MainActivity : AppCompatActivity(),
    BookListFragment.OnBookSelectedListener {

    private val bookViewModel: BookViewModel by viewModels()
    private var isTwoPane = false

    companion object {
        private const val STATE_TWO_PANE = "twoPane"
        private const val STATE_DETAIL_VISIBLE = "detailVisible"
        private const val TAG_LIST = "bookList"
        private const val TAG_SINGLE_PLAYER = "singlePlayer"
        private const val TAG_TWO_PANE_PLAYER = "twoPanePlayer"
    }

    private val searchLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {
                val response = result.data?.getStringExtra(
                    SearchActivity.EXTRA_RESULTS_JSON
                ) ?: return@registerForActivityResult

                try {
                    val jsonArray = JSONArray(response)
                    val books = ArrayList<Book>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonBook = jsonArray.getJSONObject(i)

                        val book = Book(
                            title = jsonBook.getString("book_title"),
                            author = jsonBook.getString("author_name"),
                            id = jsonBook.getInt("book_id"),
                            coverURI = jsonBook.getString("cover_uri")
                        )

                        books.add(book)
                    }

                    bookViewModel.replaceBooks(books)
                    showBookListAfterSearch()

                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Unable to display search results",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTwoPane =
            findViewById<View>(R.id.bookDetailContainer) != null

        findViewById<Button>(R.id.searchButton)
            .setOnClickListener {
                val intent = Intent(
                    this,
                    SearchActivity::class.java
                )
                searchLauncher.launch(intent)
            }

        val previousTwoPane =
            savedInstanceState?.getBoolean(STATE_TWO_PANE)

        val previousDetailVisible =
            savedInstanceState?.getBoolean(STATE_DETAIL_VISIBLE)
                ?: false

        val manager = supportFragmentManager

        if (previousTwoPane != null &&
            previousTwoPane != isTwoPane
        ) {
            manager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            removeFragment(TAG_SINGLE_PLAYER)
            removeFragment(TAG_TWO_PANE_PLAYER)
        }

        removeFragment("bookPlayer")

        if (manager.findFragmentByTag(TAG_LIST) == null) {
            manager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.bookListContainer,
                    BookListFragment(),
                    TAG_LIST
                )
                .commitNow()
        }

        if (isTwoPane) {
            if (manager.findFragmentByTag(
                    TAG_TWO_PANE_PLAYER
                ) == null
            ) {
                manager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(
                        R.id.bookDetailContainer,
                        BookPlayerFragment(),
                        TAG_TWO_PANE_PLAYER
                    )
                    .commitNow()
            }
        } else {
            val showSelectedBook =
                if (previousTwoPane == true) {
                    bookViewModel.getSelectedBook() != null
                } else {
                    previousDetailVisible
                }

            if (showSelectedBook &&
                bookViewModel.getSelectedBook() != null &&
                manager.findFragmentByTag(
                    TAG_SINGLE_PLAYER
                )?.isAdded != true
            ) {
                showPortraitPlayer()
            }
        }
    }

    override fun onBookSelected() {
        if (!isTwoPane) {
            showPortraitPlayer()
        }
    }

    private fun showPortraitPlayer() {
        val manager = supportFragmentManager

        if (manager.findFragmentByTag(
                TAG_SINGLE_PLAYER
            )?.isAdded == true
        ) {
            return
        }

        manager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(
                R.id.bookListContainer,
                BookPlayerFragment(),
                TAG_SINGLE_PLAYER
            )
            .addToBackStack(TAG_SINGLE_PLAYER)
            .commit()
    }

    private fun showBookListAfterSearch() {
        if (!isTwoPane) {
            supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    private fun removeFragment(tag: String) {
        val manager = supportFragmentManager

        manager.findFragmentByTag(tag)?.let { fragment ->
            manager.beginTransaction()
                .remove(fragment)
                .commitNow()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_TWO_PANE, isTwoPane)

        val detailVisible =
            if (isTwoPane) {
                bookViewModel.getSelectedBook() != null
            } else {
                supportFragmentManager.findFragmentByTag(
                    TAG_SINGLE_PLAYER
                )?.isAdded == true
            }

        outState.putBoolean(
            STATE_DETAIL_VISIBLE,
            detailVisible
        )

        super.onSaveInstanceState(outState)
    }
}
