package com.jahanbabu.deskerademo.ui.fruitView.fruitDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jahanbabu.deskerademo.Injection
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.util.AppConstants
import com.jahanbabu.deskerademo.util.replaceFragmentInActivity

class TableItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_item_details)

        val detailFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as FruitDetailsFragment? ?:
        FruitDetailsFragment.newInstance(intent.getIntExtra(AppConstants.ARG_TABLE_ITEM_ID, 0), intent.getStringExtra(AppConstants.ARG_TABLE_ITEM)).also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }
        // Create the presenter
        FruitDetailsPresenter(detailFragment, Injection.provideFruitRepository(applicationContext))
    }
}
