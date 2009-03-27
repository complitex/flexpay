-- add init data
delete from accounting_document_types_tbl;

insert into accounting_document_types_tbl (id, code, name)
	values (1, 'CASH_PAYMENT', 'accounting.document.type.cash_payment');
insert into accounting_document_types_tbl (id, code, name)
	values (2, 'CASH_RETURN', 'accounting.document.type.cash_payment_return');
insert into accounting_document_types_tbl (id, code, name)
	values (3, 'CASHLESS_PAYMENT', 'accounting.document.type.cashless_payment');
insert into accounting_document_types_tbl (id, code, name)
	values (4, 'CASHLESS_PAYMENT_RETURN', 'accounting.document.type.cashless_payment_return');
