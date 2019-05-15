package com.common.constant;

import java.util.ArrayList;
import java.util.List;

public class TemplateConstant {
	/**普通技能Id*/
	public static final int template_id_10000 = 10000;
	/**穿云箭*/
	public static final int template_id_10100 = 10100;
	/**海浪*/
	public static final int template_id_10200 = 10200;
	/**冲击波*/
	public static final int template_id_10300 = 10300;
	/**火墙*/
	public static final int template_id_10400 = 10400;
	/**暴风雪*/
	public static final int template_id_10500 = 10500;
	/**震荡*/
	public static final int template_id_10600 = 10600;
	/**陷阱*/
	public static final int template_id_10700 = 10700;
	/**持续吟唱海浪*/
	public static final int template_id_10800 = 10800;
	/**持续吟唱冲击波*/
	public static final int template_id_10900 = 10900;
	/**持续吟唱火墙*/
	public static final int template_id_11000 = 11000;
	/**持续吟唱暴风雪*/
	public static final int template_id_11100 = 11100;
	/**持续吟唱震荡*/
	public static final int template_id_11200 = 11200;
	/**瞬时吟唱海浪*/
	public static final int template_id_11300 = 11300;
	/**瞬时吟唱冲击波*/
	public static final int template_id_11400 = 11400;
	/**瞬时吟唱火墙*/
	public static final int template_id_11500 = 11500;
	/**瞬时吟唱暴风雪*/
	public static final int template_id_11600 = 11600;
	/**瞬时吟唱震荡*/
	public static final int template_id_11700 = 11700;
	/**光法*/
	public static final int template_id_11800 = 11800;
	/**努努大*/
	public static final int template_id_11900 = 11900;
	/**二连戳*/
	public static final int template_id_12000 = 12000;
	
	/**减速*/
	public static final int template_id_20001 = 20001;
	/**剧毒*/
	public static final int template_id_20002 = 20002;
	/**靠近*/
	public static final int template_id_20003 = 20003;
	/**远离*/
	public static final int template_id_20004 = 20004;
	/**伤害加深*/
	public static final int template_id_20005 = 20005;
	/**致盲*/
	public static final int template_id_20006 = 20006;
	/**狂暴*/
	public static final int template_id_20007 = 20007;
	/**沉默*/
	public static final int template_id_20008 = 20008;
	/**禁步*/
	public static final int template_id_20009 = 20009;
	/**晕眩*/
	public static final int template_id_20010 = 20010;
	/**治疗*/
	public static final int template_id_20011 = 20011;
	/**回复*/
	public static final int template_id_20012 = 20012;
	/**护盾*/
	public static final int template_id_20013 = 20013;
	/**净化*/
	public static final int template_id_20014 = 20014;
	/**链接*/
	public static final int template_id_20015 = 20015;
	
	/** 所有技能Id列表 **/
	public static final List<Integer> templateIdList = new ArrayList<Integer>();
	/** A技能列表 **/
	public static final List<Integer> templateIdAList = new ArrayList<Integer>();
	/** B技能列表 **/
	public static final List<Integer> templateIdBList = new ArrayList<Integer>();
	
	static {
		// A技能
		templateIdAList.add(template_id_10100);
		templateIdAList.add(template_id_10200);
		templateIdAList.add(template_id_10300);
		templateIdAList.add(template_id_10400);
		templateIdAList.add(template_id_10500);
		templateIdAList.add(template_id_10600);
		templateIdAList.add(template_id_10700);
		/*templateIdAList.add(template_id_10800);
		templateIdAList.add(template_id_10900);
		templateIdAList.add(template_id_11000);
		templateIdAList.add(template_id_11100);
		templateIdAList.add(template_id_11200);*/
		
		// B技能
		templateIdBList.add(template_id_20001);
		templateIdBList.add(template_id_20002);
		templateIdBList.add(template_id_20003);
		templateIdBList.add(template_id_20004);
		templateIdBList.add(template_id_20005);
		templateIdBList.add(template_id_20006);
		templateIdBList.add(template_id_20007);
		templateIdBList.add(template_id_20008);
		templateIdBList.add(template_id_20009);
		templateIdBList.add(template_id_20010);
		templateIdBList.add(template_id_20011);
		templateIdBList.add(template_id_20012);
		templateIdBList.add(template_id_20013);
		templateIdBList.add(template_id_20014);
//		templateIdBList.add(template_id_20015);
		
		// 所有技能
		templateIdList.addAll(templateIdAList);
		templateIdList.addAll(templateIdBList);
	}
}
