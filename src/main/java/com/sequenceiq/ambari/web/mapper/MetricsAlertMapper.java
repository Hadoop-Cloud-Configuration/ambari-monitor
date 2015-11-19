package com.sequenceiq.ambari.web.mapper;

import com.sequenceiq.ambari.domain.MetricsAlert;
import com.sequenceiq.ambari.web.json.MetricsAlertJson;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/7.
 */
@Component
public class MetricsAlertMapper extends ConfigurableMapper{
    protected void configure(MapperFactory factory) {
        factory.classMap(MetricsAlertJson.class, MetricsAlert.class)
                .field("alertName","name")
                .field("alertDefinition", "definitionName")
                .field("time", "timeDefinition")
                .byDefault()
                .register();
    }
}
