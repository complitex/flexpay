openssl dsaparam -out dsaparam.pem 1024
openssl gendsa -out dsaprivkey.pem dsaparam.pem
openssl req -new -x509 -key dsaprivkey.pem -out dsacert.pem
