package com.github.tky.kutils.generator.builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.github.tky.kutils.Files;
import com.github.tky.kutils.Strings;
import com.github.tky.kutils.generator.GConfiguration;
import com.github.tky.kutils.generator.loader.DataLoader;
import com.github.tky.kutils.generator.loader.DefaultTemplateLoader;
import com.github.tky.kutils.generator.loader.SourceFileLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

public abstract class AbstractTemplateBuilder implements TemplateBuilder {

	Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_23);
	private GConfiguration configuration;
	DataLoader dataLoader;
	SourceFileLoader sourceFileLoader;

	public AbstractTemplateBuilder(GConfiguration configuration) {
		this.configuration = configuration;
		this.dataLoader = configuration.getDataLoader();
		sourceFileLoader = new DefaultTemplateLoader(configuration);
	}

	@Override
	public void generate() {
		Map<Object, Object> dataModel = dataLoader.load();
		List<File> sourceFiles = sourceFileLoader.load();

		if (sourceFiles != null) {
			for (File file : sourceFiles) {
				generate(file, dataModel);
			}
		}

	}

	@Override
	public void generate(File file, Map<Object, Object> dataModel) {
		BufferedReader reader = null;
		try {
			if (file.isDirectory()) {
				return;
			}
			String absPath = file.getAbsolutePath();
			if (!absPath.endsWith(".ftl")) {
				return;
			}
			String ftl = absPath.substring(configuration.getTemplatesRoot().length() + 1 + absPath.indexOf(configuration.getTemplatesRoot()));

			reader = new BufferedReader(new FileReader(file));
			String path = getOutputPath(reader.readLine(), dataModel);
			if (Strings.isEmpty(path)) {
				reader.close();
				reader = new BufferedReader(new FileReader(file));
			}
			Template template = new Template(file.getName(), reader, freemarkerConfiguration);
			File outFile = createOutputFile(ftl, path);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			template.process(dataModel, out);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	protected String getOutputPath(String path, Map<Object, Object> dataModel) {
		if (path.startsWith("KPATH:")) {
			path = path.replace("KPATH:", "").replace(" ", "");
			path = path.replace("${groupId}", (CharSequence) dataModel.get("groupId"));
			path = path.replace("${artifactId}", (CharSequence) dataModel.get("artifactId"));
			return path.replace("-", ".");
		}
		return null;
	}

	private File createOutputFile(String ftl, String path) throws IOException {

		if (!ftl.endsWith("ftl")) {
			return null;
		}

		String templateFilename = ftl.substring(ftl.lastIndexOf(File.separator) + 1);

		int suffixIndex = templateFilename.indexOf("_");
		String suffix = templateFilename.substring(suffixIndex + 1, templateFilename.lastIndexOf("."));
		if (suffixIndex >= 0) {
			suffix += "." + templateFilename.substring(0, suffixIndex).toLowerCase();
		}
		String generateFile = getOutputFilename() + suffix;

		File outFile = null;
		if (Strings.isNotEmpty(path)) {
			outFile = new File(path.replace(".", File.separator) + File.separator + generateFile);
		} else {
			outFile = new File(ftl.replace(templateFilename, generateFile));
		}
		Files.createFile(outFile);

		return outFile;
	}

	/**
	 * get the output filename without suffix
	 * 
	 * @return the filename without suffix
	 */
	public abstract String getOutputFilename();

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	public SourceFileLoader getSourceFileLoader() {
		return sourceFileLoader;
	}

	public void setSourceFileLoader(SourceFileLoader sourceFileLoader) {
		this.sourceFileLoader = sourceFileLoader;
	}

	public GConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(GConfiguration configuration) {
		this.configuration = configuration;
	}

}
