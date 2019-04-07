package com.jahanbabu.deskerademo.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.util.AppConstants
import com.jahanbabu.deskerademo.util.Temperature
import kotlinx.android.synthetic.main.activity_select_temperature_unit.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class SelectTemperatureUnitActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_temperature_unit)

        toolbar_title.setText("Temperature")
        farenheitTextView.setOnClickListener(this)
        celciusTextView.setOnClickListener(this)
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.celciusTextView -> publishData(Temperature.CELSIUS)
            R.id.farenheitTextView -> publishData(Temperature.FAHRENHEIT)
        }
    }

    private fun publishData(unit: Temperature) {
        val intent = Intent()
        intent.putExtra(AppConstants.ARG_UNIT, unit.value)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
