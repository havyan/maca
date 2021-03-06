/**
 * 
 */
package com.maca.proxy.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.apache.commons.beanutils.BeanUtils;

import com.maca.proxy.DOFactory;
import com.maca.proxy.interfaces.Bean;

/**
 * @author HWYan
 * 
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Person person = new Person();
		person = DOFactory.createDynamicBeanObject(person);
		DOFactory.createDynamicBeanObject(new Person());
		DOFactory.createDynamicBeanObject(new Person());
		person.setName("haowei");
		person.setAge(27);
		person.toString();
		((Bean) person).setProperty("name", "xinxin");

		System.out.println(ArrayList.class.isAssignableFrom(List.class));
		System.out.println(BeanUtils.class.getResource("").getPath());
		System.out.println(JButton.class.isAssignableFrom(JComponent.class));
	}

}
