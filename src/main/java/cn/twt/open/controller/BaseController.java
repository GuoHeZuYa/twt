package cn.twt.open.controller;

import cn.twt.open.utils.MapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lino
 */
public abstract class BaseController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public static MapCache cache = MapCache.getInstance();

}
