/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sequenceiq.ambari.client.services

import groovy.util.logging.Slf4j

@Slf4j
trait MonitorService extends ClusterService {
	def void createAlert(String definition) {
		ambari.get(path: "clusters/${getClusterName()}/hosts/ip-172-31-35-79.ec2.internal", { it })
	}
	def List<Map<String, String>> getHostsMonitor() {
		utils.getAllResources('hosts/ip-172-31-35-79.ec2.internal', '').items.collect {
			def details = [:]
			def definition = it
			println "1321312312"
			//      details << ['enabled': definition.enabled]
			//      details << ['scope': definition.scope]
			//      details << ['interval': definition.interval as String]
			//      details << ['description': definition.description]
			//      details << ['name': definition.name]
			//      details << ['label': definition.label]
			//      details << ['service_name': definition.service_name]
			details
		}
	}
}