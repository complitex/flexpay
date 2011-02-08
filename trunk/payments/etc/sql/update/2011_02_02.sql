INSERT INTO common_user_roles_tbl (id, status, external_id) values (3, 0, 'seniorCashier');
SELECT @senior_cashier_id:=3;

INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Старший кассир', @senior_cashier_id, @ru_id);
INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Senior cashier', @senior_cashier_id, @en_id);
