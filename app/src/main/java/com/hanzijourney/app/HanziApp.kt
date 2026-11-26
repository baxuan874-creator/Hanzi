package com.hanzijourney.app

import android.app.Application
import com.hanzijourney.app.di.AppContainer

class HanziApp : Application() {
    // Manual dependency injection (PRD section 11). Created lazily on first use.
    val container: AppContainer by lazy { AppContainer(this) }
}
