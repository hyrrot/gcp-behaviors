package jp.hyrrot.terraform_tests

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ImpersonatedCredentials
import com.google.cloud.bigquery.datatransfer.v1.DataTransferServiceClient
import com.google.cloud.bigquery.datatransfer.v1.DataTransferServiceSettings
import com.google.cloud.bigquery.datatransfer.v1.ProjectName
import com.google.cloud.bigquery.datatransfer.v1.TransferConfig
import com.google.protobuf.Struct
import com.google.protobuf.Value
import java.util.*

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val applicationDefaultCredentials = GoogleCredentials.getApplicationDefault()
        .createScoped(Arrays.asList("https://www.googleapis.com/auth/iam"))

    val impersonatedCredentials = ImpersonatedCredentials.create(applicationDefaultCredentials, "terraform-user@hyrrot-automa-dev.iam.gserviceaccount.com", null, Arrays.asList("https://www.googleapis.com/auth/cloud-platform"), 3600)

    val dataTransferServiceClient = DataTransferServiceClient.create(
        DataTransferServiceSettings
        .newBuilder()
        .setQuotaProjectId("hyrrot-automa-dev")
        .setCredentialsProvider(FixedCredentialsProvider.create(impersonatedCredentials))
        .build())

    dataTransferServiceClient.createTransferConfig(
        ProjectName.of("hyrrot-automa-dev"), TransferConfig.newBuilder()
        .setDestinationDatasetId("test")
        .setDisplayName("scheduled_query_test")
        .setDataSourceId("scheduled_query")
        .setSchedule("every 24 hours")
        .setParams(
            Struct.newBuilder().putAllFields(
                mapOf("query" to Value.newBuilder().setStringValue("SELECT 1").build()
                    , "destination_table_name_template" to Value.newBuilder().setStringValue("scheduled_query_test_table").build())
            )
        )
        .build())


}