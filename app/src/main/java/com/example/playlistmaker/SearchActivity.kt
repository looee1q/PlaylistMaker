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

class SearchActivity : AppCompatActivity() {
    lateinit var textEditor: EditText
    lateinit var searchRequest: String
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
        textEditor.setText(savedInstanceState?.getString(INPUT_IN_SEARCH_ACTIVITY))
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