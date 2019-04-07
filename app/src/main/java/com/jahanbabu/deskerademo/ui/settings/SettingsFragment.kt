package com.jahanbabu.deskerademo.ui.settings

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.util.ActivityNavigator
import com.jahanbabu.deskerademo.util.AppConstants
import com.jahanbabu.deskerademo.util.AppUtils
import com.jahanbabu.deskerademo.util.DatePickerFragment
import com.orhanobut.hawk.Hawk
import java.util.*

class SettingsFragment : Fragment(), SettingsContract.View, View.OnClickListener, CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener{

    override fun showDOP(date: Long) {
        tvProbationDate!!.text = AppUtils.getDateFromMillis(date)
    }

    override fun showTempUnit(unit: String) {
        tvTemperature!!.setText(unit)
    }

    override val isActive: Boolean
        get() = isAdded

    override fun setDataToView(user: User) {
        soundSwitch!!.isChecked = user.sound
        notificationSwitch!!.isChecked = user.notification
        tvTemperature!!.text = user.tempUnit

        if (user.dop > 0)
            tvProbationDate!!.text = AppUtils.getDateFromMillis(user.dop)
        else tvProbationDate!!.text = "Date not available"
    }

    override lateinit var presenter: SettingsContract.Presenter

    private var toolbarTitleTextView: TextView? = null
    private var tv_view_details: TextView? = null
    private var tvTemperature: TextView? = null
    private var tvProbationDate:TextView? = null
    private var soundSwitch: Switch? = null
    private var notificationSwitch:Switch? = null

    companion object {
        private val TAG = SettingsFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        initViews(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private fun initViews(view: View) {
        toolbarTitleTextView = view.findViewById(R.id.toolbar_title)
        toolbarTitleTextView!!.setText("Settings")

        notificationSwitch = view.findViewById(R.id.switch_notification)
        soundSwitch = view.findViewById(R.id.switch_sound)
        tvProbationDate = view.findViewById(R.id.probationDateTextView)
        tvTemperature = view.findViewById(R.id.temperatureTextView)
        tv_view_details = view.findViewById(R.id.tv_view_details)

        notificationSwitch!!.setOnCheckedChangeListener(this)
        soundSwitch!!.setOnCheckedChangeListener(this)
        tvProbationDate!!.setOnClickListener(this)
        tvTemperature!!.setOnClickListener(this)
        tv_view_details!!.setOnClickListener(this)
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_view_details -> ActivityNavigator.launchSettingDetailsActivity(activity!!)
            R.id.temperatureTextView -> ActivityNavigator.launchTemperatureUnitActivity(
                this@SettingsFragment, AppConstants
                    .RequestCodes.REQUEST_TEMPERATURE_UNIT
            )
            R.id.probationDateTextView -> {
                val u = Hawk.get("User", User())
                val datePickerFragment = DatePickerFragment.getInstance(this, u.dop)
                datePickerFragment.show(activity!!.getSupportFragmentManager(), DatePickerFragment.TAG)
            }
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, day)
        val newDate = c.timeInMillis
        presenter.saveProbationDate(newDate)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.switch_notification -> {
                presenter.saveNotification(isChecked)
            }
            R.id.switch_sound -> {
                presenter.saveSound(isChecked)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.RequestCodes.REQUEST_TEMPERATURE_UNIT
            && null != data
            && data.hasExtra(AppConstants.ARG_UNIT)) {
            val newUnit = data.getStringExtra(AppConstants.ARG_UNIT)

            presenter.saveTempUnit(newUnit)
        }
    }
}
