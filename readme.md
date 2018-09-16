# FiReKonf – Firebase Remote Config, best practices and Kotlin support

Firebase Remote Config is very easy and straightforward to implement. 

That is, until you find that you throttle the cloud and get random amounts of `FirebaseRemoteConfigFetchException` or `FirebaseRemoteConfigFetchThrottledException`.

In this [YouTube video](https://www.youtube.com/watch?v=6TWJ_rR7K6g) Todd Kerpelman highlights several approaches
on how to make sure users get latest data from Firebase Remote Config promptly.

This library implements #3 approach:
- Fetch and activate on first init
- Fetch, but don't activate on all other inits
- Activate previously fetched data on every init
- Support force-refetch in case of critical errors

In addition to that, thanks to [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization), 
it is now possible to write a data class, and back it by data coming from Remote Config.

Same class can be used to provide default values.

Take a look:
 ```kotlin
 @Serializable
 data class TestKonf(
         @SerialName("list_ads_frequency")
         val myCustomParam: Int = 0,
         val someTestParam: String = "default_value"
 ) 
 ```
 
 Then in your Application class add:
 ```kotlin
override fun onCreate() {
        super.onCreate()

        val defaultsKonfig = TestKonf()
        
        fiReKonf.init(defaultsKonfig, FiReKonfOptions(isFirst = sharedPrefs.isFirst /*, forceUpdatePending = true*/))
        sharedPrefs.isFirst = false
    }
```

And later you can simply use the data by calling

```kotlin
val fetchedKonfig: TestKonf = fiReKonf.get(remoteConfig)
```

Depending on current state of Remote Config, FiReConf will fill in data class, merging defaults with data from Remote Config

# Installation



