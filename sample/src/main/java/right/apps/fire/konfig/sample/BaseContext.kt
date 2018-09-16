package right.apps.fire.konfig.sample

import android.app.Application
import android.preference.PreferenceManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import right.apps.fire.konfig.FiReKonf
import right.apps.fire.konfig.FiReKonfOptions

@Serializable
data class TestKonf(
        @SerialName("list_ads_frequency")
        val myCustomParam: Int = 0,
        val someTestParam: String = "default_value"
)

class BaseContext : Application() {

    val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }
    val fiReKonf: FiReKonf by lazy {
        FiReKonf(remoteConfig)
    }
    val sharedPrefs: SharedPrefs by lazy {
        SharedPrefs(PreferenceManager.getDefaultSharedPreferences(baseContext))
    }

    override fun onCreate() {
        super.onCreate()

        val defaultsKonfig = TestKonf()
        println("========== DefaultsKonfig = $defaultsKonfig")
        fiReKonf.init(defaultsKonfig, FiReKonfOptions(isFirst = sharedPrefs.isFirst /*, forceUpdatePending = true*/))
        sharedPrefs.isFirst = false
    }
}