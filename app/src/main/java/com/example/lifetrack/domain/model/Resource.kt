package com.example.lifetrack.domain.model

sealed class Resource {
    object Failure : Resource()
    object Success : Resource()
    object Idle : Resource()
}
