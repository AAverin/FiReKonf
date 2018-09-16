package right.apps.fire.konfig.sample

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val baseContext = (applicationContext as BaseContext)
        val fetchedKonfig: TestKonf = baseContext.fiReKonf.get(baseContext.remoteConfig)
        println("========== FetchedKonfig = $fetchedKonfig")
    }
}