package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.PlacementMapper;
import priv.yjs.umbrellasharing.model.entity.Placement;
import priv.yjs.umbrellasharing.model.entity.Umbrella;

import javax.annotation.Resource;
import java.util.*;

/**
 * 放置点服务层
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Service
public class PlacementService extends ServiceImpl<PlacementMapper, Placement> implements IService<Placement> {
    @Resource
    private UmbrellaService umbrellaService;
    /**
     * 根据id更新
     */
    public boolean updatePlacement(Placement source) {
        if (Objects.isNull(source)) {
            return false;
        }
        getById(source.getId()).ifPresent(original ->
                source.setStatus(original.getStatus())
                        .setCreateTime(original.getCreateTime()));
        return updateById(source);
    }

    /**
     * 通过id查询Placement
     */
    public Optional<Placement> getById(Long id) {
        return lambdaQuery().eq(Objects.nonNull(id), Placement::getId, id).oneOpt();

    }

    /**
     * 创建放置点
     */
    public boolean savePlacement(Placement placement) {
        //设置剩余数量为0
        placement.setUmbrellaStock(0);
        return save(placement);
    }

    /**
     * 添加雨伞到放置点
     * @param pmId 放置点id
     * @param umbrellaIds 雨伞id
     */
    @Transactional(rollbackFor = GlobalException.class)
    public boolean addUmbrellas(Long pmId, List<Long> umbrellaIds) {
        final var ids = new HashSet<Long>(umbrellaIds.size());
        if (!ids.addAll(umbrellaIds)) {
            return false;
        }
        //查询繁殖点信息
        final var placement = getById(pmId).orElseThrow(() -> GlobalException.causeBy(ResultType.PLACEMENT_NOT_EXISTED));

        final List<Umbrella> umbrellas = new ArrayList<>(ids.size());
        //查询雨伞信息和修改雨伞状态
        ids.forEach(id ->{
            umbrellaService.getById(id).ifPresent(umbrella -> {
                umbrella.setPmId(pmId);
                umbrellas.add(umbrella);
            });
        });

        int stock = placement.getUmbrellaStock() + umbrellas.size();
        //库存不足
        if (stock > placement.getUmbrellaTotal()) {
            return false;
        }
        placement.setUmbrellaStock(stock);
        //更新
        return updateById(placement) && umbrellaService.updateBatchById(umbrellas);
    }

}
