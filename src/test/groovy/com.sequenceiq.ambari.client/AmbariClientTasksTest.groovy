package com.sequenceiq.ambari.client

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class AmbariClientTasksTest extends Specification {

  private enum Scenario {
    TASKS, NO_TASKS
  }

  def ambari = new AmbariClient()


  def "test get task as map"() {
    given:
    mockResponses(Scenario.TASKS)

    when:
    def result = ambari.getTaskMap()

    then:
    [
      'DATANODE INSTALL'          : 'COMPLETED',
      'GANGLIA_MONITOR INSTALL'   : 'COMPLETED',
      'GANGLIA_SERVER INSTALL'    : 'COMPLETED',
      'HDFS_CLIENT INSTALL'       : 'COMPLETED',
      'HISTORYSERVER INSTALL'     : 'COMPLETED',
      'MAPREDUCE2_CLIENT INSTALL' : 'COMPLETED',
      'NAMENODE INSTALL'          : 'COMPLETED',
      'NODEMANAGER INSTALL'       : 'COMPLETED',
      'RESOURCEMANAGER INSTALL'   : 'COMPLETED',
      'SECONDARY_NAMENODE INSTALL': 'COMPLETED',
      'YARN_CLIENT INSTALL'       : 'COMPLETED',
      'ZOOKEEPER_CLIENT INSTALL'  : 'COMPLETED',
      'ZOOKEEPER_SERVER INSTALL'  : 'COMPLETED',
      'DATANODE START'            : 'COMPLETED',
      'GANGLIA_MONITOR START'     : 'COMPLETED',
      'GANGLIA_SERVER START'      : 'COMPLETED',
      'NAMENODE START'            : 'COMPLETED',
      'ZOOKEEPER_SERVER START'    : 'COMPLETED',
      'HISTORYSERVER START'       : 'COMPLETED',
      'RESOURCEMANAGER START'     : 'COMPLETED',
      'SECONDARY_NAMENODE START'  : 'COMPLETED',
      'NODEMANAGER START'         : 'COMPLETED'] == result
  }

  def "test get task as map for no tasks"() {
    given:
    mockResponses(Scenario.NO_TASKS)

    when:
    def result = ambari.getTaskMap()

    then:
    [:] == result
  }


  def private mockResponses(Scenario scenario) {
    // mocking the getResource method of the class being tested
    ambari.metaClass.getResource = { Map resourceRequestMap ->
      String jsonFileName = selectResponseJson(resourceRequestMap, scenario)
      String jsonAsText = getClass().getClassLoader().getResourceAsStream(jsonFileName).text
      return new JsonSlurper().parseText(jsonAsText)
    }
  }

  def private String selectResponseJson(Map resourceRequestMap, Scenario scenario) {
    def thePath = resourceRequestMap.get("path");

    def json = null
    if (thePath == TestResources.CLUSTERS.uri()) {
      json = "clusters.json"
    } else if (thePath == TestResources.TASKS.uri()) {
      switch (scenario) {
        case Scenario.TASKS: json = "request-tasks.json"
          break
        case Scenario.NO_TASKS: json = "no-request-tasks.json"
          break
      }
    } else {
      log.error("Unsupported resource path: {}", thePath)
    }
    return json
  }

}