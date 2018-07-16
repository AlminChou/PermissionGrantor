package com.almin.permissiongrantor.permission

import android.Manifest


/**
 * Created by Almin on 2018/7/10.
 */
class Permission(val name: String, val requestCode: Int = -1) {

    companion object {
        private const val MICROPHONE_REQUEST_CODE = 0
        private const val CONTACTS_REQUEST_CODE = 1
        private const val PHONE_REQUEST_CODE = 2
        private const val CAMERA_REQUEST_CODE = 3
        private const val LOCATION_REQUEST_CODE = 4
        private const val SENSORS_REQUEST_CODE = 5
        private const val STORAGE_REQUEST_CODE = 6
        private const val SMS_REQUEST_CODE = 7
        private const val CALENDAR_REQUEST_CODE = 8

        private const val MICROPHONE = Manifest.permission.RECORD_AUDIO
        private const val CONTACTS = Manifest.permission.GET_ACCOUNTS
        private const val PHONE = Manifest.permission.READ_PHONE_STATE
        private const val CAMERA = Manifest.permission.CAMERA
        private const val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val SENSORS = Manifest.permission.BODY_SENSORS
        private const val STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val SMS = Manifest.permission.READ_SMS
        private const val CALENDAR = Manifest.permission.WRITE_CALENDAR


        // dangerous permission list, just need request one of the groups
        // and you need to register the same permissions to the manifest XML file, otherwise not work.

        /*
        group:android.permission-group.MICROPHONE
            permission:android.permission.RECORD_AUDIO
         */
        fun mic() = Permission(MICROPHONE, MICROPHONE_REQUEST_CODE)

        /*
        group:android.permission-group.CONTACTS
            permission:android.permission.WRITE_CONTACTS
            permission:android.permission.GET_ACCOUNTS
            permission:android.permission.READ_CONTACTS
        */
        fun contacts() = Permission(CONTACTS, CONTACTS_REQUEST_CODE)


        /*
        group:android.permission-group.PHONE
            permission:android.permission.READ_CALL_LOG
            permission:android.permission.READ_PHONE_STATE
            permission:android.permission.CALL_PHONE
            permission:android.permission.WRITE_CALL_LOG
            permission:android.permission.USE_SIP
            permission:android.permission.PROCESS_OUTGOING_CALLS
            permission:com.android.voicemail.permission.ADD_VOICEMAIL
         */
        fun phone() = Permission(PHONE, PHONE_REQUEST_CODE)

        /*
        group:android.permission-group.CAMERA
            permission:android.permission.CAMERA
         */
        fun camera() = Permission(CAMERA, CAMERA_REQUEST_CODE)

        /*
        group:android.permission-group.LOCATION
            permission:android.permission.ACCESS_FINE_LOCATION
            permission:android.permission.ACCESS_COARSE_LOCATION
         */
        fun location() = Permission(LOCATION, LOCATION_REQUEST_CODE)

        /*
        group:android.permission-group.SENSORS
            permission:android.permission.BODY_SENSORS
         */
        fun sensors() = Permission(SENSORS, SENSORS_REQUEST_CODE)

        /*
        group:android.permission-group.STORAGE
            permission:android.permission.READ_EXTERNAL_STORAGE
            permission:android.permission.WRITE_EXTERNAL_STORAGE
         */
        fun storage() = Permission(STORAGE, STORAGE_REQUEST_CODE)

        /*
        group:android.permission-group.SMS
            permission:android.permission.READ_SMS
            permission:android.permission.RECEIVE_WAP_PUSH
            permission:android.permission.RECEIVE_MMS
            permission:android.permission.RECEIVE_SMS
            permission:android.permission.SEND_SMS
            permission:android.permission.READ_CELL_BROADCASTS
         */
        fun sms() = Permission(SMS, SMS_REQUEST_CODE)

        /*
        group:android.permission-group.CALENDAR
            permission:android.permission.READ_CALENDAR
            permission:android.permission.WRITE_CALENDAR
         */
        fun calendar() = Permission(CALENDAR, CALENDAR_REQUEST_CODE)
    }

}