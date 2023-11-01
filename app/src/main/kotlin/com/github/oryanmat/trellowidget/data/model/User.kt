package com.github.oryanmat.trellowidget.data.model

data class User(
        val id: String = "",
        val fullName: String = "",
        val username: String = "") {
    override fun toString() = "$fullName (@$username)"
}