package com.maca.proxy;

import java.util.ArrayList;
import java.util.List;

import com.maca.mock.Person;
import com.maca.mock.Profile;
import com.maca.proxy.interfaces.Bean;
import com.maca.proxy.interfaces.DynamicCollection;

import junit.framework.TestCase;

public class DynamicObjectFactoryTest extends TestCase {

	public void setUp() {

	}

	public void testCreateDynamicBeanObject() throws Exception {
		Person person = new Person("Haowei", 31);
		List<Profile> profiles = new ArrayList<Profile>();
		Profile profile1 = new Profile("a", 170, 140);
		Profile profile2 = new Profile("b", 170, 140);
		Profile profile3 = new Profile("c", 170, 140);
		Profile profile4 = new Profile("d", 170, 140);
		profiles.add(profile1);
		profiles.add(profile2);
		profiles.add(profile3);
		profiles.add(profile4);
		person.setProfiles(profiles);
		person.setProfile(new Profile("e", 170, 140));
		Person bean = DOFactory.createDynamicBeanObject(person);
		assertTrue(bean instanceof Bean);
		assertTrue(bean.getProfile() instanceof Bean);
		assertTrue(bean.getProfiles() instanceof DynamicCollection);
		assertTrue(bean.getProfiles().get(0) instanceof Bean);
		bean.getProfiles().remove(profile3);
		assertTrue(bean.getProfiles().size() == 3);
		profiles = new ArrayList<Profile>();
		profiles.add(profile1);
		profiles.add(profile2);
		bean.getProfiles().removeAll(profiles);
		assertTrue(bean.getProfiles().size() == 1);
		assertTrue(bean.getProfiles().get(0).getFace().equals("d"));
	}
	
	public void testLoopDependency() throws Exception {
		Person person = new Person("Haowei", 31);
		List<Profile> profiles = new ArrayList<Profile>();
		Profile profile1 = new Profile("a", 170, 140);
		Profile profile2 = new Profile("b", 170, 140);
		Profile profile3 = new Profile("c", 170, 140);
		Profile profile4 = new Profile("d", 170, 140);
		profiles.add(profile1);
		profiles.add(profile2);
		profiles.add(profile3);
		profiles.add(profile4);
		person.setProfiles(profiles);
		
		Profile profile = new Profile("e", 170, 140);
		person.setProfile(profile);
		profile.setPerson(person);
		Person bean = DOFactory.createDynamicBeanObject(person);
		bean.getProfile().setHeight(180);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testCreateDynamicListObject() throws Exception {
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person("Haowei", 31));
		persons.add(new Person("Zhangsan", 15));
		persons.add(new Person("Lisi", 16));
		persons.add(new Person("Kobe", 17));
		persons.add(new Person("James", 20));
		List list = DOFactory.createDynamicListObject(persons);
		assertTrue(list instanceof DynamicCollection);
		assertTrue(list.get(0) instanceof Bean);
		DynamicCollection dlist = (DynamicCollection) list;
		List<Object> events = new ArrayList<Object>();
		dlist.addChangeListener((e) -> {
			events.add(e);
		});
		list.get(3);
		list.isEmpty();
		list.hashCode();
		assertTrue(events.size() == 0);
		list.add(new Person());
		assertTrue(events.size() == 1);
	}

	public void testEquals() {
		Person person1 = new Person("Haowei", 31);
		Person person2 = DOFactory.createDynamicBeanObject(person1);
		assertTrue(person2.equals(person1));
	}

	public void testList() {
		List<Person> list = new ArrayList<Person>();
		Person person1 = new Person();
		list.add(person1);
		Person person2 = new Person();
		list.add(person2);
		list = DOFactory.createDynamicObject(list);
		assertTrue(list.indexOf(person1) == 0);
		assertTrue(list.indexOf(person2) == 1);
	}
	
	public void testCloneSource() {
		Person person1 = new Person("Haowei", 31);
		List<Profile> profiles = new ArrayList<Profile>();
		Profile profile1 = new Profile("a", 170, 140);
		Profile profile2 = new Profile("b", 170, 140);
		Profile profile3 = new Profile("c", 170, 140);
		Profile profile4 = new Profile("d", 170, 140);
		profiles.add(profile1);
		profiles.add(profile2);
		profiles.add(profile3);
		profiles.add(profile4);
		person1.setProfiles(profiles);
		
		Person bean = DOFactory.createDynamicBeanObject(person1);
		Profile profile5 = new Profile("2", 170, 140);
		bean.getProfiles().add(profile5);
		Person person2 = (Person)(((Bean) bean).cloneSource());
		assertTrue(!(person2 instanceof Bean));
		assertTrue(person2.getProfiles().size() == 5);
		assertTrue(person1.getProfiles().get(0) != person2.getProfiles().get(0));
		assertTrue(!(person2.getProfiles().get(4) instanceof Bean));
	}

}
