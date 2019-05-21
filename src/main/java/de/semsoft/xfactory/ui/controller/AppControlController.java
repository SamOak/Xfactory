package de.semsoft.xfactory.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

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
	private Combobox areaSelection;
	
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
	
	@Listen("onClick = #slotOverView")
	public void selectASlot() {
		areaSelection.setValue("IN");
	}
	
	
	@Listen("onClick = #refreshSlotList")
	public void refresSlotMetricView() {
		
		slotOverView.setItemRenderer( new SlotOverviewItemRenderer() );
		
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


	@SuppressWarnings("rawtypes")
	public class SlotOverviewItemRenderer implements ListitemRenderer {
  
	    @Override
		public void render (Listitem listitem, Object value, int index) {
	        final SlotMetric metric = (SlotMetric)value;
	
	        listitem.setValue(value);
	        
	        if( metric.isTransformationReady() ) {
	        	addListcell(listitem, metric.getSlotName(), false);
	        } else {
	        	addListcell(listitem, metric.getSlotName(), true);
	        }
	        addListcell(listitem, Integer.toString(metric.getCountIN()), false);
	        addListcell(listitem, Integer.toString(metric.getCountOUT()), false );
	        addListcell(listitem, Integer.toString(metric.getCountDONE()), false );
	        addListcell(listitem, Integer.toString(metric.getCountERROR()), false );
	        
	
	    }
	    private void addListcell (Listitem listitem, String value, boolean highligth) {
	        final Listcell lc = new Listcell ();
	        final Label lb = new Label(value);
	        if( highligth ) {
	        	lb.setStyle("color:red; font-weight: bold");
	        	lc.setTooltiptext("Please add a xsl file named (" + ((SlotMetric)listitem.getValue()).getSlotName() + ".xsl) to the library!!!");
	        }
	        lb.setParent(lc);
	        lc.setParent(listitem);
	    }
	}
	
	
	
	
}


