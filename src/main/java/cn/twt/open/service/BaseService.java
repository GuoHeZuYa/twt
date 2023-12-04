package cn.twt.open.service;


import cn.twt.open.utils.MapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lino
 */
public interface BaseService {
    public static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    public static MapCache cache = MapCache.getInstance();
}
