package com.camon;

import org.openqa.selenium.UnhandledAlertException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by jooyong on 2016-03-03.
 */
public class LoginJob implements Job{
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        String googleUsename = mergedJobDataMap.get("googleUsename").toString();
        String googlePw = mergedJobDataMap.get("googlePw").toString();
        String tooniId = mergedJobDataMap.get("tooniId").toString();
        String tooniPw = mergedJobDataMap.get("tooniPw").toString();

        // 로그인 테스트
        open("http://www.tooniland.com/index.tn");
        $("#ids").val(tooniId);
        $("#password").val(tooniPw);
        $(".loginArea > a").click();

        boolean exists = false;

        try {
            TooniMainPage page = open("http://www.tooniland.com/index.tn", TooniMainPage.class);
            exists = page.getResult().exists();
            close();
        } catch (UnhandledAlertException e) {
            // 로그인실패
        }

        // email 전송
        String from = googleUsename + "@gmail.com";
        String to = googleUsename + "@gmail.com";
        String text = "login status : " + exists;

        try {
            new Mail().send(googleUsename, googlePw, from, to, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
