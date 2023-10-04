package com.example.playlistmaker.domain.sharing.repository

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun openLink(linkToOpen: String)

    fun shareLink(linkToShare: String)

    fun sendEmail(emailData: EmailData)

    fun getTermsLink(): String

    fun getShareAppLink(): String

    fun getSupportEmailData(): EmailData
}