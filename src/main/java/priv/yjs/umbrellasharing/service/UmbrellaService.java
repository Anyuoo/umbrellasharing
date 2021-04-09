package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.mapper.UmbrellaMapper;
import priv.yjs.umbrellasharing.model.entity.Umbrella;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Service
public class UmbrellaService extends ServiceImpl<UmbrellaMapper, Umbrella> implements IService<Umbrella> {

    /**
     * 添加雨伞
     */
    public boolean saveUmbrella(Umbrella umbrella) {
        return save(umbrella);
    }


    /**
     * 更新雨伞信息
     */
    public boolean updateUmbrellaById(Umbrella source) {
        if (Objects.isNull(source)) {
            return false;
        }
        //设置不可更改信息
        getById(source.getId()).ifPresent(original -> {
            source.setCreateTime(original.getCreateTime())
                    .setStatus(original.getStatus());
        });
        return updateById(source);
    }

    /**
     * 根据id获取雨伞
     */
    public Optional<Umbrella> getById(Long id) {
        return lambdaQuery().eq(Objects.nonNull(id), Umbrella::getId, id).oneOpt();
    }
}
