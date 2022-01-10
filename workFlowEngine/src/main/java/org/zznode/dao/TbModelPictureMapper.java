package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zznode.entity.TbModelPicture;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_model_picture(模型图示记录表)】的数据库操作Mapper
 * @Entity generator.domain.TbModelPicture
 */
public interface TbModelPictureMapper extends BaseMapper<TbModelPicture> {
    /**
     * 场景图片
     *
     * @param scenesDataId scenesDataId
     * @return list
     */
    List<Map<String, Object>> getModelPictureByScenesDataId(@Param(value = "scenesDataId") String scenesDataId);
}




