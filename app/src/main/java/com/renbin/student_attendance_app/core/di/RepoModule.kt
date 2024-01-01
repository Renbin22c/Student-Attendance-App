package com.renbin.student_attendance_app.core.di

import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepo
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepoImpl
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepoImpl
import com.renbin.student_attendance_app.data.repo.notes.NoteRepo
import com.renbin.student_attendance_app.data.repo.notes.NoteRepoImpl
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepoImpl
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepoImpl
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
    fun providesTeacherRepo(authService: AuthService): TeacherRepo {
        return TeacherRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesStudentRepo(authService: AuthService): StudentRepo {
        return StudentRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesClassesRepo(authService: AuthService): ClassesRepo {
        return ClassesRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun provideLessonsRepo(authService: AuthService): LessonRepo{
        return LessonRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun provideNotesRepo(authService: AuthService): NoteRepo{
        return NoteRepoImpl(authService = authService)
    }
}