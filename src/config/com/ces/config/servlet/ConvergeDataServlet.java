package com.ces.config.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.config.dhtmlx.service.appmanage.TimingService;
import com.ces.config.utils.TimeManager;
import com.ces.xarch.core.web.listener.XarchListener;

public class ConvergeDataServlet extends HttpServlet {   
		  
	    private static final long serialVersionUID = 1L;   
 
	    public ConvergeDataServlet() {   
	        super();   
	     }   
	  
	    public void destroy() {   
	        super.destroy();   
	       if(TimeManager.timer!=null){   
	    	   TimeManager.timer.cancel();   
	         }
	     }  
	       
	    public void doGet(HttpServletRequest request, HttpServletResponse response)   
	            throws ServletException, IOException {   
	           
	     }   
	  
	    public void doPost(HttpServletRequest request, HttpServletResponse response)   
	            throws ServletException, IOException {   
	         doGet(request, response);          
	     }   
	  
	    public void init() throws ServletException {   
	         TimingService timingService = XarchListener.getBean(TimingService.class);
	 		 List<TimingEntity> timingList = (List<TimingEntity>)timingService.getTimingStart();
	 		 TimeManager.startTask = getInitParameter("startTask"); 
	 		 if(TimeManager.startTask.equals("true")){
	 			 for (TimingEntity timingEntity : timingList) {
	 					TimeManager.startSchedule(timingEntity);
		             }
	 		 }
	    }
}    