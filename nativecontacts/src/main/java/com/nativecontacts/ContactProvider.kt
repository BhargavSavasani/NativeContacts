package com.nativecontacts

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.*
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Event
import android.provider.ContactsContract.CommonDataKinds.Website
import android.provider.ContactsContract.Data
import android.provider.ContactsContract.RawContacts
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

object ContactProvider {

    private fun getContactProjection() = arrayOf(
        Data.CONTACT_ID,
        Data.RAW_CONTACT_ID,
        Contactables.PHOTO_URI,
        StructuredName.PHOTO_THUMBNAIL_URI,
        StructuredName.STARRED,
        RawContacts.ACCOUNT_NAME,
        RawContacts.ACCOUNT_TYPE,
        Data.MIMETYPE,
        StructuredName.GIVEN_NAME,
        StructuredName.FAMILY_NAME,
        StructuredName.MIDDLE_NAME,
        StructuredName.SUFFIX,
        StructuredName.PREFIX,
        StructuredName.PHONETIC_GIVEN_NAME,
        StructuredName.PHONETIC_FAMILY_NAME,
        StructuredName.PHONETIC_MIDDLE_NAME,
        Phone.TYPE,
        Phone.LABEL,
        Phone.NUMBER,
        Phone.NORMALIZED_NUMBER,
        Phone.IS_PRIMARY,
        Phone.IS_SUPER_PRIMARY,
        Email.TYPE,
        Email.LABEL,
        Email.ADDRESS,
        Email.IS_PRIMARY,
        Email.IS_SUPER_PRIMARY,
        Organization.COMPANY,
        Organization.TITLE,
        Organization.DEPARTMENT,
        Organization.JOB_DESCRIPTION,
        Organization.OFFICE_LOCATION,
        StructuredPostal.TYPE,
        StructuredPostal.LABEL,
        StructuredPostal.FORMATTED_ADDRESS,
        StructuredPostal.STREET,
        StructuredPostal.POBOX,
        StructuredPostal.NEIGHBORHOOD,
        StructuredPostal.CITY,
        StructuredPostal.REGION,
        StructuredPostal.POSTCODE,
        StructuredPostal.COUNTRY,
        StructuredPostal.IS_PRIMARY,
        StructuredPostal.IS_SUPER_PRIMARY,
        Website.TYPE,
        Website.LABEL,
        Website.URL,
        Website.IS_PRIMARY,
        Website.IS_SUPER_PRIMARY,
        Im.PROTOCOL,
        Im.CUSTOM_PROTOCOL,
        Im.DATA,
        Relation.TYPE,
        Relation.LABEL,
        Relation.NAME,
        Event.TYPE,
        Event.LABEL,
        Event.START_DATE,
        SipAddress.SIP_ADDRESS,
        Nickname.NAME,
        Note.NOTE
    )

