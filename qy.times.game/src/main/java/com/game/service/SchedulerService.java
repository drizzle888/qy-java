package com.game.service;

import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.common.constant.CircleConstant;
import com.common.entity.Circle;
import com.common.entity.Room;
import com.common.enumerate.RoomState;
import com.common.helper.TimeHelper;
import com.game.config.CircleConfig;
import com.game.model.Model;
import com.game.template.CircleTemplate;

@Component
public class SchedulerService {

	@Autowired
	private GameService gameService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private FriendService friendService;
	@Autowired
	private TeamService teamService;
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
	
	@Scheduled(fixedDelay=CircleConstant.interval)
	public void eachSecond() {
		taskCircle();
	}
	
//	@Scheduled(fixedDelay=RoleConstant.heart_beat_cycle * 1000)
	public void each10Second() {
		memberService.testHearbeat();
	}
	
	@Scheduled(fixedDelay=TimeHelper.HOUR_S * 1000)
	public void eachHour() {
		friendService.clearHistoricalRecord();
		teamService.clearHistoricalRecord();
	}
	
	private void taskCircle() {
		Set<Integer> roomIdSet = Model.getInstance().roomMap.keySet();
		for (Iterator<Integer> it = roomIdSet.iterator(); it.hasNext();) {
			Integer roomId = it.next();
			Room room = Model.getInstance().roomMap.get(roomId);
			if (room.state == RoomState.Start) {
				Circle circle = room.circle;
				int currTime = TimeHelper.getTime();
				// 如果毒圈生命周期已经超过当前时间，则重新计算毒圈最初圆点
				if (circle != null) {
					if (currTime >= circle.circleBegin + circle.circleLiftime) {
						if (circle.templateId + 1 < CircleConfig.last_templateId) {
							logger.info(String.format("taskCircle 准备设置毒圈 templateId=%d", circle.templateId + 1));
							CircleTemplate bigTemplate = CircleConfig.map.get(room.circle.templateId + 1);
							roomService.createCircle(room, bigTemplate, room.circle.center, bigTemplate.getRadius());
						} else if (circle.templateId + 1 == CircleConfig.last_templateId) {
							CircleTemplate bigTemplate = CircleConfig.map.get(room.circle.templateId + 1);
							logger.info("taskCircle 准备设置最后的毒圈，大圈是最后的圈，没有小圈");
							roomService.createCircle(room, bigTemplate, room.circle.center, bigTemplate.getRadius());
						}
					}
				}
				// 缩圈
				gameService.shrinkCircle(room);
				// 计算毒圈伤害
//				gameService.circleHurt(room);
			}
		}
	}
}
