package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.domain.sharing.repository.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(linkToShare: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, linkToShare)
        }
        val chooser: Intent = Intent.createChooser(shareIntent, "").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(chooser)
    }

    override fun openLink(linkToOpen: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(linkToOpen)
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        context.startActivity(sendIntent)
    }

    override fun sendEmail(emailData: EmailData) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
        try {
            context.startActivity(sendIntent)
        } catch (exception: Exception) {
            Log.d("ExternalNavigatorError", "Cannot sendEmail: ${exception.message}")
        }

    }

    override fun getTermsLink(): String {
        return DataForExternalNavigation.termsLink
    }

    override fun getShareAppLink(): String {
        return DataForExternalNavigation.shareAppLink
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            email = DataForExternalNavigation.supportEmail,
            subject = DataForExternalNavigation.supportEmailDefaultSubject,
            text = DataForExternalNavigation.supportEmailDefaultText
        )
    }

}