package com.ytx.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ytx.dto.Exposer;
import com.ytx.dto.SeckillExecution;
import com.ytx.dto.SeckillResult;
import com.ytx.entity.Seckill;
import com.ytx.enums.SeckillStateEnum;
import com.ytx.exception.RepeatKillException;
import com.ytx.exception.SeckillCloseException;
import com.ytx.service.SeckillService;


/**
 * 秒杀控制层
 * @author yuantian xin
 *
 */
@Controller
@RequestMapping("/meseckill")   //   url:/模块/资源/{id}/细分
public class SeckillController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	
	/**
	 * 获取秒杀商品列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list",list);
		return "list"; //跳转到list.jsp  /WEB-INF/jsp/list.jsp
	}
	
	
	/**
	 * 秒杀商品详情
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId,Model model) {
		
		if(seckillId == null) {
			return "redirect:/seckill/list";
		}
		
		Seckill seckill=seckillService.getById(seckillId);
		
		if(seckill == null) {
			 return "forward:/seckill/list";
		}
		
		model.addAttribute("seckill",seckill);
		return "detail";
	}
	
	
	
	
	
	/**
	 * ajax请求，返回json数据 暴露秒杀接口地址
	 * @param seckillId
	 * @return
	 */
	//produces = {"application/json;charset=UTF-8"}) 设置content-type，能够防止json乱码
	@RequestMapping(value = "/{seckillId}/exposer",
					method = RequestMethod.GET,
					produces = {"application/json;charset=UTF-8"})
	@ResponseBody  //SpringMVC中的注解ResponseBody能够把对象转换成json格式
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {

		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	
	
	
	
	
	/**
	 * 执行秒杀
	 * @param seckillId
	 * @param md5
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/{md5}/execution",
					method = RequestMethod.POST,
					produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone",required = false) Long userPhone) {
		if (userPhone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		
		SeckillResult<SeckillExecution> result;

		try {
			
			/*SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);*/
			//高并发优化 调用mysql的存储过程
			SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
			
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, execution);
		}

	}

	
	/**
	 * 获取系统时间
	 * 返回的也是json，待会我们需要通过ajax请求拿到这个系统时间要
	 * @return
	 */
	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}
	
}
