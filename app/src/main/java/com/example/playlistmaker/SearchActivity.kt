package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {
    private lateinit var textEditor: EditText
    private lateinit var searchRequest: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backToMainScreen = findViewById<ImageView>(R.id.to_main_screen_from_search)
        val clearingCross = findViewById<ImageView>(R.id.clearingCrossInSettingActivity)
        textEditor = findViewById<EditText>(R.id.searchEditTextInSettingsActivity)
        searchRequest = textEditor.text.toString()

        backToMainScreen.setOnClickListener {
            //val displayIntent = Intent(this, MainActivity::class.java)
            //startActivity(displayIntent)
            finish()
        }

        clearingCross.setOnClickListener {
            textEditor.setText("")
            closeKeyboard()
        }

        val trackListImitation = mutableListOf<Track>(
            Track("Smells Like Teen Spirit", "Nirvana", "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean", "Michael Jackson", "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin' Alive", "Bee Gees", "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"),
            Track("Extra long name of incredibly exciting wonderful summer song", "Philip Anthony Hopkins, Robert De Niro, Leonardo di ser Piero da Vinci", "999:59",
            "")
        )

        val trackListImitationLong = MutableList(100) {trackListImitation[it % trackListImitation.size]}

        val trackList = findViewById<RecyclerView>(R.id.track_list_recycler_view)
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = TrackAdapter(trackListImitationLong)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (input.isNullOrEmpty()) clearingCross.visibility = View.GONE
                else clearingCross.visibility = View.VISIBLE
                searchRequest = input.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        textEditor.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_IN_SEARCH_ACTIVITY, searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textEditor.setText(savedInstanceState.getString(INPUT_IN_SEARCH_ACTIVITY))
    }

    private fun closeKeyboard() {
        val currentView = this.currentFocus
        if (currentView != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(`currentView`.windowToken, 0)
        }
    }

    companion object {
        const val INPUT_IN_SEARCH_ACTIVITY = "INPUT_IN_SEARCH_ACTIVITY"
    }
}