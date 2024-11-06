locals {
  organization_id = var.organization_id

  projects_to_create = [
    "scheduled-query",
  ]
}

resource "random_id" "project_prefix" {
  for_each    = toset(local.projects_to_create)
  byte_length = 4
}

resource "google_project" "project" {
  for_each        = toset(local.projects_to_create)
  org_id          = local.organization_id
  name            = each.value
  project_id      = "${each.value}-${random_id.project_prefix[each.key].hex}"
  deletion_policy = "DELETE"
}

resource "google_project_iam_custom_role" "bigquery_transfers_get" {
  project = google_project.project["scheduled-query"].project_id
  role_id = "bigquery_transfers_get"
  title   = "bigquery.transfers.get"
  permissions = [
    "bigquery.transfers.get",
  ]
}

resource "google_project_iam_custom_role" "bigquery_transfers_update" {
  project = google_project.project["scheduled-query"].project_id
  role_id = "bigquery_transfers_update"
  title   = "bigquery.transfers.update"
  permissions = [
    "bigquery.transfers.update",
  ]
}


resource "google_service_account" "scheduled_query_1" {
  project      = google_project.project["scheduled-query"].project_id
  account_id   = "scheduled-query-1"
  display_name = "Scheduled Query 1"
}

resource "google_project_iam_member" "scheduled_query_1_can_bigquery_transfers_get" {
  project = google_project.project["scheduled-query"].project_id
  role    = google_project_iam_custom_role.bigquery_transfers_get.id
  member  = "serviceAccount:${google_service_account.scheduled_query_1.email}"
}

resource "google_service_account" "scheduled_query_2" {
  project      = google_project.project["scheduled-query"].project_id
  account_id   = "scheduled-query-2"
  display_name = "Scheduled Query 2"
}

resource "google_project_iam_member" "scheduled_query_2_can_bigquery_transfers_update" {
  project = google_project.project["scheduled-query"].project_id
  role    = google_project_iam_custom_role.bigquery_transfers_update.id
  member  = "serviceAccount:${google_service_account.scheduled_query_2.email}"
}

