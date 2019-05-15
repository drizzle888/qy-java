package com.game.job.vision;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.entity.Role;
import com.common.entity.Room;
import com.game.helper.MsgHelper;
import com.game.vision.VisionAdapter;

@DisallowConcurrentExecution
public class UpdateRoleVisionJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(UpdateRoleVisionJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role currRole = (Role)jobDataMap.get("currRole");
		int vision = jobDataMap.getInt("vision");
		List<Role> visionRoleList = VisionAdapter.getVisionRoleList(currRole, room.roleMap, vision);
		List<Role> inRoleList = VisionAdapter.getInRoleList(currRole.visionRoleList, visionRoleList);
		List<Role> outRoleList = VisionAdapter.getOutRoleList(currRole.visionRoleList, visionRoleList);
		if (CollectionUtils.isNotEmpty(inRoleList)) {
			currRole.visionRoleList.addAll(inRoleList);
		}
		if (CollectionUtils.isNotEmpty(outRoleList)) {
			currRole.visionRoleList.removeAll(outRoleList);
		}
		if (!currRole.isNpc) {
			if (CollectionUtils.isNotEmpty(inRoleList) || CollectionUtils.isNotEmpty(outRoleList)) {
				MsgHelper.broadcastVisionRoleList(Arrays.asList(currRole), inRoleList, outRoleList, currRole.roomId);
			}
			if (CollectionUtils.isNotEmpty(inRoleList)) {
				MsgHelper.sendBuffStatus(currRole, inRoleList);
			}
		}
	}
}
