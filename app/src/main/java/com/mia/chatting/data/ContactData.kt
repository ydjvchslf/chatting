package com.mia.chatting.data

data class ContactData (
    val people: List<UserData>? = null
) {
    override fun toString(): String {
        return "[ContactData] friendList.size: ${people?.size}"
    }
}
