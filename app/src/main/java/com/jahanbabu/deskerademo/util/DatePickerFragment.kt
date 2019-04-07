package com.jahanbabu.deskerademo.util

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

import java.util.Calendar

class DatePickerFragment : DialogFragment() {
    private var mListener: OnDateSetListener? = null

    fun setListener(mListener: OnDateSetListener) {
        this.mListener = mListener
    }


    override fun onCreateDialog(bundle: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        if (null != arguments && arguments!!.containsKey(AppConstants.ARG_DATE_IN_MILLS)) {
            c.timeInMillis = arguments!!.getLong(AppConstants.ARG_DATE_IN_MILLS)
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity!!, mListener, year, month, day)
    }

    companion object {

        val TAG = DatePickerFragment::class.java.name

        fun getInstance(listener: OnDateSetListener, date: Long): DatePickerFragment {
            val datePickerFragment = DatePickerFragment()
            if (0L != date) {
                val bundle = Bundle()
                bundle.putLong(AppConstants.ARG_DATE_IN_MILLS, date)
                datePickerFragment.arguments = bundle
            }

            datePickerFragment.setListener(listener)
            return datePickerFragment
        }
    }
}