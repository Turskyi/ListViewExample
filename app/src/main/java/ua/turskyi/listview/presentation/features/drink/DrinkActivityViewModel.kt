package ua.turskyi.listview.presentation.features.drink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrinkActivityViewModel : ViewModel() {

    fun <R> execute(
        onPreExecute: (() -> Unit)? = null,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ): Job {
        return viewModelScope.launch {
            onPreExecute?.invoke()
            val result = withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
                doInBackground()
            }
            onPostExecute(result)
        }
    }
}