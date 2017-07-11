package com.ces.component.lssctbxz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.jiaoyixinxixinzeng.dao.JiaoyixinxixinzengDao;
import com.ces.component.lssctbxz.dao.LssctbxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class LssctbxzService extends TraceShowModuleDefineDaoService<StringIDEntity, LssctbxzDao> {
	@Override
    public void setDao(LssctbxzDao dao){
		super.setDao(dao);
	}
	//根据理货编号获取商品名称
	public List<Map<String,Object>> getSpmcByLhbh(String lhbh){
		List<Object[]> spmcList = getDao().getSpmcByLhbh(lhbh);
		return toMap(spmcList);
	}
	
	//获取所有商品名称
	public List<Map<String,Object>> getAllSpmc(){
		List<Object[]> spmcList = getDao().getAllSpmc();
		return toMap(spmcList);
	}
	
	//获取所有状态开启的经营者
	public List<Map<String,Object>> getJyzByZt(){
		List<Object[]> jyzList = getDao().getJyzByZt();
		return toMap(jyzList);
	}
	
	//获取所有蔬菜进场编号
	public List<Map<String,Object>> getAllScjcbh(){
		List<Object[]> scjcbhList = getDao().getAllScjcbh();
		return toScjcbhMap(scjcbhList);
	}
	
	//根据经营者获取蔬菜进场编号
	public List<Map<String,Object>> getScjcbhByJyzbm(String jyzbm){
		List<Object[]> scjcbhList = getDao().getScjcbhByJyzbm(jyzbm);
		return toScjcbhMap(scjcbhList);
	}
	
	//根据进场理货编号获得ID
	public String getIdByScjcbh(String scjcbh){
		return getDao().getIdByScjcbh(scjcbh);
	}
	
	public List<Map<String,Object>> toMap(List<Object[]> list){
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map;
		if(list!=null&&list.size()>0){
			for(Object[] obj:list){
				if(obj!=null&&obj.length>0){
					map = new HashMap<String,Object>();
					map.put("text",obj[0].toString());
					map.put("value",obj[1].toString());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}
	
	public List<Map<String,Object>> toScjcbhMap(List<Object[]> list){
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map;
		if(list!=null&&list.size()>0){
			for(Object[] obj:list){
				if(obj!=null&&obj.length>0){
					map = new HashMap<String,Object>();
					map.put("text",obj[0].toString());
					map.put("value",obj[0].toString());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}
	
}
