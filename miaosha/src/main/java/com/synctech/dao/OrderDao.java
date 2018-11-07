package com.synctech.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.synctech.domian.MiaoshaOrder;
import com.synctech.domian.OrderInfo;

@Mapper
public interface OrderDao {

	@Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
	MiaoshaOrder getOrderInfoByUserIdGoodsId(@Param("userId")Long userId, @Param("goodsId")Long goodsId);

	@Insert("insert into order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date)"
			+ "values (#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
	@SelectKey(before=false,keyColumn="id",keyProperty="id",resultType=Long.class,statement="select last_insert_id()")
	Integer insertOrderInfo(OrderInfo orderInfo);
	@Insert("insert into miaosha_order(user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId})")
	Integer insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

	@Select("select * from order_info where id = #{orderId}")
	OrderInfo getOrderByOrderId(@Param("orderId")Long orderId);

	
}
