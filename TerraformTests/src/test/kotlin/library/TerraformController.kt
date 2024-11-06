package library

import org.json.JSONObject

class TerraformController(val terraformDirectory: String, val terraformExecutablePath: String) {

    private fun runTerraformProcess(command: Array<String>): Int {
        println(command)
        val result = ProcessBuilder(*command)
            .inheritIO()
            .directory(java.io.File(terraformDirectory))
            .start()
            .waitFor()
        return result
    }

    // TODO: Refactor this to use the same method as runTerraformProcess
    private fun runTerraformProcessAndGetStdout(command: Array<String>): String {
        val process = ProcessBuilder(*command)
            .directory(java.io.File(terraformDirectory))
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        val stdout = process.inputStream.bufferedReader().readText()
        process.waitFor()
        this.throwErrorIfProcessFailed(process.exitValue())
        return stdout
    }

    private fun throwErrorIfProcessFailed(result: Int) {
        if (result != 0) {
            throw Exception("Terraform process failed with exit code $result")
        }
    }

    fun init() {
        val result = this.runTerraformProcess(arrayOf(terraformExecutablePath, "init"))
        this.throwErrorIfProcessFailed(result)
    }

    fun apply(targetAddress: String? = null) {
        if (targetAddress != null) {
            val result = this.runTerraformProcess(arrayOf(terraformExecutablePath, "apply", "-auto-approve", "-target=$targetAddress"))
            this.throwErrorIfProcessFailed(result)
            return
        }
        val result = this.runTerraformProcess(arrayOf(terraformExecutablePath, "apply", "-auto-approve"))
        this.throwErrorIfProcessFailed(result)
    }

    fun destroy(targetAddress: String? = null) {
        if (targetAddress != null) {
            val result = this.runTerraformProcess(arrayOf(terraformExecutablePath, "apply", "-destroy", "-auto-approve", "-target=$targetAddress"))
            this.throwErrorIfProcessFailed(result)
            return
        }
        val result = this.runTerraformProcess(arrayOf(terraformExecutablePath, "apply", "-destroy", "-auto-approve"))
        this.throwErrorIfProcessFailed(result)
    }

    fun getOutputs() : JSONObject {
        val outputs = this.runTerraformProcessAndGetStdout(arrayOf(terraformExecutablePath, "output", "-json"))
        return JSONObject(outputs)
    }


}