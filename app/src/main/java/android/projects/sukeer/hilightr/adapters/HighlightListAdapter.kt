package android.projects.sukeer.hilightr.adapters

import android.projects.sukeer.hilightr.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
class HighlightListAdapter(val items: MutableList<ListItem>, val itemClick: (ListItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HighlightListItemViewHolder) {
            holder.bindView(items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return HighlightListItemViewHolder(view, itemClick)
    }

    override fun getItemCount() = items.size

    class HighlightListItemViewHolder(val view: View, val itemClick: (ListItem) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindView(item: ListItem) {
            with(item) {
                itemView.tv_title.text = item.title
                itemView.tv_place.text = item.place
                itemView.tv_person.text = item.person
                itemView.tv_date.text = item.date
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

    data class ListItem(val id: Long, val title: String, val message: String,
                        val place: String, val person: String, val date: String)
}