package com.example.newsapp.features.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.newsapp.R

class CountryDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("kek")

            builder.setItems(R.array.pref_country_values
            ) { dialog, which ->
                Toast.makeText(context, which.toString(), Toast.LENGTH_SHORT).show()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}