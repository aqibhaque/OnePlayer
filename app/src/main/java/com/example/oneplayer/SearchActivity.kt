
package com.example.oneplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class SearchActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULTS_JSON = "searchResultsJson"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchEditText =
            findViewById<EditText>(R.id.searchEditText)

        val searchButton =
            findViewById<Button>(R.id.performSearchButton)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()

            searchButton.isEnabled = false

            thread {
                var connection: HttpURLConnection? = null

                try {
                    val encodedQuery =
                        URLEncoder.encode(query, "UTF-8")

                    val url = URL(
                        "https://kamorris.com/lab/audlibplayer/searchbooks.php?query=$encodedQuery"
                    )

                    connection =
                        url.openConnection() as HttpURLConnection

                    connection.requestMethod = "GET"
                    connection.connectTimeout = 10000
                    connection.readTimeout = 10000

                    val response =
                        connection.inputStream.bufferedReader().use {
                            it.readText()
                        }

                    JSONArray(response)

                    runOnUiThread {
                        val result = Intent().apply {
                            putExtra(EXTRA_RESULTS_JSON, response)
                        }

                        setResult(RESULT_OK, result)
                        finish()
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Search failed. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()

                        searchButton.isEnabled = true
                    }
                } finally {
                    connection?.disconnect()
                }
            }
        }
    }
}
