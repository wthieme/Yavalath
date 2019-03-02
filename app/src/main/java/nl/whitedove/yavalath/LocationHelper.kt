package nl.whitedove.yavalath

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.preference.PreferenceManager
import java.io.IOException

internal object LocationHelper {

    fun getCountryCode(cxt: Context): String {

        val unknownCountry = ""
        if (!Helper.testInternet(cxt)) {
            return unknownCountry
        }
        val location = Helper.mCurrentBestLocation

        if (location == null) {
            return unknownCountry
        }

        val country: String
        val lat = location.latitude
        val lng = location.longitude
        val geocoder = Geocoder(cxt)

        val list: List<Address>?
        try {
            list = geocoder.getFromLocation(lat, lng, 1)
        } catch (e: IOException) {
            return unknownCountry
        }

        if (list != null && list.size > 0) {
            val address = list[0]
            country = address.countryCode
        } else {
            return unknownCountry
        }
        return country
    }
}
