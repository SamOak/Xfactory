package de.semsoft.xfactory.ui.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
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
import org.zkoss.zul.Textbox;

import de.semsoft.xfactory.logging.modell.LoggingEntry;
import de.semsoft.xfactory.logging.repo.LoggingRepository;
import de.semsoft.xfactory.services.ApplicationControl;
import de.semsoft.xfactory.ui.FileServiceImpl;
import de.semsoft.xfactory.ui.FsFile;
import de.semsoft.xfactory.ui.SlotMetric;
import de.semsoft.xfactory.ui.SlotMetricListImpl;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class BatchTransformController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Listbox protocolListbox;

	@Wire
	private Listbox slotOverView;

	@Wire
	private Listbox fileListView;	
	
	@Wire 
	private Combobox areaSelection;
	
	@Wire
	private Label startTimeLabel;

	@Wire
	private Label lastPingLabel;

	@Wire
	private Label noJobsLabel;
	
	@Wire
	private Textbox newSlotName;

	@Wire
	private Image startStop;

	@Wire
	private Image shutdown;

	@WireVariable("ApplicationControl")
	private ApplicationControl appctrl;

	@WireVariable("SlotMetricListImpl")
	private SlotMetricListImpl slotMetricList;

	@WireVariable("xlstFileServiceImpl")
	private FileServiceImpl fileService;
	
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
	
	@Listen("onDoubleClick = #fileListView")
	public void viewFileByDoubleClick() {
		viewFile();
	}
	
	@Listen("onClick = #viewFileFileList")
	public void  viewFile() {
		
		if (fileListView.getSelectedCount() > 0) {

			final HashMap<String, String> map = new HashMap<String, String>();
			map.put("content", ((FsFile) fileListView.getSelectedItem().getValue()).getContent());
			map.put("filename", ((FsFile) fileListView.getSelectedItem().getValue()).getFileName());

			Executions.createComponents("~./zul/ViewPopup.zul", null, map);

		}
		
	}
	
	@Listen("onUpload = #addFileFileList")
	public void addFile(UploadEvent event) {
		
		final Listitem li = slotOverView.getSelectedItem();
		if( li != null ) {
			final SlotMetric metric = (SlotMetric)li.getValue(); 
			if (event.getMedia().getContentType().startsWith("text")) {
				try {
					fileService.addNewFile(IOUtils.toInputStream(event.getMedia().getStringData(), "UTF-8"),
							event.getMedia().getName(), metric.getSlotName(), areaSelection.getValue());
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} else {
				
				fileService.addNewFile(event.getMedia().getStreamData(), event.getMedia().getName(), metric.getSlotName(), areaSelection.getValue());
				if( event.getMedia().getFormat().toLowerCase().equals("zip") ) {

					//extract all files from zip...
					
					
					//delete zip file
					
				}
			}
		}
		refreshFilesView();
		refresSlotMetricView();
	
	}
	
	@Listen("onClick = #deleteFileFileList")
	public void deleteFile() {

		if (fileListView.getSelectedCount() > 0) {

			fileService.deleteFile((FsFile) fileListView.getSelectedItem().getValue());
			refreshFilesView();
			refresSlotMetricView();
		}
	}
	
	
	@Listen("onClick = #slotOverView")
	public void selectASlot() {
		areaSelection.setValue("IN");
		refreshFilesView();
	}
	
	
	@Listen("onClick = #refreshSlotList")
	public void refresSlotMetricView() {
		
		slotOverView.setItemRenderer( new SlotOverviewItemRenderer() );
		
		final List<SlotMetric> result = slotMetricList.getMetricList();
		if (result != null) {
			slotOverView.setModel(new ListModelList<SlotMetric>(result));
		}

	}

	
	@Listen("onClick = #addSlot")
	public void addSlot() {
		if( newSlotName.getValue().length() > 0 ) {
			fileService.addSlot(newSlotName.getValue());
			refresSlotMetricView();
		}
	}
	
	@Listen("onClick = #deleteSlot")
	public void deleteSlot() {
	
		final Listitem li = slotOverView.getSelectedItem();
		if( li != null ) {
			fileService.deleteSlot(((SlotMetric)li.getValue()).getSlotName());
			refresSlotMetricView();
		}

	}
		
		
	@Listen("onSelect = #areaSelection")
	public void selectArea() {
		refreshFilesView();
	}
	
	@Listen("onClick = #refreshFileList")
	public void refreshFilesView() {
		 
		final Listitem li = slotOverView.getSelectedItem();
		if( li != null ) {
			final SlotMetric metric = (SlotMetric)li.getValue(); 
			final List<FsFile> result = fileService.findAllSlot(metric.getSlotName(), areaSelection.getValue());
			fileListView.setModel(new ListModelList<FsFile>(result));
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


