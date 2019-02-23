package nl.whitedove.yavalath

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private var mContext: Context = this
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FcmSender.mMyFcmToken = Helper.getFcmToken(mContext)
        initLocation()
        initViews()
    }

    private fun initViews() {

        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)

        FontManager.setIconAndText(btnEnter,
                iconFont,
                getString(R.string.fa_sign_in_alt),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.enter),
                ContextCompat.getColor(this, R.color.colorPrimary))
        btnEnter.setOnClickListener { enter() }

        val name = Helper.getName(this)
        etName.setText(name)

        etName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                Helper.setName(mContext, etName.text.toString())
            }
        })

        val clickRules = getString(R.string.clickRules)

        tvrulestxt.movementMethod = LinkMovementMethod.getInstance()
        tvrulestxt.setText(clickRules, TextView.BufferType.SPANNABLE)
        val sp = tvrulestxt.text as Spannable
        val cs1 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showRulesDialog()
            }
        }
        sp.setSpan(cs1, 0, clickRules.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        FontManager.markAsIconContainer(tvMenu, iconFont)
        tvMenu.setOnClickListener { view -> menuClick(view) }

        if (Helper.DEBUG)
            tvDebug.visibility=View.VISIBLE
        else
            tvDebug.visibility=View.GONE
    }

    private fun showRulesDialog() {
        val rd = RulesDialog(this)
        Helper.showDialog(rd, false)
    }

    private fun showAboutDialog() {
        val ad = AboutDialog(this)
        Helper.showDialog(ad, false)
    }

    private fun enter() {
        if (!checkName(this)) return
        if (!Helper.testInternet(this)) return
        createPlayer()
        fcmActive()
        val intent = Intent(this, PlayerListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        this.finish()
    }

    private fun createPlayer()
    {
        val name = Helper.getName(mContext)
        val country = LocationHelper.getCountry(mContext)
        Database.createOrUpdatePlayer(name, country)
    }

    private fun fcmActive() {
        Helper.fcmActive(mContext, tvFcmBolt)
    }

    private fun checkName(cxt: Context): Boolean {
        val nick = Helper.getName(cxt)
        if (nick.isEmpty()) {
            Helper.showMessage(cxt, getString(R.string.NameMustNotBeEmpty))
            return false
        }
        return true
    }

    private fun menuClick(oView: View) {
        val popup = PopupMenu(this, oView)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_main, popup.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.itRules -> {
                    showRulesDialog()
                    return@OnMenuItemClickListener true
                }

                R.id.itAbout -> {
                    showAboutDialog()
                    return@OnMenuItemClickListener true
                }
            }
            true
        })

        popup.show()
    }

    private fun initLocation() {

        // Acquire a reference to the system Location Manager
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            return
        }

        var netLastLocation: Location? = null

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Helper.ONE_MINUTE, Helper.ONE_KM, locationListener)
            netLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

        if (netLastLocation != null) makeUseOfNewLocation(netLastLocation)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocation()
                } else {
                    Helper.showMessage(this, "Without location permission the country will be unknown in the player list")
                }
            }
        }
    }

    private fun makeUseOfNewLocation(location: Location) {
        Helper.mCurrentBestLocation = location
    }

}
