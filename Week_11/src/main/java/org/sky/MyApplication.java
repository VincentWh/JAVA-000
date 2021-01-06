package org.sky;

import lombok.extern.slf4j.Slf4j;
import org.sky.lock.RedisLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MyApplication {

    private static final String LOCK_NAME = "RedisLock";

    // Seconds
    private static final int EXPIRE_TIME = 10;

    private static int inventory = 10;

    public static void lockTest() {
        System.out.println("lock test:: start sleep 10");

        if (!RedisLock.getInstance().lock(LOCK_NAME, EXPIRE_TIME)) {
            log.error("获取锁失败");
            return;
        }

        try {
            Thread.sleep(10000);
            inventory -= 1;
            log.info("剩余库存: {}", inventory);
        } catch (InterruptedException e) {
            log.error("减库存失败", e);
        }

        RedisLock.getInstance().release(LOCK_NAME);
        log.info("完成，释放锁");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MyApplication.class, args);
        Thread thread1 = new Thread(MyApplication::lockTest);
        Thread thread2 = new Thread(MyApplication::lockTest);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        Thread thread3 = new Thread(MyApplication::lockTest);
        thread3.start();
        thread3.join();
        Thread thread4 = new Thread(MyApplication::lockTest);
        thread4.start();
        thread4.join();
    }
}
