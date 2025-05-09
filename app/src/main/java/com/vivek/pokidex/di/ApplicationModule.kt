package com.vivek.pokidex.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.vivek.pokidex.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {


    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(
            app.resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()


    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    @Singleton
    fun provideCacheSize(): Long {
        return 10 * 1024 * 1024 // 10 MB cache size
    }



}