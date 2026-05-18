package com.company.core.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

import com.company.core.process.dto.TenantsDto;
import com.company.core.process.multitenantmanager.MultiTenantManager;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.company")
@ComponentScan("com.company")
@EnableAsync
public class CompanyProcessApiApplication {

	@Value("${spring.profiles.active:}")
	private String activeProfile;
	
	@Value("${url}")
	private String url;
	
	@Value("${dbUserName}")
	private String dbUserName;
	
	@Value("${dbPassword}")
	private String dbPassword;
	
	@Value("${driverClassName}")
	private String driverClassName;
	
	private  String tenantId = "TSTTESTDB";

	@Value("${maxPoolSize}")
	private int maxPoolSize;

	@Value("${minPoolSize}")
	private int minPoolSize;

	private static DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
	
	private final MultiTenantManager tenantManager;
	
	public CompanyProcessApiApplication(MultiTenantManager tenantManager) {

		this.tenantManager = tenantManager;
		//this.tenantManager.setTenantResolver(tenantResolver(tenantId));
	}

	public static void main(String[] args) {
		SpringApplication.run(CompanyProcessApiApplication.class, args);
	}

	/**
	 * Load tenant datasource properties from the folder 'tenants/onStartUp` when
	 * the app has started.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@EventListener
	public void onReady(ApplicationReadyEvent event) throws IOException {

		List<TenantsDto> tenentIdList = new ArrayList<>();
		var tempTenantID = new TenantsDto();

		try {

			tempTenantID.setTenantId(tenantId);
			tempTenantID.setUrl(url);
			tempTenantID.setUsername(dbUserName);
			tempTenantID.setPassword(dbPassword);
			tempTenantID.setDriverClass(driverClassName);
			tempTenantID.setClientCode(null);
			tempTenantID.setMaximumPoolSize(maxPoolSize);
			tempTenantID.setMinimumIdle(minPoolSize);

			tenantManager.addTenant(tempTenantID);

			tenentIdList.add(tempTenantID);

			var datetime1 = LocalDateTime.now();

			log.info(
					datetime1.format(newPattern) + " Account Module Main method onReady Loaded DataSource for tenant >>"
							+ tempTenantID.getTenantId());

			setupDBConfig(tempTenantID);

		} catch (Exception e) {

			var datetime2 = LocalDateTime.now();

			String dateStr = datetime2.format(newPattern);

			log.error(dateStr,
					" ==> Account Module Main method onReady " + "Could not load DataSource for tenant '%s'!",
					tempTenantID.getTenantId() + e);
		}

		log.info(" API application main Loading of all tenant datasource completed");
	}

	/**
	 * Example of the tenant resolver - load the given tenant data source properties
	 * from the folder 'tenants/atRuntime'
	 *
	 * @param tenantId tenant id
	 * @return tenant DataSource
	 */
	private Map<String, DataSourceProperties> tenantResolver(String tenantId) {
		Map<String, DataSourceProperties> map = new HashMap<>();
		var properties = new DataSourceProperties();
		properties.setUrl(url);
		properties.setUsername(dbUserName);
		properties.setPassword(dbPassword);
		map.put(tenantId, properties);
		return map;
	}

	/**
	 * Below method is use to setup the DB configuration using Flyway.
	 * 
	 * @param tenantObj
	 */
	public void setupDBConfig(TenantsDto tenantObj) {

		var scriptLocation = "classpath:db/migration/common/";

		try {

			var flyway = Flyway.configure().table("flyway_schema_history_common").ignoreMissingMigrations(true)
					.locations(scriptLocation).baselineOnMigrate(Boolean.TRUE)
					.dataSource((DataSource) tenantManager.tenantDataSources.get(tenantObj.getTenantId())).load();

			log.info("activeProfile >>> " + activeProfile);

			if (!StringUtils.isEmpty(activeProfile) && !"default".equalsIgnoreCase(activeProfile)) { // do not execute this in Dev or SQA
				flywayRepair(tenantObj, flyway);
			}
			flyway.migrate();
		} catch (Exception excp) {

			log.error("Eception occured while running Flyway script for Tenant >>" + tenantObj.getTenantId() + ">>>>>"
					+ excp.getLocalizedMessage());
		}

		log.debug("All DB Configuration Script run is completed successfully!!!");

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
