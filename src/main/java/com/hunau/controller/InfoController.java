package com.hunau.controller;

import com.hunau.entity.Info;
import com.hunau.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TanXY
 * @date 2021/4/24 - 16:37
 */
@Controller
public class InfoController {
    @Autowired
    InfoService infoService;

    @GetMapping("")
    public String infoPage() {
        return "info";
    }

    @ResponseBody
    @GetMapping("/info/list")
    public Map<String, Object> getInfo(Info info, int page, int limit) {
        Map<String, Object> data = new HashMap<>(3);

        List<Info> list = infoService.selectInfoList(info, page, limit);
        int count = infoService.countInfo();

        data.put("data", list);
        data.put("count", count);
        data.put("code", 0);

        return data;
    }
}
