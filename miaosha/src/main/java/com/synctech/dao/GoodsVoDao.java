package com.synctech.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.synctech.domian.MiaoshaGoods;
import com.synctech.vo.GoodsVo;

@Mapper
public interface GoodsVoDao {

	@Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.id = g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.id = g.id where mg.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")Long goodsId);

	@Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count > 0")
	public int reduce(MiaoshaGoods miaoshaGoods);

}
