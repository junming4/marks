package com.marks.module.system.syslog.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.marks.common.domain.PojoDomain;
import com.marks.module.system.core.data.StaticData;
import com.marks.module.system.sys.dao.SysLogDao;
import com.marks.module.system.sys.pojo.SysLog;
import com.marks.module.system.syslog.service.SysLogService;

public class SysLogServiceImpl implements SysLogService{
   

    private SysLogDao sysLogDao;

    public SysLogDao getSysLogDao(){
        return sysLogDao;
    }
    public void setSysLogDao(SysLogDao sysLogDao){
        this.sysLogDao =sysLogDao;
    }
	
	public PojoDomain<SysLog> list(int page_number, int page_size, Map<String,Object> param) {
		PojoDomain<SysLog> pojoDomain = new PojoDomain<SysLog>();
		PageBounds pageBounds = new PageBounds(page_number, page_size);
		List<SysLog> list = sysLogDao.list(pageBounds,param);
		PageList<SysLog> pageList = (PageList<SysLog>)list; 
		pojoDomain.setPojolist(list);
		pojoDomain.setPage_number(page_number);
		pojoDomain.setPage_size(page_size);
		pojoDomain.setTotal_count(pageList.getPaginator().getTotalCount());
		return pojoDomain;
	}
	@Override
	public void clearData() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String clear_syslog_data_str=StaticData.getSysConf("clear_syslog_data");
		int clearNum=60;
		if(null !=clear_syslog_data_str && !"".equals(clear_syslog_data_str)){
			clearNum=Integer.parseInt(clear_syslog_data_str);
		}
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, -clearNum);
		sysLogDao.deleteData(sdf.format(today.getTime()));
	}
	
}