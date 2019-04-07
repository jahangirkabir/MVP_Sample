package com.jahanbabu.deskerademo.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jahanbabu.deskerademo.ui.fruitView.TableEditActivity
import com.jahanbabu.deskerademo.ui.fruitView.fruitDetail.TableItemDetailsActivity
import com.jahanbabu.deskerademo.ui.settings.SelectTemperatureUnitActivity
import com.jahanbabu.deskerademo.ui.settings.UserDetailsActivity

object ActivityNavigator {

    fun launchTemperatureUnitActivity(fragment: Fragment, requestCode: Int) {
        fragment.startActivityForResult(
            Intent(fragment.getContext(), SelectTemperatureUnitActivity::class.java),
            requestCode
        )
    }

    fun launchSettingDetailsActivity(context: Context) {
        context.startActivity(Intent(context, UserDetailsActivity::class.java))
    }

    fun launchEditTableActivity(context: Context) {
        context.startActivity(Intent(context, TableEditActivity::class.java))
    }

    fun launchItemDetailsActivity(context: Context, id: Int, name: String) {
        val intent = Intent(context, TableItemDetailsActivity::class.java)
        intent.putExtra(AppConstants.ARG_TABLE_ITEM_ID, id)
        intent.putExtra(AppConstants.ARG_TABLE_ITEM, name)
        context.startActivity(intent)
    }

    fun openCameraIntent(fragment: Fragment, profileImageUri: Uri) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, profileImageUri)
        fragment.startActivityForResult(intent, AppConstants.RequestCodes.REQUEST_CAMERA)
    }

    fun openGalleryIntent(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            AppConstants.IMAGE_TYPE
        )
        if (null != intent.resolveActivity(fragment.getContext()!!.getPackageManager())) {
            fragment.startActivityForResult(intent, AppConstants.RequestCodes.REQUEST_GALLERY)
        } else {
            Toast.makeText(
                fragment.getContext(), "No Intent available to handle action", Toast
                    .LENGTH_SHORT
            ).show()
        }
    }
}
