package com.nezuko.domain.model

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
) {
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "photoUrl" to photoUrl
        )
    }
}