    fun getNativeContacts(
        context: Context,
        onGetContacts: (contactList: ArrayList<Contact>) -> Unit
    ) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    onGetContacts(getContacts(context))
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                }

            })
            .setDeniedMessage(
                context.getString(R.string.msg_no_permission_contact)
                    .replace("#", context.getString(R.string.app_name))
            )
            .setPermissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            ).check()
    }

    private fun getContacts(context: Context): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()
        context.contentResolver.query(
            Data.CONTENT_URI,
            getContactProjection(),
            null,
            null,
            null
        )?.let { cursor ->
            while (cursor.moveToNext()) {
                val cId = cursor.getStringValue(Data.CONTACT_ID)
                if (!contactList.any { it.contactId == cId }) {
                    contactList.add(Contact(contactId = cId))
                }
                contactList.find { it.contactId == cId }?.apply {
                    rawContactId = cursor.getStringValue(Data.RAW_CONTACT_ID)
                    imageURL = cursor.getStringValue(Contactables.PHOTO_URI)
                    thumbnailUri = cursor.getStringValue(StructuredName.PHOTO_THUMBNAIL_URI)
                    starred = cursor.getIntValue(StructuredName.STARRED)
                    accountName = cursor.getStringValue(RawContacts.ACCOUNT_NAME)
                    accountType = cursor.getStringValue(RawContacts.ACCOUNT_TYPE)
                    val mimeType = cursor.getStringValue(Data.MIMETYPE)
                    setContactDetails(context, cursor, this, mimeType)
                }
            }
            cursor.close()
        }
        return contactList
    }

    private fun setContactDetails(
        context: Context,
        cursor: Cursor,
        contact: Contact,
        mimeType: String
    ) {
        when (mimeType) {
            StructuredName.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    firstName = cursor.getStringValue(StructuredName.GIVEN_NAME)
                    lastName = cursor.getStringValue(StructuredName.FAMILY_NAME)
                    displayName = cursor.getStringValue(StructuredName.DISPLAY_NAME)
                    middleName = cursor.getStringValue(StructuredName.MIDDLE_NAME)
                    nameSuffix = cursor.getStringValue(StructuredName.SUFFIX)
                    namePrefix = cursor.getStringValue(StructuredName.PREFIX)
                    phoneticFirstName = cursor.getStringValue(StructuredName.PHONETIC_GIVEN_NAME)
                    phoneticLastName = cursor.getStringValue(StructuredName.PHONETIC_FAMILY_NAME)
                    phoneticMiddleName = cursor.getStringValue(StructuredName.PHONETIC_MIDDLE_NAME)
                }
            }
            Phone.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val type = cursor.getIntValue(Phone.TYPE)
                    phoneNumberList.add(
                        PhoneNumber(
                            type = type,
                            label = getPhoneLabel(context, cursor, type),
                            number = cursor.getStringValue(Phone.NUMBER),
                            normalizedNumber = cursor.getStringValue(Phone.NORMALIZED_NUMBER),
                            isPrimary = cursor.getIntValue(Phone.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(Phone.IS_SUPER_PRIMARY).toBoolean(),
                        )
                    )
                }
            }
            Email.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val type = cursor.getIntValue(Email.TYPE)
                    emailList.add(
                        Email(
                            type = type,
                            label = getEmailLabel(context, cursor, type),
                            emailAddress = cursor.getStringValue(Email.ADDRESS),
                            isPrimary = cursor.getIntValue(Email.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(Email.IS_SUPER_PRIMARY).toBoolean(),
                        )
                    )
                }
            }
            Organization.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    company = cursor.getStringValue(Organization.COMPANY)
                    department = cursor.getStringValue(Organization.DEPARTMENT)
                    jobDescription = cursor.getStringValue(Organization.JOB_DESCRIPTION)
                    officeLocation = cursor.getStringValue(Organization.OFFICE_LOCATION)
                    jobTitle = cursor.getStringValue(Organization.TITLE)
                }
            }
            StructuredPostal.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val type = cursor.getIntValue(StructuredPostal.TYPE)
                    val nativeLabel = getAddressLabel(context, cursor, type)
                    addressList.add(
                        Address(
                            type = type,
                            label = nativeLabel,
                            formattedAddress = cursor.getStringValue(StructuredPostal.FORMATTED_ADDRESS),
                            street = cursor.getStringValue(StructuredPostal.STREET),
                            poBox = cursor.getStringValue(StructuredPostal.POBOX),
                            neighborhood = cursor.getStringValue(StructuredPostal.NEIGHBORHOOD),
                            city = cursor.getStringValue(StructuredPostal.CITY),
                            region = cursor.getStringValue(StructuredPostal.REGION),
                            postcode = cursor.getStringValue(StructuredPostal.POSTCODE),
                            country = cursor.getStringValue(StructuredPostal.COUNTRY),
                            isPrimary = cursor.getIntValue(StructuredPostal.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(StructuredPostal.IS_SUPER_PRIMARY)
                                .toBoolean()
                        )
                    )
                }
            }
            Website.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val type = cursor.getIntValue(Website.TYPE)
                    websiteList.add(
                        Website(
                            type = type,
                            label = getAddressLabel(context, cursor, type),
                            url = cursor.getStringValue(Website.URL),
                            isPrimary = cursor.getIntValue(Website.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(Website.IS_SUPER_PRIMARY)
                                .toBoolean(),
                        )
                    )
                }
            }
            Im.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val protocol = cursor.getIntValue(Im.PROTOCOL)
                    instantMessagingList.add(
                        InstantMessaging(
                            protocol = protocol,
                            label = getInstantMessagingLabel(context, cursor, protocol),
                            data = cursor.getStringValue(Im.DATA),
                            isPrimary = cursor.getIntValue(Im.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(Im.IS_SUPER_PRIMARY).toBoolean(),
                        )
                    )
                }
            }
            Relation.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val type = cursor.getIntValue(Relation.TYPE)
                    relationShipList.add(
                        RelationShip(
                            type = type,
                            label = getRelationshipLabel(context, cursor, type),
                            name = cursor.getStringValue(Relation.NAME),
                            isPrimary = cursor.getIntValue(Relation.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(Relation.IS_SUPER_PRIMARY)
                                .toBoolean(),
                        )
                    )
                }
            }
            Event.CONTENT_ITEM_TYPE -> {
                contact.apply {
                    val type = cursor.getIntValue(Event.TYPE)
                    eventList.add(
                        Event(
                            type = type,
                            label = getEventLabel(context, cursor, type),
                            date = cursor.getStringValue(Event.START_DATE),
                            isPrimary = cursor.getIntValue(Event.IS_PRIMARY).toBoolean(),
                            isSuperPrimary = cursor.getIntValue(Event.IS_SUPER_PRIMARY).toBoolean(),
                        )
                    )
                }
            }
            SipAddress.CONTENT_ITEM_TYPE -> {
                contact.sip = cursor.getStringValue(SipAddress.SIP_ADDRESS)
            }
            Nickname.CONTENT_ITEM_TYPE -> contact.nickName = cursor.getStringValue(Nickname.NAME)
            Note.CONTENT_ITEM_TYPE -> contact.notes = cursor.getStringValue(Note.NOTE)
        }
    }

    private fun getPhoneLabel(context: Context, cursor: Cursor, type: Int): String {
        return if (type == BaseTypes.TYPE_CUSTOM) {
            cursor.getStringValue(Phone.LABEL)
        } else {
            Phone.getTypeLabel(
                context.resources,
                type,
                cursor.getColumnIndex(Phone.LABEL).toString()
            ).toString()
        }
    }

    private fun getEmailLabel(context: Context, cursor: Cursor, type: Int): String {
        return if (type == BaseTypes.TYPE_CUSTOM) {
            cursor.getStringValue(Email.LABEL)
        } else {
            Email.getTypeLabel(
                context.resources,
                type,
                cursor.getColumnIndex(Email.LABEL).toString()
            ).toString()
        }
    }

    private fun getAddressLabel(context: Context, cursor: Cursor, type: Int): String {
        return if (type == BaseTypes.TYPE_CUSTOM) {
            cursor.getStringValue(StructuredPostal.LABEL)
        } else {
            StructuredPostal.getTypeLabel(
                context.resources, type,
                cursor.getColumnIndex(StructuredPostal.LABEL).toString()
            ).toString()
        }
    }

    private fun getInstantMessagingLabel(context: Context, cursor: Cursor, protocol: Int): String {
        return if (protocol == Im.PROTOCOL_CUSTOM) {
            cursor.getStringValue(Im.CUSTOM_PROTOCOL)
        } else {
            Im.getProtocolLabel(
                context.resources, protocol,
                cursor.getColumnIndex(Im.CUSTOM_PROTOCOL).toString()
            ).toString()
        }
    }

    private fun getRelationshipLabel(context: Context, cursor: Cursor, type: Int): String {
        return if (type == BaseTypes.TYPE_CUSTOM) {
            cursor.getStringValue(Relation.LABEL)
        } else {
            Relation.getTypeLabel(
                context.resources, type,
                cursor.getColumnIndex(Relation.LABEL).toString()
            ).toString()
        }
    }

    private fun getEventLabel(context: Context, cursor: Cursor, type: Int): String {
        return if (type == BaseTypes.TYPE_CUSTOM) {
            cursor.getStringValue(Event.LABEL)
        } else {
            Event.getTypeLabel(
                context.resources,
                type,
                cursor.getColumnIndex(Event.LABEL).toString()
            ).toString()
        }
    }
}