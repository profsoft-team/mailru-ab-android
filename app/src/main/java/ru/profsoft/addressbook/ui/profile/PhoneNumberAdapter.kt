package ru.profsoft.addressbook.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_phone_number.*
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.data.models.Profile

class PhoneNumberAdapter(
    private val items: List<String>
): RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhoneNumberViewHolder(inflater.inflate(R.layout.item_phone_number, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    abstract inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item : String)
    }

    inner class PhoneNumberViewHolder(convertView: View) : ViewHolder(convertView), LayoutContainer {
        override fun bind(item: String) {
            tv_phone_number.text = item
        }
    }
}