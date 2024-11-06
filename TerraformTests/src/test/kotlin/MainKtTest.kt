import com.google.api.gax.core.FixedCredentialsProvider
import com.google.cloud.bigquery.datatransfer.v1.DataTransferServiceClient
import com.google.cloud.bigquery.datatransfer.v1.DataTransferServiceSettings
import com.google.cloud.bigquery.datatransfer.v1.ProjectName
import com.google.cloud.bigquery.datatransfer.v1.TransferConfig
import com.google.protobuf.Struct
import com.google.protobuf.Value
import library.GCPAuthHelper
import library.TerraformController
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*


class ScheduledQueryTest {

    val terraformController = TerraformController(System.getenv("TERRAFORM_DIR"), System.getenv("TERRAFORM_EXECUTABLE_PATH"))
    var terraformOutput: JSONObject? = null
    var project: String? = null
    val authHelper = GCPAuthHelper(Arrays.asList("https://bigquerydatatransfer.googleapis.com"))


    @BeforeEach
    fun setUp() {
        terraformController.init()
        terraformController.apply("module.scheduled-query")
        this.terraformOutput = terraformController.getOutputs()
        this.project = terraformOutput!!.getJSONObject("project").getString("value")
    }

    @AfterEach
    fun tearDown() {
        terraformController.destroy("module.scheduled-query")
    }

    @Test
    fun testSA1CanCreateSA() {
        println(this.terraformOutput.toString())
        assert(true)

        val sa1Credential = authHelper.getImpersonatedCredentials(
            "scheduled-query-1@${this.project}.iam.gserviceaccount.com",
            Arrays.asList("https://www.googleapis.com/auth/cloud-platform")
        )
        val dataTransferServiceClient = DataTransferServiceClient.create(DataTransferServiceSettings
            .newBuilder()
            .setQuotaProjectId(this.project)
            .setCredentialsProvider(FixedCredentialsProvider.create(sa1Credential))
            .build())

        dataTransferServiceClient.createTransferConfig(this.project, TransferConfig.newBuilder()
            .setDestinationDatasetId("scheduled_query_test")
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




}