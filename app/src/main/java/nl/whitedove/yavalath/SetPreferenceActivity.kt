package nl.whitedove.yavalath

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class SetPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fm: FragmentManager = getSupportFragmentManager()
        fm.beginTransaction().replace(android.R.id.content, PrefsFragment()).commit()
    }
}