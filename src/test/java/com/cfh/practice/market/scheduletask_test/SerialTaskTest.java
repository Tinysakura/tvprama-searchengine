package com.cfh.practice.market.scheduletask_test;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: cfh
 * @Date: 2018/9/18 15:00
 * @Description: 测试串行的执行定时任务
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@SpringBootConfiguration
public class SerialTaskTest {

    //使用volatile类型的变量标识上一个定时任务是否结束
    volatile boolean taskEnd = true;

    @Test
    public void testSerialTask() throws Exception{
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        TimerTask serialTask = new TimerTask();

        scheduledExecutorService.scheduleAtFixedRate(serialTask, 0, 3000, TimeUnit.MILLISECONDS);
    }


    private class TimerTask implements Runnable{

        @Override
        public void run() {
            if(taskEnd){
                System.out.println("上一次任务结束,开始下一次任务");
                //模拟耗时操作
                try {
                    Thread.currentThread().sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("上一次任务尚未结束，当次任务不开始");
            }
        }
    }
}
