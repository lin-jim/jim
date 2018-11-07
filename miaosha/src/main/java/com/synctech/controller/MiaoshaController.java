package com.synctech.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synctech.access.AccessLimit;
import com.synctech.domian.MiaoshaOrder;
import com.synctech.domian.MiaoshaUser;
import com.synctech.rabbitmq.MQSender;
import com.synctech.rabbitmq.MiaoshaMessage;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.GoodsKey;
import com.synctech.result.CodeMsg;
import com.synctech.result.Result;
import com.synctech.service.GoodsVoService;
import com.synctech.service.MiaoshaService;
import com.synctech.service.OrderService;
import com.synctech.vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

	private static Map<Long, Boolean> localMap = new HashMap<>();
	
	@Autowired
	private GoodsVoService goodsVoService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MiaoshaService miaoshaService;
	
	@Autowired
	private RedisUtils redisService;
	
	@Autowired
	private MQSender sender;
	
	/*@RequestMapping("/do_miaosha")
	public String login(Model model,MiaoshaUser user,@RequestParam("goodsId")long goodsId){
		GoodsVo goodsVo = goodsVoService.getGoodsVoByGoodsId(goodsId);
		Integer stockCount = goodsVo.getStockCount();
		if (stockCount < 0) {//判断库存
			model.addAttribute("errormsg",CodeMsg.MIAOSHA_OVER.getMsg());
			return "miaosha_fial";
		}
		MiaoshaOrder miaoshaInfo = orderService.getOrderInfoByUserIdGoodsId(user.getId(),goodsId);
		if (miaoshaInfo != null) {//是否秒杀到
			model.addAttribute("errormsg",CodeMsg.REPEAT_MIAOSHA.getMsg());
			return "miaosha_fail";
		}
		//减库存  下订单  写入秒杀订单
		OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
		model.addAttribute("orderInfo",orderInfo);
		model.addAttribute("goods",goodsVo);
		model.addAttribute("user",user);
		return "order_detail";
	}*/
	
	/**
	 * 将商品初始化到redis中
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> list = goodsVoService.listGoodsVo();
		if (list == null) {
			return;
		}
		for (GoodsVo goods : list) {//将库存加载到redis中,标记商品
			localMap.put(goods.getId(), false);
			System.out.println(goods.getStockCount());
			redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), goods.getStockCount());
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="/{path}/do_miaosha",method=RequestMethod.POST)
	public Result<Integer> do_miaosha(Model model,MiaoshaUser user,@RequestParam("goodsId")Long goodsId,
			@PathVariable("path")String path){
		model.addAttribute("user", user);
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		boolean check = miaoshaService.getMiaoshaPath(user.getId(),goodsId,path);
		if (!check) {
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		Boolean over = localMap.get(goodsId);
		if (over) {//为true者库存不足
			return Result.error(CodeMsg.MIAOSHA_OVER);
		}
		Long stock = redisService.decr(GoodsKey.getGoodsStock, ""+goodsId);
		if (stock < 0) {//判断库存
			localMap.put(goodsId, true);
			return Result.error(CodeMsg.MIAOSHA_OVER);
		}
		MiaoshaOrder miaoshaInfo = orderService.getOrderInfoByUserIdGoodsId(user.getId(),goodsId);
		if (miaoshaInfo != null) {//是否秒杀到
			return Result.error(CodeMsg.REPEAT_MIAOSHA);
		}
		//入队
		MiaoshaMessage mm = new MiaoshaMessage();
		mm.setUser(user);
		mm.setGoodsId(goodsId);
		sender.sendMiaoshaMessage(mm);
		return Result.success(0);
//		GoodsVo goodsVo = goodsVoService.getGoodsVoByGoodsId(goodsId);
//		Integer stockCount = goodsVo.getStockCount();
//		if (stockCount < 0) {//判断库存
//			return Result.error(CodeMsg.MIAOSHA_OVER);
//		}
//		MiaoshaOrder miaoshaInfo = orderService.getOrderInfoByUserIdGoodsId(user.getId(),goodsId);
//		if (miaoshaInfo != null) {//是否秒杀到
//			return Result.error(CodeMsg.REPEAT_MIAOSHA);
//		}
//		//减库存  下订单  写入秒杀订单
//		OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
//		model.addAttribute("orderInfo",orderInfo);
//		model.addAttribute("goods",goodsVo);
//		model.addAttribute("user",user);
//		return Result.success(orderInfo);
	}
	
	/**
	 * 成功返回orderId
	 * 失败返回-1
	 * 轮询返回0
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/result",method=RequestMethod.GET)
	public Result<Long> getMiaoshaResult(Model model,MiaoshaUser user,@RequestParam("goodsId")Long goodsId){
		model.addAttribute("user", user);
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
		return Result.success(result);
	}
	
	@ResponseBody
	@AccessLimit(seconds=5,maxCount=5,needLogin=true)
	@RequestMapping(value="/path",method=RequestMethod.GET)
	public Result<String> getMiaoshaPath(HttpServletRequest request,MiaoshaUser user,@RequestParam("goodsId")Long goodsId){
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
//		String uri = request.getRequestURI();
//		String key = ""+uri+"_"+user.getId();
//		Integer count = redisService.get(AccessKey.getAccessCount, key, Integer.class);
//		if (count == null) {
//			redisService.set(AccessKey.getAccessCount, key, 1);
//		}else if (count < 5) {
//			count++;
//			redisService.incr(AccessKey.getAccessCount, key);
//		}else {
//			return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
//		}
		String path = miaoshaService.createMiaoshaPath(user.getId(),goodsId);
		return Result.success(path);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkCode",method=RequestMethod.GET)
	public Result<Boolean> checkCode(Model model,MiaoshaUser user,@RequestParam("goodsId")Long goodsId,
			@RequestParam(value="verifyCode",defaultValue="0")String verifyCode){
		model.addAttribute("user", user);
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		Boolean over = localMap.get(goodsId);
		if (over) {//为true者库存不足
			return Result.error(CodeMsg.MIAOSHA_OVER);
		}
		Long stock = redisService.get(GoodsKey.getGoodsStock, ""+goodsId,Long.class);
		if (stock < 0) {//判断库存
			localMap.put(goodsId, true);
			return Result.error(CodeMsg.MIAOSHA_OVER);
		}
		MiaoshaOrder miaoshaInfo = orderService.getOrderInfoByUserIdGoodsId(user.getId(),goodsId);
		if (miaoshaInfo != null) {//是否秒杀到
			return Result.error(CodeMsg.REPEAT_MIAOSHA);
		}
		if (StringUtils.isEmpty(verifyCode)) {
			return Result.error(CodeMsg.VERIFYCODE_EMPTY);
		}
		boolean check = miaoshaService.checkVerifyCode(user.getId(),goodsId,verifyCode);
		if (!check) {
			return Result.error(CodeMsg.VERIFYCODE_ERROR);
		}
		return Result.success(check);
	}
	@ResponseBody
	@RequestMapping(value="/verifyCode",method=RequestMethod.GET)
	public Result<String> getVerifyCode(HttpServletResponse response,MiaoshaUser user,@RequestParam("goodsId")Long goodsId){
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		try {
			BufferedImage image = miaoshaService.createVerifyCode(user.getId(),goodsId);
			OutputStream out = response.getOutputStream();
			ImageIO.write(image, "JPEG", out);
			out.flush();
			out.close();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return Result.error(CodeMsg.MIAOSHA_FAIL);
		}
		
	}
	
	
}
