package de.semsoft.xfactory.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import de.semsoft.xfactory.logging.modell.LoggingEntry;
import de.semsoft.xfactory.logging.repo.LoggingRepository;
import de.semsoft.xfactory.services.ApplicationControl;
import de.semsoft.xfactory.ui.SlotMetric;
import de.semsoft.xfactory.ui.SlotMetricListImpl;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AppControlController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Listbox protocolListbox;

	@Wire
	private Listbox slotOverView;

	@Wire
	private Label startTimeLabel;

	@Wire
	private Label lastPingLabel;

	@Wire
	private Label noJobsLabel;

	@Wire
	private Image startStop;

	@Wire
	private Image shutdown;

	@WireVariable("ApplicationControl")
	private ApplicationControl appctrl;

	@WireVariable("SlotMetricListImpl")
	private SlotMetricListImpl slotMetricList;
	
	@WireVariable("LoggingRepository")
	private LoggingRepository loggingRep;

	@Listen("onClick = #refreshButton")
	public void refresh() {
		startTimeLabel.setValue(appctrl.getStartupTimeString());
		lastPingLabel.setValue(appctrl.getLastPingTimeString());
		noJobsLabel.setValue(Long.toString(appctrl.getJobCounter()));

		final List<LoggingEntry> result = loggingRep.findAll();
		if (result != null) {
			protocolListbox.setModel(new ListModelList<LoggingEntry>(result));
		}

	}
	
	@Listen("onClick = #refreshSlotList")
	public void refresSlotMetricView() {
		
		final List<SlotMetric> result = slotMetricList.getMetricList();
		if (result != null) {
			slotOverView.setModel(new ListModelList<SlotMetric>(result));
		}
		
	}
	
	@Listen("onClick = #shutdown")
	public void shutdown() {
		appctrl.shutdown();
	}

	@Listen("onClick = #startStop")
	public void startStop() {
		appctrl.toggleBlocked();
		if (appctrl.isBlocked()) {
			startStop.setSrc("img/start.png");
		} else {
			startStop.setSrc("img/pause.png");
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		refresh();
		refresSlotMetricView();
	}

}
