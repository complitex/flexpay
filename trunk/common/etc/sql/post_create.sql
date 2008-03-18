ALTER TABLE person_identities_tbl
  ADD INDEX data_index (first_name, middle_name,last_name);
