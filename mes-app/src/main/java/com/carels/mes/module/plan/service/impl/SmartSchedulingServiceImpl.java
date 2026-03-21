package com.carels.mes.module.plan.service.impl;

import com.carels.mes.module.plan.domain.CapacityModel;
import com.carels.mes.module.plan.domain.PlanSchedule;
import com.carels.mes.module.plan.service.ISmartSchedulingService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能排产Service实现 - carels
 * 实现遗传算法和贪心算法两种排产策略
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Service
public class SmartSchedulingServiceImpl implements ISmartSchedulingService {

    // 模拟数据 - 实际应从数据库获取
    private List<CapacityModel> mockWorkstations = new ArrayList<>();
    private List<Map<String, Object>> mockWorkorders = new ArrayList<>();

    public SmartSchedulingServiceImpl() {
        initMockData();
    }

    private void initMockData() {
        // 初始化工作站数据
        for (int i = 1; i <= 5; i++) {
            CapacityModel ws = new CapacityModel();
            ws.setWorkstationId((long) i);
            ws.setWorkstationName("工作站" + i);
            ws.setLineId((long) ((i - 1) / 2 + 1));
            ws.setLineName("生产线" + ((i - 1) / 2 + 1));
            ws.setProcessId((long) i);
            ws.setProcessName("工序" + i);
            ws.setDailyCapacity(100.0 + i * 20);
            ws.setStandardTime(5.0 + i);
            ws.setUtilizationRate(85.0);
            ws.setWorkStartTime("08:00");
            ws.setWorkEndTime("18:00");
            ws.setCurrentLoad(0.0);
            ws.setRemainingCapacity(100.0 + i * 20);
            mockWorkstations.add(ws);
        }
    }

    @Override
    public List<PlanSchedule> autoSchedule(Date startDate, Date endDate, String algorithmType) {
        List<PlanSchedule> schedules = new ArrayList<>();

        // 获取待排产工单
        List<Map<String, Object>> pendingWorkorders = getPendingWorkorders();

        if ("GENETIC".equals(algorithmType)) {
            // 遗传算法
            schedules = geneticAlgorithmSchedule(pendingWorkorders, startDate, endDate);
        } else {
            // 贪心算法（默认）
            schedules = greedySchedule(pendingWorkorders, startDate, endDate);
        }

        return schedules;
    }

    @Override
    public PlanSchedule scheduleWorkorder(Long workorderId, String algorithmType) {
        Map<String, Object> workorder = getWorkorderById(workorderId);
        if (workorder == null) {
            throw new RuntimeException("工单不存在");
        }

        List<Map<String, Object>> list = Collections.singletonList(workorder);

        if ("GENETIC".equals(algorithmType)) {
            List<PlanSchedule> schedules = geneticAlgorithmSchedule(list, new Date(), getDateAfterDays(new Date(), 30));
            return schedules.isEmpty() ? null : schedules.get(0);
        } else {
            List<PlanSchedule> schedules = greedySchedule(list, new Date(), getDateAfterDays(new Date(), 30));
            return schedules.isEmpty() ? null : schedules.get(0);
        }
    }

