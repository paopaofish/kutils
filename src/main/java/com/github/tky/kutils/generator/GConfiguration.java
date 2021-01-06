package com.github.tky.kutils.generator;

import java.io.File;
import java.util.Properties;

import com.github.tky.kutils.generator.loader.DataLoader;
import com.github.tky.kutils.jdbc.DataSourceInfo;
import com.github.tky.kutils.type.TypeHandlerRegistry;

public class GConfiguration {
	
	private String DEFAULT_OUTPUT_PATH = "src/main/java/" ;
	
	private String outputDir ;
	private File directoryForTemplateLoadingFile ;
	private DataLoader dataLoader ;
	private String templatesRoot = "ftls" ;
	Properties properties = new Properties();
	TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry() ;
	
	public void addProperties(Properties data) {
		if(data != null) {
			this.properties.putAll(data);
		}
	}
	
	public DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(properties.getProperty("k.generator.jdbc.driver"),
				properties.getProperty("k.generator.jdbc.url"),
				properties.getProperty("k.generator.jdbc.username"),
				properties.getProperty("k.generator.jdbc.password")) ;
	}


	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public File getDirectoryForTemplateLoadingFile() {
		return directoryForTemplateLoadingFile;
	}
	public void setDirectoryForTemplateLoadingFile(File directoryForTemplateLoadingFile) {
		this.directoryForTemplateLoadingFile = directoryForTemplateLoadingFile;
	}
	public String getTemplatesRoot() {
		return templatesRoot;
	}
	public void setTemplatesRoot(String templatesRoot) {
		this.templatesRoot = templatesRoot;
	}
	public String getUpperCamelEntityName() {
		return "MarketingPosInfo";
	}

	public String getOutputDir() {
		if(outputDir == null) { return DEFAULT_OUTPUT_PATH ;}
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public void setTypeHandlerRegistry(TypeHandlerRegistry typeHandlerRegistry) {
		this.typeHandlerRegistry = typeHandlerRegistry;
	}

}