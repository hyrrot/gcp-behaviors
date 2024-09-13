import * as pulumi from "@pulumi/pulumi";
import * as gcp from "@pulumi/gcp";

// Create a GCP resource (Storage Bucket)
// const bucket = new gcp.storage.Bucket("my-bucket", {
//     location: "US"
// });

// Export the DNS name of the bucket
// export const bucketName = bucket.url;

// Define the project details

function throwError(message: string): never {
    throw new Error(message);
}

const PROJECT_ID = process.env.PROJECT_ID || throwError("PROJECT_ID is not set");
const BILLING_ACCOUNT_ID = process.env.BILLING_ACCOUNT_ID || throwError("BILLING_ACCOUNT_ID is not set");

const project = new gcp.organizations.Project(PROJECT_ID, {
    name: PROJECT_ID,
    projectId: PROJECT_ID, // Make sure this is a unique project ID
    billingAccount: BILLING_ACCOUNT_ID, // Replace with your billing account ID
    autoCreateNetwork: true, // Automatically create the default network
    deletionPolicy: "DELETE", // Delete the project when the resource is deleted
});

// Export the project ID
export const projectId = project.projectId;