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
}
```

And later you can simply use the data by calling

```kotlin
val fetchedKonfig: TestKonf = fiReKonf.get(remoteConfig)
```

Depending on current state of Remote Config, FiReConf will fill in data class, merging defaults with data from Remote Config

You can see more detailed example in `sample` folder

# Installation

[Install via Jitpack.io](https://jitpack.io/#AAverin/FiReKonf)

Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```
implementation "com.github.aaverin:firekonf:1.0"
```

Additionally, you will need to add [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) to the project.
Add following to the build.gradle in the root folder:
```
 buildscript {
    repositories {
        maven { url "https://kotlin.bintray.com/kotlinx" }
    }
 }
 ```

Apply plugin in the app/build.gradle file:
```
apply plugin: 'kotlinx-serialization'
```

# Extra

A little javascript to build a Kotlin data class from Firebase Remote Config.

```
process = () => {
    let names = $("mat-card > .parameter .content .name").map((i, e) => {
        return $(e).html()
    }).toArray()
    let descriptions = $("mat-card > .parameter .content").map((i, e) => {
        let desc = $(e).find(".description").html()
        if (desc) { return desc } else { return "" }
    }).toArray()
    let values = $("mat-card > .parameter .content .values").map((i, e) => {
        return $(e).find(".default-value .parameter-value").html()
    }).toArray()

    if (names.length != descriptions.length || names.length != values.length || descriptions.length != values.length) {
        console.log(`Critical script error, please report to author! names: ${names.length}, descriptions: ${descriptions.length}, values: ${values.length}`)
        return
    }

    let kotlinDataClass = []

    kotlinDataClass.push("@Serializable\n")
    kotlinDataClass.push("data class FiReKonf(\n")
    console.log(names)
    let structure = names.flatMap((name, index) => {
        let description = descriptions[index]
        let value = values[index]
        var fixedValue
        if (value.length > 60) {
            fixedValue = `"""${value}"""`
        } else {
            fixedValue = `"${value}"`
        }
        let result = [
            `    //${description}\n`,
            `    val ${name}: String = ${fixedValue}`
        ]
        if (index != names.length - 1) {
            result.push(",\n")
        }
        return result
    })
    structure.forEach(e => {
        kotlinDataClass.push(e)
    })


    kotlinDataClass.push("\n)")

    console.log(kotlinDataClass.join(""))
}
```

## How to use

1. Go to your Firebase Remote Config page in Chrome
2. Select `Menu -> More Tools -> Developer Tools` (or press `Option + Shift + I` on Mac / `Alt + Shift + I` on Windows)
3. Select `Console` tab
4. Paste javascript into the Console and press Enter
5. Type `process()` and press Enter

Resulting Kotlin data class will be printed into the output.
Copy it and put into your project.

You can use `@SerialName` kotlinx.serialization annotation to instruct parser which name to grab from Remote Config
