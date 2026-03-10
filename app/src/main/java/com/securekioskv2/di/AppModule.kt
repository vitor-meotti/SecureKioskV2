package com.securekioskv2.di

import android.content.Context
import com.securekioskv2.core.kiosk.KioskLockTaskManager
import com.securekioskv2.core.security.AdminSecurityRepository
import com.securekioskv2.core.security.BruteForceGuard
import com.securekioskv2.core.security.EncryptedPrefsAdminSecurityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideAdminRepo(@ApplicationContext c: Context): AdminSecurityRepository =
        EncryptedPrefsAdminSecurityRepository(c)

    @Provides fun provideGuard(): BruteForceGuard = BruteForceGuard()

    @Provides @Singleton fun provideLockTask(@ApplicationContext c: Context): KioskLockTaskManager =
        KioskLockTaskManager(c)
}
