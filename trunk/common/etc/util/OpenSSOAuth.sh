#!/bin/sh

LIB=../../lib/htmlunit
LIB2=../../lib/commons
CP=$LIB/cssparser-0.9.5.jar:$LIB/htmlunit-2.6.jar:$LIB/htmlunit-core-js-2.6.jar:$LIB/nekohtml-1.9.13.jar:$LIB/sac-1.3.jar:$LIB/serializer-2.7.1.jar:$LIB/xalan-2.7.1.jar:$LIB/xercesImpl-2.9.1.jar
CP=$CP:$LIB2/commons-codec-1.3.jar:$LIB2/commons-io-1.4.jar:$LIB2/commons-logging-1.1.1.jar:$LIB2/commons-lang-2.4.jar:$LIB2/commons-httpclient-3.1.jar:$LIB2/commons-collections-3.2.1.jar

echo $CP

groovy -cp $CP groovy/OpenSSOAuth.groovy
