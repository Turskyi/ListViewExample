package ua.turskyi.listview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_drink.*

class DrinkActivity : AppCompatActivity(R.layout.activity_drink) {

    companion object {
        const val EXTRA_DRINKID = "drinkId"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get the drink from the intent
        val drinkId = intent.extras?.get(EXTRA_DRINKID) as Int
        val drink: Drink = Drink.drinks[drinkId]
        //Populate the drink name
        name.text = drink.name
        //Populate the drink description
        description.text = drink.description
        //Populate the drink image
        photo.setImageResource(drink.imageResourceId)
        photo.contentDescription = drink.name
    }
}