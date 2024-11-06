package library

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ImpersonatedCredentials
import java.util.*

class GCPAuthHelper(scopes: List<String> = listOf("https://www.googleapis.com/auth/cloud-platform")) {
    private val CREDENTIAL_LIFETIME_SECONDS = 3600

    private val applicationDefaultCredentials = GoogleCredentials.getApplicationDefault()
        .createScoped(Arrays.asList("https://www.googleapis.com/auth/iam"))

    fun getImpersonatedCredentials(targetPrincipal: String, scopes: List<String>, delegates: List<String>? = null) : ImpersonatedCredentials{
        return ImpersonatedCredentials.create(applicationDefaultCredentials, targetPrincipal, delegates, scopes, CREDENTIAL_LIFETIME_SECONDS)
    }

}