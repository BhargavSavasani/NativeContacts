package com.nativecontacts

data class Contact(
    var contactId: String = "",
    var rawContactId: String = "",
    var firstName: String = "",//givenName
    var lastName: String = "",//familyName
    var displayName: String = "", //display name
    var middleName: String = "", //middleName
    var namePrefix: String = "", //namePrefix
    var nameSuffix: String = "", //nameSuffix
    var nickName: String = "", //nickname
    var phoneticFirstName: String = "", //phoneticGivenName
    var phoneticLastName: String = "", //phoneticFamilyName
    var phoneticMiddleName: String = "", //phoneticMiddleName
    var starred: Int = 0, //Fav or not 1 or 0
    var imageURL: String = "",
    var thumbnailUri: String = "",
    var company: String = "",
    var jobTitle: String = "", //TITLE
    var department: String = "",
    var jobDescription: String = "",
    var officeLocation: String = "",
    var sip: String = "",
    var notes: String = "",
    var accountName: String = "",
    var accountType: String = "",
    var phoneNumberList: ArrayList<PhoneNumber> = ArrayList(),
    var emailList: ArrayList<Email> = ArrayList(),
    var addressList: ArrayList<Address> = ArrayList(),
    var eventList: ArrayList<Event> = ArrayList(),
    var websiteList: ArrayList<Website> = ArrayList(),
    var relationShipList: ArrayList<RelationShip> = ArrayList(),
    var instantMessagingList: ArrayList<InstantMessaging> = ArrayList()
)

data class PhoneNumber(
    var type: Int = 0,
    var label: String = "",
    var number: String = "",
    var normalizedNumber: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)

data class Email(
    var type: Int = 0,
    var label: String = "",
    var emailAddress: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)

data class Address(
    var type: Int = 0,
    var label: String = "",
    var formattedAddress: String = "",
    var street: String = "",
    var poBox: String = "",
    var neighborhood: String = "",
    var city: String = "",
    var region: String = "",
    var postcode: String = "",
    var country: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)

data class Event(
    var type: Int = 0,
    var label: String = "",
    var date: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)

data class Website(
    var type: Int = 0,
    var label: String = "",
    var url: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)

data class RelationShip(
    var type: Int = 0,
    var label: String = "",
    var name: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)

data class InstantMessaging(
    val protocol: Int = 0,
    val label: String = "",
    val data: String = "",
    var isPrimary: Boolean,
    var isSuperPrimary: Boolean
)
