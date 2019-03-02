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
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mContext: Context = this
    private val mMyPermissionsRequestLocation = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FcmSender.mMyFcmToken = Helper.getFcmToken(mContext)
        initLocation()
        initViews()
    }

    private fun initViews() {
        val clickRules = getString(R.string.clickRules)
        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvMenu, iconFont)
        tvMenu.setOnClickListener { view -> menuClick(view) }

        tvrulestxt.movementMethod = LinkMovementMethod.getInstance()
        tvrulestxt.setText(clickRules, TextView.BufferType.SPANNABLE)
        val sp = tvrulestxt.text as Spannable
        val cs1 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showRulesDialog()
            }
        }
        sp.setSpan(cs1, 0, clickRules.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val mode = Helper.getGameMode(mContext)
        when (mode) {
            GameMode.HumanVsHumanInternet -> {
                rbH2H2Internet.isChecked = true
                modeChanged(llHumanVsHumanInternet)
            }
            GameMode.HumanVsHumanLocal -> {
                rbH2HLocal.isChecked = true
                modeChanged(llHumanVsHumanLocal)
            }
            GameMode.HumanVsComputer -> {
                rbH2Computer.isChecked = true
                modeChanged(llHumanVsComputer)
            }
        }

        rgMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbH2H2Internet -> {
                    Helper.setGameMode(mContext, GameMode.HumanVsHumanInternet)
                    modeChanged(llHumanVsHumanInternet)
                }
                R.id.rbH2HLocal -> {
                    Helper.setGameMode(mContext, GameMode.HumanVsHumanLocal)
                    modeChanged(llHumanVsHumanLocal)
                }
                R.id.rbH2Computer -> {
                    Helper.setGameMode(mContext, GameMode.HumanVsComputer)
                    modeChanged(llHumanVsComputer)
                }
            }
        }

        val name1 = Helper.getName1(this)
        etNameH2HInternet.setText(name1)

        etNameH2HInternet.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                Helper.setName1(mContext, etNameH2HInternet.text.toString())
            }
        })

        FontManager.setIconAndText(btnEnter,
                iconFont,
                getString(R.string.fa_sign_in_alt),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.enter),
                ContextCompat.getColor(this, R.color.colorPrimary))

        btnEnter.setOnClickListener { enter() }

        etNameH2HLocal1.setText(name1)

        etNameH2HLocal1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                Helper.setName1(mContext, etNameH2HLocal1.text.toString())
            }
        })

        val name2 = Helper.getName2(this)
        etNameH2HLocal2.setText(name2)
        etNameH2HLocal2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                Helper.setName2(mContext, etNameH2HLocal2.text.toString())
            }
        })

        FontManager.setIconAndText(btnStartHumanVsHumanLocal,
                iconFont,
                getString(R.string.fa_play),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.Start),
                ContextCompat.getColor(this, R.color.colorPrimary))
        btnStartHumanVsHumanLocal.setOnClickListener { startHumanToHuman() }

        val level = Helper.getGameLevel(mContext)
        when (level) {
            GameLevel.Easy -> rbEasy.isChecked = true
            GameLevel.Intermediate -> rbIntermediate.isChecked = true
            GameLevel.Expert -> rbExpert.isChecked = true
        }

        rgLevel.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbEasy ->
                    Helper.setGameLevel(mContext, GameLevel.Easy)
                R.id.rbIntermediate ->
                    Helper.setGameLevel(mContext, GameLevel.Intermediate)
                R.id.rbExpert ->
                    Helper.setGameLevel(mContext, GameLevel.Expert)
            }
        }

        etNameComputer.setText(name1)

        etNameComputer.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                Helper.setName1(mContext, etNameComputer.text.toString())
            }
        })

        FontManager.setIconAndText(btnStartComputer,
                iconFont,
                getString(R.string.fa_play),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.Start),
                ContextCompat.getColor(this, R.color.colorPrimary))
        btnStartComputer.setOnClickListener { startHumanToComputer() }

        if (Helper.DEBUG)
            tvDebug.visibility = View.VISIBLE
        else
            tvDebug.visibility = View.GONE
    }

    private fun modeChanged(to: View) {
        llHumanVsHumanInternet.visibility = View.GONE
        llHumanVsHumanLocal.visibility = View.GONE
        llHumanVsComputer.visibility = View.GONE
        val duration = 500
        to.alpha = 0.0f
        to.visibility = View.VISIBLE
        to.animate().alpha(1.0f).duration = duration.toLong()
    }

    private fun showRulesDialog() {
        val rd = RulesDialog(this)
        Helper.showDialog(rd, false)
    }

    private fun showAboutDialog() {
        val ad = AboutDialog(this)
        Helper.showDialog(ad, false)
    }

    private fun startHumanToComputer() {
        if (!checkName1(this)) return
        val gameLevel = Helper.getGameLevel(mContext)
        val token1 = UUID.randomUUID().toString()
        val token2 = UUID.randomUUID().toString()
        GameHelper.mGame = GameInfo(Helper.getName1(mContext), token1, getString(R.string.computer), token2, token1, GameMode.HumanVsComputer, gameLevel)
        GameHelper.mPointsWhite = 0
        GameHelper.mPointsBlack = 0
        val intent = Intent(this, GameActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        this.finish()
    }

    private fun startHumanToHuman() {
        if (!checkName1(this)) return
        if (!checkName2(this)) return
        val token1 = UUID.randomUUID().toString()
        val token2 = UUID.randomUUID().toString()
        GameHelper.mGame = GameInfo(Helper.getName1(mContext), token1, Helper.getName2(mContext), token2, token1, GameMode.HumanVsHumanLocal, GameLevel.Easy)
        GameHelper.mPointsWhite = 0
        GameHelper.mPointsBlack = 0
        val intent = Intent(this, GameActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        this.finish()
    }

    private fun enter() {
        if (!checkName1(this)) return
        if (!Helper.testInternet(this)) return
        createPlayer()
        fcmActive()
        val intent = Intent(this, PlayerListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        this.finish()
    }

    private fun createPlayer() {
        val name = Helper.getName1(mContext)
        val country = LocationHelper.getCountryCode(mContext)
        Database.createOrUpdatePlayer(name, country)
    }

    private fun fcmActive() {
        Helper.fcmActive(mContext, tvFcmBolt)
    }

    private fun checkName1(context: Context): Boolean {
        val nick = Helper.getName1(context)
        if (nick.isEmpty()) {
            Helper.showMessage(context, getString(R.string.NameMustNotBeEmpty))
            return false
        }
        if (nick.equals(getString(R.string.computer), true)) {
            Helper.showMessage(context, getString(R.string.ComputerReserved))
            return false
        }
        return true
    }

    private fun checkName2(context: Context): Boolean {
        val nick2 = Helper.getName2(context)
        if (nick2.isEmpty()) {
            Helper.showMessage(context, getString(R.string.Name2MustNotBeEmpty))
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
                    mMyPermissionsRequestLocation)
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
            mMyPermissionsRequestLocation -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
