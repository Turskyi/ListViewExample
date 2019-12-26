package ua.turskyi.listview

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class DrinkCategoryActivity : ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listDrinks = listView
        val listAdapter: ArrayAdapter<Drink> = ArrayAdapter(
            this, R.layout.list_item, Drink.drinks
        )
        listDrinks.adapter = listAdapter
    }

    override fun onListItemClick(
        l: ListView,
        v: View,
        position: Int,
        id: Long
    ) { //Pass the drink the user clicks on to DrinkActivity
        val intent = Intent(this@DrinkCategoryActivity, DrinkActivity::class.java)
        intent.putExtra(DrinkActivity.EXTRA_DRINKID, id.toInt())
        startActivity(intent)
    }
}