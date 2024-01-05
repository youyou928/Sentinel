package com.alibaba.csp.sentinel.dashboard.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.ACCESS_KEY;
import static com.alibaba.nacos.api.PropertyKeyConst.CLUSTER_NAME;
import static com.alibaba.nacos.api.PropertyKeyConst.CONFIG_LONG_POLL_TIMEOUT;
import static com.alibaba.nacos.api.PropertyKeyConst.CONFIG_RETRY_TIME;
import static com.alibaba.nacos.api.PropertyKeyConst.ENABLE_REMOTE_SYNC_CONFIG;
import static com.alibaba.nacos.api.PropertyKeyConst.ENCODE;
import static com.alibaba.nacos.api.PropertyKeyConst.MAX_RETRY;
import static com.alibaba.nacos.api.PropertyKeyConst.NAMESPACE;
import static com.alibaba.nacos.api.PropertyKeyConst.PASSWORD;
import static com.alibaba.nacos.api.PropertyKeyConst.SECRET_KEY;
import static com.alibaba.nacos.api.PropertyKeyConst.SERVER_ADDR;
import static com.alibaba.nacos.api.PropertyKeyConst.USERNAME;

/**
 * Nacos配置
 * 
 * @author fucy
 * @since V1.0
 * @date 2024/01/04 14:12
 */
@ConfigurationProperties(NacosSentinelProperites.PREFIX)
public class NacosSentinelProperites {
	
	private static final Logger log = LoggerFactory.getLogger(NacosSentinelProperites.class);
	
	public static final String PREFIX = "spring.cloud.sentinel.nacos.config";
	
	/**
	 * Nacos default namespace .
	 */
	public static final String DEFAULT_NAMESPACE = "public";
	
	/**
	 * nacos config server address.
	 */
	private String serverAddr;
	
	/**
	 * the nacos authentication username.
	 */
	private String username;
	
	/**
	 * the nacos authentication password.
	 */
	private String password;
	
	/**
	 * namespace, separation configuration of different environments.
	 */
	private String namespace;
	
	/**
	 * nacos config group, group is config data meta info.
	 */
	private String group = "SENTINEL_GROUP";
	
	/**
	 * encode for nacos config content.
	 */
	private String encode;
	
	/**
	 * timeout for get config from nacos.
	 */
	private int timeout = 3000;
	
	/**
	 * nacos maximum number of tolerable server reconnection errors.
	 */
	private String maxRetry;
	
	/**
	 * nacos get config long poll timeout.
	 */
	private String configLongPollTimeout;
	
	/**
	 * nacos get config failure retry time.
	 */
	private String configRetryTime;
	
	/**
	 * If you want to pull it yourself when the program starts to get the configuration
	 * for the first time, and the registered Listener is used for future configuration
	 * updates, you can keep the original code unchanged, just add the system parameter:
	 * enableRemoteSyncConfig = "true" ( But there is network overhead); therefore we
	 * recommend that you use {@link ConfigService#getConfigAndSignListener} directly.
	 */
	private boolean enableRemoteSyncConfig = false;
	
	/**
	 * access key for namespace.
	 */
	private String accessKey;
	
	/**
	 * secret key for namespace.
	 */
	private String secretKey;
	
	/**
	 * nacos config cluster name.
	 */
	private String clusterName;
	
	
	public String getServerAddr() {
		return serverAddr;
	}
	
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getEncode() {
		return encode;
	}
	
	public void setEncode(String encode) {
		this.encode = encode;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public String getMaxRetry() {
		return maxRetry;
	}
	
	public void setMaxRetry(String maxRetry) {
		this.maxRetry = maxRetry;
	}
	
	public String getConfigLongPollTimeout() {
		return configLongPollTimeout;
	}
	
	public void setConfigLongPollTimeout(String configLongPollTimeout) {
		this.configLongPollTimeout = configLongPollTimeout;
	}
	
	public String getConfigRetryTime() {
		return configRetryTime;
	}
	
	public void setConfigRetryTime(String configRetryTime) {
		this.configRetryTime = configRetryTime;
	}
	
	public boolean isEnableRemoteSyncConfig() {
		return enableRemoteSyncConfig;
	}
	
	public void setEnableRemoteSyncConfig(boolean enableRemoteSyncConfig) {
		this.enableRemoteSyncConfig = enableRemoteSyncConfig;
	}
	
	public String getAccessKey() {
		return accessKey;
	}
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public String getSecretKey() {
		return secretKey;
	}
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public String getClusterName() {
		return clusterName;
	}
	
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	
	/**
	 * refer
	 * https://github.com/alibaba/spring-cloud-alibaba/issues/2872
	 * https://github.com/alibaba/spring-cloud-alibaba/issues/2869 .
	 */
	private String resolveNamespace() {
		if (DEFAULT_NAMESPACE.equals(this.namespace)) {
			log.info("set nacos config namespace 'public' to ''");
			return "";
		}
		else {
			return Objects.toString(this.namespace, "");
		}
	}
	
	/**
	 * assemble properties for configService. (cause by rename : Remove the interference
	 * of auto prompts when writing,because autocue is based on get method.
	 * @return properties
	 */
	public Properties assembleConfigServiceProperties() {
		Properties properties = new Properties();
		
		properties.put(SERVER_ADDR, Objects.toString(this.serverAddr, ""));
		properties.put(USERNAME, Objects.toString(this.username, ""));
		properties.put(PASSWORD, Objects.toString(this.password, ""));
		properties.put(ENCODE, Objects.toString(this.encode, ""));
		properties.put(NAMESPACE, this.resolveNamespace());
		properties.put(ACCESS_KEY, Objects.toString(this.accessKey, ""));
		properties.put(SECRET_KEY, Objects.toString(this.secretKey, ""));
		properties.put(CLUSTER_NAME, Objects.toString(this.clusterName, ""));
		properties.put(MAX_RETRY, Objects.toString(this.maxRetry, ""));
		properties.put(CONFIG_LONG_POLL_TIMEOUT, Objects.toString(this.configLongPollTimeout, ""));
		properties.put(CONFIG_RETRY_TIME, Objects.toString(this.configRetryTime, ""));
		properties.put(ENABLE_REMOTE_SYNC_CONFIG, Objects.toString(this.enableRemoteSyncConfig, ""));
		
		return properties;
	}
}
