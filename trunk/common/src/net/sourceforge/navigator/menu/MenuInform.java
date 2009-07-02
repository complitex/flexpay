package net.sourceforge.navigator.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.exception.FlexPayException;

import java.util.HashSet;
import java.util.Set;

public class MenuInform {

	private Set<String> namesOfAllMenuComponents = new HashSet<String>();

	public void addNameOfMenuComponent(String name) throws FlexPayException {
		if (!namesOfAllMenuComponents.add(name)) {
			throw new FlexPayException("Error in menu structure: two elements with same name " + name);
		}
	}

	public Set<String> getNamesOfAllMenuComponents() {
		return namesOfAllMenuComponents;
	}

	public void setNamesOfAllMenuComponents(Set<String> namesOfAllMenuComponents) {
		this.namesOfAllMenuComponents = namesOfAllMenuComponents;
	}

}
