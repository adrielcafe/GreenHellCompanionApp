package cafe.adriel.greenhell.view.main.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import cafe.adriel.greenhell.*
import kotlinx.android.synthetic.main.dialog_about.*

class AboutDialog private constructor(context: Context) : AppCompatDialog(context) {

    companion object {
        fun show(context: Context) = AboutDialog(context).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_about)

        val projectRepoIcon = context.getDrawable(R.drawable.ic_github).also {
            it?.setTint(context.colorFrom(R.color.colorPrimaryDark))
        }
        vProjectRepo.setCompoundDrawablesRelativeWithIntrinsicBounds(projectRepoIcon, null, null, null)
        vAppVersion.text = "${context.getString(R.string.app_name)} v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"

        vClose.setOnClickListener { dismiss() }
        vEmail.setOnClickListener {
            sendEmail()
            Analytics.logSendEmail()
        }
        vWebsite.setOnClickListener {
            Uri.parse(App.WEBSITE).open(context)
            Analytics.logOpenUrl(App.WEBSITE)
        }
        vGitHub.setOnClickListener {
            Uri.parse(App.GITHUB_PROFILE_URL).open(context)
            Analytics.logOpenUrl(App.GITHUB_PROFILE_URL)
        }
        vLinkedIn.setOnClickListener {
            Uri.parse(App.LINKEDIN_PROFILE_URL).open(context)
            Analytics.logOpenUrl(App.LINKEDIN_PROFILE_URL)
        }
        vProjectRepo.setOnClickListener {
            Uri.parse(App.PROJECT_REPO_URL).open(context)
            Analytics.logOpenUrl(App.PROJECT_REPO_URL)
        }
    }

    private fun sendEmail(){
        val email = Uri.parse("mailto:${App.EMAIL}")
        val subject = "${context.getString(R.string.app_name)} for Android | v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}), SDK ${Build.VERSION.SDK_INT}"
        Intent(Intent.ACTION_SENDTO, email).run {
            putExtra(Intent.EXTRA_SUBJECT, subject)
            context.startActivity(this)
        }
    }

}