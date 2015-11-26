package com.sequenceiq.ambari.factory;

import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.domain.InstanceState;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/18.
 */
@Component
public class InstanceFactory {
    public Instance createInstance(String ip, InstanceState state){
        return new Instance(ip,state);
    }
}
