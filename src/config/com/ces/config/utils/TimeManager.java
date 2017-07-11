package com.ces.config.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.config.servlet.Task;


/**
 * 定时任务管理类
 * 
 **/
public class TimeManager {
    
    private static Log log = LogFactory.getLog(TimeManager.class);

	public static final Timer timer = new Timer(true);
	
	public static Set<Task> tasks = new HashSet<Task>();
	
	public static String startTask = null;
    /**
     * 启动定时任务
     */
	private static void startSchedule4timming(TimingEntity timingEntity){
		Task task = new Task();
		task.setTimingEntity(timingEntity);
		tasks.add(task);
		String time = timingEntity.getTime();
		String[] t = time.split(":");
		timer.scheduleAtFixedRate(task, processTime4timming(t), 24*60*60*1000);
	}

	/**
     * 启动间隔任务
     */
	private static void startSchedule4interval(TimingEntity timingEntity){
		Task task = new Task();
		task.setTimingEntity(timingEntity);
		tasks.add(task);
		String time = timingEntity.getTime();
		String[] t = time.split(":");
		timer.scheduleAtFixedRate(task, 0, processTime4interval(t));
	}
	
	public static void startSchedule(TimingEntity timingEntity){
		if("1".equals(timingEntity.getTimingType())){
			startSchedule4interval(timingEntity);
		}else if("0".equals(timingEntity.getTimingType())){
			startSchedule4timming(timingEntity);
		}
		log.info(timingEntity.getName()+"-----任务启动了");
	}
	
	/**
     * 停止定时/间隔任务
     */
	public static void stopSchedule(String Id){
		Task task = getTaskByID(Id);
		if(null!=task){
			task.cancel();
		}
		timer.purge();
		log.info(task.getTimingEntity().getName()+"定时任务停止了..");
	}
	
	
	
	/**
     * 组装时间(返回Date)
     */
	private static long processTime4timming(String[] times){
		Date date=new Date();
		long nowtime = date.getTime();
		long delaytime = 0;
		date.setHours(Integer.parseInt(times[0].trim()));
		date.setMinutes(Integer.parseInt(times[1].trim()));
		if (nowtime > date.getTime()) {
			date.setDate(date.getDate() + 1);
		}
		delaytime = date.getTime() - nowtime;
		return delaytime;
	}

	/**
     * 组装时间(返回long)
     */
	private static long processTime4interval(String[] times){
		String hour = times[0];
		String min = times[1];
		long period = 0;
		period = hour.length()!=0?Long.parseLong(hour)*60*60*1000:0;
		period += min.length()!=0?Long.parseLong(min)*60*1000:period;
		return period;
	}
	
	/**
     * 根据任务ID查找任务
     */
	public static Task getTaskByID(String timingEntity_id){
		Iterator<Task> iterator = tasks.iterator();
		Task task = null;
		while (iterator.hasNext()) {
			task = (Task) iterator.next();
			if(timingEntity_id.equals(task.getTimingEntity().getId())){
				return task;
			}
			task = null;
		}
		return null;
	}
}