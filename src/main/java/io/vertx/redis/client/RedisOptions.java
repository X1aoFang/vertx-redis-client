/*
 * Copyright 2019 Red Hat, Inc.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * <p>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 * <p>
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.redis.client;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClientOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis Client Configuration options.
 *
 * @author Paulo Lopes
 */
@DataObject(generateConverter = true)
public class RedisOptions {

  /**
   * The default redis endpoint = {@code redis://localhost:6379}
   */
  public static final String DEFAULT_ENDPOINT = "redis://localhost:6379";

  private RedisClientType type;
  private NetClientOptions netClientOptions;
  private List<String> endpoints;
  private int maxWaitingHandlers;
  private int maxNestedArrays;
  private String masterName;
  private RedisRole role;
  private RedisSlaves slaves;
  // pool related options
  private int poolCleanerInterval;
  private int maxPoolSize;
  private int maxPoolWaiting;
  private int poolRecycleTimeout;

  private void init() {
    netClientOptions =
      new NetClientOptions()
        .setTcpKeepAlive(true)
        .setTcpNoDelay(true);

    maxWaitingHandlers = 2048;
    maxNestedArrays = 32;
    masterName = "mymaster";
    role = RedisRole.MASTER;
    slaves = RedisSlaves.NEVER;
    type = RedisClientType.STANDALONE;
    poolCleanerInterval = -1;
    maxPoolSize = 1;
    maxPoolWaiting = 1;
    poolRecycleTimeout = 15_000;
  }

  /**
   * Creates a default configuration object using redis server defaults
   */
  public RedisOptions() {
    init();
  }

  /**
   * Copy constructor.
   * @param other the object to clone.
   */
  public RedisOptions(RedisOptions other) {
    this.type = other.type;
    this.netClientOptions = other.netClientOptions;
    this.endpoints = other.endpoints;
    this.maxWaitingHandlers = other.maxWaitingHandlers;
    this.maxNestedArrays = other.maxNestedArrays;
    this.masterName = other.masterName;
    this.role = other.role;
    this.slaves = other.slaves;
    // pool related options
    this.poolCleanerInterval = other.poolCleanerInterval;
    this.maxPoolSize = other.maxPoolSize;
    this.maxPoolWaiting = other.maxPoolWaiting;
    this.poolRecycleTimeout = other.poolRecycleTimeout;
  }

  /**
   * Copy from JSON constructor.
   * @param json source json
   */
  public RedisOptions(JsonObject json) {
    init();
    RedisOptionsConverter.fromJson(json, this);
  }

  /**
   * Get the type of client to be created.
   * @return the desired client type.
   */
  public RedisClientType getType() {
    return type;
  }

  /**
   * Set the desired client type to be created.
   * @param type the client type.
   * @return fluent self.
   */
  public RedisOptions setType(RedisClientType type) {
    this.type = type;
    return this;
  }


  /**
   * Get the net client options used to connect to the server.
   * @return the net socket options.
   */
  public NetClientOptions getNetClientOptions() {
    return netClientOptions;
  }

  /**
   * Set the net client options to be used while connecting to the redis server. Use this to tune your connection.
   *
   * @param netClientOptions custom net client options.
   * @return fluent self.
   */
  public RedisOptions setNetClientOptions(NetClientOptions netClientOptions) {
    this.netClientOptions = netClientOptions;
    return this;
  }

  /**
   * Gets the list of redis endpoints to use (mostly used while connecting to a cluster)
   * @return list of socket addresses.
   */
  public List<String> getEndpoints() {
    if (endpoints == null) {
      endpoints = new ArrayList<>();
      endpoints.add(DEFAULT_ENDPOINT);
    }
    return endpoints;
  }

  /**
   * Gets the redis endpoint to use
   * @return socket address.
   */
  public String getEndpoint() {
    if (endpoints == null || endpoints.size() == 0) {
      return DEFAULT_ENDPOINT;
    }

    return endpoints.get(0);
  }

  /**
   * Set the endpoints to use while connecting to the redis server. Only the cluster mode will consider more than
   * 1 element. If more are provided, they are not considered by the client when in single server mode.
   *
   * @param endpoints list of socket addresses.
   * @return fluent self.
   */
  public RedisOptions setEndpoints(List<String> endpoints) {
    this.endpoints = endpoints;
    return this;
  }

  /**
   * Adds a endpoint to use while connecting to the redis server. Only the cluster mode will consider more than
   * 1 element. If more are provided, they are not considered by the client when in single server mode.
   *
   * @param endpoint a socket addresses.
   * @return fluent self.
   */
  public RedisOptions addEndpoint(String endpoint) {
    if (endpoints == null) {
      endpoints = new ArrayList<>();
    }
    this.endpoints.add(endpoint);
    return this;
  }

