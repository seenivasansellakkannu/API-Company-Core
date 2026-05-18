/**
 * 
 */
package com.company.core.process.multitenantmanager;

import static java.lang.String.format;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import com.company.core.process.dto.TenantsDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MultiTenantManager {

	public final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
	
	private final DataSourceProperties properties;
	
	private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
	
	private Map<String, DataSourceProperties> tenantResolver;
	
	private AbstractRoutingDataSource multiTenantDataSource;
	
	private DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
	

	@Value("${spring.profiles.active:}")
	private String activeProfile;
	
	@Value("${spring.datasource.hikari.connection-timeout:}")
	private Integer timeout;
	
	@Value("${spring.datasource.hikari.keepalive-time:}")
	private Integer keepaliveTimeMs;
	
	@Value("${spring.datasource.hikari.idle-timeout}")
	private Integer idleTimeOutMs;
	
	@Value("${maxPoolSize}")
	private int maxPoolSize;

	@Value("${minPoolSize}")
	private int minPoolSize;
	
	
	/*
	 * @Autowired DataSourceService dataSourceService;
	 */
	
	public MultiTenantManager(DataSourceProperties properties) {
		this.properties = properties;
	}

	@Bean
	public DataSource dataSource() {

		multiTenantDataSource = new AbstractRoutingDataSource() {

			@Override
			protected Object determineCurrentLookupKey() {
				return currentTenant.get();
			}
		};

		multiTenantDataSource.setTargetDataSources(tenantDataSources);
		multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
		multiTenantDataSource.afterPropertiesSet();

		return multiTenantDataSource;
	}

	public void setTenantResolver(Map<String, DataSourceProperties> tenantResolver) {
		this.tenantResolver = tenantResolver;
	}

	public void setCurrentTenant(String tenantId) throws Exception {

		if (tenantIsAbsent(tenantId)) {

			if (tenantResolver != null) {

				DataSourceProperties tmpProp;

				try {

					tmpProp = tenantResolver.get(tenantId);

					var datetime = LocalDateTime.now();

					log.info(datetime.format(newPattern)
							+ " MultiTenantManager in method setCurrentTenant Datasource properties resolved for tenant ID >>"
							+ tenantId);

				} catch (Exception e) {
					throw new RuntimeException("Could not resolve the tenant!" + tenantId);
				}

				var tempTenantID = new TenantsDto();

				tempTenantID.setTenantId(tenantId);
				tempTenantID.setUrl(tmpProp.getUrl());
				tempTenantID.setUsername(tmpProp.getUsername());
				tempTenantID.setPassword(tmpProp.getPassword());
				tempTenantID.setDriverClass(this.properties.getDriverClassName());
				tempTenantID.setMaximumPoolSize(maxPoolSize);
				tempTenantID.setMinimumIdle(minPoolSize);
				
				addTenant(tempTenantID);

			} else {

				throw new RuntimeException(format("Tenant %s not found!", tenantId));
			}
		}

		currentTenant.set(tenantId);

		LocalDateTime datetime = LocalDateTime.now();

		log.debug(datetime.format(newPattern)
				+ " MultiTenantManager in method setCurrentTenant Tenant set as current >>" + tenantId);
	}

	public void addTenant(TenantsDto tenantInfo) throws SQLException {

		if (!StringUtils.hasText(tenantInfo.getDriverClass()) && tenantInfo.getUrl().startsWith("jdbc:h2:mem")) {

			tenantInfo.setDriverClass("org.h2.Driver");
		}

		log.debug("JDBC Info>>>>>>>>" + tenantInfo);

		var config = new HikariConfig();
		config.setMaximumPoolSize(tenantInfo.getMaximumPoolSize());
		config.setMinimumIdle(tenantInfo.getMinimumIdle());
		config.setDriverClassName(tenantInfo.getDriverClass());
		config.setUsername(tenantInfo.getUsername());
		config.setPassword(tenantInfo.getPassword());
		config.setJdbcUrl(tenantInfo.getUrl());
		config.setPoolName(tenantInfo.getTenantId());
		config.setValidationTimeout(timeout);
		config.setKeepaliveTime(keepaliveTimeMs);
		config.setIdleTimeout(idleTimeOutMs); // Added idle timeout as 10 minutes

		config.setRegisterMbeans(Boolean.TRUE);
		
		try {
			DataSource dataSource = new HikariDataSource(config);

			// Verify the connection whether configuration is valid or not
			try (var c = dataSource.getConnection()) {

				tenantDataSources.put(tenantInfo.getTenantId(), dataSource);
				multiTenantDataSource.afterPropertiesSet();

				var datetime = LocalDateTime.now();

				log.debug(datetime.format(newPattern) + "  MultiTenantManager in method addTenant Tenant added >>"
						+ tenantInfo.getTenantId());
			}
		} catch (Exception e) {

			log.error("  MultiTenantManager in method addTenant failed for Tenant >>" + tenantInfo.getTenantId());
		}
	}

	public DataSource removeTenant(String tenantId) {

		Object removedDataSource = tenantDataSources.remove(tenantId);
		multiTenantDataSource.afterPropertiesSet();

		return (DataSource) removedDataSource;
	}
	
	/**
	 * To close all DB connections
	 *
	 * @author 10659020
	 */
	public void closeAllDbConnections() {

		DataSource dataSource = null;

		for (Map.Entry<Object, Object> entry : tenantDataSources.entrySet()) {

			try {

				dataSource = (DataSource) entry.getValue();
				dataSource.getConnection().close();

			} catch (Exception e) {

				log.error("Issue while closing connection for " + entry.getKey() + " is " + e.getMessage());

			}
		}

	}

	public void clearContext() {

		currentTenant.set(null);

	}

	public boolean tenantIsAbsent(String tenantId) {
		return !tenantDataSources.containsKey(tenantId);
	}

	public Collection<Object> getTenantList() {
		return tenantDataSources.keySet();
	}

	private DriverManagerDataSource defaultDataSource() {
		var defaultDataSource = new DriverManagerDataSource();
		defaultDataSource.setDriverClassName("org.h2.Driver");
		defaultDataSource.setUrl("jdbc:h2:mem:testdb");
		defaultDataSource.setUsername("default");
		defaultDataSource.setPassword("default");
		return defaultDataSource;
	}

//	@Profile("!default, !unknown")
	public void flywayRepair(TenantsDto tenantObj, Flyway flyway) {

		try {

			flyway.repair();

		} catch (Exception excp) {

			log.error("Eception occured while running flywayRepair for Tenant >>" + tenantObj.getTenantId() + ">>>>>"
					+ excp.getLocalizedMessage());
		}

		log.debug("All DB Configuration flywayRepair Script run is completed successfully!!!");

	}

}
