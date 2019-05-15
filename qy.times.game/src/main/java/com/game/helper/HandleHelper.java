package com.game.helper;

import org.quartz.JobDataMap;
import org.quartz.JobKey;

import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.SelfSkill;
import com.game.template.SkillTemplate;

public class HandleHelper {
	
	@FunctionalInterface
	public interface CallbackBeMoveHandle {
	    void run(Room room, Role beAttRole, SkillTemplate skillTemplate, Location addLocation);
	}
	
	@FunctionalInterface
	public interface StopRoomJobHandle {
	    void run(Room room, boolean isDeath, int cnt);
	}
	
	@FunctionalInterface
	public interface CreateSkillHandle {
	    void createJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate);
	}
	
	@FunctionalInterface
	public interface CreateSelfSkillHandle {
	    void createJob(Role currRole, SelfSkill selfSkill);
	}
	
	@FunctionalInterface
	public interface ReplaceHandle {
	    boolean isReplace(JobDataMap dataMap, Role beAttRole);
	}
}
