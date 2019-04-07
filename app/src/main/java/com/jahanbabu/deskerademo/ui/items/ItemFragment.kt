package com.jahanbabu.deskerademo.ui.items

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.Item
import com.jahanbabu.deskerademo.data.User

class ItemFragment : Fragment(), ItemContract.View, ItemRVAdapter.ItemClickListener {
    override fun onItemClicked(position: Int, id: String) {

    }

    override val isActive: Boolean
        get() = isAdded

    override fun setDataToView(items: MutableList<Item>) {

        this.items.clear()
        this.items.addAll(items)

        val adapter = ItemRVAdapter(context!!, this.items)
        adapter.setClickListener(this@ItemFragment)
        recyclerView!!.setAdapter(adapter)
    }

    override lateinit var presenter: ItemContract.Presenter

    var items = mutableListOf<Item>()
    var recyclerView: RecyclerView? = null
    private var mToolbar: Toolbar? = null

    companion object {
        private val TAG = ItemFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private var mPage: Int = 0

        fun newInstance(param1: Int): ItemFragment {
            val fragment = ItemFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPage = arguments!!.getInt(ARG_PARAM1)
        Log.e("onCreate", mPage.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_item, container, false)

        initViews(view)

//        setListeners()

        return view
    }

    override fun onResume() {
        super.onResume()
//        presenter.getItems(mPage)
    }

    private fun initViews(view: View) {
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
