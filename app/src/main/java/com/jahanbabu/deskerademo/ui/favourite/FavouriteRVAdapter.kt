package com.jahanbabu.deskerademo.ui.favourite

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.Item
import kotlinx.android.synthetic.main.row_item.view.*

class FavouriteRVAdapter(private val mContext: Context, private val list: MutableList<Item>): RecyclerView.Adapter<FavouriteRVAdapter.ViewHolder>() {
    internal lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = list[position]

        holder.itemCategoryTextView!!.text = holder.mItem.category
        holder.titleTextView!!.text = holder.mItem.title
        holder.descriptionTextView!!.text = holder.mItem.description

        if (holder.mItem.favourite){
            holder.itemFavouriteImageView.setImageResource(R.drawable.ic_favourite_yes)
        } else{
            holder.itemFavouriteImageView.setImageResource(R.drawable.ic_favorite_no)
        }

        Log.e("thumb", holder.mItem.url)
        val mDefaultBackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.resources.getDrawable(R.drawable.ic_image_placeholder, null)
        } else {
            mContext.resources.getDrawable(R.drawable.ic_image_placeholder)
        }

        Glide.with(mContext).load(holder.mItem.url)
            .placeholder(R.drawable.ic_image_placeholder).dontAnimate()
            .error(mDefaultBackground).apply(RequestOptions().transform(CircleCrop()))
            .into(holder.thumbImageView)

//        holder.mView.setOnClickListener {
//            itemClickListener.onItemClicked(position, holder.mItem.id)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        internal var titleTextView = mView.itemNameTextView
        internal var descriptionTextView = mView.itemDescriptionTextView
        internal var itemCategoryTextView = mView.itemCategoryTextView
        internal var thumbImageView = mView.itemImageView
        internal var itemFavouriteImageView = mView.itemFavouriteImageView

        lateinit var mItem: Item
    }

    fun setClickListener(clickListener: ItemClickListener) {
        this.itemClickListener = clickListener
    }

    interface ItemClickListener {
        fun onItemClicked(position: Int, id: String)
    }
}
