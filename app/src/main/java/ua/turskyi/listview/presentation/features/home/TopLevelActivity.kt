package ua.turskyi.listview.presentation.features.home

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_top_level.*
import ua.turskyi.listview.R
import ua.turskyi.listview.data.StarbuzzDatabaseHelper
import ua.turskyi.listview.presentation.executeAsyncTask
import ua.turskyi.listview.presentation.features.category.DrinkCategoryActivity
import ua.turskyi.listview.presentation.features.drink.DrinkActivity

/**
 * @Description
 * The app shows a list of options, by clicking on first one, user get a second screen with
 * list of coffees, by clicking on one of them user get a screen with description of a
 * particular coffee.
 */
class TopLevelActivity : AppCompatActivity(R.layout.activity_top_level) {

    lateinit var db: SQLiteDatabase
    lateinit var favoritesCursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        /* Populating the list_favorites ListView from a cursor */
        val listFavorites: ListView = findViewById(R.id.list_favorites)
        lifecycleScope.executeAsyncTask(onPreExecute = {
            /* Creating an OnItemClickListener */
            val itemClickListener = OnItemClickListener { _, _, position, _ ->
                if (position == 0) {
                    val intent = Intent(
                        this@TopLevelActivity,
                        DrinkCategoryActivity::class.java
                    )
                    startActivity(intent)
                }
            }
            /* Adding the listener to the list view */
            list_options.onItemClickListener = itemClickListener
        }, doInBackground = {
            /* value to return */
            try {
                val starbuzzDatabaseHelper: SQLiteOpenHelper = StarbuzzDatabaseHelper(this)
                db = starbuzzDatabaseHelper.readableDatabase
                favoritesCursor = db.query(
                    "DRINK",
                    arrayOf("_id", "NAME"), "FAVORITE = 1",
                    null, null, null, null
                )

                val favoriteAdapter: CursorAdapter = SimpleCursorAdapter(
                    this@TopLevelActivity,
                    android.R.layout.simple_list_item_1, favoritesCursor,
                    arrayOf("NAME"),
                    intArrayOf(android.R.id.text1), 0
                )
                runOnUiThread{
                    listFavorites.adapter = favoriteAdapter
                }
                true
            } catch (e: SQLiteException) {
                false
            }
        }, onPostExecute = {success ->
            // ... here "it" contains data returned from "doInBackground"
            if (!success) {
                val toast: Toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
                toast.show()
            }

            /* Navigating to DrinkActivity if a drink is clicked */
            listFavorites.onItemClickListener = OnItemClickListener { _, _, _, id: Long ->
                val intent = Intent(this@TopLevelActivity, DrinkActivity::class.java)
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, id.toInt())
                startActivity(intent)
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        lifecycleScope.executeAsyncTask( doInBackground = {
            /* value to return */
            try {
                val starbuzzDatabaseHelper = StarbuzzDatabaseHelper(this)
                db = starbuzzDatabaseHelper.readableDatabase
                val newCursor: Cursor = db.query(
                    "DRINK",
                    arrayOf("_id", "NAME"), "FAVORITE = 1",
                    null, null, null, null
                )
                val listFavorites: ListView = findViewById(R.id.list_favorites)
                val adapter: CursorAdapter = listFavorites.adapter as CursorAdapter
                runOnUiThread {
                    adapter.changeCursor(newCursor)
                }
                favoritesCursor = newCursor
                true
            } catch (e: SQLiteException) {
                false
            }
        }, onPostExecute = {success ->
            // ... here "it" contains data returned from "doInBackground"
            if (!success) {
                val toast: Toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
                toast.show()
            }
        })
    }

    /* Closing the cursor and database in the onDestroy() method @Override */
    override fun onDestroy() {
        super.onDestroy()
        favoritesCursor.close()
        db.close()
    }
}