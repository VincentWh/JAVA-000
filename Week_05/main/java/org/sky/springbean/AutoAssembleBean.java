package org.sky.springbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan(basePackageClasses = Student.class)
public class AutoAssembleBean {
    private static final Logger logger = LoggerFactory.getLogger(AutoAssembleBean.class);

    @Autowired
    Student student;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AutoAssembleBean.class);
        context.refresh();
        AutoAssembleBean autoAssembleBean = context.getBean(AutoAssembleBean.class);
        Student student = autoAssembleBean.student;
        student.setName("sky");
        student.setStudentId(1);
        logger.info("Student:" + student.toString());
    }
}
