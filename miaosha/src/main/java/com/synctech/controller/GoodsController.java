package com.synctech.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.synctech.domian.MiaoshaUser;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.GoodsKey;
import com.synctech.result.Result;
import com.synctech.service.GoodsVoService;
import com.synctech.vo.GoodsDetailVo;
import com.synctech.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	private GoodsVoService goodsVoService;
	
	@Autowired
	private RedisUtils redisService;
	
	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;
	
	@Autowired
	private ApplicationContext appctx;
	
	/**
	 * 页面缓存
	 * @param request
	 * @param response
	 * @param model
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/toList",produces="text/html")
	public String toList(HttpServletRequest request,HttpServletResponse response,Model model,MiaoshaUser user){
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		List<GoodsVo> goodsVo = goodsVoService.listGoodsVo();
		model.addAttribute("goodsList",goodsVo);
		model.addAttribute("user",user);
		SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), appctx);
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
	}
	
	@ResponseBody
	@RequestMapping("/detail/{goodsId}")
	public Result<GoodsDetailVo> detail(HttpServletRequest request,HttpServletResponse response,Model model,MiaoshaUser user,@PathVariable("goodsId")long goodsId){
		GoodsVo goods = goodsVoService.getGoodsVoByGoodsId(goodsId);
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int status = 0;
		int remainSeconds = 0;
		if (now < startAt) {//秒杀倒计时
			status = 0;
			remainSeconds = (int) ((startAt - now)/1000);
		}else if (now > endAt) {//秒杀已结束
			status = 2;
			remainSeconds = -1;
		}else {//秒杀进行中
			status = 1;
			remainSeconds = 0;		
		}
		GoodsDetailVo detailVo = new GoodsDetailVo();
		detailVo.setUser(user);
		detailVo.setGoods(goods);
		detailVo.setStatus(status);
		detailVo.setRemainSeconds(remainSeconds);
		return Result.success(detailVo);
	}
	
	@ResponseBody
	@RequestMapping(value="/to_detail/{goodsId}",produces="text/html")
	public String detail2(HttpServletRequest request,HttpServletResponse response,Model model,MiaoshaUser user,@PathVariable("goodsId")long goodsId){
		String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		model.addAttribute("user",user);
		GoodsVo goods = goodsVoService.getGoodsVoByGoodsId(goodsId);
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int status = 0;
		int remainSeconds = 0;
		if (now < startAt) {//秒杀倒计时
			status = 0;
			remainSeconds = (int) ((startAt - now)/1000);
		}else if (now > endAt) {//秒杀已结束
			status = 2;
			remainSeconds = -1;
		}else {//秒杀进行中
			status = 1;
			remainSeconds = 0;		
		}
		model.addAttribute("status",status);
		model.addAttribute("remainSeconds",remainSeconds);
		model.addAttribute("goods",goods);
		SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), appctx);
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
		}
		return html;
	}
	
	
}
