package model.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> subTaskIds = new ArrayList<>();
    private int duration;

    public Long getId() {
        return id;
    }

    public boolean isNoDependency() {
        return subTaskIds == null || subTaskIds.isEmpty();
    }

    public List<Long> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(List<Long> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<String> getSubTaskIdStrings() {
        List<String> subTaskIdStrings = new ArrayList<>();
        for (Long id : subTaskIds) {
            subTaskIdStrings.add(Long.toString(id));
        }
        return subTaskIdStrings;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof TaskDto) {
            TaskDto other = (TaskDto) obj;
            return this.id.equals(other.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) id.longValue();
    }
}
