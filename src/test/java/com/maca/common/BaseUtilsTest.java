package com.maca.common;

import com.maca.mock.MockFactory;
import com.maca.mock.Person;
import com.maca.utils.MacaUtils;

import junit.framework.TestCase;

public class BaseUtilsTest  extends TestCase{

	public void testConvertPropertyName() {
		assertEquals(MacaUtils.convertPropertyName("a.1.b.c"), "a[1].b.c");
		assertEquals(MacaUtils.convertPropertyName("1.a.b.c"), "[1].a.b.c");
		assertEquals(MacaUtils.convertPropertyName("a1.1.b1.2"), "a1[1].b1[2]");
		assertEquals(MacaUtils.convertPropertyName("1"), "[1]");
	}
	
	public void testGetProperty() {
		Person person = MockFactory.createPerson();
		assertEquals(MacaUtils.getProperty(person, "profile.face"), "e");
		assertEquals(MacaUtils.getProperty(person, "profiles.2.face"), "c");
		assertEquals(MacaUtils.getProperty(person, "name"), "Haowei");
	}
	
	public void testSetProperty() {
		Person person = MockFactory.createPerson();
		assertEquals(MacaUtils.getProperty(person, "map.attr1"), "value1");
		assertEquals(MacaUtils.getProperty(person.getMap(), "attr1"), "value1");
	}
	
}
