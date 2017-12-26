package com.wms.action;

import com.util.common.HttpMessage;
import com.wms.service.BlockQueryService;
import com.wms.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
@Controller
@RequestMapping("/task")
public class TaskAction {

    @Resource
    private TaskService taskService;

    @RequestMapping("cancelJob.do")
    @ResponseBody
    public HttpMessage cancelJob(String mcKey) {
        HttpMessage httpMessage = taskService.cancelJob(mcKey);
        return httpMessage;
    }

    @RequestMapping("finishJob.do")
    @ResponseBody
    public HttpMessage finishJob(String mcKey) {
        HttpMessage httpMessage = taskService.finishJob(mcKey);
        return httpMessage;
    }

    @RequestMapping("finishAsrsJob.do")
    @ResponseBody
    public HttpMessage finishAsrsJob(String mcKey) {
        HttpMessage httpMessage = taskService.finishAsrsJob(mcKey);
        return httpMessage;
    }


}
