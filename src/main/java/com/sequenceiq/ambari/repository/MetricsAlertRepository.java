package com.sequenceiq.ambari.repository;

import com.sequenceiq.ambari.domain.MetricsAlert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jiang on 15/11/7.
 */
public interface MetricsAlertRepository extends CrudRepository<MetricsAlert, Long> {

    List<MetricsAlert> findAllByCluster(@Param("clusterId") Long clusterId);

    MetricsAlert findByCluster(@Param("clusterId")long clusterId, @Param("alertId") long alertId);
}
