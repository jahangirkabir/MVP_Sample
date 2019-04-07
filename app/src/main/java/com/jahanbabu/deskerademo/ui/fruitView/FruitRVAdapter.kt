package com.jahanbabu.deskerademo.ui.fruitView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.Fruit
import kotlinx.android.synthetic.main.row_table_item.view.*

class FruitRVAdapter(private val mContext: Context, private val list: MutableList<Fruit>): RecyclerView.Adapter<FruitRVAdapter.ViewHolder>(),
    Filterable {

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                var results = FilterResults()
                var charString = constraint.toString()
                var filteredList = mutableListOf<Fruit>()
                if (charString.length == 0) {
                    filteredList = list
                } else {
                    for (fruit in list) {
                        if (fruit.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(fruit);
                        }
                    }
                }
                results.values = filteredList
                return results;
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                mSearchedTableItems = results.values as List<Fruit>
                mSearchedTableItems = mSearchedTableItems!!.sortedBy {
                    it.name
                }
                notifyDataSetChanged();
            }
        }
    }

    internal lateinit var fruitClickListener: FruitClickListener
    private var mSearchedTableItems: List<Fruit>? = null

    init {
        mSearchedTableItems = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_table_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mFruit = mSearchedTableItems!![position]

        holder.titleTextView!!.text = holder.mFruit.name

        holder.mView.setOnClickListener {
            fruitClickListener.onFruitClicked(position, holder.mFruit.id, holder.mFruit.name)
        }
    }

    override fun getItemCount(): Int {
        return if (null != mSearchedTableItems) mSearchedTableItems!!.size else 0
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        internal var titleTextView = mView.fruitNameTextView
        internal var checkBox = mView.checkBox

        lateinit var mFruit: Fruit
    }

    fun setClickListener(clickListener: FruitClickListener) {
        this.fruitClickListener = clickListener
    }

    interface FruitClickListener {
        fun onFruitClicked(position: Int, id: Int, name: String)
    }
}
