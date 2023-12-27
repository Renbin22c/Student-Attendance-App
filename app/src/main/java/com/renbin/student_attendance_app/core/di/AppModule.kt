package com.renbin.student_attendance_app.core.di

import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.service.AuthServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Define a Dagger module for the application
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // Provide the AuthService implementation as a singleton
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }

}