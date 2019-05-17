package de.semsoft.xfactory.ui.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import de.semsoft.xfactory.ui.LibFile;
import de.semsoft.xfactory.ui.FileServiceImpl;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FileController extends SelectorComposer<Component> {
 
    private static final long serialVersionUID = 1L;
    
    @Wire
    private Listbox fileListbox;
    
    @Wire
    private Label libPath;
    
    
    @WireVariable("xlstFileServiceImpl")
    private FileServiceImpl fileService;
    
    
    
    
    @Listen("onClick = #toolbarFileLibraryRefresh")
    public void refreshList() {
    	
		List<LibFile> result = fileService.findAll();
		fileListbox.setModel(new ListModelList<LibFile>(result));
    	
    }
       
    @Listen("onDoubleClick = #fileListbox")
    public void viewFileByDoubleClick() {
    	openView();
    }
    
    @Listen("onClick = #toolbarFileLibraryView")
    public void openView() {
    	
    	if( fileListbox.getSelectedCount() > 0 ) {
	    
    		HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("content", ((LibFile)fileListbox.getSelectedItem().getValue()).getContent());
	    	map.put("filename", ((LibFile)fileListbox.getSelectedItem().getValue()).getFileName());
	    	
	    	Executions.createComponents("~./zul/ViewPopup.zul", null, map);
	    	
    	}
    	
    }
    
    
    @Listen("onUpload = #toolbarFileLibraryAdd")
    public void addFile(UploadEvent event){
    	
    	if(event.getMedia().getContentType().startsWith("text") ) {
    		try {
				fileService.addNewFile(
						IOUtils.toInputStream(event.getMedia().getStringData(), "UTF-8"), 
						event.getMedia().getName()
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	} else {
    		fileService.addNewFile(
    				event.getMedia().getStreamData(), 
    				event.getMedia().getName()
    		);
    	}
    	refreshList();
    	
    }  
       
    
    @Listen("onClick = #toolbarFileLibraryDelete")
    public void deleteFile() {
    	
    	if( fileListbox.getSelectedCount() > 0 ) {
    		
    		fileService.deleteFile(
    				(LibFile)fileListbox.getSelectedItem().getValue()
    		);
    		refreshList();
    	}
    }
    
	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
		refreshList();

		libPath.setValue(fileService.getPath());
	}
    

	
	
}
