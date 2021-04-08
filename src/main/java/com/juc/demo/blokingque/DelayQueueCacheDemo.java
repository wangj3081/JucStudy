package com.juc.demo.blokingque;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DelayQueueCacheDemo
 * @Description 通过延迟队列实现缓存
 * @Author wangjian
 * @Date 2021/3/23 下午10:30
 * @Version 1.0
 **/
public class DelayQueueCacheDemo {
    public static void main(String[] args) throws InterruptedException {
        Cache<Long, Member> cache = new Cache<>();
        cache.put(101L, new Member(101L,"小明", 10), 5, TimeUnit.SECONDS);
        cache.put(102L, new Member(102L,"小红", 8), 5, TimeUnit.SECONDS);
        cache.put(103L, new Member(103L,"小青", 9), 2, TimeUnit.SECONDS);
        System.out.println(cache.get(103L));
        System.out.println(cache.get(102L));
        System.out.println(cache.get(101L));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.get(103L));
        System.out.println(cache.get(102L));
        System.out.println(cache.get(101L));
    }
}

class Member {
    private Long id;
    private String name;
    private Integer age;

    public Member(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

/**
 * 1、创建一个缓存类、存储缓存信息，在指定缓存时间内使缓存失效
 * 2、
 *
 * @param <K>
 * @param <V>
 */
class Cache<K, V> {
    // 默认缓存时间
    private static final int DELAY_TIME = 2;
    // 默认缓存时间单位
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;
    // 存储使缓存失效的延迟队列
    private DelayQueue<CacheItem<Param>> queue = new DelayQueue<CacheItem<Param>>();
    // 在缓存中保留的值
    private ConcurrentHashMap<K, V> cacheMap = new ConcurrentHashMap<>();

    public Cache() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("开始执行啦啦啦啦啦啦");
                    CacheItem<Param> take = Cache.this.queue.take();
                    Param item = take.getItem();
                    if (item != null) {
                        // 移除到期的key
                        boolean bFlag = Cache.this.cacheMap.remove(item.getKey(), item.getValue());
                        System.out.println("缓存Key:" + item.getKey() + "移除结果:" + bFlag);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 设置成守护线程
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public V get(K key) {
        return cacheMap.get(key);
    }

    /**
     * 写入缓存数据
     *
     * @param key
     * @param value
     */
    public void put(K key, V value, long time, TimeUnit unit) {
        V oldVal = cacheMap.put(key, value);
        if (oldVal != null) {
            this.queue.remove(oldVal);
        }
        Param param = new Param();
        param.setKey(key);
        param.setValue(value);
        // 缓存数据值
        // 写入到延迟队列中
        this.queue.put( new CacheItem(param, time, unit));
    }

    public void put(K key, V value, long time) {
        V oldVal = cacheMap.put(key, value);
        if (oldVal != null) {
            this.queue.remove(key);
        }
        Param param = new Param();
        param.setKey(key);
        param.setValue(value);
        // 缓存数据值
        CacheItem<Param> cacheItem = new CacheItem(param, time, timeUnit);
        // 写入到延迟队列中
        this.queue.put(cacheItem);
    }

    public void put(K key, V value) {
        V oldVal = cacheMap.get(key);
        if (oldVal != null) {
            cacheMap.remove(key);
        }
        // 缓存数据值
        CacheItem<V> cacheItem = new CacheItem<>(value, DELAY_TIME, timeUnit);
        cacheMap.put(key, cacheItem.getItem());
    }


    /**
     * 写入到缓存中的键值
     */
    private class Param {
        private K key;
        private V value;

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * 缓存对象
     *
     * @param <T>
     */
    private class CacheItem<T> implements Delayed {
        private T item;
        private long delay; // 保存时间
        private long expire; // 失效时间

        public CacheItem(T item, long delay, TimeUnit timeUnit) {
            this.item = item;
            this.delay = TimeUnit.MILLISECONDS.convert(delay, timeUnit); // 保存时间
            this.expire = System.currentTimeMillis() + this.delay; // 失效时间
        }

        @Override
        public long getDelay(TimeUnit unit) {
            // 延时弹出计算
            return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            // 时间比较
            return (int) (this.delay - this.getDelay(TimeUnit.MILLISECONDS));
        }

        public T getItem() {
            return this.item;
        }
    }

}
