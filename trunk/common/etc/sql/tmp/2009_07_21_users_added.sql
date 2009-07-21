insert into common_users_tbl (discriminator, user_name, full_name, last_name, language_code, page_size)
	values ('common', 'developer', 'Гейтс Билл', 'Гейтс', 'en', 20);
select @user_test:=1;
insert into common_users_tbl (discriminator, user_name, full_name, last_name, language_code, page_size)
	values ('common', 'testpay', 'Мария Ивановна Пугачева', 'Пугачева', 'en', 10);

insert into common_users_tbl (discriminator, user_name, full_name, last_name, language_code)
	values ('common', 'buhgalter', 'Иванова Александра Петровна', 'Иванова', 'ru');
