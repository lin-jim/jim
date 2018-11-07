package com.synctech.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synctech.dao.GoodsVoDao;
import com.synctech.domian.MiaoshaGoods;
import com.synctech.service.GoodsVoService;
import com.synctech.vo.GoodsVo;

@Service
public class GoodsVoServiceImpl implements GoodsVoService{

	
	@Autowired
	private GoodsVoDao goodsVoDao;
	
	@Override
	public List<GoodsVo> listGoodsVo() {
		
		return goodsVoDao.listGoodsVo();
	}

	@Override
	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsVoDao.getGoodsVoByGoodsId(goodsId);
	}

	@Transactional
	@Override
	public boolean reduce(GoodsVo goodsVo) {
		MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
		miaoshaGoods.setGoodsId(goodsVo.getId());
		int reduce = goodsVoDao.reduce(miaoshaGoods);
		return reduce > 0;
	}

}
