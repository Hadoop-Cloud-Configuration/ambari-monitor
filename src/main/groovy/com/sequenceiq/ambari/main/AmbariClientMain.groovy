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
package com.sequenceiq.ambari.main

import com.sequenceiq.ambari.client.AmbariClient

class AmbariClientMain {
  public static void main(String[] args) {
    def host = '54.172.117.195'
    def port = '8080'
    if (args.size == 2) {
      host = args[0]
      port = args[1]
    }

    AmbariClient client = new AmbariClient(host, port,"admin","admin")
	
    println "\n  clusterList: \n${client.showClusterList()}"
//    println "\n  healthCheck: \n ${factory.getHostsMonitor()}"
//    println "\n  hostsList: \n${factory.showHostList()}"
	String temp = client.showHostDiskInfo();
  	println "\n Hosts Disk Info: \n${temp}"
	  String t = client.getYarnData();
	  println "\n Available Memory: \n${t}MB"
   // println "\n  tasksList: \n${factory.showTaskList()}"
   // println "\n  serviceList: \n${factory.showServiceList()}"
   // println "\n  blueprintList: \n${factory.showBlueprints()}"
   // println "\n  clusterBlueprint: \n${factory.showClusterBlueprint()}"

  }
}
