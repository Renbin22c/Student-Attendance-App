package com.renbin.student_attendance_app.core.di

import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.repo.ClassesRepo
import com.renbin.student_attendance_app.data.repo.ClassesRepoImpl
import com.renbin.student_attendance_app.data.repo.UserRepo
import com.renbin.student_attendance_app.data.repo.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    @Singleton
    fun providesUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesClassesRepo(authService: AuthService): ClassesRepo {
        return ClassesRepoImpl(authService = authService)
    }
}