package kz.coffee.go.presentation.cafeteria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.coffee.go.R
import kz.coffee.go.databinding.RowListSectionItemLayoutBinding


class TextListAdapter(private val list: MutableList<String>) :
    RecyclerView.Adapter<TextListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowListSectionItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_list_section_item_layout,
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    var onItemClick: ((String) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSectionText: TextView = itemView.findViewById(R.id.tvSectionText)
        fun bind(textStr: String) {
            tvSectionText.text = textStr
            if (android.util.Patterns.PHONE.matcher(textStr).matches()) {
                tvSectionText.setOnClickListener {
                    onItemClick?.invoke(textStr)
                }
            }
        }
    }
}