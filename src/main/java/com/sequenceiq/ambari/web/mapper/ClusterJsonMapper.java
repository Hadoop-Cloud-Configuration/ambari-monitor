package com.sequenceiq.ambari.web.mapper;


import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.web.json.ClusterJson;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/6.
 */

@Component
public class ClusterJsonMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(Cluster.class, ClusterJson.class)
                .byDefault()
                .register();
    }

}
