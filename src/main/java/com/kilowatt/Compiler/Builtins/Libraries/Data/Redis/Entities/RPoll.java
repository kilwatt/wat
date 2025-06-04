package com.kilowatt.Compiler.Builtins.Libraries.Data.Redis.Entities;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
Пул jedis
 */
@AllArgsConstructor
@Getter
public class RPoll {
    // пул
    private final JedisPool pool;

    // установка значения
    public void set(String key, String value) {
        pool.getResource().set(key, value);
    }

    public String get(String key) {
        return pool.getResource().get(key);
    }

    public void set_add(String key, WattList values) {
        Set<String> set = new HashSet<>();
        for (Object o : values.getList()) {
            set.add(o.toString());
        }
        pool.getResource().sadd(key, set.toArray(String[]::new));
    }

    public WattList set_members(String key) {
        WattList list = new WattList();
        for (String s : pool.getResource().smembers(key)) {
            list.add(s);
        }
        return list;
    }

    public void set_delete(String key, String val) {
        pool.getResource().srem(key, val);
    }

    public boolean set_contains(String key, String val) {
        return pool.getResource().sismember(key, val);
    }

    public long set_length(String key) {
        return pool.getResource().scard(key);
    }

    public void incr(String key) {
        pool.getResource().incr(key);
    }

    public void decr(String key) {
        pool.getResource().decr(key);
    }

    public void incr_by(String key, long increment) {
        pool.getResource().incrBy(key, increment);
    }

    public void decr_by(String key, long increment) {
        pool.getResource().decrBy(key, increment);
    }

    public void incr_by_float(String key, float increment) {
        pool.getResource().incrByFloat(key, increment);
    }

    public void decr_by_float(String key, float increment) {
        pool.getResource().incrByFloat(key, -increment);
    }

    public void concat(String key, String value) {
        pool.getResource().append(key, value);
    }

    public void hash_set(String key, String field, String value) {
        pool.getResource().hset(key, field, value);
    }

    public String hash_get(String key, String field) {
        return pool.getResource().hget(key, field);
    }

    public void hash_delete(String key, String field) {
        pool.getResource().hdel(key, field);
    }

    public WattMap hash_get_all(String key) {
        Map<String, String> map = pool.getResource().hgetAll(key);
        WattMap wattMap = new WattMap();
        for (String k : map.keySet()) {
            wattMap.set(k, map.get(k));
        }
        return wattMap;
    }

    public void hash_incr(String key, String field) {
        pool.getResource().hincrBy(key, field, 1);
    }

    public void hash_decr(String key, String field) {
        pool.getResource().hincrBy(key, field, -1);
    }

    public void hash_incr_by(String key, String field, long value) {
        pool.getResource().hincrBy(key, field, value);
    }

    public void hash_decr_by(String key, String field, long value) {
        pool.getResource().hincrBy(key, field, -value);
    }

    public void hash_incr_by_float(String key, String field, float value) {
        pool.getResource().hincrByFloat(key, field, value);
    }

    public void hash_decr_by_float(String key, String field, float value) {
        pool.getResource().hincrByFloat(key, field, -value);
    }

    public long hash_length(String key) {
        return pool.getResource().hlen(key);
    }

    public void list_lpush(String key, String value) {
        pool.getResource().lpush(key, value);
    }

    public void list_rpush(String key, String value) {
        pool.getResource().rpush(key, value);
    }

    public String list_lpop(String key) {
        return pool.getResource().lpop(key);
    }

    public String list_rpop(String key) {
        return pool.getResource().rpop(key);
    }

    public WattList list_range(String key, long from, long to) {
        WattList list = new WattList();
        for (String s : pool.getResource().lrange(key, from, to)) {
            list.add(s);
        }
        return list;
    }

    public long list_length(String key) {
        return pool.getResource().llen(key);
    }

    public String list_index(String key, long index) {
        return pool.getResource().lindex(key, index);
    }

    public boolean list_contains(String key, String value) {
        return list_range(key, 0, -1).getList().contains(value);
    }

    public void sset_add(String key, float score, String value) {
        pool.getResource().zadd(key, score, value);
    }

    public WattList sset_members(String key) {
        WattList list = new WattList();
        for (String s : pool.getResource().zrange(key, 0, -1)) {
            list.add(s);
        }
        return list;
    }

    public void sset_delete(String key, String val) {
        pool.getResource().zrem(key, val);
    }

    public boolean sset_contains(String key, String value) {
        return pool.getResource().zscore(key, value) != null;
    }

    public long sset_length(String key) {
        return pool.getResource().zcard(key);
    }

    public float sset_score(String key, String value) {
        return (pool.getResource().zscore(key, value)).floatValue();
    }

    public void sset_incr(String key, String value) {
        pool.getResource().zincrby(key, 1, value);
    }

    public void sset_incr_by(String key, String value, float score) {
        pool.getResource().zincrby(key, score, value);
    }

    public long sset_rank(String key, String value) {
        return pool.getResource().zrank(key, value);
    }
}