#!/bin/sh

LIB=../../lib/htmlunit
LIB2=../../lib/commons
CP=$LIB/apache-mime4j-0.6.jar:$LIB/cssparser-0.9.5.jar:$LIB/htmlunit-2.8.jar:$LIB/htmlunit-core-js-2.8.jar:$LIB/httpclient-4.0.1.jar:$LIB/httpcore-4.0.1.jar:$LIB/httpmime-4.0.1.jar:$LIB/nekohtml-1.9.14.jar:$LIB/sac-1.3.jar:$LIB/serializer-2.7.1.jar:$LIB/xalan-2.7.1.jar:$LIB/xercesImpl-2.9.1.jar
CP=$CP:$LIB2/commons-codec-1.4.jar:$LIB2/commons-io-1.4.jar:$LIB2/commons-logging-1.1.1.jar:$LIB2/commons-lang-2.5.jar:$LIB2/commons-collections-3.2.1.jar

echo $CP

groovy -cp $CP groovy/OpenSSOAuth.groovy
