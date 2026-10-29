
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
        private const val STATE_DETAIL_VISIBLE = "detailVisible"
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
