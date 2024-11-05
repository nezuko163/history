package com.nezuko.auth

data class UserRegister(
    val name: String = "",
    val email: String,
    val password: String
)