  /**
   * Sets a single endpoint to use while connecting to the redis server. Will replace the previously configured endpoints.
   *
   * @param endpoint a socket addresses.
   * @return fluent self.
   */
  public RedisOptions setEndpoint(String endpoint) {
    if (endpoints == null) {
      endpoints = new ArrayList<>();
    } else {
      endpoints.clear();
    }

    this.endpoints.add(endpoint);
    return this;
  }

  /**
   * The client will always work on pipeline mode, this means that messages can start queueing. You can control how much
   * backlog you're willing to accept. This methods returns how much handlers is the client willing to queue.
   *
   * @return max allowed queued waiting handlers.
   */
  public int getMaxWaitingHandlers() {
    return maxWaitingHandlers;
  }

  /**
   * The client will always work on pipeline mode, this means that messages can start queueing. You can control how much
   * backlog you're willing to accept. This methods sets how much handlers is the client willing to queue.
   *
   * @param maxWaitingHandlers max allowed queued waiting handlers.
   * @return fluent self.
   */
  public RedisOptions setMaxWaitingHandlers(int maxWaitingHandlers) {
    this.maxWaitingHandlers = maxWaitingHandlers;
    return this;
  }

  /**
   * Get the master name (only considered in HA mode).
   * @return the master name.
   */
  public String getMasterName() {
    return masterName;
  }

  /**
   * Set the master name (only considered in HA mode).
   * @param masterName the master name.
   * @return fluent self.
   */
  public RedisOptions setMasterName(String masterName) {
    this.masterName = masterName;
    return this;
  }

  /**
   * Get the role name (only considered in HA mode).
   * @return the master name.
   */
  public RedisRole getRole() {
    return role;
  }

  /**
   * Set the role name (only considered in HA mode).
   * @param role the master name.
   * @return fluent self.
   */
  public RedisOptions setRole(RedisRole role) {
    this.role = role;
    return this;
  }

  /**
   * Get whether or not to use slave nodes (only considered in Cluster mode).
   * @return the cluster slave mode.
   */
  public RedisSlaves getUseSlave() {
    return slaves;
  }

  /**
   * Set whether or not to use slave nodes (only considered in Cluster mode).
   * @param slaves the cluster slave mode.
   * @return fluent self.
   */
  public RedisOptions setUseSlave(RedisSlaves slaves) {
    this.slaves = slaves;
    return this;
  }


  /**
   * Tune how much nested arrays are allowed on a redis response. This affects the parser performance.
   * @return the configured max nested arrays allowance.
   */
  public int getMaxNestedArrays() {
    return maxNestedArrays;
  }

  /**
   * Tune how much nested arrays are allowed on a redis response. This affects the parser performance.
   * @param maxNestedArrays the configured max nested arrays allowance.
   * @return fluent self.
   */
  public RedisOptions setMaxNestedArrays(int maxNestedArrays) {
    this.maxNestedArrays = maxNestedArrays;
    return this;
  }

  /**
   * Tune how often in milliseconds should the connection pool cleaner execute.
   * @return the cleaning internal
   */
  public int getPoolCleanerInterval() {
    return poolCleanerInterval;
  }

  /**
   * Tune how often in milliseconds should the connection pool cleaner execute.
   * @param poolCleanerInterval the interval in milliseconds (-1 for never)
   * @return fluent self.
   */
  public RedisOptions setPoolCleanerInterval(int poolCleanerInterval) {
    this.poolCleanerInterval = poolCleanerInterval;
    return this;
  }

  /**
   * Tune the maximum size of the connection pool.
   * @return the size.
   */
  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  /**
   * Tune the maximum size of the connection pool. When working with cluster or sentinel
   * this value should be atleast the total number of cluster member (or number of sentinels + 1)
   *
   * @param maxPoolSize the max pool size.
   * @return fluent self.
   */
  public RedisOptions setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
    return this;
  }

  /**
   * Tune the maximum waiting requests for a connection from the pool.
   * @return the maximum waiting requests.
   */
  public int getMaxPoolWaiting() {
    return maxPoolWaiting;
  }

  /**
   * Tune the maximum waiting requests for a connection from the pool.
   * @param maxPoolWaiting max waiting requests
   * @return fluent self.
   */
  public RedisOptions setMaxPoolWaiting(int maxPoolWaiting) {
    this.maxPoolWaiting = maxPoolWaiting;
    return this;
  }

  /**
   * Tune when a connection should be recycled in milliseconds.
   * @return the timeout for recycling.
   */
  public int getPoolRecycleTimeout() {
    return poolRecycleTimeout;
  }

  /**
   * Tune when a connection should be recycled in milliseconds.
   * @param poolRecycleTimeout the timeout for recycling.
   * @return fluent self.
   */
  public RedisOptions setPoolRecycleTimeout(int poolRecycleTimeout) {
    this.poolRecycleTimeout = poolRecycleTimeout;
    return this;
  }
  /**
   * Converts this object to JSON notation.
   * @return JSON
   */
  public JsonObject toJson() {
    final JsonObject json = new JsonObject();
    RedisOptionsConverter.toJson(this, json);
    return json;
  }
}
