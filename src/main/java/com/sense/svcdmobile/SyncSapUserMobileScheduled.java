package com.sense.svcdmobile;

import com.sense.svcdmobile.bean.EmpUser;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cailizhi
 * @date 2023/4/13 15:50
 */
@Component
@EnableScheduling
public class SyncSapUserMobileScheduled {
    private Logger logger = Logger.getLogger(SyncSapUserMobileScheduled.class);
    @Scheduled(cron = "0 11 1 * * ?")
    public void SyncSapUserMobileRun() {
        com.svw.client.SyncSapUserMobile ss = new com.svw.client.SyncSapUserMobile();
        List<EmpUser> addlist = null;
        List<EmpUser> modlist = null;
        addlist = new ArrayList<>();
        modlist = new ArrayList<>();
        ss.execSync(addlist, modlist);
        logger.info("=========1==========");
        for (EmpUser emp : addlist)
            logger.info(String.valueOf(emp.getUid()) + ">" + emp.getMail() + ">" + emp.getSamaccountname() + ">" + emp.getMobile());
        logger.info("========2===========");
        for (EmpUser emp : modlist)
            System.out.println(String.valueOf(emp.getUid()) + ">" + emp.getMail() + ">" + emp.getSamaccountname() + ">" + emp.getMobile());
        ss.UpdateData(addlist, modlist);
    }
}
