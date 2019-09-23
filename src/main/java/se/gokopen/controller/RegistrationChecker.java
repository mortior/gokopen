package se.gokopen.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.gokopen.model.ConfigRegistration;

public class RegistrationChecker {

    private RegistrationChecker() {
    }

    public static boolean isOpenForRegistration(ConfigRegistration config, int noOfRegisteredPatrolsNow) {
        try {
            int maxPatrols;
            boolean allowRegistration = false;
            
            if(null==config.getMaxPatrols()) {
                maxPatrols = 0;
            }else {
                maxPatrols = config.getMaxPatrols();
            }
            if(null != config.getAllowPublicRegistration()) {
                allowRegistration = config.getAllowPublicRegistration();
            }
            
            if(null == config.getLastRegisterDay()) {
                return allowRegistration && isItSeatsLeft(maxPatrols, noOfRegisteredPatrolsNow);
            }else {
                return allowRegistration && isOpenToday(config.getLastRegisterDay()) && isItSeatsLeft(maxPatrols, noOfRegisteredPatrolsNow);
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isOpenToday(Date lastRegisterDate) throws ParseException {
        if(lastRegisterDate==null){
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastRegister = sdf.format(lastRegisterDate);
        String today = sdf.format(new Date());
        Date lastDate = sdf.parse(lastRegister);
        Date todayDate = sdf.parse(today);
        
         if(lastDate.after(todayDate)) {
            return true;
        }
        if(lastDate.before(todayDate)) {
            return false;
        }
        if(lastDate.equals(todayDate)) {
            return true;
        }
        return false;
    }
    
    private static boolean isItSeatsLeft(int maxSeats, int registeredPatrolsNow) {
        if(maxSeats==0) {
            return true;
        }
        if(maxSeats > registeredPatrolsNow) {
            return true;
        }
        return false;
    }
}
