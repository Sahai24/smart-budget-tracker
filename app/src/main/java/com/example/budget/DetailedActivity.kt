package com.example.budget

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction : Transaction
    private lateinit var labelInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var labelLayout: TextInputLayout
    private lateinit var amountLayout: TextInputLayout
    private lateinit var closeBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detailed)


        labelInput = findViewById(R.id.labelInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        amountInput = findViewById(R.id.amountInput)
        labelLayout = findViewById(R.id.lablelayout)
        amountLayout = findViewById(R.id.amountLayout)
        closeBtn = findViewById(R.id.closebtn)

        val rootView = findViewById<View>(R.id.rootView)

        val updateBtn: Button = findViewById(R.id.updateBtn)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        labelInput.setText(transaction.label)
        amountInput.setText(transaction.amount.toString())
        descriptionInput.setText(transaction.description)

        rootView.setOnClickListener{
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }

        // Adding TextWatcher to labelInput
        labelInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Clear the error when the user starts typing
                labelLayout.error = null

                updateBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

        })

        // Adding TextWatcher to amountInput
        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Clear the error when the user starts typing
                amountLayout.error = null

                updateBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        descriptionInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Clear the error when the user starts typing
                amountLayout.error = null

                updateBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })



        updateBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                labelLayout.error = "Please enter a valid label"

            else if (amount == null)
                amountLayout.error = "Please enter a valid amount"
            else {
                val transaction = Transaction ( transaction.id,label, amount,description)
                update(transaction)
            }
        }
        closeBtn.setOnClickListener{
            finish()
        }
    }
    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}


