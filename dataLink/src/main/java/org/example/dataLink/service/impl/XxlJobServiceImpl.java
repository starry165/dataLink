package org.example.dataLink.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.JobInfoMapper;
import org.example.dataLink.service.XxlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class XxlJobServiceImpl implements XxlJobService {

    @Value("${xxl.job.admin.addresses}")
    private String adminUrl;

    @Value("${xxl.job.admin.userName}")
    private String userName;

    @Value("${xxl.job.admin.passWord}")
    private String passWord;

    @Autowired
    private JobInfoMapper jobInfoMapper;

    private final RestTemplate restTemplate;
    private volatile String cookie;
    private volatile long loginTime;
    private static final long COOKIE_TIME=30*60*1000L;
    private final Object lock=new Object();


    public XxlJobServiceImpl(){
        this.restTemplate=new RestTemplate();
        restTemplate.getInterceptors().add(new loginInterceptor());
    }

    private class loginInterceptor implements ClientHttpRequestInterceptor{

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            ensureLogin();
            request.getHeaders().add("Cookie", cookie);
            return execution.execute(request,body);
        }
    }

    private void ensureLogin(){
        if(cookie==null||System.currentTimeMillis()-loginTime>COOKIE_TIME){
            synchronized (lock){
                if(cookie==null|| System.currentTimeMillis()-loginTime>COOKIE_TIME){
                    login();
                }
            }
        }
    }

    private void login(){
        RestTemplate tempTemplate=new RestTemplate();

        String url=adminUrl+"/login";
        MultiValueMap<String,String> body=new LinkedMultiValueMap<>();
        body.add("userName",userName);
        body.add("password",passWord);

        HttpHeaders headers=new HttpHeaders();
        ResponseEntity<String> resp=tempTemplate.postForEntity(url,new HttpEntity<>(body,headers),String.class);
        List<String> cookies=resp.getHeaders().get("Set-Cookie");
        if(cookies==null||cookies.isEmpty()){
            throw new RuntimeException("登录失败："+resp);
        }
        cookie=String.join(";",cookies);
        loginTime=System.currentTimeMillis();
    }


    @Override
    public Integer addJob(MultiValueMap<String, String> jobInfo) {
        String url=adminUrl+"/jobinfo/add";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> req=new HttpEntity<>(jobInfo,headers);
        ResponseEntity<Map> resp=restTemplate.postForEntity(url,req,Map.class);
        if(resp.getBody()!=null&&(Integer) resp.getBody().get("code")==200){
            Object connect=resp.getBody().get("content");
            return Integer.parseInt(connect.toString());
        }
        throw new RuntimeException("新增任务失败:"+resp.getBody());
    }

    @Override
    public void startJob(Integer jobId) {
        String url=adminUrl+"/jobinfo/start?id="+jobId;
        ResponseEntity<Map> resp=restTemplate.postForEntity(url,null,Map.class);
        if(resp.getBody()!=null&& (Integer) resp.getBody().get("code")==200){
            log.info("任务启动成功,jobId:{}", jobId);
            jobInfoMapper.updateState(jobId,"运行中");
        }else {
            throw new RuntimeException("任务启动失败: "+resp.getBody());
        }
    }

    @Override
    public void stopJob(Integer jobId) {
        String url=adminUrl+"/jobinfo/stop?id="+jobId;
        ResponseEntity<Map> resp=restTemplate.postForEntity(url,null,Map.class);
        if(resp.getBody()!=null&& (Integer) resp.getBody().get("code")==200){
            log.info("任务停止成功,jobId:{}", jobId);
            jobInfoMapper.updateState(jobId,"停止");
        }else {
            throw new RuntimeException("任务停止失败: "+resp.getBody());
        }
    }

    @Override
    public Map<String,Object> loadJobInfo(Integer jobId){
        String getUrl=adminUrl+"/jobinfo/loadById?jobId="+jobId;
        ResponseEntity<Map> resp=restTemplate.getForEntity(getUrl,Map.class);
        if(resp.getBody()==null || (Integer) resp.getBody().get("code")!=200){
            throw new RuntimeException("获取配置信息失败："+resp.getBody());
        }
        Map<String, Object> jobInfo = (Map<String, Object>) resp.getBody().get("content");
        return  jobInfo;
    }

    @Override
    public void updateJob(Integer jobId, String newCron){
        Map<String,Object> jobInfo=loadJobInfo(jobId);
        jobInfo.put("scheduleType","CRON");
        jobInfo.put("scheduleConf",newCron);

        MultiValueMap<String,String> body=new LinkedMultiValueMap<>();
        for(Map.Entry<String,Object> entry: jobInfo.entrySet()){
            if(entry.getValue()!=null){
                String key=entry.getKey();
                if("addTime".equals(key)||"updateTime".equals(key)||"glueUpdatetime".equals(key)){
                    continue;
                }
                body.add(entry.getKey(),entry.getValue().toString());
            }
        }

        String url=adminUrl+"/jobinfo/update";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String ,String>> req=new HttpEntity<>(body,headers);
        ResponseEntity<Map> res=restTemplate.postForEntity(url,req,Map.class);
        if(res.getBody()!=null&& (Integer) res.getBody().get("code")==200){
            log.info("任务更新成功，jobId:{},newCron:{}",jobId,newCron);
        }else {
            throw new RuntimeException("任务更新失败："+res.getBody());
        }
    }
}
