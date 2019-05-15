package com.game.util;

import com.common.constant.SkillConstant;
import com.common.constant.TemplateConstant;
import com.common.entity.Role;
import com.common.enumerate.SkillType;
import com.game.config.SkillConfig;
import com.game.template.SkillTemplate;

public class SkillUtil {
	
	public static SkillTemplate makeSkillTemplateC(int aTemplateId, int bTemplateId) {
		SkillTemplate skillTemplate = SkillConfig.map.get(aTemplateId + bTemplateId);
		return skillTemplate;
	}
	
	public static SkillType getSkillType(int skillTemplateId) {
		if (skillTemplateId == TemplateConstant.template_id_10000) {
			return SkillType.General;
		} else if (skillTemplateId >= 10001 && skillTemplateId < 20000) {
			return SkillType.A;
		} else if (skillTemplateId >= 20001 && skillTemplateId < 30000) {
			return SkillType.B;
		} else if (skillTemplateId >= 30001 && skillTemplateId < 40000) {
			return SkillType.C;
		} else {
			return SkillType.Nothing;
		}
	}
	
	public static boolean isFullForBag(Role currRole) {
		int count = 0;
		for (int i = 0; i < currRole.skillPanelA.length; i++) {
			if (currRole.skillPanelA[i] != null) {
				count++;
			}
		}
		for (int i = 0; i < currRole.skillPanelB.length; i++) {
			if (currRole.skillPanelB[i] != null) {
				count++;
			}
		}
		for (int i = 0; i < currRole.skillBag.size(); i++) {
			if (currRole.skillBag.get(i) != null) {
				count++;
			}
		}
		return count > SkillConstant.bag_size;
	}
}
