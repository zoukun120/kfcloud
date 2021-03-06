package com.zk.kfcloud.Service;

import java.util.List;

public interface RedisService {

    public boolean set(final String key, final String value);

    public String get(final String key);

    public <T> boolean setList(String key, List<T> list);

    public <T> List<T> getList(String key, Class<T> clz);

    public long lpush(final String key, Object obj);

    public long rpush(final String key, Object obj);

    public String lpop(final String key);

    public boolean expire(final String key, long expire);

}
