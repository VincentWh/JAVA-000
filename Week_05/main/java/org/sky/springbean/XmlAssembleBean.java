package org.sky.springbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlAssembleBean {
    private static final Logger logger = LoggerFactory.getLogger(XmlAssembleBean.class);
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("xmlAssemble.xml" );
        Student student = (Student) context.getBean("student");
        student.setName("sky");
        student.setStudentId(1);
        logger.info("Student:" + student.toString());
    }
}
