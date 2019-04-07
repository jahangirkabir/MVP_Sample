package com.jahanbabu.deskerademo.ui.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.util.ActivityNavigator
import com.jahanbabu.deskerademo.util.AppConstants
import com.jahanbabu.deskerademo.util.AppUtils
import com.jahanbabu.deskerademo.util.DatePickerFragment
import java.util.*

class ProfileFragment : Fragment(), ProfileContract.View, DatePickerDialog.OnDateSetListener, View.OnClickListener, TextView.OnEditorActionListener {

    override fun showDOJDialog(user: User) {
        val datePickerFragment = DatePickerFragment.getInstance(
            this@ProfileFragment, user.doj
        )
        datePickerFragment.show(activity!!.getSupportFragmentManager(), DatePickerFragment.TAG)
    }

    override val isActive: Boolean
        get() = isAdded

    override fun setUserDataToView(user: User) {
        this.user = user

        if (user.name.isNotBlank())
            mToolbar!!.title = user.name

        if (user.email.isNotBlank())
            emailEditText!!.setText(user.email)

        if (user.hobby.isNotBlank())
            hobbyEditText!!.setText(user.hobby)

        if (user.doj > 0)
            DOJEditText!!.setText(AppUtils.getDateFromMillis(user.doj))

        if (!TextUtils.isEmpty(user.imageUrl)) {
            setProfileImage(Uri.parse(user.imageUrl))
        }
    }

    override fun showEmailError(state: Boolean, msg: String) {
        if (state) emailEditText!!.error = msg
        else {
            emailEditText!!.clearFocus()
            hideKeyboard()
        }
    }

    override fun showDOJ(doj: Long) {
        DOJEditText!!.setText(AppUtils.getDateFromMillis(doj))
    }

    override lateinit var presenter: ProfileContract.Presenter

    private var profileBigImageView: ImageView? = null
    private var profileSmallImageView: ImageView? = null
    private var emailEditText: EditText? = null
    private var hobbyEditText: EditText? = null
    private var DOJEditText: EditText? = null
    private var appBarLayout: AppBarLayout? = null
    private var mToolbar: Toolbar? = null

    private lateinit var cameraImageUri: Uri
    private lateinit var user: User

    companion object {
        private val TAG = ProfileFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initViews(view)

        setListeners()

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private fun initViews(view: View) {
        profileBigImageView = view.findViewById(R.id.profileBigImageView)
        emailEditText = view.findViewById(R.id.emailEditText)
        hobbyEditText = view.findViewById(R.id.hobbyEditText)
        DOJEditText = view.findViewById(R.id.DOJEditText)
        profileSmallImageView = view.findViewById(R.id.profileSmallImageView)
        mToolbar = view.findViewById(R.id.toolbar)
        appBarLayout = view.findViewById(R.id.appBarLayout)
    }

    private fun setListeners() {
        profileBigImageView!!.setOnClickListener(this)
        DOJEditText!!.setOnClickListener(this)
        hobbyEditText!!.setOnEditorActionListener(this)
        profileSmallImageView!!.setOnClickListener(this)
        emailEditText!!.setOnEditorActionListener(this)

        appBarLayout!!.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset <= 5) {
                    isShow = true
                    profileSmallImageView!!.visibility = View.VISIBLE
                    profileBigImageView!!.visibility = View.INVISIBLE
                } else if (isShow) {
                    isShow = false
                    profileBigImageView!!.visibility = View.VISIBLE
                    profileSmallImageView!!.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setProfileImage(uri: Uri) {
        Glide.with(context!!).load(uri).apply(RequestOptions().transform(CircleCrop())).into(profileSmallImageView!!)
        Glide.with(context!!).load(uri).apply(RequestOptions().transform(CircleCrop())).into(profileBigImageView!!)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        Log.i(TAG, "onDateSet: ")
        val c = Calendar.getInstance()
        c.set(year, month, day)
        val newDate = c.timeInMillis

        presenter.saveDOJ(newDate)
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.profileSmallImageView, R.id.profileBigImageView -> addProfileImage()
            R.id.DOJEditText -> presenter.onDOJClick()
        }
    }

    /**
     * Called when an action is being performed.
     *
     * @param v        The view that was clicked.
     * @param actionId Identifier of the action.  This will be either the
     * identifier you supplied, or [                 EditorInfo.IME_NULL][EditorInfo.IME_NULL] if being called due to the enter key
     * being pressed.
     * @param event    If triggered by an enter key, this is the event;
     * otherwise, this is null.
     * @return Return true if you have consumed the action, else false.
     */
    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            when (v.id) {
                R.id.emailEditText -> presenter.saveEmail(v.text.toString().trim { it <= ' ' })
                R.id.hobbyEditText -> presenter.saveHobby(v.text.toString())
            }
            return true
        }
        return false
    }

    private fun hideKeyboard() {
        if (null != activity && null != activity!!.currentFocus) {
            val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Activity.RESULT_OK == resultCode) {
            when (requestCode) {
                AppConstants.RequestCodes.REQUEST_CAMERA -> if (null != cameraImageUri) {
                    setProfileImage(cameraImageUri)
                    presenter.saveImage(cameraImageUri.toString())
                }
                AppConstants.RequestCodes.REQUEST_GALLERY -> if (null != data && null != data.data) {
                    setProfileImage(data.data)
                    presenter.saveImage(Uri.parse(data.data!!.toString()).toString())
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            AppConstants.RequestCodes.PERMISSIONS_REQUEST_WRITE_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                addProfileImage()
            } else {
                // permission denied, boo! Disable the
                Toast.makeText(context, R.string.info_storage_permission_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    /**
     * Open Chooser to select Camera,Gallery
     */
    private fun addProfileImage() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage(R.string.info_storage_permission)
                builder.setTitle(R.string.title_permission_required)
                builder.setPositiveButton(R.string.ok) { dialog, which ->
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
                        AppConstants.RequestCodes.PERMISSIONS_REQUEST_WRITE_STORAGE
                    )
                }
                builder.setNegativeButton(R.string.cancel) { dialog, which ->
                    Toast.makeText(
                        context, R.string.info_storage_permission_denied,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                builder.create().show()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), AppConstants.RequestCodes.PERMISSIONS_REQUEST_WRITE_STORAGE
                )
            }
        } else {
            openChooserDialog()
        }
    }

    private fun openChooserDialog() {
        val items = arrayOf<CharSequence>("Capture photo", "Choose from gallery", "Cancel")

        val title = TextView(context)
        title.text = "Add Profile Photo!"
        title.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        title.setPadding(10, 25, 15, 20)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.WHITE)
        title.textSize = 22f

        val builder = android.app.AlertDialog.Builder(context)

        builder.setCustomTitle(title)

        builder.setItems(items) { dialog, item ->
            if (items[item] == "Capture photo") {
                cameraImageUri = AppUtils.generateTimeStampPhotoFileUri(context!!.applicationContext)!!
                ActivityNavigator.openCameraIntent(this@ProfileFragment, cameraImageUri)
            } else if (items[item] == "Choose from gallery") {
                ActivityNavigator.openGalleryIntent(this@ProfileFragment)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
}
