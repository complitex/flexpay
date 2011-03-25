package org.flexpay.common.drools;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;

/**
 * Test If drools library configured properly
 */

public class TestDrools extends SpringBeanAwareTestCase {

	private static final String drl= "package org.flexpay.common.drools;\n" +
									 " dialect \"java\" \n" +
									 "import org.flexpay.common.drools.TestObject;\n" +
									 " global org.flexpay.common.drools.TestFlag testFlag;\n" +
									 " rule \"TestRule\"\n" +
									 " when\n" +
									 " TestObject(id!=0);\n" +
									 " then \n" +
									 " testFlag.setFlag(true);\n" +
									 " end";

	@Test
	/**
	 * Test drools, simple drl file
	 */
	public void TestDrl() throws Exception{
		Reader source = new StringReader(drl);
		PackageBuilder builder = new PackageBuilder();
		builder.addPackageFromDrl(source);
		org.drools.rule.Package pkg = builder.getPackage();
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		ruleBase.addPackage(pkg);
		WorkingMemory workingMemory = ruleBase.newStatefulSession();
		TestFlag tFlag = new TestFlag();
		workingMemory.setGlobal("testFlag", tFlag);
		workingMemory.insert(new TestObject(1));
		workingMemory.fireAllRules();
		assertTrue("Error", tFlag.isFlag());
	}
}
