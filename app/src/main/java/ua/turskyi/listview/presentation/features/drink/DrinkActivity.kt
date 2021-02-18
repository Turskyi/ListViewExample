package ua.turskyi.listview.presentation.features.drink

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ua.turskyi.listview.R
import ua.turskyi.listview.data.StarbuzzDatabaseHelper

class DrinkActivity : AppCompatActivity(R.layout.activity_drink) {

    companion object {
        const val EXTRA_DRINKID = "drinkId"
    }

    private val vm: DrinkActivityViewModel by lazy {
        ViewModelProvider(this)[DrinkActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Getting the drink id from the intent */
        val drinkId = intent.extras?.get(EXTRA_DRINKID) as Int

        vm.execute(
            doInBackground = {
                /* value to return */
                try {
                    val starbuzzDatabaseHelper: SQLiteOpenHelper = StarbuzzDatabaseHelper(this)
                    val db = starbuzzDatabaseHelper.writableDatabase
                    val cursor: Cursor = db.query(
                        "DRINK",
                        arrayOf("NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"),
                        "_id = ?",
                        arrayOf(
                            drinkId.toString()
                        ),
                        null,
                        null,
                        null,
                    )
                    /* Moving to the first record in the Cursor */
                    if (cursor.moveToFirst()) {
/* Getting the drink details from the cursor */
                        val nameText: String = cursor.getString(0)
                        val descriptionText: String = cursor.getString(1)
                        val photoId: Int = cursor.getInt(2)
                        val isFavorite = cursor.getInt(3) == 1

                        runOnUiThread{
                            /* Populating the drink name */
                            val name: TextView = findViewById(R.id.name)
                            name.text = nameText
                            /* Populating the drink description */
                            val description: TextView = findViewById(R.id.description)
                            description.text = descriptionText

/* Populating the drink image */
                            val photo: ImageView = findViewById(R.id.photo)
                            photo.setImageResource(photoId)
                            photo.contentDescription = nameText

                            /* Populating the favorite checkbox */
                            val favorite: CheckBox = findViewById<View>(R.id.favorite) as CheckBox
                            favorite.isChecked = isFavorite
                        }

                    }

                    cursor.close()
                    db.close()
                    return@execute true
                } catch (e: SQLiteException) {
                    return@execute false
                }
            }, onPostExecute = { success ->
                /* ... here "it" contains data returned from "doInBackground" */
                if (!success) {
                    val toast: Toast =
                        Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })
    }

    /* Updating the database when the checkbox is clicked */
    fun onFavoriteClicked(view: View) {
        val drinkNo: Int = intent.extras?.get(EXTRA_DRINKID) as Int
        val drinkValues = ContentValues()
        vm.execute(
            onPreExecute = {
                val favorite: CheckBox = findViewById(R.id.favorite)
                drinkValues.put("FAVORITE", favorite.isChecked)
            }, doInBackground = {
                /* value to return */
                val starbuzzDatabaseHelper: SQLiteOpenHelper =
                    StarbuzzDatabaseHelper(this@DrinkActivity)
                try {
                    val db: SQLiteDatabase = starbuzzDatabaseHelper.writableDatabase
                    db.update(
                        "DRINK", drinkValues,
                        "_id = ?", arrayOf(drinkNo.toString())
                    )
                    db.close()
                    return@execute true
                } catch (e: SQLiteException) {
                    return@execute false
                }
            }, onPostExecute = { success ->
                /* ... here "it" contains data returned from "doInBackground" */
                if (!success) {
                    val toast: Toast =
                        Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })
    }
}