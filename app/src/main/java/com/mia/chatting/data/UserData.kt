package com.mia.chatting.data

data class UserData (
    val name: String? = null,
    val uid: String? = null,
    val email: String? = null,
    val contacts: ArrayList<String>? = null
) {
    override fun toString(): String {
        return "[UserData] uid: $uid, name: $name, email: $email, contacts: $contacts"
    }
}
