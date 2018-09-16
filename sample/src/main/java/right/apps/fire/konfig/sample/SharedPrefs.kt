package right.apps.fire.konfig.sample

import android.content.SharedPreferences

class SharedPrefs(private val sharedPreferences: SharedPreferences) {

    companion object K {
        const val prefix = "FiReConf_sample_"
        const val isFirst = "${prefix}isFirst"
    }

    var isFirst: Boolean
        get() {
            return sharedPreferences.getBoolean(K.isFirst, true)
        }
        set(value) {
            sharedPreferences.edit { putBoolean(K.isFirst, value) }
        }
}

//android-ktx
inline fun SharedPreferences.edit(
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}