package com.jahanbabu.deskerademo.ui.favourite

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.Item

class FavouriteFragment : Fragment(), FavouriteContract.View, FavouriteRVAdapter.ItemClickListener {
    override fun onItemClicked(position: Int, id: String) {

    }

    override val isActive: Boolean
        get() = isAdded

    override fun setDataToView(items: MutableList<Item>) {

        this.items.clear()
        this.items.addAll(items)

        val adapter = FavouriteRVAdapter(context!!, this.items)
        adapter.setClickListener(this@FavouriteFragment)
        recyclerView!!.setAdapter(adapter)
    }

    override lateinit var presenter: FavouriteContract.Presenter

    var items = mutableListOf<Item>()
    var recyclerView: RecyclerView? = null
    private var mToolbar: Toolbar? = null
    private var toolbarTitleTextView: TextView? = null

    companion object {
        private val TAG = FavouriteFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): FavouriteFragment {
            val fragment = FavouriteFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        initViews(view)

//        setListeners()

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private fun initViews(view: View) {
        toolbarTitleTextView = view.findViewById(R.id.toolbar_title)
        toolbarTitleTextView!!.setText("Favourite")
        recyclerView = view.findViewById(R.id.itemRecyclerView)
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun setListeners() {
//        profileBigImageView!!.setOnClickListener(this)
//        DOJEditText!!.setOnClickListener(this)
//        hobbyEditText!!.setOnEditorActionListener(this)
//        profileSmallImageView!!.setOnClickListener(this)
//        emailEditText!!.setOnEditorActionListener(this)
    }
}
