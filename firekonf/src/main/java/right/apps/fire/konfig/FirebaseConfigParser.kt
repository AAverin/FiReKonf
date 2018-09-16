package right.apps.fire.konfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.serialization.*

class FirebaseConfigParser(val context: SerialContext? = null) {
    inline fun <reified T : Any> parse(config: FirebaseRemoteConfig): T = parse(config,
                                                                                context.klassSerializer(T::class))

    fun <T> parse(config: FirebaseRemoteConfig, loader: KSerialLoader<T>): T = FirebaseConfigInput(config).read(loader)

    private inner class FirebaseConfigInput(val config: FirebaseRemoteConfig) : TaggedInput<String>() {
        override fun KSerialClassDesc.getTag(index: Int): String =
                getAnnotationsForIndex(index).let { annotations ->
                    if (annotations.isNotEmpty()) {
                        (annotations.find { it is SerialName } as SerialName).value
                    } else {
                        getElementName(index)
                    }
                }

        override fun readTaggedString(tag: String): String = config.getValue(tag).asString()
        override fun readTaggedShort(tag: String): Short = config.getValue(tag).asLong().toShort()
        override fun readTaggedInt(tag: String): Int = config.getValue(tag).asLong().toInt()
        override fun readTaggedFloat(tag: String): Float = config.getValue(tag).asDouble().toFloat()
        override fun readTaggedDouble(tag: String): Double = config.getValue(tag).asDouble()
        override fun readTaggedUnit(tag: String) = Unit
        override fun readTaggedChar(tag: String): Char = config.getValue(tag).asString().toCharArray()[0]
        override fun readTaggedValue(tag: String): Any = config.getValue(tag)
    }

    companion object {
        fun <T> parse(conf: FirebaseRemoteConfig, serial: KSerialLoader<T>) = FirebaseConfigParser().parse(conf,
                                                                                                           serial)

        inline fun <reified T : Any> parse(conf: FirebaseRemoteConfig) = FirebaseConfigParser().parse<T>(conf)
    }
}