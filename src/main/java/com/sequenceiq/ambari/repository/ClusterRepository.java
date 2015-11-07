package com.sequenceiq.ambari.repository;

import com.sequenceiq.ambari.domain.Cluster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jiang on 15/11/6.
 */
public interface ClusterRepository extends CrudRepository<Cluster, Long> {
    public List<Cluster> findByState(@Param("state") ClusterState state);
}
