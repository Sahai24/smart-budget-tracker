package com.example.budget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import com.example.budget.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var labelInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var labelLayout: TextInputLayout
    private lateinit var amountLayout: TextInputLayout
    private lateinit var closeBtn: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_transaction)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        labelInput = findViewById(R.id.labelInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        amountInput = findViewById(R.id.amountInput)
        labelLayout = findViewById(R.id.lablelayout)
        amountLayout = findViewById(R.id.amountLayout)
        closeBtn = findViewById(R.id.closebtn)

        // Adding TextWatcher to labelInput
        labelInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Clear the error when the user starts typing
                labelLayout.error = null
            }
        })

        // Adding TextWatcher to amountInput
        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Clear the error when the user starts typing
                amountLayout.error = null
            }
        })

        // Initialize addTransactionBtn
        val addTransactionBtn: Button = findViewById(R.id.addTransactionBtn)

        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                labelLayout.error = "Please enter a valid label"

            else if (amount == null)
                amountLayout.error = "Please enter a valid amount"
            else {
                val transaction = Transaction(0,label, amount,description)
                insert(transaction)
            }
        }
        closeBtn.setOnClickListener{
            finish()
        }
    }
     private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions"
        ).build()

         GlobalScope.launch {
             db.transactionDao().insertAll(transaction)
             finish()
         }
     }
}

