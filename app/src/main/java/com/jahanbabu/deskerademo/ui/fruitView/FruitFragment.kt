package com.jahanbabu.deskerademo.ui.fruitView

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.Fruit
import com.jahanbabu.deskerademo.util.ActivityNavigator

class FruitFragment : Fragment(), View.OnClickListener, FruitContract.View, FruitRVAdapter.FruitClickListener {
    override fun showNewItem() {
        Toast.makeText(getContext(), "New fruit added", Toast.LENGTH_SHORT).show()
    }

    override fun showUpdateItem(pos: Int) {

    }

    override fun showCurrentItems() {

    }

    override fun onClick(v: View?) {
        when (v!!.getId()) {
            R.id.primaryTextView -> if (!fruits.isNullOrEmpty()) {
                ActivityNavigator.launchEditTableActivity(context!!)
            }
            R.id.secondaryTextView -> presenter.onAddClick(fruits.size)
        }
    }

    override fun onFruitClicked(position: Int, id: Int, name: String) {
        ActivityNavigator.launchItemDetailsActivity(context!!, id, name)
    }

    override val isActive: Boolean
        get() = isAdded

    override fun setDataToView(fruits: MutableList<Fruit>) {

        this.fruits.clear()
        this.fruits.addAll(fruits)

        adapter = FruitRVAdapter(context!!, this.fruits)
        adapter.setClickListener(this@FruitFragment)
        recyclerView!!.setAdapter(adapter)
    }

    override lateinit var presenter: FruitContract.Presenter
    lateinit var adapter: FruitRVAdapter
    var fruits = mutableListOf<Fruit>()
    var recyclerView: RecyclerView? = null
    private var mToolbar: Toolbar? = null
    private var toolbarTitleTextView: TextView? = null
    private var mtvPrimary: TextView? = null
    private var mtvSecondary: TextView? = null
    private var mSearchView: SearchView? = null

    companion object {
        private val TAG = FruitFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): FruitFragment {
            val fragment = FruitFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_table, container, false)

        initViews(view)
        setupToolbar()
        addListener()

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private fun initViews(view: View) {
        toolbarTitleTextView = view.findViewById(R.id.toolbar_title)
        toolbarTitleTextView!!.setText("Table")
        recyclerView = view.findViewById(R.id.fruitRecyclerView)
        mtvPrimary = view.findViewById(R.id.primaryTextView)
        mtvSecondary = view.findViewById(R.id.secondaryTextView)
        mSearchView = view.findViewById(R.id.searchView)
        mSearchView!!.clearFocus()

        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun setupToolbar() {
        mtvSecondary!!.setVisibility(View.VISIBLE)
        mtvPrimary!!.setVisibility(View.VISIBLE)
        mtvPrimary!!.setText(R.string.edit)
        mtvSecondary!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0)
    }

    private fun addListener() {
        mtvPrimary!!.setOnClickListener(this)
        mtvSecondary!!.setOnClickListener(this)

        mSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (null != adapter) {
                    adapter.getFilter().filter(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (null != adapter) {
                    adapter.getFilter().filter(query)
                }
                return false
            }
        })
    }
}
