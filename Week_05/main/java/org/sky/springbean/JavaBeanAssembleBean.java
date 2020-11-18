package org.sky.springbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaBeanAssembleBean {
    private static final Logger logger = LoggerFactory.getLogger(JavaBeanAssembleBean.class);
    @Bean
    public Student getStudent() {
        return new Student();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JavaBeanAssembleBean.class);
        context.refresh();
        Student student = context.getBean(Student.class);
        student.setName("sky");
        student.setStudentId(1);
        logger.info("Student:" + student.toString());
    }
}
