package com.example.demo.common.cache;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import com.example.demo.common.redis.RedisClient;
import com.example.demo.common.redis.RedisUtils;
import com.example.demo.common.service.TaskService;
import com.sunnysuperman.commons.util.StringUtil;
import com.sunnysuperman.kvcache.KvCacheExecutor;
import com.sunnysuperman.kvcache.KvCachePolicy;
import com.sunnysuperman.kvcache.KvCacheSaveFilter;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.ModelConverter;
import com.sunnysuperman.kvcache.redis.RedisKvCacheExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;

@Service
public class KvCacheFactory {
    @Value("${redis.cache}")
    private String config;
    @Autowired
    private TaskService taskService;
    private KvCacheExecutor executor;
    private RedisClient client;
    private java.util.Set<String> registeredKeys = new HashSet<>();

    @PostConstruct
    public void init() {
        JedisPool redisPool = RedisUtils.createPool(config);
        executor = new RedisKvCacheExecutor(redisPool);
        client = new RedisClient(redisPool, true);
    }

    public <K, T> KvCacheWrap<K, T> create(CacheOptions options, RepositoryProvider<K, T> repository,
                                           ModelConverter<T> converter, KvCacheSaveFilter<K, T> saveFilter) {
        if (StringUtil.isEmpty(options.getKey())) {
            throw new IllegalArgumentException("Bad cache key");
        }
        if (registeredKeys.contains(options.getKey())) {
            throw new IllegalArgumentException("Duplicate cache key");
        }
        registeredKeys.add(options.getKey());
        if (options.getVersion() <= 0) {
            throw new IllegalArgumentException("Bad cache version");
        }
        if (options.getExpireIn() <= 0) {
            throw new IllegalArgumentException("Bad cache expireIn");
        }
        KvCachePolicy policy = new KvCachePolicy();
        policy.setPrefix(options.getKey() + ":" + options.getVersion() + ":");
        policy.setExpireIn(options.getExpireIn());
        return new KvCacheWrap<K, T>(executor, policy, repository, converter, saveFilter, taskService);
    }

    public <K, T> KvCacheWrap<K, T> create(CacheOptions options, RepositoryProvider<K, T> repository,
                                           ModelConverter<T> converter) {
        return create(options, repository, converter, null);
    }

    public RedisClient getClient() {
        return client;
    }
}
