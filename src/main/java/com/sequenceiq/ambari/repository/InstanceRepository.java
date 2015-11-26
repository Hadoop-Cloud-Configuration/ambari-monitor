package com.sequenceiq.ambari.repository;

import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.domain.InstanceState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jiang on 15/11/18.
 */
public interface InstanceRepository extends CrudRepository<Instance, Long> {
    public Instance findOne(@Param("id") long id);
    public List<Instance> findNByState(@Param("state") InstanceState state, @Param("clusterId") long clusterId, Pageable pageable);
}
