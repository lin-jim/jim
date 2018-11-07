package com.synctech.service;

import java.util.List;

import com.synctech.vo.GoodsVo;

public interface GoodsVoService {

	public List<GoodsVo> listGoodsVo();

	public GoodsVo getGoodsVoByGoodsId(long goodsId);

	public boolean reduce(GoodsVo goodsVo);
}
