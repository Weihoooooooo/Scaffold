package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Car;
import com.jcweiho.scaffold.system.entity.Owner;
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
import java.util.stream.Collectors;

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
            List<String> parkInfo = ListUtils.list(false);
            parkInfo.add(IdSecureUtils.des().encrypt(parkVO.getParkLotId()));
            parkInfo.add(IdSecureUtils.des().encrypt(carVO.getParkId()));
            carVO.setParkInfo(parkInfo);
        }
        return carVOList;
    }

    @Override
    public List<Car> findAll(CarQueryCriteria criteria) {
        // 先筛选所有符合基本条件的
        List<Car> cars = this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Car.class, criteria)));
        // 根据传入的业主姓名模糊查询业主主键列表
        if (StringUtils.isNotBlank(criteria.getName())) {
            List<Long> owners = ownerService.getBaseMapper()
                    .selectList(new LambdaQueryWrapper<Owner>().like(Owner::getName, LikeCipherUtils.likeEncrypt(criteria.getName())))
                    .stream().map(Owner::getId).collect(Collectors.toList());
            // 遍历上面的cars
            if (CollUtils.isNotEmpty(owners)) {
                return cars.stream().filter(i -> owners.contains(i.getOwnerId())).collect(Collectors.toList());
            }
        }
        return cars;
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
        System.err.println(resources.getId());
        Car car = this.getById(resources.getId());
        if (!VerifyUtils.isPlateNumber(resources.getCarNumber())) {
            throw new BadRequestException("请输入正确的车牌号！");
        }
        Car carCarNumber = this.getOne(new LambdaQueryWrapper<Car>().eq(Car::getCarNumber, resources.getCarNumber()));
        if (ObjectUtil.isNotNull(carCarNumber) && !car.getId().equals(carCarNumber.getId())) {
            throw new BadRequestException("该车牌号已存在！");
        }
        Car carParkId = this.getOne(new LambdaQueryWrapper<Car>().eq(Car::getParkId, IdSecureUtils.des().decrypt(resources.getParkInfo().get(1))));
        if (ObjectUtil.isNotNull(carParkId) && !car.getId().equals(carParkId.getId())) {
            throw new BadRequestException("该停车场已被占用!");
        }
        car.setParkId(IdSecureUtils.des().decrypt(resources.getParkInfo().get(1)));
        car.setCarNumber(resources.getCarNumber());
        car.setCarColor(resources.getCarColor());
        return this.saveOrUpdate(car);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCar(CarVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        if (StringUtils.isBlank(resources.getPhone())) {
            throw new BadRequestException("业主手机不能为空！");
        }
        if (!VerifyUtils.isPlateNumber(resources.getCarNumber())) {
            throw new BadRequestException("请输入正确的车牌号！");
        }
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Car>().eq(Car::getCarNumber, resources.getCarNumber())))) {
            throw new BadRequestException("该车牌号已存在！");
        }
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Car>().eq(Car::getParkId, IdSecureUtils.des().decrypt(resources.getParkInfo().get(1)))))) {
            throw new BadRequestException("该停车场已被占用!");
        }
        Car car = carVOConvert.toEntity(resources);
        Long ownerId = ownerService.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getPhone, LikeCipherUtils.phoneLikeEncrypt(resources.getPhone()))).getId();
        car.setOwnerId(ownerId);
        car.setParkId(IdSecureUtils.des().decrypt(resources.getParkInfo().get(1)));
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
