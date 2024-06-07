package com.example.memorabilia.wanttoread
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.CircleCrop
//import com.example.memorabilia.R
//import com.example.memorabilia.search.SearchAdapter
//
//
//class WantToReadAdapter(private val list: List<SearchAdapter.Book>) : RecyclerView.Adapter<WantToReadAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
//        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val book = list[position]
//        holder.titleTextView.text = book.title
//        holder.authorTextView.text = book.author
//        Glide.with(holder.itemView.context)
//            .load(book.imageUrl)
//            .transform(CircleCrop())
//            .into(holder.profileImageView)    }
//
//    override fun getItemCount() = list.size
//}