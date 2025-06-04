package com.kilowatt.Compiler.Builtins.Libraries.Data.Redis.Entities;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
Кластер jedis
 */
@AllArgsConstructor
@Getter
public class RCluster {
    // кластер
    private final JedisCluster cluster;

    // установка значения
    public void set(String key, String value) {
        cluster.set(key, value);
    }
    public String get(String key) {
        return cluster.get(key);
    }
    public void set_add(String key, WattList values) {
        Set<String> set = new HashSet<>();
        for (Object o : values.getList()) {
            set.add(o.toString());
        }
        cluster.sadd(key, set.toArray(String[]::new));
    }
    public WattList set_members(String key) {
        WattList list = new WattList();
        for (String s : cluster.smembers(key)) {
            list.add(s);
        }
        return list;
    }
    public void set_delete(String key, String val) {
        cluster.srem(key, val);
    }
    public boolean set_contains(String key, String val) {
        return cluster.sismember(key, val);
    }
    public long set_length(String key) {
        return cluster.scard(key);
    }
    public void incr(String key) {
        cluster.incr(key);
    }
    public void decr(String key) {
        cluster.decr(key);
    }
    public void incr_by(String key, long increment) {
        cluster.incrBy(key, increment);
    }
    public void decr_by(String key, long increment) {
        cluster.decrBy(key, increment);
    }
    public void incr_by_float(String key, float increment) {
        cluster.incrByFloat(key, increment);
    }
    public void decr_by_float(String key, float increment) {
        cluster.incrByFloat(key, -increment);
    }
    public void concat(String key, String value) {
        cluster.append(key, value);
    }
    public void hash_set(String key, String field, String value) {
        cluster.hset(key, field, value);
    }
    public String hash_get(String key, String field) {
        return cluster.hget(key, field);
    }
    public void hash_delete(String key, String field) {
        cluster.hdel(key, field);
    }
    public WattMap hash_get_all(String key) {
        Map<String, String> map = cluster.hgetAll(key);
        WattMap wattMap = new WattMap();
        for (String k : map.keySet()) {
            wattMap.set(k, map.get(k));
        }
        return wattMap;
    }
    public void hash_incr(String key, String field) {
        cluster.hincrBy(key, field, 1);
    }
    public void hash_decr(String key, String field) {
        cluster.hincrBy(key, field, -1);
    }
    public void hash_incr_by(String key, String field, long value) {
        cluster.hincrBy(key, field, value);
    }
    public void hash_decr_by(String key, String field, long value) {
        cluster.hincrBy(key, field, -value);
    }
    public void hash_incr_by_float(String key, String field, float value) {
        cluster.hincrByFloat(key, field, value);
    }
    public void hash_decr_by_float(String key, String field, float value) {
        cluster.hincrByFloat(key, field, -value);
    }
    public long hash_length(String key) {
        return cluster.hlen(key);
    }
    public void list_lpush(String key, String value) {
        cluster.lpush(key, value);
    }
    public void list_rpush(String key, String value) {
        cluster.rpush(key, value);
    }
    public String list_lpop(String key) {
        return cluster.lpop(key);
    }
    public String list_rpop(String key) {
        return cluster.rpop(key);
    }
    public WattList list_range(String key, long from, long to) {
        WattList list = new WattList();
        for (String s : cluster.lrange(key, from, to)) {
            list.add(s);
        }
        return list;
    }
    public long list_length(String key) {
        return cluster.llen(key);
    }
    public String list_index(String key, long index) {
        return cluster.lindex(key, index);
    }
    public boolean list_contains(String key, String value) {
        return list_range(key, 0, -1).getList().contains(value);
    }
    public void sset_add(String key, float score, String value) {
        cluster.zadd(key, score, value);
    }
    public WattList sset_members(String key) {
        WattList list = new WattList();
        for (String s : cluster.zrange(key, 0, -1)) {
            list.add(s);
        }
        return list;
    }
    public void sset_delete(String key, String val) {
        cluster.zrem(key, val);
    }
    public boolean sset_contains(String key, String value) {
        return cluster.zscore(key, value) != null;
    }
    public long sset_length(String key) {
        return cluster.zcard(key);
    }
    public float sset_score(String key, String value) {
        return (cluster.zscore(key, value)).floatValue();
    }
    public void sset_incr(String key, String value) {
        cluster.zincrby(key, 1, value);
    }
    public void sset_incr_by(String key, String value, float score) {
        cluster.zincrby(key, score, value);
    }
    public long sset_rank(String key, String value) {
        return cluster.zrank(key, value);
    }
}