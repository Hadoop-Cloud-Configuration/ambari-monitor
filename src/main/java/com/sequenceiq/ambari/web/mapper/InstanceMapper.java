package com.sequenceiq.ambari.web.mapper;


import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.web.json.InstanceJson;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/18.
 */
@Component
public class InstanceMapper extends ConfigurableMapper {
    protected void configure(MapperFactory factory) {
        factory.classMap(Instance.class, InstanceJson.class)
                .byDefault()
                .register();
    }
}
