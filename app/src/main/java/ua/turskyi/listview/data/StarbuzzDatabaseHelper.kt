package ua.turskyi.listview.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ua.turskyi.listview.R

class StarbuzzDatabaseHelper internal constructor(context: Context?) :
/* "factory = null" parameter is an advanced feature relating to cursors.*/
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        /* the name of our database */
        private const val DB_NAME = "starbuzz"

        /* the version of the database */
        private const val DB_VERSION = 1
        private fun insertDrink(
            db: SQLiteDatabase, name: String, description: String,
            resourceId: Int
        ) {

            val drinkValues = ContentValues()
            drinkValues.put("NAME", name)
            drinkValues.put("DESCRIPTION", description)
            drinkValues.put("IMAGE_RESOURCE_ID", resourceId)
            db.insert("DRINK", null, drinkValues)
        }

        private fun updateDescriptionOfLatte(db: SQLiteDatabase, description: String) {
            val drinkValues = ContentValues()
            drinkValues.put("DESCRIPTION", description)
            db.update(
                "DRINK",
                drinkValues,
                "NAME = ?",
                arrayOf("Latte")
            )
        }

        private fun updateDescriptionById(db: SQLiteDatabase, id: Int, description: String) {
            val drinkValues = ContentValues()
            drinkValues.put("DESCRIPTION", description)
            db.update(
                "DRINK", drinkValues,
                "_id = ?",
                arrayOf(id.toString())
            )
        }

        private fun deleteByName(db: SQLiteDatabase, name: String) {
            db.delete(
                "DRINK", "NAME = ?",
                arrayOf(name)
            )
        }
    }

    override fun onCreate(db: SQLiteDatabase) = updateMyDatabase(db, 0, DB_VERSION)

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        updateMyDatabase(db, oldVersion, newVersion)
    }

    private fun updateMyDatabase(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 1) {
            db.execSQL(
                "CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "NAME TEXT, "
                        + "DESCRIPTION TEXT, "
                        + "IMAGE_RESOURCE_ID INTEGER);"
            )
            insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte)
            insertDrink(
                db, "Cappuccino", "Espresso, hot milk and steamed-milk foam",
                R.drawable.cappuccino
            )
            insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter)
        }

        if (oldVersion < 2) {
            /* adding new column "favorite" to a table */
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;")
        }
    }
}
