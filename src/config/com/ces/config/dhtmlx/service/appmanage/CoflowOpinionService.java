package com.ces.config.dhtmlx.service.appmanage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.workflow.wapi.Workitem;

import com.ces.config.dhtmlx.dao.appmanage.CoflowOpinionDao;
import com.ces.config.dhtmlx.entity.appmanage.CoflowOpinion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;

@Component
public class CoflowOpinionService extends ConfigDefineDaoService<CoflowOpinion, CoflowOpinionDao> {
    
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("coflowOpinionDao")
    @Override
    protected void setDaoUnBinding(CoflowOpinionDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * qiucs 2013-10-15 
     * <p>描述: 审批意见</p>
     * @param  dataId
     * @param  @return    设定参数   
     * @return Object    返回类型   
     * @throws
     */
    public Object getOpinions(String dataId) {
        StringBuffer opinions = new StringBuffer();
        try {
            final String pre  = "　";
            final String half = "　";
            List<CoflowOpinion> list = getDao().getOpinions(dataId);
            for (CoflowOpinion opinion : list) {
            	// opinions.append(pre);
            	if(CoflowOpinion.TYPE_UNTREAD.equals(opinion.getType())) {
                    opinions.append(pre).append(opinion.getConfirmTime().substring(0, 10)).append(half).append(opinion.getUserName()).append("（<font color='red'>退回</font>）：").append(opinion.getConfirmOpinion()).append("<br/>");
            	} else if (CoflowOpinion.TYPE_HASREAD.equals(opinion.getType())) {
                    opinions.append(pre).append(opinion.getConfirmTime().substring(0, 10)).append(half).append(opinion.getUserName()).append("（<font color='green'>阅毕</font>）：").append(opinion.getConfirmOpinion()).append("<br/>");
                } else {
                    opinions.append(pre).append(opinion.getConfirmTime().substring(0, 10)).append(half).append(opinion.getUserName()).append("：").append(opinion.getConfirmOpinion()).append("<br/>");
            	}
            }
            System.out.println(opinions);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return opinions;
    }
    
    @Transactional
    public CoflowOpinion save(String dataId, Workitem wi, String opinion, String type) {
        return save(dataId, 
                String.valueOf(wi.getProcessInstanceId()), 
                String.valueOf(wi.getId()), 
                String.valueOf(wi.getActivityId()), 
                opinion, 
                type);
    }
    
    /**
     * qiucs 2014-9-3 
     * <p>描述: 保存阅毕意见</p>
     * @param  dataId
     * @param  proccessInstanceId
     * @param  opinion
     * @param  type
     * @return CoflowOpinion    返回类型   
     * @throws
     */
    @Transactional
    public CoflowOpinion save(String dataId, String proccessInstanceId, String opinion, String type) {
        return save(dataId, proccessInstanceId, null, null, opinion, type);
    }
    
    private CoflowOpinion save(String dataId, String proccessInstanceId, String workitemId, String activityId, String opinion, String type) {
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        CoflowOpinion entity = new CoflowOpinion();
        entity.setDataId(dataId);
        entity.setProcessInstanceId(proccessInstanceId);
        entity.setWorkitemId(workitemId);
        entity.setActivityId(activityId);
        entity.setUserId(CommonUtil.getUser().getId());
        entity.setUserName(CommonUtil.getUser().getName());
        entity.setConfirmTime(formatter.format(time));
        entity.setConfirmOpinion(opinion);
        entity.setType(type);
        return save(entity);
    }
}
