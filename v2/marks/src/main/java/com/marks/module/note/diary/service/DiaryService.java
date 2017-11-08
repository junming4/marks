package com.marks.module.note.diary.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.module.note.diary.pojo.Diary;
import com.marks.module.wx.api.wxInterface.mp.user.entity.WxUser;

public interface DiaryService{

	public Diary findById(String ID);
	public void save(Diary diary);
	public void update(Diary diary);
	public void delete(String ID);
	public List<Diary> findAll();
	public void deleteBatch(List<String> ids);
	public PojoDomain<Diary> list(int page_number, int page_size,Map<String,Object> param);
	
	public void pushDairyWxMsg(WxUser wxUser);
	/**
	 * 导出txt文件
	 * @param param
	 * @return
	 */
	public String exportTxt(Map<String, Object> param,String basePath);
}