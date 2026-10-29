
package com.example.oneplayer

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(),
    BookListFragment.OnBookSelectedListener {

    private val bookViewModel: BookViewModel by viewModels()

    private var isTwoPane = false

    companion object {
        private const val STATE_TWO_PANE = "twoPane"
        private const val TAG_LIST = "bookList"
        private const val TAG_SINGLE_PLAYER = "singlePlayer"
        private const val TAG_TWO_PANE_PLAYER = "twoPanePlayer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTwoPane =
            findViewById<View>(R.id.bookDetailContainer) != null

        val previousTwoPane =
            savedInstanceState?.getBoolean(STATE_TWO_PANE)

        val layoutChanged =
            previousTwoPane != null &&
                    previousTwoPane != isTwoPane

        val manager = supportFragmentManager

        if (layoutChanged) {
            manager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            if (!isTwoPane) {
                manager.findFragmentByTag(
                    TAG_TWO_PANE_PLAYER
                )?.let { oldPlayer ->
                    manager.beginTransaction()
                        .remove(oldPlayer)
                        .commitNow()
                }
            }
        }

        if (isTwoPane) {
            setupTwoPane()
        } else {
            setupSinglePane()

            if (previousTwoPane == true &&
                bookViewModel.getSelectedBook() != null
            ) {
                showBookPlayer()
            }
        }
    }

    private fun setupTwoPane() {
        val manager = supportFragmentManager

        if (manager.findFragmentByTag(TAG_LIST) == null) {
            manager.beginTransaction()
                .replace(
                    R.id.bookListContainer,
                    BookListFragment(),
                    TAG_LIST
                )
                .commitNow()
        }

        if (manager.findFragmentByTag(
                TAG_TWO_PANE_PLAYER
            ) == null
        ) {
            manager.beginTransaction()
                .replace(
                    R.id.bookDetailContainer,
                    BookPlayerFragment(),
                    TAG_TWO_PANE_PLAYER
                )
                .commitNow()
        }
    }

    private fun setupSinglePane() {
        val manager = supportFragmentManager

        val existingList =
            manager.findFragmentByTag(TAG_LIST)

        val existingPlayer =
            manager.findFragmentByTag(TAG_SINGLE_PLAYER)

        if (existingList == null && existingPlayer == null) {
            manager.beginTransaction()
                .replace(
                    R.id.bookListContainer,
                    BookListFragment(),
                    TAG_LIST
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
        val manager = supportFragmentManager

        if (manager.findFragmentByTag(
                TAG_SINGLE_PLAYER
            ) != null
        ) {
            return
        }

        manager.beginTransaction()
            .replace(
                R.id.bookListContainer,
                BookPlayerFragment(),
                TAG_SINGLE_PLAYER
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_TWO_PANE, isTwoPane)
        super.onSaveInstanceState(outState)
    }
}
