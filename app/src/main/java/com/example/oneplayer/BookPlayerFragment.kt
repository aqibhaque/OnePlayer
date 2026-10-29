
package com.example.oneplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class BookPlayerFragment :
    Fragment(R.layout.fragment_book_player) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val existingFragment =
            childFragmentManager.findFragmentById(
                R.id.bookFragmentContainer
            )

        if (existingFragment == null) {
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.bookFragmentContainer,
                    BookFragment()
                )
                .commit()
        }
    }
}
