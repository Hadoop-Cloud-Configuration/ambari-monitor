package com.sequenceiq.ambari.web.mapper;

import com.sequenceiq.ambari.domain.Ambari;
import com.sequenceiq.ambari.web.json.AmbariJson;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/6.
 */
@Component
public class AmbariMapper extends ConfigurableMapper{
    protected void configure(MapperFactory factory) {
        factory.classMap(AmbariJson.class, Ambari.class)
                .byDefault()
                .register();
    }
}
