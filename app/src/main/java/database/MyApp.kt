package database

import android.app.Application

class MyApp : Application() {

    private lateinit var _appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun getAppComponent(): AppComponent {
        return _appComponent
    }
}