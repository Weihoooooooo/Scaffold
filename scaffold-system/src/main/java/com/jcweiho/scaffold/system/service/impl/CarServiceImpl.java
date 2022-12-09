package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.PageUtils;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Car;
import com.jcweiho.scaffold.system.entity.convert.CarVOConvert;
import com.jcweiho.scaffold.system.entity.convert.ParkVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.CarQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.CarVO;
import com.jcweiho.scaffold.system.entity.vo.ParkVO;
import com.jcweiho.scaffold.system.mapper.CarMapper;
import com.jcweiho.scaffold.system.service.CarService;
import com.jcweiho.scaffold.system.service.OwnerService;
import com.jcweiho.scaffold.system.service.ParkLotService;
import com.jcweiho.scaffold.system.service.ParkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业主车辆信息表信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-12-08
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarServiceImpl extends CommonServiceImpl<CarMapper, Car> implements CarService {
    private final CarVOConvert carVOConvert;
    private final OwnerService ownerService;
    private final ParkService parkService;
    private final ParkLotService parkLotService;
    private final ParkVOConvert parkVOConvert;

    @Override
    public List<CarVO> convertToVO(List<Car> cars) {
        List<CarVO> carVOList = carVOConvert.toPojo(cars);
        for (CarVO carVO : carVOList) {
            carVO.setName(ownerService.getById(carVO.getOwnerId()).getName());
            ParkVO parkVO = parkVOConvert.toPojo(parkService.getById(carVO.getParkId()));
            parkVO.setRegion(parkLotService.getById(parkVO.getParkLotId()).getRegion());
            carVO.setParkVO(parkVO);
        }
        return carVOList;
    }

    @Override
    public List<Car> findAll(CarQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Car.class, criteria)));
    }


    @Override
    public Map<String, Object> findAll(CarQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Car> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(List<CarVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (CarVO carVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("业主姓名", carVO.getName());
            map.put("车位区域", carVO.getParkVO().getRegion());
            map.put("车位编号", carVO.getParkVO().getIdentityId());
            map.put("车位类型", carVO.getParkVO().getType().getDisplay());
            map.put("车牌号码", carVO.getCarNumber());
            map.put("车辆颜色", carVO.getCarColor());
            map.put("创建时间", carVO.getCreateTime());
            map.put("修改时间", carVO.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCar(CarVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        Car car = this.getById(resources.getId());
        Car carCarNumber = this.getOne(new LambdaQueryWrapper<Car>().eq(Car::getCarNumber, resources.getCarNumber()));
        if (ObjectUtil.isNotNull(carCarNumber) && !car.getId().equals(carCarNumber.getId())) {
            throw new BadRequestException("该车牌号已存在！");
        }
        car.setOwnerId(resources.getOwnerId());
        car.setParkId(resources.getParkId());
        car.setCarNumber(resources.getCarNumber());
        car.setCarColor(resources.getCarColor());
        return this.saveOrUpdate(car);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCar(CarVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Car>().eq(Car::getCarNumber, resources.getCarNumber())))) {
            throw new BadRequestException("该车牌号已存在！");
        }
        Car car = carVOConvert.toEntity(resources);
        if (!car.getCarColor().endsWith("色")) {
            car.setCarColor(car.getCarColor() + "色");
        }
        return this.save(car);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCar(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
