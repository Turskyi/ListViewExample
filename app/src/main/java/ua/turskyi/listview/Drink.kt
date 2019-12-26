package ua.turskyi.listview

class Drink //Each Drink has a name, description, and an image resource
private constructor(
    val name: String,
    val description: String,
    val imageResourceId: Int
) {

    override fun toString(): String {
        return name
    }

    companion object {
        //drinks is an array of Drinks
        val drinks = arrayOf(
            Drink("Latte", "A couple of espresso shots with steamed milk", R.drawable.latte),
            Drink(
                "Cappuccino",
                "Espresso, hot milk, and a steamed milk foam",
                R.drawable.cappuccino
            ),
            Drink("Filter", "Highest quality beans roasted and brewed fresh", R.drawable.filter)
        )
    }

}