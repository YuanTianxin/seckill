package com.ytx.test.testservice;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ytx.dto.Exposer;
import com.ytx.dto.SeckillExecution;
import com.ytx.entity.Seckill;
import com.ytx.exception.RepeatKillException;
import com.ytx.exception.SeckillCloseException;
import com.ytx.service.SeckillService;
import com.ytx.test.base.SpringBaseTest;


/**
 * SeckillService的测试类
 * @author yuantian xin
 *
 */
public class SeckillServiceTest extends SpringBaseTest {
	
	private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillListTest() throws Exception {
        
    	List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);

    }

    @Test
    public void getByIdTest() throws Exception {
 
        long seckillId = 1000;
        Seckill seckill=seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
        
    }
    
    
    @Test//完整逻辑代码测试，注意可重复执行，由下面两个注释的方法合并而来，因为这两个是有执行顺序的
    public void testSeckillLogic() throws Exception {
        long seckillId=1000;
        Exposer exposer=seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
        	
        	logger.info("exposer={}",exposer);
            long userPhone=18688881111L;
            String md5=exposer.getMd5();

            try {
            	
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e){
            	logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
           logger.warn("exposer={}",exposer);
        }
    }
    
    
    /*@Test
    public void exportSeckillUrlTest() throws Exception {

        long seckillId=1000;
        Exposer exposer=seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}",exposer);

    }*/
    
    /**
     * try catch捕捉重复秒杀异常，输出异常信息，测试时可以反复执行
     * @throws Exception
     */
   /* @Test
    public void executeSeckillTest() throws Exception {

        long seckillId = 1000;
        long userPhone = 18688881111L;
        String md5="edb7cf816b1703531383132af489c92f";
        try {
        	 SeckillExecution seckillExecution=seckillService.executeSeckill(seckillId,userPhone,md5);
        	 logger.info("result={}",seckillExecution);
        } catch (RepeatKillException e) {
        	logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
        	logger.error(e.getMessage());
        }
        
    }*/
    
    
    @Test
    public void executeSeckilProcedureTest() {
    	long seckillId = 1001;
    	long phone = 18804501676L;
    	Exposer exposer = seckillService.exportSeckillUrl(seckillId);
    	if(exposer.isExposed()){
    		String md5 = exposer.getMd5();
    		SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
    		logger.info(execution.getStateInfo());
    	}
    		
    }
}
