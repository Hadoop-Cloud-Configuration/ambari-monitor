package com.sequenceiq.ambari.domain;

import javax.persistence.*;

/**
 * Created by jiang on 15/11/6.
 */
@Entity
public class Ambari {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ambari_host")
    private String host;

    @Column(name = "ambari_port")
    private String port;

    @Column(name = "ambari_user")
    private String user;

    @Column(name = "ambari_pass")
    private String pass;

    public Ambari() {
    }

    public Ambari(String host, String port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