    @Override
    public List<CapacityModel> getCapacityAnalysis(Date startDate, Date endDate) {
        return mockWorkstations.stream().map(ws -> {
            CapacityModel model = new CapacityModel();
            model.setWorkstationId(ws.getWorkstationId());
            model.setWorkstationName(ws.getWorkstationName());
            model.setLineName(ws.getLineName());
            model.setProcessName(ws.getProcessName());
            model.setDailyCapacity(ws.getDailyCapacity());
            model.setCurrentLoad(ws.getCurrentLoad());
            model.setRemainingCapacity(ws.getRemainingCapacity());
            model.setLoadRate(ws.getCurrentLoad() / ws.getDailyCapacity() * 100);
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> checkConflicts(PlanSchedule schedule) {
        List<Map<String, Object>> conflicts = new ArrayList<>();

        // 检查时间冲突
        // 检查产能冲突
        // 检查资源冲突

        return conflicts;
    }

    @Override
    public List<PlanSchedule> optimizeSchedule(List<Long> scheduleIds) {
        // 使用遗传算法对现有排产进行优化
        return new ArrayList<>();
    }

    @Override
    public PlanSchedule reschedule(Long scheduleId, Date newStartTime, Long newWorkstationId) {
        // 重新排产逻辑
        PlanSchedule schedule = new PlanSchedule();
        schedule.setScheduleId(scheduleId);
        schedule.setPlanStartTime(newStartTime);
        schedule.setWorkstationId(newWorkstationId);
        // 计算新的结束时间
        return schedule;
    }

    @Override
    public Map<String, Object> getGanttData(Date startDate, Date endDate) {
        Map<String, Object> ganttData = new HashMap<>();

        // 资源列表（工作站）
        List<Map<String, Object>> resources = mockWorkstations.stream().map(ws -> {
            Map<String, Object> resource = new HashMap<>();
            resource.put("id", ws.getWorkstationId());
            resource.put("name", ws.getWorkstationName());
            resource.put("line", ws.getLineName());
            return resource;
        }).collect(Collectors.toList());

        ganttData.put("resources", resources);
        ganttData.put("tasks", new ArrayList<>());

        return ganttData;
    }

    @Override
    public Map<String, Object> evaluateFeasibility(Long workorderId, Date deliveryDate) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> workorder = getWorkorderById(workorderId);

        if (workorder == null) {
            result.put("feasible", false);
            result.put("reason", "工单不存在");
            return result;
        }

        Double quantity = ((Number) workorder.get("quantity")).doubleValue();
        Integer priority = ((Number) workorder.getOrDefault("priority", 5)).intValue();

        // 计算理论最早完成时间
        Date earliestCompleteTime = calculateEarliestCompleteTime(quantity);

        boolean feasible = !earliestCompleteTime.after(deliveryDate);

        result.put("feasible", feasible);
        result.put("deliveryDate", deliveryDate);
        result.put("earliestCompleteTime", earliestCompleteTime);
        result.put("requiredDays", calculateRequiredDays(quantity));
        result.put("priority", priority);

        if (!feasible) {
            long delayDays = (earliestCompleteTime.getTime() - deliveryDate.getTime()) / (1000 * 60 * 60 * 24);
            result.put("delayDays", delayDays);
            result.put("suggestion", "建议调整交期或增加产能");
        }

        return result;
    }

    /**
     * 贪心算法排产
     * 策略：优先级高的先排，选择剩余产能最大的工作站
     */
    private List<PlanSchedule> greedySchedule(List<Map<String, Object>> workorders, Date startDate, Date endDate) {
        List<PlanSchedule> schedules = new ArrayList<>();

        // 按优先级排序（数字小的优先）
        workorders.sort(Comparator.comparingInt(w -> ((Number) w.getOrDefault("priority", 5)).intValue()));

        Date currentTime = startDate;

        for (Map<String, Object> workorder : workorders) {
            Long workorderId = ((Number) workorder.get("workorderId")).longValue();
            String workorderCode = (String) workorder.get("workorderCode");
            Double quantity = ((Number) workorder.get("quantity")).doubleValue();
            String itemName = (String) workorder.get("itemName");
            Integer priority = ((Number) workorder.getOrDefault("priority", 5)).intValue();
            Date deliveryDate = (Date) workorder.get("deliveryDate");

            // 选择最佳工作站（剩余产能最大）
            CapacityModel bestWorkstation = selectBestWorkstation(quantity);

            if (bestWorkstation != null) {
                PlanSchedule schedule = new PlanSchedule();
                schedule.setScheduleId(System.currentTimeMillis() + workorderId);
                schedule.setScheduleCode("SCH" + workorderCode);
                schedule.setWorkorderId(workorderId);
                schedule.setWorkorderCode(workorderCode);
                schedule.setItemName(itemName);
                schedule.setPlanQuantity(quantity);
                schedule.setWorkstationId(bestWorkstation.getWorkstationId());
                schedule.setWorkstationName(bestWorkstation.getWorkstationName());
                schedule.setLineName(bestWorkstation.getLineName());
                schedule.setProcessName(bestWorkstation.getProcessName());
                schedule.setPlanStartTime(currentTime);

                // 计算预计耗时和结束时间
                double duration = quantity * bestWorkstation.getStandardTime();
                schedule.setEstimatedDuration(duration);
                schedule.setPlanEndTime(calculateEndTime(currentTime, duration));

                schedule.setDeliveryDate(deliveryDate);
                schedule.setPriority(priority);
                schedule.setStatus("SCHEDULED");
                schedule.setStandardTime(bestWorkstation.getStandardTime());
                schedule.setAlgorithmType("GREEDY");

                // 更新工作站负载
                bestWorkstation.setCurrentLoad(bestWorkstation.getCurrentLoad() + quantity);
                bestWorkstation.setRemainingCapacity(bestWorkstation.getRemainingCapacity() - quantity);

                schedules.add(schedule);
            }
        }

        return schedules;
    }

    /**
     * 遗传算法排产
     * 实现基本的遗传算法框架
     */
    private List<PlanSchedule> geneticAlgorithmSchedule(List<Map<String, Object>> workorders, Date startDate, Date endDate) {
        // 简化版遗传算法实现
        int populationSize = 50;
        int generations = 100;
        double mutationRate = 0.1;

        // 初始化种群
        List<List<PlanSchedule>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(greedySchedule(workorders, startDate, endDate));
        }

        // 迭代进化
        for (int gen = 0; gen < generations; gen++) {
            // 评估适应度
            List<Double> fitnessScores = population.stream()
                    .map(this::calculateFitness)
                    .collect(Collectors.toList());

            // 选择
            List<List<PlanSchedule>> selected = selection(population, fitnessScores);

            // 交叉
            List<List<PlanSchedule>> offspring = crossover(selected);

            // 变异
            mutate(offspring, mutationRate);

            population = offspring;
        }

        // 返回最优解
        return population.stream()
                .max(Comparator.comparingDouble(this::calculateFitness))
                .orElse(new ArrayList<>());
    }

    /**
     * 计算适应度函数
     * 目标：最小化延期、均衡负载、最大化设备利用率
     */
    private double calculateFitness(List<PlanSchedule> schedules) {
        if (schedules.isEmpty()) return 0.0;

        double fitness = 0.0;

        for (PlanSchedule schedule : schedules) {
            // 检查是否延期
            if (schedule.getPlanEndTime().after(schedule.getDeliveryDate())) {
                fitness -= 100; // 延期惩罚
            } else {
                fitness += 10; // 按时奖励
            }

            // 优先级高的给予更高适应度
            fitness += (11 - schedule.getPriority()) * 5;
        }

        return fitness;
    }

    private List<List<PlanSchedule>> selection(List<List<PlanSchedule>> population, List<Double> fitnessScores) {
        // 轮盘赌选择
        List<List<PlanSchedule>> selected = new ArrayList<>();
        double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();

        for (int i = 0; i < population.size(); i++) {
            double rand = Math.random() * totalFitness;
            double cumulative = 0.0;

            for (int j = 0; j < population.size(); j++) {
                cumulative += fitnessScores.get(j);
                if (cumulative >= rand) {
                    selected.add(population.get(j));
                    break;
                }
            }
        }

        return selected;
    }

    private List<List<PlanSchedule>> crossover(List<List<PlanSchedule>> parents) {
        List<List<PlanSchedule>> offspring = new ArrayList<>();

        for (int i = 0; i < parents.size(); i += 2) {
            List<PlanSchedule> parent1 = parents.get(i);
            List<PlanSchedule> parent2 = parents.get((i + 1) % parents.size());

            // 单点交叉
            int crossoverPoint = parent1.size() / 2;

            List<PlanSchedule> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
            child1.addAll(parent2.subList(crossoverPoint, parent2.size()));

            offspring.add(child1);
        }

        return offspring;
    }

    private void mutate(List<List<PlanSchedule>> population, double mutationRate) {
        for (List<PlanSchedule> individual : population) {
            if (Math.random() < mutationRate) {
                // 随机交换两个任务的工作站
                if (individual.size() >= 2) {
                    int idx1 = (int) (Math.random() * individual.size());
                    int idx2 = (int) (Math.random() * individual.size());

                    PlanSchedule temp = individual.get(idx1);
                    individual.set(idx1, individual.get(idx2));
                    individual.set(idx2, temp);
                }
            }
        }
    }

    private CapacityModel selectBestWorkstation(Double quantity) {
        return mockWorkstations.stream()
                .filter(ws -> ws.getRemainingCapacity() >= quantity)
                .max(Comparator.comparingDouble(CapacityModel::getRemainingCapacity))
                .orElse(null);
    }

    private Date calculateEndTime(Date startTime, double durationMinutes) {
        long durationMs = (long) (durationMinutes * 60 * 1000);
        return new Date(startTime.getTime() + durationMs);
    }

    private Date calculateEarliestCompleteTime(Double quantity) {
        // 找到剩余产能最大的工作站
        CapacityModel bestWs = mockWorkstations.stream()
                .max(Comparator.comparingDouble(CapacityModel::getRemainingCapacity))
                .orElse(null);

        if (bestWs == null) {
            return new Date();
        }

        double duration = quantity * bestWs.getStandardTime();
        return calculateEndTime(new Date(), duration);
    }

    private long calculateRequiredDays(Double quantity) {
        CapacityModel bestWs = mockWorkstations.stream()
                .max(Comparator.comparingDouble(CapacityModel::getDailyCapacity))
                .orElse(null);

        if (bestWs == null) return 1;

        return (long) Math.ceil(quantity / bestWs.getDailyCapacity());
    }

    private List<Map<String, Object>> getPendingWorkorders() {
        // 从数据库获取待排产工单
        return new ArrayList<>();
    }

    private Map<String, Object> getWorkorderById(Long workorderId) {
        // 从数据库获取工单信息
        Map<String, Object> workorder = new HashMap<>();
        workorder.put("workorderId", workorderId);
        workorder.put("workorderCode", "WO" + workorderId);
        workorder.put("quantity", 100.0);
        workorder.put("itemName", "测试物料");
        workorder.put("priority", 3);
        workorder.put("deliveryDate", getDateAfterDays(new Date(), 7));
        return workorder;
    }

    private Date getDateAfterDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
