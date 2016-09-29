package com.helper;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

import java.io.IOException;

/**
 * Log4j配置文件类
 * @function 
 */
public class AppLogConfigurator {
	private Level rootLevel = Level.DEBUG; // 根等级，初始比较等级
	private String filePattern = "%d - [%p::%c::%t] - %m%n";  // 文件输出模式
	private String logCatPattern = "%m%n";	// 日志模式
	private String fileName = "TestKit.log"; // 文件名字
	private int maxBackupSize = 5; // 最大备份数量
	private long maxFileSize = 1024 * 1024 * 1024; // 最大文件大小
	private boolean immediateFlush = true;  // 马上刷新
	private boolean useFileAppender = true; // 使用文件输出器
	private boolean resetConfiguration = true; // 重新设置配置清单
	private boolean internalDebugging = true; // 内部调试

	public AppLogConfigurator() {
	}

	public void configure() {
		final Logger root = Logger.getRootLogger(); // 获取跟日志级别

		if (isResetConfiguration()) {   // 如果重设，则执行重设命令
			LogManager.getLoggerRepository().resetConfiguration();
		}

		LogLog.setInternalDebugging(isInternalDebugging());

		if (isUseFileAppender()) {
			configureFileAppender();
		}

		root.setLevel(getRootLevel());
	}

	public void setLevel(final String loggerName, final Level level) {
		Logger.getLogger(loggerName).setLevel(level);
	}

	/**
	 * 返回logger的等级
	 * @return
	 */
	public Level getRootLevel() {
		return rootLevel;
	}

	public void setRootLevel(final Level level) {
		this.rootLevel = level;
	}

	/**
	 * 获取文件的正则表达式的表示
	 * @return
	 */
	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(final String filePattern) {
		this.filePattern = filePattern;
	}

	/**
	 * 获取logcat输出模式
	 * @return
	 */
	public String getLogCatPattern() {
		return logCatPattern;
	}

	public void setLogCatPattern(final String logCatPattern) {
		this.logCatPattern = logCatPattern;
	}

	/**
	 * 获取文件名字的正则表达式
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public int getMaxBackupSize() {
		return maxBackupSize;
	}

	public void setMaxBackupSize(final int maxBackupSize) {
		this.maxBackupSize = maxBackupSize;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(final long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public boolean isImmediateFlush() {
		return immediateFlush;
	}

	public void setImmediateFlush(final boolean immediateFlush) {
		this.immediateFlush = immediateFlush;
	}

	/**
	 * 返回是否用户文件输出
	 * @return
	 */
	public boolean isUseFileAppender() {
		return useFileAppender;
	}

	public void setUseFileAppender(final boolean useFileAppender) {
		this.useFileAppender = useFileAppender;
	}

	public void setResetConfiguration(boolean resetConfiguration) {
		this.resetConfiguration = resetConfiguration;
	}

	/**
	 * 返回是否重设配置
	 * @return
	 */
	public boolean isResetConfiguration() {
		return resetConfiguration;
	}

	public void setInternalDebugging(boolean internalDebugging) {
		this.internalDebugging = internalDebugging;
	}

	/**
	 * 返回是否内部调试
	 * @return
	 */
	public boolean isInternalDebugging() {
		return internalDebugging;
	}

	/**
	 * 配置文件输出器
	 */
	private void configureFileAppender() {
		final Logger root = Logger.getRootLogger();
		// 这个输出器旨在，当用户的日志文件超过一个确定的大小去备份文件
		final RollingFileAppender rollingFileAppender;
		// 	Extend this abstract class to create your own log layout format
		// A flexible layout configurable with pattern string.
		final Layout fileLayout = new PatternLayout(getFilePattern());

		try {
			rollingFileAppender = new RollingFileAppender(fileLayout,
					getFileName());  // 规定文件输出模式和文件名字输出模式
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Exception configuring log system", e);
		}

		rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());  // 设置最大备份索引
		rollingFileAppender.setMaximumFileSize(getMaxFileSize()); // 设置最大文件大小
		rollingFileAppender.setImmediateFlush(isImmediateFlush()); // 设置是否立即刷新

		root.addAppender(rollingFileAppender);  // 在根上添加输出器
	}

}
