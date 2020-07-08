package ua.turskyi.listview

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_top_level.*

/**
 * @Description
 * The app shows a list of options, by clicking on first one, user get a second screen with
 * list of coffees, by clicking on one of them user get a screen with description of a
 * particular coffee.
 */
class TopLevelActivity : AppCompatActivity(R.layout.activity_top_level) {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        //Create an OnItemClickListener
        val itemClickListener = OnItemClickListener {
                _, _, position, _ ->
            if (position == 0) {
                val intent = Intent(
                    this@TopLevelActivity,
                    DrinkCategoryActivity::class.java
                )
                startActivity(intent)
            }
        }
        //Add the listener to the list view
        list_options.onItemClickListener = itemClickListener
    }
}