package com.dream.iot;

import com.dream.iot.codec.filter.CombinedFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FilterManager implements InitializingBean {

    private List<CombinedFilter> filters;
    private static FilterManager manager;
    private Map<Class<? extends FrameworkComponent>, CombinedFilter> classFilterMap = new HashMap<>(16);

    protected FilterManager(List<CombinedFilter> filters) {
        this.filters = filters;
        manager = this;
    }

    public static FilterManager getInstance() {
        return manager;
    }

    /**
     * 注册组件过滤器
     * @param filter
     */
    public void register(CombinedFilter filter) {
        Assert.notNull(filter, "过滤器不允许为null");
        Class component = filter.component();
        Assert.notNull(component, "未指定过滤器的泛型参数<C>");

        if(classFilterMap.containsKey(component)) {
            throw new IllegalStateException("同意组件不允许注册多个过滤器");
        }

        classFilterMap.put(component, filter);
    }

    /**
     * 获取组件注册的过滤器
     * @param clazz
     * @return
     */
    public Optional<CombinedFilter> getFilter(Class<? extends FrameworkComponent> clazz) {
        return Optional.ofNullable(classFilterMap.get(clazz));
    }

    public Map<Class<? extends FrameworkComponent>, CombinedFilter> getClassFilterMap() {
        return classFilterMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!CollectionUtils.isEmpty(this.filters)) {
            this.filters.forEach(item -> this.register(item));
        }
    }
}
