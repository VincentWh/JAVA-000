package sky.week04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * （必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * * 写出你的方法，越多越好，提交到github。
 */
public class MyHomeWork {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        int result = methodA(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
    }

    /**
     * 1.Use inline Thread
     */
    private static int methodA() {
        final int[] result = new int[1];
        Thread thread = new Thread(() -> result[0] = sum());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }


    /**
     * 2.Use ExecutorService thread pool + Future
     */
    private static int methodB() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(MyHomeWork::sum);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return 0;
    }

    /**
     * 3.Use FutureTask
     */
    private static int methodC() {
        FutureTask<Integer> task = new FutureTask<>(MyHomeWork::sum);
        new Thread(task).start();
        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 4.Use ExecutorService thread pool + FutureTask
     */
    private static int methodD() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureTask<Integer> task = new FutureTask<>(MyHomeWork::sum);
        executorService.submit(task);
        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return 0;
    }

    /**
     * 5.Use CompletableFuture
     */
    private static int methodE() {
        return CompletableFuture.supplyAsync(MyHomeWork::sum).join();
    }

    /**
     * 6.Use CountDownLatch + inline Thread
     */
    private static int methodF() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final int[] result = new int[1];
        Thread thread = new Thread(() -> {
            result[0] = sum();
            countDownLatch.countDown();
        });
        new Thread(thread).start();
        try {
            countDownLatch.await();
            return result[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 7.Use CyclicBarrier
     */
    private static int methodG() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        final int[] result = new int[1];
        Thread thread = new Thread(() -> {
            result[0] = sum();
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        new Thread(thread).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }

    /**
     * 8. Use Semaphore
     */
    private static int methodH() {
        Semaphore semaphore = new Semaphore(2);
        final int[] result = new int[1];
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire(2);
                result[0] = sum();
                semaphore.release(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        new Thread(thread).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }

    /**
     * 8.Use parallel stream
     */
    private static int methodI() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        List<Integer> result = list.stream().parallel()
                .map(i -> sum())
                .collect(Collectors.toList());
        return result.get(0);
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
