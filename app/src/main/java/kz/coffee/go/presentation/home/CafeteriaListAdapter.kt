package kz.coffee.go.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kz.coffee.go.R
import kz.coffee.go.databinding.RowCafeteriaListItemLayoutBinding
import kz.coffee.go.domain.cafeteria.Cafeteria
import kz.coffee.go.domain.cafeteria.Product

class CafeteriaListAdapter : RecyclerView.Adapter<CafeteriaListAdapter.ViewHolder>() {

    private lateinit var cafeteriaList: List<Cafeteria>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowCafeteriaListItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_cafeteria_list_item_layout,
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return if (::cafeteriaList.isInitialized) cafeteriaList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cafeteria = cafeteriaList[position]
        holder.bind(cafeteria)
    }

    var onItemClick: ((String) -> Unit)? = null

    fun updateList(cafeteriaList: List<Cafeteria>) {
        this.cafeteriaList = cafeteriaList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCafeteriaImage: ImageView = itemView.findViewById(R.id.ivCafeteriaImage)
        private val ivCafeteriaLogo: ImageView = itemView.findViewById(R.id.ivCafeteriaLogo)
        private val tvCafeteriaName: TextView = itemView.findViewById(R.id.tvCafeteriaName)
        private val tvCafeteriaStartingPrice: TextView =
            itemView.findViewById(R.id.tvCafeteriaStartingPrice)

        fun bind(cafeteria: Cafeteria) {
            Glide.with(itemView.context).load(cafeteria.imageUrl)
                .placeholder(R.drawable.ic_image_gray_200dp).error(R.drawable.ic_broken_image_gray)
                .centerCrop().into(ivCafeteriaImage)
            Glide.with(itemView.context).load(cafeteria.logoUrl)
                .placeholder(R.drawable.ic_image_gray_34dp).error(R.drawable.ic_broken_image_gray)
                .apply(
                    RequestOptions.circleCropTransform()
                ).centerCrop().into(ivCafeteriaLogo)
            tvCafeteriaName.text = cafeteria.name
            val minPrice = "от ${cafeteria.products.minBy { it.price!! }?.price}"
            tvCafeteriaStartingPrice.text = minPrice
            itemView.setOnClickListener {
                onItemClick?.invoke(cafeteria.cafeteriaId!!)
            }
        }

    }

}