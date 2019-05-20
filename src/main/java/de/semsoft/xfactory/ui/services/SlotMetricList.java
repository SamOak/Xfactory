package de.semsoft.xfactory.ui.services;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.semsoft.xfactory.ui.SlotMetric;

@Repository
public interface SlotMetricList {

	public List<SlotMetric> getMetricList();
}



