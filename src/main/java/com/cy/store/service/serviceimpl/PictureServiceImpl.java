package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.PictureMapper;
import com.cy.store.model.Picture;
import com.cy.store.service.PictureService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;

    @Value("${file.picture-path}")
    protected String pictureSavePath;

    @Override
    public JSONObject add(Picture picture) {
        if(picture.getCode().isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //判断重复
        Picture pic = get(picture.getCode());
        if(pic!=null) throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);
        int rs = pictureMapper.insertSelective(picture);
        if(rs > 0){
            return CommonOperation.success(picture.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject edit(Picture picture) {
        if(picture.getId() == null || picture.getId() < 1) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        if(picture.getCode().isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //判断重复
        Picture pic = get(picture.getCode());
        if(pic!=null && pic.getId()!=picture.getId()) throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);
        int rs = pictureMapper.updateByPrimaryKeySelective(picture);
        if(rs > 0){
            return CommonOperation.success(picture.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        //获取picture
        Picture pic = get(id);
        if(pic == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        try {
            CommonOperation.removeFile(pictureSavePath+pic.getUrl().replace("getimg?filename=", ""));
        }catch (JsonException e){
            System.out.println(e.toJson());
        }

        int rs = pictureMapper.deleteByPrimaryKey(id);

        if(rs > 0){
            return CommonOperation.success(id);
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public List<Picture> getList(Map<String, Object> filter) {
        return pictureMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return pictureMapper.countByFilter(filter);
    }

    @Override
    public Picture get(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        return pictureMapper.selectByPrimaryKey(id);
    }

    @Override
    public Picture get(String code) {
        if(code == null || code.isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);

        return pictureMapper.selectByCode(code);
    }
}
