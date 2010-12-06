alter table common_registry_records_tbl

  add index I_registry_town (registry_id, town_name),
  add index I_registry_street (registry_id, street_name),
  add index I_registry_apartment (registry_id, apartment_number);
