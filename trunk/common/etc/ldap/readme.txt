для внесения изменений в работающий LDAP сервер нужно выполнить:

ldapmodify -h localhost -p 50389 -D"cn=directory manager" -w flexpayadmin -c  -f flexpay.ldif
