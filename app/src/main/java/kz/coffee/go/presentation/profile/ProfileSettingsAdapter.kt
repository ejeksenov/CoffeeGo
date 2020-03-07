package kz.coffee.go.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.coffee.go.R
import kz.coffee.go.databinding.RowProfileSettingsItemLayoutBinding

class ProfileSettingsAdapter(val list: MutableList<String>): RecyclerView.Adapter<ProfileSettingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowProfileSettingsItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_profile_settings_item_layout, parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val tvSettingsItemTitle: TextView = itemView.findViewById(R.id.tvSettingsItemTitle)

        fun bind(position: Int) {
            tvSettingsItemTitle.text = list[position]
            tvSettingsItemTitle.setOnClickListener {
                onItemClick?.invoke(position)
            }
        }
    }
}