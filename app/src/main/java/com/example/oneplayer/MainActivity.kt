
package com.example.oneplayer

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),
    BookListFragment.OnBookSelectedListener {

    private val bookViewModel: BookViewModel by viewModels()

    private var isTwoPane = false
    private var detailVisible = false
    private lateinit var backCallback: OnBackPressedCallback

    companion object {
        private const val STATE_TWO_PANE = "twoPane"
        private const val STATE_DETAIL_VISIBLE = "detailVisible"
        private const val TAG_LIST = "bookList"
        private const val TAG_PLAYER = "bookPlayer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configuration = resources.configuration

        isTwoPane =
            configuration.smallestScreenWidthDp >= 600 ||
                    configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val previousTwoPane =
            savedInstanceState?.getBoolean(STATE_TWO_PANE)

        detailVisible =
            savedInstanceState?.getBoolean(STATE_DETAIL_VISIBLE)
                ?: false

        if (previousTwoPane == true &&
            !isTwoPane &&
            bookViewModel.getSelectedBook() != null
        ) {
            detailVisible = true
        }

        val manager = supportFragmentManager

        if (manager.findFragmentByTag(TAG_LIST) == null) {
            manager.beginTransaction()
                .setReorderingAllowed(true)
                .add(
                    R.id.bookListContainer,
                    BookListFragment(),
                    TAG_LIST
                )
                .commitNow()
        }

        if (isTwoPane || detailVisible) {
            ensureBookPlayer()
        }

        backCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                showBookList()
            }
        }

        onBackPressedDispatcher.addCallback(this, backCallback)

        updatePaneVisibility()
    }

    private fun ensureBookPlayer() {
        val manager = supportFragmentManager

        if (manager.findFragmentByTag(TAG_PLAYER) == null) {
            manager.beginTransaction()
                .setReorderingAllowed(true)
                .add(
                    R.id.bookDetailContainer,
                    BookPlayerFragment(),
                    TAG_PLAYER
                )
                .commitNow()
        }
    }

    override fun onBookSelected() {
        if (!isTwoPane) {
            showBookPlayer()
        }
    }

    private fun showBookPlayer() {
        ensureBookPlayer()
        detailVisible = true
        updatePaneVisibility()
    }

    private fun showBookList() {
        detailVisible = false
        updatePaneVisibility()
    }

    private fun updatePaneVisibility() {
        val listContainer =
            findViewById<View>(R.id.bookListContainer)

        val detailContainer =
            findViewById<View>(R.id.bookDetailContainer)

        if (isTwoPane) {
            listContainer.visibility = View.VISIBLE
            detailContainer.visibility = View.VISIBLE
        } else {
            listContainer.visibility =
                if (detailVisible) View.GONE else View.VISIBLE

            detailContainer.visibility =
                if (detailVisible) View.VISIBLE else View.GONE
        }

        backCallback.isEnabled = !isTwoPane && detailVisible
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_TWO_PANE, isTwoPane)
        outState.putBoolean(STATE_DETAIL_VISIBLE, detailVisible)
        super.onSaveInstanceState(outState)
    }
}