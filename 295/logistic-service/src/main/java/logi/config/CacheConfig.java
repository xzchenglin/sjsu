package logi.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

@Configuration
//@EnableCaching
public class CacheConfig {

//    @Bean
//    public CacheManager cacheManager() {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
//        return cacheManager;
//    }
}
