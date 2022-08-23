package com.example.demo.common.cache;

import com.sunnysuperman.kvcache.RepositoryProvider;

public abstract class SingleRepositoryProvider<K, V> implements RepositoryProvider<K, V> {

    @Override
    public final java.util.Map<K, V> findByKeys(java.util.Collection<K> keys) throws Exception {
        throw new UnsupportedOperationException("findByKeys");
    }

}
