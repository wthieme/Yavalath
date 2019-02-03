package nl.whitedove.yavalath

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.joda.time.DateTime
import org.joda.time.Minutes

internal class CustomListAdapterPlayers(private val context: Context, private val listData: List<PlayerInfo>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private val mContext: Context = context

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): PlayerInfo {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val holder: ViewHolder
        if (cv == null) {
            cv = layoutInflater.inflate(R.layout.player_list_layout, parent, false)
            holder = ViewHolder()
            holder.tvNaam = cv!!.findViewById(R.id.tvPlayerName)
            holder.tvLastActive = cv.findViewById(R.id.tvLastActive)
            holder.tvCountry = cv.findViewById(R.id.tvCountry)
            cv.tag = holder
        } else {
            holder = cv.tag as ViewHolder
        }

        val player = getItem(position)
        holder.tvNaam!!.text = player.name

        val nu = DateTime.now()
        if (player.lastActive.isAfter(nu.minusMinutes(1)))
            holder.tvLastActive!!.text = mContext.getString(R.string.momentAgo)
        else if (player.lastActive.isAfter(nu.minusMinutes(2)))
            holder.tvLastActive!!.text = mContext.getString(R.string.minuteAgo)
        else
            holder.tvLastActive!!.text = String.format(mContext.getString(R.string.minutesAgo), Math.abs(Minutes.minutesBetween(nu, player.lastActive).minutes))

         holder.tvCountry!!.text = player.country

        return cv
    }

    private class ViewHolder {
        internal var tvNaam: TextView? = null
        internal var tvLastActive: TextView? = null
        internal var tvCountry: TextView? = null
    }
}