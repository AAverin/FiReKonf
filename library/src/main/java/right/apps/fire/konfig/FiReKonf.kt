package right.apps.fire.konfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.experimental.launch
import kotlinx.serialization.Mapper
import java.util.concurrent.TimeUnit

data class FiReKonfOptions(
        val expirationTime: Long = TimeUnit.HOURS.toSeconds(12),
        val forceUpdatePending: Boolean = false,
        val isFirst: Boolean = false
)

class FiReKonf(private val remoteConfig: FirebaseRemoteConfig) {

    inline fun <reified T : Any> init(konfig: T, options: FiReKonfOptions = FiReKonfOptions()) {
        init(Mapper.map(konfig), options)
    }

    fun init(defaults: Map<String, Any>, options: FiReKonfOptions = FiReKonfOptions()) {
        remoteConfig.setDefaults(defaults)
        if (!options.isFirst) {
            remoteConfig.activateFetched()
        }

        launch {
            when {
                //refresh ASAP and activate – eg, when force-update push was received
                options.forceUpdatePending -> {
                    remoteConfig.fetch(0).await()
                    remoteConfig.activateFetched()
                }

                //on first start fetch regularly and activate
                options.isFirst -> {
                    remoteConfig.fetch(options.expirationTime).await()
                    remoteConfig.activateFetched()
                }

                //otherwise just lazy-fetch and we will only activate on next init() call
                else -> remoteConfig.fetch(options.expirationTime).await()
            }
        }

    }

    inline fun <reified T : Any> get(remoteConfig: FirebaseRemoteConfig): T {
        return FirebaseConfigParser.parse(remoteConfig)
    }
}