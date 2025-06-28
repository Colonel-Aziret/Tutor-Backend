//package com.example.tutor.scheduler;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@Slf4j
//public class Scheduler {
//    private static final String CRON_EVERY_DAY_7AM = "0 0 7 * * *";
//    private static final String CRON_EVERY_SUNDAY_1AM = "0 0 1 * * SUN";
//    private static final String CRON_EVERY_DAY_12AM = "0 0 0 * * ?";
//    private static final String CRON_EVERY_DAY_1AM = "0 0 1 * * *";
//
//    private final PlanRegisterRepository planRegisterRepository;
//    private final PlanTaskRegisterService planTaskRegisterService;
//    private final PasswordResetTokenRepository passwordResetTokenRepository;
//
//    private final SysLogRequestService sysLogRequestService;
//
//    @Autowired
//    public Scheduler(
//            PlanRegisterRepository planRegisterRepository,
//            PlanTaskRegisterService planTaskRegisterService,
//            PasswordResetTokenRepository passwordResetTokenRepository,
//            SysLogRequestService sysLogRequestService) {
//        this.planRegisterRepository = planRegisterRepository;
//        this.planTaskRegisterService = planTaskRegisterService;
//        this.passwordResetTokenRepository = passwordResetTokenRepository;
//        this.sysLogRequestService = sysLogRequestService;
//    }
//
//    @Scheduled(cron = CRON_EVERY_DAY_1AM) // каждый день в 1:00 ночи
//    public void checkAndMarkOverduePlans() {
//        List<PlanRegister> allPlansWithConfirmedDate = planRegisterRepository.findAllByConfirmedDateIsNotNullAndOverdueFalse();
//
//        LocalDate today = LocalDate.now();
//
//        for (PlanRegister plan : allPlansWithConfirmedDate) {
//            LocalDate deadline = DateUtils.addDays(plan.getConfirmedDate(), 13, true); // срок до 14-го дня включительно
//            if (today.isAfter(deadline)) {
//                log.info("План ID {} просрочен. Обновляем overdue = true", plan.getId());
//                plan.setOverdue(true);
//                planRegisterRepository.save(plan);
//            }
//        }
//    }
//
//
//    //    @Scheduled(cron = CRON_EVERY_DAY_7AM)
//    public void checkPlanStatus() {
//        List<PlanRegister> plans = planRegisterRepository.findAllByPlanStatus(PlanStatus.IN_PROCESS);
//        int counterOfChangedPlans = 0;
//
//        for (PlanRegister plan : plans) {
//            Long totalTasks = planTaskRegisterService.countPlanTasks(plan.getId());
//            Long countCompletedTasks = planTaskRegisterService.countCompletedTasksByPlanId(plan.getId());
//            Long countRejectedTasks = planTaskRegisterService.countRejectedTasksByPlanId(plan.getId());
//
//            if (totalTasks > (countCompletedTasks + countRejectedTasks)) {
//                continue;
//            }
//            plan.setStatus(PlanStatus.PROCESSED);
//            counterOfChangedPlans++;
//        }
//
//        if (counterOfChangedPlans > 0) {
//            planRegisterRepository.saveAll(plans);
//            sysLogRequestService.saveSuccessToFileAndDb(
//                    this.getClass().getSimpleName(),
//                    Thread.currentThread().getStackTrace()[1].getMethodName(),
//                    "plans were updated by the scheduler " + counterOfChangedPlans,
//                    null);
//        }
//    }
//
//    @Scheduled(cron = CRON_EVERY_DAY_12AM) // Запуск каждый день в 00:00
//    public void clearOldResetTokens() {
//        Date now = new Date();
//        passwordResetTokenRepository.deleteByExpiryDateBefore(now);
//    }
//
//    //версия с redis
////    @Scheduled(cron = CRON_EVERY_SUNDAY_1AM)
////    public void CompareTerritorialUnits() {
////        List<HBTerritorialUnit> allInMap = territorialUnitService.getAllFromRedis();
////        List<HBTerritorialUnit> allInFarmAccount = territorialUnitService.getAll(new TUFilter());
////
////        Map<Long, HBTerritorialUnit> unitMap = allInMap.stream()
////                .collect(Collectors.toMap(HBTerritorialUnit::getId, unit -> unit, (a, b) -> a));
////
////        for (HBTerritorialUnit unit : allInMap) {
////            if (unit.getParent() != null) {
////                HBTerritorialUnit parent = unitMap.get(unit.getParent().getId());
////                unit.setParent(parent);
////            }
////        }
////
////        if (allInFarmAccount.isEmpty()) {
////            territorialUnitService.saveAll(new ArrayList<>(unitMap.values()));
////        } else {
////            Map<Long, HBTerritorialUnit> farmMap = allInFarmAccount.stream()
////                    .collect(Collectors.toMap(HBTerritorialUnit::getId, unit -> unit));
////
////            List<HBTerritorialUnit> toSave = new ArrayList<>();
////            List<HBTerritorialUnit> toDelete = new ArrayList<>();
////
////            for (Map.Entry<Long, HBTerritorialUnit> entry : unitMap.entrySet()) {
////                Long id = entry.getKey();
////                HBTerritorialUnit mapUnit = entry.getValue();
////
////                if (farmMap.containsKey(id)) {
////                    HBTerritorialUnit farmUnit = farmMap.get(id);
////                    if (!mapUnit.equals(farmUnit)) {
////                        farmUnit.setNameRu(mapUnit.getNameRu());
////                        farmUnit.setNameKy(mapUnit.getNameKy());
////                        farmUnit.setCode(mapUnit.getCode());
////                        farmUnit.setType(mapUnit.getType());
////                        farmUnit.setParent(mapUnit.getParent());
////
////                        toSave.add(farmUnit);
////                    }
////                } else {
////                    toSave.add(mapUnit);
////                }
////            }
////
////            for (HBTerritorialUnit farmUnit : allInFarmAccount) {
////                if (!unitMap.containsKey(farmUnit.getId())) {
////                    toDelete.add(farmUnit);
////                }
////            }
////
////            if (!toSave.isEmpty()) {
////                territorialUnitService.saveAll(toSave);
////            }
////
////            if (!toDelete.isEmpty()) {
////                territorialUnitService.deleteAll(toDelete);
////            }
////        }
////    }
//
//}
//
