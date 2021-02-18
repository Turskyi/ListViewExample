package ua.turskyi.listview.presentation.features.category

import android.app.ListActivity
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import ua.turskyi.listview.data.StarbuzzDatabaseHelper
import ua.turskyi.listview.presentation.features.drink.DrinkActivity

/// list activity does not need layout, but now it is deprecated and should not be used
class DrinkCategoryActivity : ListActivity() {
    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listDrinks = listView

        try {
           val starbuzzDatabaseHelper: SQLiteOpenHelper = StarbuzzDatabaseHelper(this)
            db = starbuzzDatabaseHelper.readableDatabase
            cursor = db.query(
                "DRINK",
                arrayOf("_id", "NAME"),
                null, null, null, null, null,
            )

            val listAdapter: CursorAdapter = SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1,
                cursor,
                arrayOf("NAME"),
                intArrayOf(android.R.id.text1), 0
            )
            listDrinks.adapter = listAdapter
        } catch (e: SQLiteException) {
            val toast : Toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor.close()
        db.close()
    }

    override fun onListItemClick(
        l: ListView,
        v: View,
        position: Int,
        id: Long
    ) {
        /* Passing the drink the user clicks on to DrinkActivity */
        val intent = Intent(this@DrinkCategoryActivity, DrinkActivity::class.java)
        intent.putExtra(DrinkActivity.EXTRA_DRINKID, id.toInt())
        startActivity(intent)
    }
}