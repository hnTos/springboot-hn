package com.utopia.springboothn.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.utopia.springboothn.common.BaseJson;
import com.utopia.springboothn.common.BaseJsonData;
import com.utopia.springboothn.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hn
 * @date 2018/08/03
 */
@RestController
public class RedisController extends BaseController {
    final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    IRedisService redisService;

    private static int THREAD_NUM = 100;

    @RequestMapping(value = "/set")
    public BaseJson set(String key,String value){
        redisService.set(key,value);
        return new BaseJson();
    }
    @RequestMapping(value = "/get")
    public BaseJsonData get(String key){
        BaseJsonData jsonData = new BaseJsonData();
        Object value = redisService.get(key);
        jsonData.setData(value);
        return jsonData;
    }
    @RequestMapping(value = "getLock")
    public BaseJson lock(){
        BaseJson json = new BaseJson();
        boolean result = redisService.getLock("lock",request.getSession().getId());
        if (!result){
            json.setErrorMessage("获得锁失败");
        }
        return json;
    }
    @RequestMapping(value = "unLock")
    public BaseJson unLock(){
        BaseJson json = new BaseJson();
        boolean result = redisService.unLock("lock",request.getSession().getId());
        if (!result){
            json.setErrorMessage("释放锁失败");
        }
        return json;
    }
    @RequestMapping(value = "competeLock")
    public BaseJson competeLock(){
        BaseJson json = new BaseJson();
        try {
            compete(THREAD_NUM);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return json;
    }
    public void compete(int nthreads) throws InterruptedException{
        CountDownLatch startGate = new CountDownLatch(1);
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("springboot-hn-thread_%d").build();
        ExecutorService executor = new ThreadPoolExecutor(THREAD_NUM,200,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue(1024),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        final CountDownLatch endGate = new CountDownLatch(nthreads);

        for(int i = 0; i < nthreads; ++i){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        try{
                            task();
                        }finally{
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }////end for

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        logger.info("total elapsed :"+(end-start)+" millisecond");
    }
    private void task() throws InterruptedException {
        AtomicInteger tryNum = new AtomicInteger(1);
        while(true){
            logger.info("thread："+Thread.currentThread().getName()+"第"+(tryNum.get())+"次"+"try to get the distripute lock");
            boolean result = redisService.getLock("lock",String.valueOf(Thread.currentThread().getName()));
            if (result){
                logger.info("thread："+Thread.currentThread().getName()+"第"+tryNum.get()+"次"+"get distripute lock success");
                break;
            } else {
                try {
                    logger.info("thread："+Thread.currentThread().getName()+"第"+tryNum.get()+"次"+"try to get distripute lock fail");
                    if (tryNum.get() >= 10){
                        logger.info("thread："+Thread.currentThread().getName()+"最终获得锁失败");
                        break;
                    } else {
                        Thread.sleep(3000);
                    }
                    tryNum.incrementAndGet();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
