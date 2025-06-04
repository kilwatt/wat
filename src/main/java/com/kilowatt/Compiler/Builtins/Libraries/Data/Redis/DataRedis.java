package com.kilowatt.Compiler.Builtins.Libraries.Data.Redis;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattMap;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/*
Data -> Редис
 */
public class DataRedis {
    public JedisPool pool(String host, int port) {
        return new JedisPool(host, port);
    }

    public JedisCluster cluster(WattMap map) {
        Set<HostAndPort> cluster = new HashSet<HostAndPort>();
        for (Object key : map.getMap().keySet()) {
            cluster.add(new HostAndPort(key.toString(), (int)map.get(key)));
        }
        return new JedisCluster(cluster);
    }
}
