package se.gokopen.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.gokopen.model.ConfigRegistration;

import java.util.List;

@Repository
public class ConfigRegistrationDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(ConfigRegistration configRegistration) {
        sessionFactory.getCurrentSession().saveOrUpdate(configRegistration);
    }

    public ConfigRegistration getCurrentConfigRegistration(){
        ConfigRegistration configRegistration;
        List<ConfigRegistration> configs = sessionFactory.getCurrentSession().createQuery("from configregistration").list();
        if(configs.isEmpty()){
            configRegistration = new ConfigRegistration();
        }else{
            configRegistration = configs.get(0);
        }
        return configRegistration;
    }
}
