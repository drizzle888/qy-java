package com.game.job.vision;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.entity.Book;
import com.common.entity.Role;
import com.common.entity.Room;
import com.game.helper.MsgHelper;
import com.game.util.SkillUtil;
import com.game.vision.VisionAdapter;

@DisallowConcurrentExecution
public class UpdateBookVisionJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(UpdateBookVisionJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role currRole = (Role)jobDataMap.get("currRole");
		int vision = jobDataMap.getInt("vision");
		boolean isFull = SkillUtil.isFullForBag(currRole);
		// 如果背包没有满，则更新Npc视野内技能书
		if (!isFull) {
			List<Book> visionBookList = VisionAdapter.getVisionBookList(currRole, room.bookMap, vision);
			List<Book> inBookList = VisionAdapter.getInBookList(currRole.visionBookList, visionBookList);
			List<Book> outBookList = VisionAdapter.getOutBookList(currRole.visionBookList, visionBookList);
			if (CollectionUtils.isNotEmpty(inBookList)) {
				currRole.visionBookList.addAll(inBookList);
			}
			if (CollectionUtils.isNotEmpty(outBookList)) {
				currRole.visionBookList.removeAll(outBookList);
			}
			if (!currRole.isNpc) {
				// 如果玩家视野改变，则通知玩家视野
				if (CollectionUtils.isNotEmpty(inBookList) || CollectionUtils.isNotEmpty(outBookList)) {
					// 发送消息范围内视野改变的宝箱列表
					MsgHelper.sendVisionBook(currRole, inBookList, outBookList);
				}
			}
		}
		
	}
}
