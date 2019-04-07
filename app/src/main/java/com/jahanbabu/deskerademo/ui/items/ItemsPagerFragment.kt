package com.jahanbabu.deskerademo.ui.items


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.jahanbabu.deskerademo.Injection
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.util.*

class ItemsPagerFragment : Fragment() {
    lateinit var mSectionsPagerAdapter: ItemsPagerAdapter
    lateinit var pager: ViewPager
    lateinit var tabs: TabLayout
    private var appBarLayout: AppBarLayout? = null
    private var mToolbar: Toolbar? = null

    companion object {
        private val TAG = ItemsPagerFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): ItemsPagerFragment {
            val fragment = ItemsPagerFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_item_pager, container, false)

        initViews(view)

        return view
    }

    private fun initViews(view: View) {
        tabs = view.findViewById(R.id.tabs)
        pager = view.findViewById(R.id.pager)
        mToolbar = view.findViewById(R.id.toolbar)
        appBarLayout = view.findViewById(R.id.appBarLayout)

        mSectionsPagerAdapter = ItemsPagerAdapter(activity!!.supportFragmentManager)

        pager.adapter = mSectionsPagerAdapter
        tabs.setupWithViewPager(pager)

        pager.addOnPageChangeListener(pageChangeListener)

        pager.post {
            pageChangeListener.onPageSelected(pager.currentItem)
        }
    }

    var pageChangeListener = object: ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            Log.e("onPageSelected", position.toString())

            val fragment = mSectionsPagerAdapter.getRegisteredFragment(pager.currentItem) as ItemContract.View
            val p = ItemPresenter(fragment, Injection.provideItemRepository(DApplication.appContext!!))
            p.getItems(position)
        }

    }

    class ItemsPagerAdapter(fm: FragmentManager): SmartFragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): ItemFragment {
            // getItem is called to instantiate the fragment for the given page.
            Log.e("getItem", position.toString())
            val f = ItemFragment.newInstance(position)
//            ItemPresenter(f, Injection.provideItemRepository(DApplication.appContext!!))
            return f
        }

        override fun getItemPosition(`object`: Any): Int {
            Log.e("getItemPosition", `object`.toString() + "")
            return PagerAdapter.POSITION_NONE
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "ALL"
                1 -> "Category A"
                else -> "Category B"
            }
        }
    }
}
