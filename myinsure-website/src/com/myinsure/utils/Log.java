package com.myinsure.utils;

import java.text.MessageFormat;

import com.myinsure.utils.FileUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Log
{
    private static Log _log = new Log();

    private Logger _logger = Logger.getRootLogger();

    public void init(String logfile)
    {
	init(null, logfile);
    }

    public void init(String realPath, String logfile)
    {
	if ((realPath == null) || (realPath.length() == 0))
	{
	    realPath = FileUtil.getClassPath();
	}
	if ((logfile == null) || (logfile.length() == 0))
	{
	    logfile = "log.properties";
	}

	logfile = realPath + logfile;
	PropertyConfigurator.configure(logfile);
    }

    public static Log getInstance()
    {
	return _log;
    }

    public void showDebug(String sInfo)
    {
	this._logger.debug(sInfo);
    }

    public void showDebug(String sInfo, Object [] params)
    {
	sInfo = MessageFormat.format(sInfo, params);
	this._logger.debug(sInfo);
    }

    public void showInfo(String sInfo)
    {
	this._logger.info(sInfo);
    }

    public void showWarn(String sInfo)
    {
	sInfo = traceMethod() + "\nwarn: " + sInfo;
	this._logger.warn(sInfo);
    }

    public void showWarn(String sInfo, Object [] params)
    {
	sInfo = MessageFormat.format(sInfo, params);
	showWarn(sInfo);
    }

    public void showError(String sInfo)
    {
	this._logger.error(traceMethod() + "\nerror: " + sInfo);
    }

    public void showError(String sInfo, Object [] params)
    {
	sInfo = MessageFormat.format(sInfo, params);
	showError(sInfo);
    }

    public void showError(String sInfo, Throwable e)
    {
	sInfo = traceMethod() + "\nerror: " + sInfo;
	this._logger.error(sInfo, e);
    }

    public void showError(Throwable e)
    {
	this._logger.error(traceMethod(), e);
    }

    private String traceMethod()
    {
	StackTraceElement [] stack = new Throwable().getStackTrace();

	int ix = 0;
	while (ix < stack.length)
	{
	    StackTraceElement frame = stack[ix];
	    String cname = frame.getClassName();
	    if (cname.equals(Log.class.getName()))
	    {
		break;
	    }
	    ix++;
	}

	while (ix < stack.length)
	{
	    StackTraceElement frame = stack[ix];
	    String cname = frame.getClassName();
	    if (!cname.equals(Log.class.getName()))
	    {
		StringBuilder sb = new StringBuilder(cname);
		sb.append(".").append(frame.getMethodName()).append(" on line: ");
		sb.append(frame.getLineNumber());
		return sb.toString();
	    }
	    ix++;
	}

	return "";
    }
}