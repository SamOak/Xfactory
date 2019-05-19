package de.semsoft.xfactory.ui.controller;

import org.zkoss.lang.Library;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class IndexController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Combobox themeSelection;

	@Listen("onChange = #themeSelection")
	public void selectTheme() {

		Library.setProperty("org.zkoss.theme.preferred", themeSelection.getSelectedItem().getValue());
		Executions.sendRedirect("");

	}

}
