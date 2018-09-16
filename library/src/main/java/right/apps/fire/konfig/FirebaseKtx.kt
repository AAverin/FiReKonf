package right.apps.fire.konfig

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener {
        continuation.resume(it)
    }
    addOnFailureListener {
        continuation.resumeWithException(it)
    }
    addOnCanceledListener { continuation.cancel() }
}