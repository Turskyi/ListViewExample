package ua.turskyi.listview.presentation
import kotlinx.coroutines.*

fun <R> CoroutineScope.executeAsyncTask(
    onPreExecute: (() -> Unit)? = null,
    doInBackground: () -> R,
    onPostExecute: (R) -> Unit
): Job {
    return launch {
        onPreExecute?.invoke()
        /* runs in background thread without blocking the Main Thread */
        val result = withContext(Dispatchers.IO) {
            doInBackground()
        }
        onPostExecute(result)
    }
}