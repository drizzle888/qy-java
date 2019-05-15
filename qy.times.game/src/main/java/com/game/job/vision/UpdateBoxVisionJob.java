package com.game.job.vision;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.entity.Box;
import com.common.entity.Role;
import com.common.entity.Room;
import com.game.helper.MsgHelper;
import com.game.util.SkillUtil;
import com.game.vision.VisionAdapter;

@DisallowConcurrentExecution
public class UpdateBoxVisionJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(UpdateBoxVisionJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role currRole = (Role)jobDataMap.get("currRole");
		int vision = jobDataMap.getInt("vision");
		boolean isFull = SkillUtil.isFullForBag(currRole);
		// 如果背包没有满，则更新Npc视野内宝箱
		if (!isFull) {
			List<Box> visionBoxList = VisionAdapter.getVisionBoxList(currRole, room.boxMap, vision);
			List<Box> inBoxList = VisionAdapter.getInBoxList(currRole.visionBoxList, visionBoxList);
			List<Box> outBoxList = VisionAdapter.getOutBoxList(currRole.visionBoxList, visionBoxList);
			if (CollectionUtils.isNotEmpty(inBoxList)) {
				currRole.visionBoxList.addAll(inBoxList);
			}
			if (CollectionUtils.isNotEmpty(outBoxList)) {
				currRole.visionBoxList.removeAll(outBoxList);
			}
			if (!currRole.isNpc) {
				// 如果玩家视野改变，则通知玩家视野
				if (CollectionUtils.isNotEmpty(inBoxList) || CollectionUtils.isNotEmpty(outBoxList)) {
					// 发送消息范围内视野改变的宝箱列表
					MsgHelper.sendVisionBox(currRole, inBoxList, outBoxList);
				}
			}
		}
		
	}
}
