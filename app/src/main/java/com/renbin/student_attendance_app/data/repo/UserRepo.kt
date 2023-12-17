package com.renbin.student_attendance_app.data.repo

import com.renbin.student_attendance_app.data.model.User

interface UserRepo {
    suspend fun addUser(user: User)
    suspend fun getUser(): User?
    suspend fun removeUser()
    suspend fun updateUser(user: User)
}