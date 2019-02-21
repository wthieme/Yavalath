package nl.whitedove.yavalath

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
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
            holder.tvEnvironment = cv.findViewById((R.id.tvEnvironment))
            cv.tag = holder
        } else {
            holder = cv.tag as ViewHolder
        }

        val player = getItem(position)
        holder.tvNaam!!.text = player.name

        val nu = DateTime.now()
        when {
            player.lastActive.isAfter(nu.minusMinutes(1)) -> holder.tvLastActive!!.text = mContext.getString(R.string.momentAgo)
            player.lastActive.isAfter(nu.minusMinutes(2)) -> holder.tvLastActive!!.text = mContext.getString(R.string.minuteAgo)
            else -> holder.tvLastActive!!.text = String.format(mContext.getString(R.string.minutesAgo), Math.abs(Minutes.minutesBetween(nu, player.lastActive).minutes))
        }

        holder.tvCountry!!.text = player.country
        var env = "1.0"
        var icon = mContext.getString(R.string.fa_os_unknown)
        var col = ContextCompat.getColor(mContext, R.color.colorBlue)
        var iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME_SOLID)
        if (player.platform.startsWith("android", true)) {
            iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME_BRANDS)
            icon = mContext.getString(R.string.fa_os_android)
            col = ContextCompat.getColor(mContext, R.color.colorGreen)
            env = player.platform.replace("android", "", true).trim()
        }
        if (player.platform.startsWith("ios",true)) {
            iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME_BRANDS)
            icon = mContext.getString(R.string.fa_os_ios)
            col = ContextCompat.getColor(mContext, R.color.colorDisabled)
            env = player.platform.replace("ios", "", true).trim()
        }
        FontManager.setIconAndText(holder.tvEnvironment!!,
                iconFont,
                icon,
                col,
                Typeface.DEFAULT,
                env,
                ContextCompat.getColor(mContext, R.color.colorPrimary))
        return cv
    }

    private class ViewHolder {
        internal var tvNaam: TextView? = null
        internal var tvLastActive: TextView? = null
        internal var tvCountry: TextView? = null
        internal var tvEnvironment: TextView? = null
    }
}