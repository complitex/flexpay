-- put here module initialization data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('payments'); 

-- init document types
insert into payments_document_types_tbl (id, version, code)
	values (1, 0, 1);
select @doc_type_1:=1;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Оплата наличными', @ru_id, @doc_type_1);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cash payment', @en_id, @doc_type_1);
insert into payments_document_types_tbl (id, version, code)
	values (2, 0, 2);
select @doc_type_2:=2;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Возврат наличных', @ru_id, @doc_type_2);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cash return', @en_id, @doc_type_2);
insert into payments_document_types_tbl (id, version, code)
	values (3, 0, 3);
select @doc_type_3:=3;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Безналичная оплата', @ru_id, @doc_type_3);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cashless payment', @en_id, @doc_type_3);
insert into payments_document_types_tbl (id, version, code)
	values (4, 0, 4);
select @doc_type_4:=4;
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Безналичный возврат', @ru_id, @doc_type_4);
insert into payments_document_type_translations_tbl (name, language_id, type_id)
	values ('Cashless payment return', @en_id, @doc_type_4);

-- init document statuses
insert into payments_document_statuses_tbl (id, version, code)
	values (1, 0, 1);
select @doc_status_1:=1;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Создан', @ru_id, @doc_status_1);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Created', @en_id, @doc_status_1);
insert into payments_document_statuses_tbl (id, version, code)
	values (2, 0, 2);
select @doc_status_2:=2;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Проведен', @ru_id, @doc_status_2);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Registered', @en_id, @doc_status_2);
insert into payments_document_statuses_tbl (id, version, code)
	values (3, 0, 3);
select @doc_status_3:=3;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Удален', @ru_id, @doc_status_3);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Deleted', @en_id, @doc_status_3);
insert into payments_document_statuses_tbl (id, version, code)
	values (4, 0, 4);
select @doc_status_4:=4;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Возвращен', @ru_id, @doc_status_4);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Returned', @en_id, @doc_status_4);
insert into payments_document_statuses_tbl (id, version, code)
	values (5, 0, 5);
select @doc_status_5:=5;
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Ошибка проводки', @ru_id, @doc_status_5);
insert into payments_document_status_translations_tbl(name, language_id, status_id)
	values ('Error', @en_id, @doc_status_5);

-- init operation levels
insert into payments_operation_levels_tbl (id, version, code)
	values (1, 0, 1);
select @operation_level_1:=1;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Низший', @ru_id, @operation_level_1);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Lowest', @en_id, @operation_level_1);
insert into payments_operation_levels_tbl (id, version, code)
	values (2, 0, 2);
select @operation_level_2:=2;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Низкий', @ru_id, @operation_level_2);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Low', @en_id, @operation_level_2);
insert into payments_operation_levels_tbl (id, version, code)
	values (3, 0, 3);
select @operation_level_3:=3;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Средний', @ru_id, @operation_level_3);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Average', @en_id, @operation_level_3);
insert into payments_operation_levels_tbl (id, version, code)
	values (4, 0, 4);
select @operation_level_4:=4;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Высший', @ru_id, @operation_level_4);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('High', @en_id, @operation_level_4);
insert into payments_operation_levels_tbl (id, version, code)
	values (5, 0, 5);
select @operation_level_5:=5;
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Отложенный', @ru_id, @operation_level_5);
insert into payments_operation_level_translations_tbl(name, language_id, level_id)
	values ('Suspended', @en_id, @operation_level_5);

-- init operation statuses
insert into payments_operation_statuses_tbl (id, version, code)
	values (1, 0, 1);
select @operation_status_1:=1;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Создана', @ru_id, @operation_status_1);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Created', @en_id, @operation_status_1);
insert into payments_operation_statuses_tbl (id, version, code)
	values (2, 0, 2);
select @operation_status_2:=2;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Проведена', @ru_id, @operation_status_2);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Registered', @en_id, @operation_status_2);
insert into payments_operation_statuses_tbl (id, version, code)
	values (3, 0, 3);
select @operation_status_3:=3;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Удалена', @ru_id, @operation_status_3);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Deleted', @en_id, @operation_status_3);
insert into payments_operation_statuses_tbl (id, version, code)
	values (4, 0, 4);
select @operation_status_4:=4;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Возвращена', @ru_id, @operation_status_4);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Returned', @en_id, @operation_status_4);
insert into payments_operation_statuses_tbl (id, version, code)
	values (5, 0, 5);
select @operation_status_5:=5;
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Ошибка проводки', @ru_id, @operation_status_5);
insert into payments_operation_status_translations_tbl(name, language_id, status_id)
	values ('Error', @en_id, @operation_status_5);

-- init operation types
insert into payments_operation_types_tbl (id, version, code)
	values (1, 0, 1);
select @operation_type_1:=1;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Оплата наличными услуги', @ru_id, @operation_type_1);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cash payment', @en_id, @operation_type_1);
insert into payments_operation_types_tbl (id, version, code)
	values (2, 0, 2);
select @operation_type_2:=2;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Безналичная оплата услуги', @ru_id, @operation_type_2);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cashless payment', @en_id, @operation_type_2);
insert into payments_operation_types_tbl (id, version, code)
	values (3, 0, 3);
select @operation_type_3:=3;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат оплаты услуги наличными', @ru_id, @operation_type_3);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cash payment return', @en_id, @operation_type_3);
insert into payments_operation_types_tbl (id, version, code)
	values (4, 0, 4);
select @operation_type_4:=4;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат безналичной оплаты услуги', @ru_id, @operation_type_4);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Service cashless payment return', @en_id, @operation_type_4);
insert into payments_operation_types_tbl (id, version, code)
	values (5, 0, 5);
select @operation_type_5:=5;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Оплата наличными квитанции', @ru_id, @operation_type_5);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cash payment', @en_id, @operation_type_5);
insert into payments_operation_types_tbl (id, version, code)
	values (6, 0, 6);
select @operation_type_6:=6;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Безналичная оплата квитанции', @ru_id, @operation_type_6);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cashless payment', @en_id, @operation_type_6);
insert into payments_operation_types_tbl (id, version, code)
	values (7, 0, 7);
select @operation_type_7:=7;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат оплаты квитанции наличными', @ru_id, @operation_type_7);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cash payment return', @en_id, @operation_type_7);
insert into payments_operation_types_tbl (id, version, code)
	values (8, 0, 8);
select @operation_type_8:=8;
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Возврат безналичной оплаты квитанции', @ru_id, @operation_type_8);
insert into payments_operation_type_translations_tbl(name, language_id, type_id)
	values ('Quittance cashless payment return', @en_id, @operation_type_8);
