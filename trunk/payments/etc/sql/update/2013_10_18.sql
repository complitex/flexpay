INSERT INTO common_user_roles_tbl (id, status, external_id) values (4, 0, 'localAdmin');
SELECT @local_admin_id:=4;
SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
SELECT @en_id:=id from common_languages_tbl where lang_iso_code='en';

INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
  VALUES ('Локальный администратор', @local_admin_id, @ru_id);
INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
  VALUES ('Local admin', @local_admin_id, @en_id);

update common_version_tbl set last_modified_date='2013-10-18', date_version=0;