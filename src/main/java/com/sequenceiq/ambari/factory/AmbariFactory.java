package com.sequenceiq.ambari.factory;

import com.sequenceiq.ambari.domain.Ambari;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/11.
 */
@Component
public class AmbariFactory {
    public Ambari createAmbari(String host, String port, String user, String pass){
        assert StringUtils.isNotEmpty(host);
        return new Ambari(host, port, user, pass);
    }
}
