package ru.profsoft.addressbook.ui.profiles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.data.models.Profile

class ProfileAdapter(
    val listener : (Profile) -> Unit
): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private var items: List<Profile> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProfileViewHolder(inflater.inflate(R.layout.item_profile, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data : List<Profile>) {
        items = data
        notifyDataSetChanged()
    }

    abstract inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item : Profile)
    }

    inner class ProfileViewHolder(convertView: View) : ViewHolder(convertView), LayoutContainer {
        override fun bind(item: Profile) {

        }
    }
}