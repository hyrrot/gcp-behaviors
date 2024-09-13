import {
    LocalProgramArgs,
    LocalWorkspace,
    OutputMap
  } from "@pulumi/pulumi/automation";
  import * as upath from "upath";
  
  const args: LocalProgramArgs = {
    stackName: "dev",
    workDir: upath.joinSafe(__dirname, ".", "infra"),
  };
  
  export async function deploy(): Promise<OutputMap> {
    console.log("Initialising stack...");
    const stack = await LocalWorkspace.createOrSelectStack(args);
  
    console.log("Setting region...");
    await stack.setConfig("gcp:project", {value: "hyrrot-pulumi"});
  
    console.log("Run update...");
    const up = await stack.up({ onOutput: console.log });
  
    return up.outputs;
  }
  
  export async function destroy() {
    console.log("Selecting stack...");
    const stack = await LocalWorkspace.createOrSelectStack(args);
  
    console.log("Destroying stack");
    await stack.destroy({onOutput: console.log});
  }
  
  export async function getOutputs(): Promise<OutputMap> {
    const stack = await LocalWorkspace.createOrSelectStack(args);
  
    var outputs = stack.outputs();
  
    return outputs;
  }
  
  export default {deploy, getOutputs};