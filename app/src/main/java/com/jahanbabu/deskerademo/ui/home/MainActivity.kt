package com.jahanbabu.deskerademo.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jahanbabu.deskerademo.R
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jahanbabu.deskerademo.Injection
import com.jahanbabu.deskerademo.ui.favourite.FavouriteFragment
import com.jahanbabu.deskerademo.ui.favourite.FavouritePresenter
import com.jahanbabu.deskerademo.ui.fruitView.FruitFragment
import com.jahanbabu.deskerademo.ui.fruitView.FruitPresenter
import com.jahanbabu.deskerademo.ui.items.ItemsPagerFragment
import com.jahanbabu.deskerademo.ui.profile.ProfileFragment
import com.jahanbabu.deskerademo.ui.profile.ProfilePresenter
import com.jahanbabu.deskerademo.ui.settings.SettingsFragment
import com.jahanbabu.deskerademo.ui.settings.SettingsPresenter
import com.jahanbabu.deskerademo.util.DApplication
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MainContract.View {
    override fun navigateToDetailScreen(complete: Boolean) {

    }

    override fun setView() {

    }

    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar!!.visibility = View.GONE
    }

    var progressBar: ProgressBar? = null

    override lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeView()
        initProgressBar()
        Hawk.init(this).build()

        presenter = MainPresenter(this)
    }

    /**
     * Initializing Toolbar and BottomNavigationView
     */
    private fun initializeView() {

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val menuView = navigation.getChildAt(0) as BottomNavigationMenuView
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setChecked(item.itemData.isChecked)
        }
        navigation.setSelectedItemId(R.id.navigation_user)
    }

    /**
     * Initializing progressbar programmatically
     */
    private fun initProgressBar() {
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        progressBar!!.isIndeterminate = true

        val relativeLayout = RelativeLayout(this)
        relativeLayout.gravity = Gravity.CENTER
        relativeLayout.addView(progressBar)

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        progressBar!!.visibility = View.INVISIBLE

        this.addContentView(relativeLayout, params)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_user -> {
                val f = ProfileFragment.newInstance("", "")
                goToFragment(f, "ProfileFragment")
                ProfilePresenter(f)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_item -> {
                goToFragment(ItemsPagerFragment.newInstance("", ""), "ItemsPagerFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favourite -> {
                val f = FavouriteFragment.newInstance("", "")
                goToFragment(f, "FavouriteFragment")
                FavouritePresenter(f, Injection.provideItemRepository(DApplication.appContext!!))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_table -> {
                val f = FruitFragment.newInstance("", "")
                goToFragment(f, "FruitFragment")
                FruitPresenter(f, Injection.provideFruitRepository(DApplication.appContext!!))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                val f = SettingsFragment.newInstance("", "")
                goToFragment(f, "SettingsFragment")
                SettingsPresenter(f)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun goToFragment(fragment: Fragment, TAG: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, TAG).commit()
    }

}
