package nl.whitedove.yavalath

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.preference.PreferenceManager
import java.io.IOException

internal object LocationHelper {

    fun getCountry(cxt: Context): String {

        if (!Helper.testInternet(cxt)) {
            return cxt.getString(R.string.CountryUnknown)
        }
        val location = Helper.mCurrentBestLocation

        if (location == null) {
            return cxt.getString(R.string.CountryUnknown)
        }

        val country: String
        val lat = location.getLatitude()
        val lng = location.getLongitude()
        val geocoder = Geocoder(cxt)

        val list: List<Address>?
        try {
            list = geocoder.getFromLocation(lat, lng, 1)
        } catch (e: IOException) {
            return cxt.getString(R.string.CountryUnknown)
        }

        if (list != null && list.size > 0) {
            val address = list[0]
            country = address.countryName
            LocationHelper.saveCountry(cxt, country)
        } else {
            return cxt.getString(R.string.CountryUnknown)
        }
        return country
    }

    private fun saveCountry(cxt: Context, country: String?) {
        if (country == null || country.isEmpty() || country.equals(cxt.getString(R.string.CountryUnknown), ignoreCase = true))
            return

        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putString("country", country)
        editor.apply()
    }
}
