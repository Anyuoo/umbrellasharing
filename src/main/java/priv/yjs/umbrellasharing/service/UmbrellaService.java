package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.UmbrellaMapper;
import priv.yjs.umbrellasharing.model.entity.Placement;
import priv.yjs.umbrellasharing.model.entity.Umbrella;
import priv.yjs.umbrellasharing.model.param.UmbrellaPageCondition;
import priv.yjs.umbrellasharing.model.vo.UmbrellaVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UmbrellaService extends ServiceImpl<UmbrellaMapper, Umbrella> implements IService<Umbrella> {

    @Resource
    private PlacementService placementService;
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

    public Page<UmbrellaVo> listIdleUmbrellas(UmbrellaPageCondition condition) {
        Page<Umbrella> umbrellaPage = condition.initWrapper(lambdaQuery()).page(condition.initPage());
        final List<UmbrellaVo> vos = umbrellaPage.getRecords().stream().map(record -> {
            var vo = new UmbrellaVo();
            vo.setId(record.getId());
            vo.setIdle(record.getIdle());
            vo.setPrice(record.getPrice());
            vo.setTimes(record.getTimes());
            vo.setPmName(placementService.getById(record.getPmId()).map(Placement::getPosition).orElse(""));
            return vo;
        }).collect(Collectors.toUnmodifiableList());
        final var voPage = new Page<UmbrellaVo>(condition.getCurrent(), condition.getSize());
        BeanUtils.copyProperties(umbrellaPage,voPage);
        voPage.setRecords(vos);
        return voPage;
    }




//
    public boolean borrow(long umbrellaId) {
        Umbrella umbrella = getById(umbrellaId).orElseThrow(() -> GlobalException.causeBy(ResultType.FAIL));
        umbrella.setIdle(false);
        return updateById(umbrella);
    }

    public boolean returnUmbrella(long umbrellaId) {
        Umbrella umbrella = getById(umbrellaId).orElseThrow(() -> GlobalException.causeBy(ResultType.FAIL));
        umbrella.setIdle(true);
        umbrella.setTimes(umbrella.getTimes() + 1);
        return updateById(umbrella);
    }
}
