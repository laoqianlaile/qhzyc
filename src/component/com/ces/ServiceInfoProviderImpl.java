package com.ces;

import cesgroup.rear.lib.client.enterprise.ServiceInfoProvider;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ServiceInfoProviderImpl implements ServiceInfoProvider{
    @Override
    public String getTarget() {
//       return "http://10.0.137.62:8081/sdcenter/rest";
      //  return "http://10.0.137.62:8081/sdcenter0302/rest";
        return "http://localhost:8089/qhcenter/rest";
     // return "http://localhost:8089/sdcenter/rest";
       // return "http://10.250.198.99:8092/sdcenter/rest";
       //return "http://192.10.22.187:8089/sdcenter/rest";
      //  return "http://123.232.26.175/sdcenter/rest";
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getSecret() {
        return null;
    }
}
