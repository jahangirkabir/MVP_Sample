package com.jahanbabu.deskerademo.util

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.jahanbabu.deskerademo.BuildConfig
import com.jahanbabu.deskerademo.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Random

object AppUtils {

    private val photoDirectory: File?
        get() {
            var outputDir: File? = null
            val externalStorageStagte = Environment.getExternalStorageState()
            if (externalStorageStagte == Environment.MEDIA_MOUNTED) {
                val path = Environment.getExternalStorageDirectory().toString() + AppConstants.IMAGE_FOLDER
                outputDir = File(path)
                if (!outputDir.exists()) {
                    outputDir = File(Environment.getExternalStorageDirectory().path + AppConstants.IMAGE_FOLDER)
                    outputDir.mkdirs()
                }
            }
            return outputDir
        }

    fun generateTimeStampPhotoFileUri(context: Context): Uri? {
        val directory = photoDirectory
        var photoFileUri: Uri? = null
        if (directory != null) {
            val photoFile = File(directory, System.currentTimeMillis().toString() + AppConstants.JPG_EXTENSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoFileUri =
                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + AppConstants.PROVIDER, photoFile)
            } else {
                photoFileUri = Uri.fromFile(photoFile)
            }
        }
        return photoFileUri
    }

    fun loadJSONFromAsset(file: String): String? {
        var json: String? = null
        try {
            json = DApplication.appContext!!.assets.open(file).bufferedReader().use{
                it.readText()
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }


    fun getDateDifference(activity: Activity, start: Long, end: Long): String {

        val startCalendar = GregorianCalendar()
        startCalendar.timeInMillis = start
        val endCalendar = GregorianCalendar()
        endCalendar.timeInMillis = end

        val diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
        val diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
        val days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH)
        return activity.getString(R.string.probation_duration, diffMonth, days)
    }

    fun getProbationLength(activity: Activity, start: Long, end: Long): String {
        val startCalendar = GregorianCalendar()
        startCalendar.timeInMillis = start
        val endCalendar = GregorianCalendar()
        endCalendar.timeInMillis = end

        val diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
        val diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
        val days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH)

        return if (diffMonth > 6 || diffMonth == 6 && days > 0) {
            activity.getString(R.string.more_than_six_months)
        } else if (diffMonth == 6 && days == 0) {
            activity.getString(R.string.six_months)
        } else {
            activity.getString(R.string.less_than_six_months)
        }
    }

    fun getDateFromMillis(millis: Long): String {
        val formatter = SimpleDateFormat(AppConstants.DESKERA_DATE_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    fun random(): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
