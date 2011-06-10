package org.flexpay.common.util.ejb;

import org.hibernate.ejb.packaging.NamedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Set;

public class NativeScanner extends org.hibernate.ejb.packaging.NativeScanner {

	private static final Logger log = LoggerFactory.getLogger(NativeScanner.class);

	private static final String HBM_XML_PATTERN = "**/*.hbm.xml";

	@Override
	public Set<Package> getPackagesInJar(URL jarToScan, Set<Class<? extends Annotation>> annotationsToLookFor) {
		return super.getPackagesInJar(jarToScan, annotationsToLookFor);
	}

	@Override
	public Set<Class<?>> getClassesInJar(URL jarToScan, Set<Class<? extends Annotation>> annotationsToLookFor) {
		return super.getClassesInJar(jarToScan, annotationsToLookFor);
	}

	@Override
	public Set<NamedInputStream> getFilesInJar(URL jarToScan, Set<String> filePatterns) {
		filePatterns.remove(HBM_XML_PATTERN);
		log.debug("getFilesInJar('{}', '{}')", jarToScan, filePatterns);
		return super.getFilesInJar(jarToScan, filePatterns);
	}

	@Override
	public Set<NamedInputStream> getFilesInClasspath(Set<String> filePatterns) {
		filePatterns.remove(HBM_XML_PATTERN);
		log.debug("getFilesInClasspath('{}')", filePatterns);
		return super.getFilesInClasspath(filePatterns);
	}

	@Override
	public String getUnqualifiedJarName(URL jarToScan) {
		return super.getUnqualifiedJarName(jarToScan);
	}
}
