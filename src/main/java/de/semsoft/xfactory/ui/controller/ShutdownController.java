package de.semsoft.xfactory.ui.controller;



import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import de.semsoft.xfactory.services.ApplicationControl;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ShutdownController extends SelectorComposer<Component> {
 
    private static final long serialVersionUID = 1L;
    
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
    

	
	
}
