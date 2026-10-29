
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTwoPane =
            findViewById<View?>(R.id.bookDetailContainer) != null

        val previousTwoPane =
            savedInstanceState?.getBoolean("twoPane")

        if (previousTwoPane != null &&
            previousTwoPane != isTwoPane
        ) {
            supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            val transaction =
                supportFragmentManager.beginTransaction()

            supportFragmentManager.fragments.forEach {
                transaction.remove(it)
            }

            transaction.commitNow()
        }

        if (isTwoPane) {
            setupTwoPane()
        } else {
            setupSinglePane()
        }
    }

    private fun setupTwoPane() {
        val manager = supportFragmentManager

        if (manager.findFragmentById(
                R.id.bookListContainer
            ) == null
        ) {
            manager.beginTransaction()
                .add(
                    R.id.bookListContainer,
                    BookListFragment()
                )
                .commitNow()
        }

        if (manager.findFragmentById(
                R.id.bookDetailContainer
            ) == null
        ) {
            manager.beginTransaction()
                .add(
                    R.id.bookDetailContainer,
                    BookPlayerFragment()
                )
                .commitNow()
        }
    }

    private fun setupSinglePane() {
        val manager = supportFragmentManager

        if (manager.findFragmentById(
                R.id.bookListContainer
            ) != null
        ) {
            return
        }

        manager.beginTransaction()
            .add(
                R.id.bookListContainer,
                BookListFragment()
            )
            .commitNow()

        if (bookViewModel.getSelectedBook() != null) {
            showBookPlayer()
        }
    }

    override fun onBookSelected() {
        if (!isTwoPane) {
            showBookPlayer()
        }
    }

    private fun showBookPlayer() {
        if (supportFragmentManager.findFragmentById(
                R.id.bookListContainer
            ) is BookPlayerFragment
        ) {
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.bookListContainer,
                BookPlayerFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("twoPane", isTwoPane)
        super.onSaveInstanceState(outState)
    }
}
