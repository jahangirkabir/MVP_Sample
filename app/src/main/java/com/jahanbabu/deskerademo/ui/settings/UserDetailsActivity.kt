package com.jahanbabu.deskerademo.ui.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.util.AppConstants
import com.jahanbabu.deskerademo.util.AppUtils
import com.orhanobut.hawk.Hawk

class UserDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private var tvUserName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvDoj: TextView? = null
    private var tvTemperature: TextView? = null
    private var tvSound: TextView? = null
    private var tvNotification: TextView? = null
    private var tvProbationEnd: TextView? = null
    private var tvDuration: TextView? = null
    private var tvPermanentDate: TextView? = null
    private var tvProbationLength: TextView? = null
    private var tvHobbies: TextView? = null
    private var tvBack: TextView? = null
    private var tvTitle: TextView? = null
    private var ivProfile: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)
        initViews()
        initListeners()
        setUpData()
    }

    private fun initListeners() {
        tvBack!!.visibility = View.VISIBLE
        tvTitle!!.visibility = View.VISIBLE
        tvBack!!.setOnClickListener(this)
    }

    private fun initViews() {
        tvUserName = findViewById(R.id.tvUserName)
        tvEmail = findViewById(R.id.tvEmail)
        tvDoj = findViewById(R.id.tvDoj)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvSound = findViewById(R.id.tvSound)
        tvNotification = findViewById(R.id.tvNotification)
        tvProbationEnd = findViewById(R.id.tvProbationEnd)
        tvDuration = findViewById(R.id.tvDuration)
        tvPermanentDate = findViewById(R.id.tvPermanentDate)
        tvProbationLength = findViewById(R.id.tvProbationLength)
        tvHobbies = findViewById(R.id.tvHobbies)
        ivProfile = findViewById(R.id.iv_profile)
        tvBack = findViewById(R.id.primaryTextView)
        tvTitle = findViewById(R.id.toolbar_title)
    }

    private fun setUpData() {
        tvTitle!!.text = "Profile Details"
        val user = Hawk.get("User", User())
        tvUserName!!.text = user.name
        tvEmail!!.text = user.email
        tvTemperature!!.text = user.tempUnit
        tvSound!!.text = user.sound.toString()
        tvNotification!!.text = user.notification.toString()
        tvProbationEnd!!.text = getDateTextIfValid(user.dop)
        tvDoj!!.text = getDateTextIfValid(user.doj)
        var duration = getString(R.string.label_na)
        var length = getString(R.string.label_na)
//        if (0L != doj && 0L != doj) {
//            duration = AppUtils.getDateDifference(user.doj, user.dop)
//            length = AppUtils.getProbationLength(user.doj, user.dop)
//        }
        tvHobbies!!.text = user.hobby
        tvProbationLength!!.text = length
        tvDuration!!.text = duration
        if (0L != user.dop) {
            tvPermanentDate!!.text = AppUtils.getDateFromMillis(user.dop + AppConstants.DAYS_TO_MILLIS)
        } else {
            tvPermanentDate!!.setText(R.string.date_not_available)
        }

        if (!TextUtils.isEmpty(user.imageUrl)) {
            Glide.with(this).load(user.imageUrl).apply(
                RequestOptions()
                    .transform(CircleCrop())
            ).into(ivProfile!!)
        }
    }

    private fun getDateTextIfValid(millis: Long): String {
        var dateText = getString(R.string.date_not_available)
        if (0L != millis) {
            dateText = AppUtils.getDateFromMillis(millis)
        }
        return dateText
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.primaryTextView -> onBackPressed()
        }
    }
}
