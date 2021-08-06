package com.hunau.service.impl;

import com.hunau.entity.Info;
import com.hunau.mapper.InfoMapper;
import com.hunau.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TanXY
 * @date 2021/4/24 - 16:35
 */
@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    InfoMapper infoMapper;

    @Override
    public List<Info> selectInfoList(Info info, int page, int size) {
        page = (page - 1) * size;
        return infoMapper.selectInfoList(info, page, size);
    }

    @Override
    public int countInfo() {
        return infoMapper.countInfo();
    }

    @Override
    public int addInfo(Info info) {
        return infoMapper.addInfo(info);
    }
}
