package com.manager.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.entity.TileWall;
import com.common.util.AssertUtil;
import com.manager.dao.TileWallDao;

@Service
public class TileWallService {

	@Autowired
	private TileWallDao tileWallDao;

	public List<TileWall> getList(Integer weight) {
		TileWall tileWall = new TileWall();
		tileWall.setWeight(weight);
		List<TileWall> tileWallList = tileWallDao.getList(tileWall);
		return tileWallList;
	}

	public TileWall getById(Integer id) {
		TileWall tileWall = tileWallDao.getById(id);
		AssertUtil.asWarnTrue(tileWall != null, "牌墙不存在");
		return tileWall;
	}

	public void edit(TileWall tileWall) {
		validate(tileWall);
		tileWallDao.edit(tileWall);
	}

	public Integer add(TileWall tileWall) {
		validate(tileWall);
		return tileWallDao.add(tileWall);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "id不能为空");
		AssertUtil.asWarnTrue(id > 0, "id不正确");
		TileWall tileWall = tileWallDao.getById(id);
		AssertUtil.asWarnTrue(tileWall != null, "删除的牌墙不存在");
		tileWallDao.delete(id);
	}

	private void validate(TileWall tileWall) {
		AssertUtil.asWarnTrue(tileWall != null, "牌墙列表不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(tileWall.getTiles()), "牌墙不能为空");
		AssertUtil.asWarnTrue(tileWall.getWeight() != null, "权重值不能为空");
		AssertUtil.asWarnTrue(tileWall.getWeight() > 0, "权重值大于0");
	}
}
